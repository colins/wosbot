package cl.camodev.wosbot.emulator.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import cl.camodev.wosbot.emulator.Emulator;
import cl.camodev.utiles.PlatformUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LDPlayerEmulator extends Emulator {

    private static final Logger logger = LoggerFactory.getLogger(LDPlayerEmulator.class);

    public LDPlayerEmulator(String consolePath) {
        super(consolePath);
    }

    @Override
    protected String getDeviceSerial(String emulatorNumber) {
        // LDPlayer usa el formato emulator-XXXX donde XXXX = 5554 + (emulatorNumber * 2)
        int targetPort = 5554 + (Integer.parseInt(emulatorNumber) * 2);
        return "emulator-" + targetPort;
    }

    @Override
    public void launchEmulator(String emulatorNumber) {
        String executableName = getExecutableName("ldconsole");
        String[] command = { consolePath + File.separator + executableName, "launch", "--index", emulatorNumber };
        executeCommand(command);
        logger.info("LDPlayer launched at index {}", emulatorNumber);
    }

    @Override
    public void closeEmulator(String emulatorNumber) {
        String executableName = getExecutableName("ldconsole");
        String[] command = { consolePath + File.separator + executableName, "quit", "--index", emulatorNumber };
        executeCommand(command);
        logger.info("LDPlayer closed at index {}", emulatorNumber);
    }

    @Override
    public boolean isRunning(String emulatorNumber) {
        try {
            String executableName = getExecutableName("ldconsole");
            String[] command = { consolePath + File.separator + executableName, "isrunning", "--index", emulatorNumber };
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(consolePath).getParentFile());

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();

            process.waitFor();

            return line != null && line.trim().equalsIgnoreCase("running");
        } catch (IOException | InterruptedException e) {
            logger.error("Error checking if emulator is running", e);
        }
        return false;
    }

    private void executeCommand(String[] command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(consolePath).getParentFile());
            Process process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            logger.error("Error executing command", e);
        }
    }
    
    /**
     * Gets the appropriate executable name based on the operating system
     */
    private String getExecutableName(String baseName) {
        return PlatformUtil.getExecutableName(baseName);
    }
}
