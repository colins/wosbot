#!/bin/bash

# macOS Native Libraries Download Script for Whiteout Survival Bot
# This script downloads the appropriate OpenCV and ADB binaries for macOS

set -e

echo "🍎 macOS Native Libraries Download Script for Whiteout Survival Bot"
echo "================================================================"

# Detect architecture
ARCH=$(uname -m)
if [ "$ARCH" = "arm64" ] || [ "$ARCH" = "aarch64" ]; then
    ARCH_NAME="arm64"
    echo "📱 Detected Apple Silicon (ARM64) architecture"
else
    ARCH_NAME="x86_64"
    echo "💻 Detected Intel (x86_64) architecture"
fi

# Create directories
echo "📁 Creating directory structure..."
mkdir -p wos-hmi/lib/opencv/macos-${ARCH_NAME}
mkdir -p wos-hmi/adb
mkdir -p wos-utiles/src/main/resources/native/opencv/macos-${ARCH_NAME}

# Download OpenCV for macOS
echo "📦 Downloading OpenCV for macOS ${ARCH_NAME}..."
if [ "$ARCH_NAME" = "arm64" ]; then
    OPENCV_URL="https://github.com/opencv/opencv/releases/download/4.11.0/opencv-4.11.0-macos-arm64.dmg"
else
    OPENCV_URL="https://github.com/opencv/opencv/releases/download/4.11.0/opencv-4.11.0-macos-x86_64.dmg"
fi

echo "🔗 OpenCV URL: $OPENCV_URL"
echo "⚠️  Note: OpenCV for macOS needs to be downloaded manually from the official website"
echo "   Visit: https://opencv.org/releases/"
echo "   Or use Homebrew: brew install opencv"

# Download ADB for macOS
echo "📱 Downloading ADB for macOS..."
ADB_URL="https://dl.google.com/android/repository/platform-tools-latest-darwin.zip"

echo "🔗 ADB URL: $ADB_URL"
echo "📥 Downloading ADB..."

# Download and extract ADB
curl -L -o platform-tools.zip "$ADB_URL"
unzip -q platform-tools.zip
cp platform-tools/adb wos-hmi/adb/
cp platform-tools/adb wos-utiles/src/main/resources/native/

# Clean up
rm -rf platform-tools platform-tools.zip

echo "✅ ADB downloaded and installed successfully!"
echo ""
echo "📋 Next steps:"
echo "1. Download OpenCV for macOS from: https://opencv.org/releases/"
echo "2. Extract the OpenCV library to: wos-hmi/lib/opencv/macos-${ARCH_NAME}/"
echo "3. Make sure the library is named: libopencv_java4110.dylib"
echo "4. Run: chmod +x wos-hmi/adb/adb"
echo "5. Build the project with: mvn clean install package"
echo ""
echo "🎉 Setup complete! You can now build and run the bot on macOS." 