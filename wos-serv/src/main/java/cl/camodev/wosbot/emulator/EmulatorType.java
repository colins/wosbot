package cl.camodev.wosbot.emulator;

import cl.camodev.wosbot.console.enumerable.EnumConfigurationKey;
import cl.camodev.utiles.PlatformUtil;

import java.io.File;

public enum EmulatorType {
	// @formatter:off
    MUMU("MuMuPlayer", EnumConfigurationKey.MUMU_PATH_STRING.name(), getExecutableName("MuMuManager"), getDefaultPath("MuMuPlayer")),
    MEMU("MEmu Player", EnumConfigurationKey.MEMU_PATH_STRING.name(), getExecutableName("memuc"), getDefaultPath("MEmu")),
    LDPLAYER("LDPlayer", EnumConfigurationKey.LDPLAYER_PATH_STRING.name(), getExecutableName("ldconsole"), getDefaultPath("LDPlayer"));
	    // @formatter:on

	private final String displayName;
	private final String configKey;
	private final String executableName;
	private final String defaultPath;

	EmulatorType(String displayName, String configKey, String executableName, String defaultPath) {
		this.displayName = displayName;
		this.configKey = configKey;
		this.executableName = executableName;
		this.defaultPath = defaultPath;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getConfigKey() {
		return configKey;
	}

	public String getExecutableName() {
		return executableName;
	}

	public String getDefaultPath() {
		return defaultPath + File.separator + executableName;
	}
	
	/**
	 * Gets the appropriate executable name based on the operating system
	 */
	private static String getExecutableName(String baseName) {
		return PlatformUtil.getExecutableName(baseName);
	}
	
	/**
	 * Gets the default installation path based on the operating system
	 */
	private static String getDefaultPath(String emulatorName) {
		if (PlatformUtil.isWindows()) {
			switch (emulatorName) {
				case "MuMuPlayer":
					return "C:\\Program Files\\Netease\\MuMuPlayerGlobal-12.0\\shell\\";
				case "MEmu":
					return "C:\\Program Files\\Microvirt\\MEmu\\";
				case "LDPlayer":
					return "C:\\LDPlayer\\LDPlayer9\\";
				default:
					return "";
			}
		} else if (PlatformUtil.isMacOS()) {
			switch (emulatorName) {
				case "MuMuPlayer":
					return "/Applications/MuMu Player.app/Contents/MacOS/";
				case "MEmu":
					return "/Applications/MEmu.app/Contents/MacOS/";
				case "LDPlayer":
					return "/Applications/LDPlayer.app/Contents/MacOS/";
				default:
					return "";
			}
		} else {
			// Linux and other Unix-like systems
			switch (emulatorName) {
				case "MuMuPlayer":
					return "/opt/mumu-player/";
				case "MEmu":
					return "/opt/memu/";
				case "LDPlayer":
					return "/opt/ldplayer/";
				default:
					return "";
			}
		}
	}
}
