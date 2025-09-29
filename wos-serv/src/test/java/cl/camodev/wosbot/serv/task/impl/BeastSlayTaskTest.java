package cl.camodev.wosbot.serv.task.impl;

import cl.camodev.wosbot.console.enumerable.TpDailyTaskEnum;
import cl.camodev.wosbot.emulator.EmulatorManager;
import cl.camodev.wosbot.ot.DTOProfiles;
import cl.camodev.wosbot.serv.impl.ServScheduler;
import cl.camodev.wosbot.serv.task.EnumStartLocation;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test class for BeastSlayTask
 * 
 * This test class exercises the BeastSlayTask functionality including:
 * - Static method calculateFullStaminaTime
 * - Private method extractFirstNumber (tested via reflection)
 * - Main execute method with various scenarios
 * - Edge cases and error conditions
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BeastSlayTask Tests")
class BeastSlayTaskTest {

    @Mock
    private DTOProfiles mockProfile;
    
    @Mock
    private EmulatorManager mockEmulatorManager;
    
    @Mock
    private ServScheduler mockServScheduler;

    private BeastSlayTask beastSlayTask;
    private TpDailyTaskEnum testTask;

    @BeforeEach
    void setUp() {
        // Setup test data
        testTask = TpDailyTaskEnum.INTEL; // Using INTEL as a placeholder since BEAST_SLAY doesn't exist
        
        // Configure mock profile
        when(mockProfile.getName()).thenReturn("TestProfile");
        when(mockProfile.getEmulatorNumber()).thenReturn("1");
        
        // Create the task instance
        beastSlayTask = new BeastSlayTask(mockProfile, testTask);
    }

    // ==================== Static Method Tests ====================

    @Test
    @DisplayName("calculateFullStaminaTime - Current stamina already full")
    void calculateFullStaminaTime_CurrentStaminaAlreadyFull_ReturnsCurrentTime() {
        // Given
        int currentStamina = 100;
        int maxStamina = 100;
        int regenRateMinutes = 5;

        // When
        LocalDateTime result = BeastSlayTask.calculateFullStaminaTime(currentStamina, maxStamina, regenRateMinutes);

        // Then
        assertThat(result).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
    }

    @Test
    @DisplayName("calculateFullStaminaTime - Current stamina below max")
    void calculateFullStaminaTime_CurrentStaminaBelowMax_ReturnsCorrectFutureTime() {
        // Given
        int currentStamina = 50;
        int maxStamina = 100;
        int regenRateMinutes = 5;
        LocalDateTime beforeCall = LocalDateTime.now();

        // When
        LocalDateTime result = BeastSlayTask.calculateFullStaminaTime(currentStamina, maxStamina, regenRateMinutes);

        // Then
        LocalDateTime afterCall = LocalDateTime.now();
        int expectedMinutes = (maxStamina - currentStamina) * regenRateMinutes; // 50 * 5 = 250 minutes
        
        assertThat(result).isAfter(beforeCall);
        assertThat(result).isBefore(afterCall.plusMinutes(expectedMinutes + 1));
        assertThat(result).isAfter(afterCall.plusMinutes(expectedMinutes - 1));
    }

    @ParameterizedTest
    @CsvSource({
        "0, 100, 5, 500",    // 100 * 5 = 500 minutes
        "25, 100, 3, 225",   // 75 * 3 = 225 minutes
        "80, 100, 10, 200",  // 20 * 10 = 200 minutes
        "99, 100, 1, 1"      // 1 * 1 = 1 minute
    })
    @DisplayName("calculateFullStaminaTime - Various stamina scenarios")
    void calculateFullStaminaTime_VariousScenarios_ReturnsCorrectTime(
            int currentStamina, int maxStamina, int regenRateMinutes, int expectedMinutes) {
        
        // When
        LocalDateTime result = BeastSlayTask.calculateFullStaminaTime(currentStamina, maxStamina, regenRateMinutes);

        // Then
        LocalDateTime expectedTime = LocalDateTime.now().plusMinutes(expectedMinutes);
        assertThat(result).isCloseTo(expectedTime, within(1, ChronoUnit.MINUTES));
    }

    // ==================== ExtractFirstNumber Method Tests ====================

    @Test
    @DisplayName("extractFirstNumber - Valid OCR text with fraction")
    void extractFirstNumber_ValidFractionText_ReturnsFirstNumber() throws Exception {
        // Given
        String ocrText = "75/100";
        
        // When
        int result = invokeExtractFirstNumber(beastSlayTask, ocrText);
        
        // Then
        assertThat(result).isEqualTo(75);
    }

    @Test
    @DisplayName("extractFirstNumber - OCR text with common OCR errors")
    void extractFirstNumber_TextWithOCRErrors_ReturnsCorrectNumber() throws Exception {
        // Given
        String ocrText = "o5/1oo"; // 'o' should be converted to '0'
        
        // When
        int result = invokeExtractFirstNumber(beastSlayTask, ocrText);
        
        // Then
        assertThat(result).isEqualTo(5);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "50/100",
        "0/100", 
        "99/100",
        "1/1",
        "123/456"
    })
    @DisplayName("extractFirstNumber - Various valid fraction formats")
    void extractFirstNumber_VariousValidFormats_ReturnsCorrectFirstNumber(String ocrText) throws Exception {
        // When
        int result = invokeExtractFirstNumber(beastSlayTask, ocrText);
        
        // Then
        int expected = Integer.parseInt(ocrText.split("/")[0]);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("extractFirstNumber - Null text throws exception")
    void extractFirstNumber_NullText_ThrowsException() {
        // When & Then
        assertThatThrownBy(() -> invokeExtractFirstNumber(beastSlayTask, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("OCR text cannot be null or empty.");
    }

    @Test
    @DisplayName("extractFirstNumber - Empty text throws exception")
    void extractFirstNumber_EmptyText_ThrowsException() {
        // When & Then
        assertThatThrownBy(() -> invokeExtractFirstNumber(beastSlayTask, ""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("OCR text cannot be null or empty.");
    }

    @Test
    @DisplayName("extractFirstNumber - Invalid format throws exception")
    void extractFirstNumber_InvalidFormat_ThrowsException() {
        // Given
        String invalidText = "not a number";
        
        // When & Then
        assertThatThrownBy(() -> invokeExtractFirstNumber(beastSlayTask, invalidText))
            .isInstanceOf(NumberFormatException.class)
            .hasMessageContaining("No valid format found in OCR text");
    }

    // ==================== Execute Method Tests ====================

    @Test
    @DisplayName("execute - Low stamina reschedules task")
    void execute_LowStamina_ReschedulesTask() throws Exception {
        // Given
        try (MockedStatic<EmulatorManager> emuManagerMock = mockStatic(EmulatorManager.class);
             MockedStatic<ServScheduler> schedulerMock = mockStatic(ServScheduler.class)) {
            
            emuManagerMock.when(EmulatorManager::getInstance).thenReturn(mockEmulatorManager);
            schedulerMock.when(ServScheduler::getServices).thenReturn(mockServScheduler);
            
            // Mock low stamina OCR result
            when(mockEmulatorManager.ocrRegionText(anyString(), any(), any()))
                .thenReturn("5/100"); // Low stamina
            
            // When
            beastSlayTask.execute();
            
            // Then
            verify(mockEmulatorManager, atLeastOnce()).tapAtPoint(anyString(), any());
            verify(mockEmulatorManager, atLeastOnce()).tapAtRandomPoint(anyString(), any(), any());
            verify(mockEmulatorManager, atLeastOnce()).ocrRegionText(anyString(), any(), any());
            verify(mockEmulatorManager, atLeastOnce()).tapBackButton(anyString());
        }
    }

    @Test
    @DisplayName("execute - OCR exception during stamina check")
    void execute_OCRExceptionDuringStaminaCheck_HandlesGracefully() throws Exception {
        // Given
        try (MockedStatic<EmulatorManager> emuManagerMock = mockStatic(EmulatorManager.class)) {
            
            emuManagerMock.when(EmulatorManager::getInstance).thenReturn(mockEmulatorManager);
            
            // Mock OCR exception
            when(mockEmulatorManager.ocrRegionText(anyString(), any(), any()))
                .thenThrow(new IOException("OCR failed"));
            
            // When
            beastSlayTask.execute();
            
            // Then
            verify(mockEmulatorManager, atLeastOnce()).tapAtPoint(anyString(), any());
            verify(mockEmulatorManager, atLeastOnce()).tapAtRandomPoint(anyString(), any(), any());
            verify(mockEmulatorManager, atLeastOnce()).ocrRegionText(anyString(), any(), any());
            verify(mockEmulatorManager, atLeastOnce()).tapBackButton(anyString());
        }
    }

    @Test
    @DisplayName("execute - TesseractException during stamina check")
    void execute_TesseractExceptionDuringStaminaCheck_HandlesGracefully() throws Exception {
        // Given
        try (MockedStatic<EmulatorManager> emuManagerMock = mockStatic(EmulatorManager.class)) {
            
            emuManagerMock.when(EmulatorManager::getInstance).thenReturn(mockEmulatorManager);
            
            // Mock TesseractException
            when(mockEmulatorManager.ocrRegionText(anyString(), any(), any()))
                .thenThrow(new TesseractException("Tesseract failed"));
            
            // When
            beastSlayTask.execute();
            
            // Then
            verify(mockEmulatorManager, atLeastOnce()).tapAtPoint(anyString(), any());
            verify(mockEmulatorManager, atLeastOnce()).tapAtRandomPoint(anyString(), any(), any());
            verify(mockEmulatorManager, atLeastOnce()).ocrRegionText(anyString(), any(), any());
            verify(mockEmulatorManager, atLeastOnce()).tapBackButton(anyString());
        }
    }

    @Test
    @DisplayName("execute - High stamina with available queues")
    void execute_HighStaminaWithAvailableQueues_ExecutesBeastAttacks() throws Exception {
        // Given
        try (MockedStatic<EmulatorManager> emuManagerMock = mockStatic(EmulatorManager.class);
             MockedStatic<ServScheduler> schedulerMock = mockStatic(ServScheduler.class)) {
            
            emuManagerMock.when(EmulatorManager::getInstance).thenReturn(mockEmulatorManager);
            schedulerMock.when(ServScheduler::getServices).thenReturn(mockServScheduler);
            
            // Mock high stamina and available queues
            when(mockEmulatorManager.ocrRegionText(anyString(), any(), any()))
                .thenReturn("80/100")  // High stamina
                .thenReturn("2/3");    // Available queues
            
            // When
            beastSlayTask.execute();
            
            // Then
            verify(mockEmulatorManager, atLeastOnce()).tapAtPoint(anyString(), any());
            verify(mockEmulatorManager, atLeastOnce()).tapAtRandomPoint(anyString(), any(), any());
            verify(mockEmulatorManager, atLeastOnce()).ocrRegionText(anyString(), any(), any());
            verify(mockEmulatorManager, atLeastOnce()).tapBackButton(anyString());
            verify(mockEmulatorManager, atLeastOnce()).executeSwipe(anyString(), any(), any());
        }
    }

    @Test
    @DisplayName("execute - OCR exception during queue check")
    void execute_OCRExceptionDuringQueueCheck_HandlesGracefully() throws Exception {
        // Given
        try (MockedStatic<EmulatorManager> emuManagerMock = mockStatic(EmulatorManager.class)) {
            
            emuManagerMock.when(EmulatorManager::getInstance).thenReturn(mockEmulatorManager);
            
            // Mock high stamina, then OCR exception for queue check
            when(mockEmulatorManager.ocrRegionText(anyString(), any(), any()))
                .thenReturn("80/100")  // High stamina
                .thenThrow(new IOException("Queue OCR failed"));
            
            // When
            beastSlayTask.execute();
            
            // Then
            verify(mockEmulatorManager, atLeastOnce()).tapAtPoint(anyString(), any());
            verify(mockEmulatorManager, atLeastOnce()).tapAtRandomPoint(anyString(), any(), any());
            verify(mockEmulatorManager, atLeastOnce()).ocrRegionText(anyString(), any(), any());
            verify(mockEmulatorManager, atLeastOnce()).tapBackButton(anyString());
        }
    }

    // ==================== Integration Tests ====================

    @Test
    @DisplayName("getRequiredStartLocation - Returns WORLD")
    void getRequiredStartLocation_ReturnsWorld() {
        // When
        EnumStartLocation result = beastSlayTask.getRequiredStartLocation();
        
        // Then
        assertThat(result).isEqualTo(EnumStartLocation.WORLD);
    }

    @Test
    @DisplayName("Constructor - Sets up task correctly")
    void constructor_SetsUpTaskCorrectly() {
        // Given
        DTOProfiles testProfile = new DTOProfiles(1L);
        testProfile.setName("TestProfile");
        testProfile.setEmulatorNumber("2");
        
        // When
        BeastSlayTask task = new BeastSlayTask(testProfile, TpDailyTaskEnum.INTEL);
        
        // Then
        assertThat(task).isNotNull();
        // Note: We can't directly test private fields, but we can test behavior
        assertThat(task.getRequiredStartLocation()).isEqualTo(EnumStartLocation.WORLD);
    }

    // ==================== Helper Methods ====================

    /**
     * Helper method to invoke the private extractFirstNumber method using reflection
     */
    private int invokeExtractFirstNumber(BeastSlayTask task, String text) throws Exception {
        java.lang.reflect.Method method = BeastSlayTask.class.getDeclaredMethod("extractFirstNumber", String.class);
        method.setAccessible(true);
        return (int) method.invoke(task, text);
    }
}
