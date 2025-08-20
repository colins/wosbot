package cl.camodev.utiles;

/**
 * Utility class for platform detection and platform-specific operations
 */
public class PlatformUtil {
    
    /**
     * Operating system types
     */
    public enum OS {
        WINDOWS,
        MACOS,
        LINUX,
        UNKNOWN
    }
    
    /**
     * Architecture types
     */
    public enum ARCH {
        X86_64,
        ARM64,
        UNKNOWN
    }
    
    private static final OS currentOS = detectOS();
    private static final ARCH currentArch = detectArch();
    
    /**
     * Detects the current operating system
     */
    private static OS detectOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return OS.WINDOWS;
        } else if (osName.contains("mac")) {
            return OS.MACOS;
        } else if (osName.contains("linux") || osName.contains("unix")) {
            return OS.LINUX;
        } else {
            return OS.UNKNOWN;
        }
    }
    
    /**
     * Detects the current architecture
     */
    private static ARCH detectArch() {
        String arch = System.getProperty("os.arch").toLowerCase();
        if (arch.contains("x86_64") || arch.contains("amd64")) {
            return ARCH.X86_64;
        } else if (arch.contains("aarch64") || arch.contains("arm64")) {
            return ARCH.ARM64;
        } else {
            return ARCH.UNKNOWN;
        }
    }
    
    /**
     * Gets the current operating system
     */
    public static OS getOS() {
        return currentOS;
    }
    
    /**
     * Gets the current architecture
     */
    public static ARCH getArch() {
        return currentArch;
    }
    
    /**
     * Checks if the current platform is Windows
     */
    public static boolean isWindows() {
        return currentOS == OS.WINDOWS;
    }
    
    /**
     * Checks if the current platform is macOS
     */
    public static boolean isMacOS() {
        return currentOS == OS.MACOS;
    }
    
    /**
     * Checks if the current platform is Linux
     */
    public static boolean isLinux() {
        return currentOS == OS.LINUX;
    }
    
    /**
     * Gets the appropriate file extension for executables on the current platform
     */
    public static String getExecutableExtension() {
        return isWindows() ? ".exe" : "";
    }
    
    /**
     * Gets the appropriate file extension for native libraries on the current platform
     */
    public static String getNativeLibraryExtension() {
        if (isWindows()) {
            return ".dll";
        } else if (isMacOS()) {
            return ".dylib";
        } else {
            return ".so";
        }
    }
    
    /**
     * Gets the appropriate executable name for the given base name
     */
    public static String getExecutableName(String baseName) {
        return baseName + getExecutableExtension();
    }
    
    /**
     * Gets the appropriate native library name for the given base name
     */
    public static String getNativeLibraryName(String baseName) {
        return baseName + getNativeLibraryExtension();
    }
    
    /**
     * Gets platform-specific path separator
     */
    public static String getPathSeparator() {
        return System.getProperty("file.separator");
    }
    
    /**
     * Gets the default ADB executable name for the current platform
     */
    public static String getAdbExecutableName() {
        return getExecutableName("adb");
    }
    
    /**
     * Gets the default OpenCV library name for the current platform
     */
    public static String getOpenCVLibraryName() {
        return getNativeLibraryName("opencv_java4110");
    }
} 