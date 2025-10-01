@echo off
echo ========================================
echo    COMPILANDO CHROME MONITOR APK
echo ========================================
echo.

REM Verificar si Java está instalado
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java no está instalado o no está en el PATH
    echo Por favor instala Java JDK 8 o superior
    echo Descarga desde: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

echo ✓ Java encontrado
echo.

REM Crear directorios necesarios si no existen
if not exist "gradle\wrapper" mkdir "gradle\wrapper"

REM Descargar gradle wrapper si no existe
if not exist "gradle\wrapper\gradle-wrapper.jar" (
    echo Descargando Gradle Wrapper...
    powershell -Command "Invoke-WebRequest -Uri 'https://github.com/gradle/gradle/raw/v8.0.0/gradle/wrapper/gradle-wrapper.jar' -OutFile 'gradle\wrapper\gradle-wrapper.jar'"
    if %errorlevel% neq 0 (
        echo ERROR: No se pudo descargar Gradle Wrapper
        echo Verifica tu conexión a internet
        pause
        exit /b 1
    )
    echo ✓ Gradle Wrapper descargado
)

echo.
echo Compilando APK...
echo Esto puede tomar unos minutos la primera vez...
echo.

REM Compilar el APK
gradlew.bat assembleDebug

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo    ✓ APK COMPILADO EXITOSAMENTE
    echo ========================================
    echo.
    echo El APK se encuentra en:
    echo app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo Para instalar en tu Honor:
    echo 1. Copia el archivo app-debug.apk a tu teléfono
    echo 2. Activa "Fuentes desconocidas" en Configuración
    echo 3. Toca el archivo APK para instalarlo
    echo.
) else (
    echo.
    echo ========================================
    echo    ✗ ERROR EN LA COMPILACIÓN
    echo ========================================
    echo.
    echo Revisa los errores arriba para más detalles
    echo.
)

pause