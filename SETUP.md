# Whiteout Survival Bot Setup Guide

This guide will help you set up the Whiteout Survival Bot on your preferred operating system.

## 🖥️ System Requirements

- **Java**: Version 21 or later
- **Maven**: Version 3.6 or later
- **Android Emulator**: MuMu Player, MEmu Player, or LDPlayer
- **Operating System**: Windows 10/11, macOS 10.14+, or Linux

## 🚀 Quick Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd cs_wosbot
```

### 2. Download Native Libraries

#### Windows Users
```cmd
download-windows-libs.bat
```

#### macOS Users
```bash
chmod +x download-macos-libs.sh
./download-macos-libs.sh
```

#### Linux Users
```bash
# Create directories manually
mkdir -p wos-hmi/lib/opencv/linux-x86_64
mkdir -p wos-hmi/adb
mkdir -p wos-utiles/src/main/resources/native/opencv/linux-x86_64

# Download ADB
curl -L -o platform-tools.zip "https://dl.google.com/android/repository/platform-tools-latest-linux.zip"
unzip platform-tools.zip
cp platform-tools/adb wos-hmi/adb/
cp platform-tools/adb wos-utiles/src/main/resources/native/
rm -rf platform-tools platform-tools.zip
chmod +x wos-hmi/adb/adb
```

### 3. Download OpenCV Libraries

**Important**: OpenCV libraries need to be downloaded manually from the official website.

1. Visit [OpenCV Releases](https://opencv.org/releases/)
2. Download the appropriate version for your platform:
   - **Windows**: `opencv-4.11.0-windows.exe`
   - **macOS**: `opencv-4.11.0-macos-arm64.dmg` (Apple Silicon) or `opencv-4.11.0-macos-x86_64.dmg` (Intel)
   - **Linux**: `opencv-4.11.0-linux-x86_64.tar.gz`

3. Extract and copy the Java bindings:
   - **Windows**: Copy `opencv_java4110.dll` to `wos-hmi/lib/opencv/`
   - **macOS**: Copy `libopencv_java4110.dylib` to `wos-hmi/lib/opencv/macos-arm64/` or `wos-hmi/lib/opencv/macos-x86_64/`
   - **Linux**: Copy `libopencv_java4110.so` to `wos-hmi/lib/opencv/linux-x86_64/`

### 4. Build the Project
```bash
mvn clean install package
```

### 5. Run the Bot
```bash
java -jar wos-hmi/target/wos-bot-1.5.3.jar
```

## 🔧 Manual Setup (Alternative)

If the automated scripts don't work, you can set up the project manually:

### Directory Structure
```
cs_wosbot/
├── wos-hmi/
│   ├── lib/
│   │   ├── opencv/
│   │   │   ├── opencv_java4110.dll          # Windows
│   │   │   ├── macos-arm64/
│   │   │   │   └── libopencv_java4110.dylib # macOS Apple Silicon
│   │   │   ├── macos-x86_64/
│   │   │   │   └── libopencv_java4110.dylib # macOS Intel
│   │   │   └── linux-x86_64/
│   │   │       └── libopencv_java4110.so    # Linux
│   │   └── tesseract/
│   │       └── [Tesseract files]
│   └── adb/
│       ├── adb.exe                           # Windows
│       ├── adb                               # macOS/Linux
│       ├── AdbWinApi.dll                     # Windows only
│       └── AdbWinUsbApi.dll                  # Windows only
└── wos-utiles/
    └── src/
        └── main/
            └── resources/
                └── native/
                    └── opencv/
                        └── [Same structure as above]
```

### Platform-Specific Notes

#### Windows
- Uses `.exe` and `.dll` files
- Default emulator paths are in `C:\Program Files\`
- ADB requires Windows-specific DLLs

#### macOS
- Uses no extension for executables and `.dylib` for libraries
- Default emulator paths are in `/Applications/`
- Supports both Intel and Apple Silicon architectures
- ADB is a Unix binary

#### Linux
- Uses no extension for executables and `.so` for libraries
- Default emulator paths are in `/opt/`
- ADB is a Unix binary

## 🐛 Troubleshooting

### Common Issues

1. **"Library not found" errors**
   - Ensure OpenCV libraries are in the correct directory
   - Check file permissions (especially on macOS/Linux)
   - Verify the library name matches exactly

2. **ADB connection issues**
   - Make sure ADB is executable: `chmod +x wos-hmi/adb/adb`
   - Check if emulator is running and accessible
   - Verify ADB server is running: `wos-hmi/adb/adb devices`

3. **Build failures**
   - Ensure Java 21+ is installed: `java -version`
   - Check Maven version: `mvn -version`
   - Clean and rebuild: `mvn clean install package`

4. **Emulator not found**
   - Verify emulator installation path
   - Check if emulator executable exists
   - Ensure emulator is compatible with your OS

### Platform-Specific Issues

#### macOS
- **Gatekeeper**: Right-click the app and select "Open" if blocked
- **Permissions**: Grant necessary permissions in System Preferences
- **Architecture**: Ensure you're using the correct OpenCV version for your CPU

#### Linux
- **Dependencies**: Install required system libraries: `sudo apt-get install libopencv-dev`
- **Permissions**: Ensure proper file permissions and ownership
- **Display**: Set `DISPLAY` environment variable if running headless

## 📚 Additional Resources

- [OpenCV Documentation](https://docs.opencv.org/)
- [Android Debug Bridge (ADB)](https://developer.android.com/studio/command-line/adb)
- [Maven Documentation](https://maven.apache.org/guides/)
- [JavaFX Documentation](https://openjfx.io/)

## 🤝 Support

If you encounter issues:
1. Check the troubleshooting section above
2. Search existing issues in the repository
3. Create a new issue with detailed information about your setup
4. Join the Discord community for help

---

**Note**: This bot is designed for educational purposes. Please respect the terms of service of the games you use it with. 