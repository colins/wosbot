@echo off
echo Windows Native Libraries Download Script for Whiteout Survival Bot
echo ================================================================

echo Creating directory structure...
if not exist "wos-hmi\lib\opencv" mkdir "wos-hmi\lib\opencv"
if not exist "wos-hmi\adb" mkdir "wos-hmi\adb"
if not exist "wos-utiles\src\main\resources\native\opencv" mkdir "wos-utiles\src\main\resources\native\opencv"

echo Downloading ADB for Windows...
powershell -Command "Invoke-WebRequest -Uri 'https://dl.google.com/android/repository/platform-tools-latest-windows.zip' -OutFile 'platform-tools.zip'"

echo Extracting ADB...
powershell -Command "Expand-Archive -Path 'platform-tools.zip' -DestinationPath '.' -Force"

echo Copying ADB files...
copy "platform-tools\adb.exe" "wos-hmi\adb\"
copy "platform-tools\AdbWinApi.dll" "wos-hmi\adb\"
copy "platform-tools\AdbWinUsbApi.dll" "wos-hmi\adb\"
copy "platform-tools\adb.exe" "wos-utiles\src\main\resources\native\"

echo Cleaning up...
rmdir /s /q platform-tools
del platform-tools.zip

echo.
echo ADB downloaded and installed successfully!
echo.
echo Next steps:
echo 1. Download OpenCV for Windows from: https://opencv.org/releases/
echo 2. Extract the OpenCV library to: wos-hmi\lib\opencv\
echo 3. Make sure the library is named: opencv_java4110.dll
echo 4. Build the project with: mvn clean install package
echo.
echo Setup complete! You can now build and run the bot on Windows.
pause 