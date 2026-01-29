
package com.example.EWaste_Management_System.Service;

/*
import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
@RequiredArgsConstructor
public class EmailService {

   /*
    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);
*//*
                try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("yourgmail@gmail.com");

            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }

     @Value("${BREVO_API_KEY}")
    private String apiKey;

    public void sendEmail(String to, String subject, String content) throws IOException {

        OkHttpClient client = new OkHttpClient();

        String json = """
        {
          "sender": { "email": "verified@yourdomain.com" },
          "to": [ { "email": "%s" } ],
          "subject": "%s",
          "htmlContent": "%s"
        }
        """.formatted(to, subject, content);

        Request request = new Request.Builder()
            .url("https://api.brevo.com/v3/smtp/email")
            .post(RequestBody.create(json, MediaType.parse("application/json")))
            .addHeader("api-key", apiKey)
            .addHeader("Content-Type", "application/json")
            .build();

        client.newCall(request).execute();


    }
}
*/

/*
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public void sendEmail(String to, String subject, String content) {

        OkHttpClient client = new OkHttpClient();

        String json = """
        {
          "sender": { "email": "demomailforstudy@gmail.com", "name": "E-Waste System" },
          "to": [ { "email": "%s" } ],
          "subject": "%s",
          "htmlContent": "%s"
        }
        """.formatted(to, subject, content);

        Request request = new Request.Builder()
                .url("https://api.brevo.com/v3/smtp/email")
                .post(RequestBody.create(json, JSON))
                .addHeader("api-key", apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Email failed: " + response.body().string());
            } else {
                System.out.println("Email sent successfully to " + to);
            }
        } catch (IOException e) {
            System.out.println("Email exception: " + e.getMessage());
        }
    }
}
*/


import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public void sendEmail(String to, String subject, String content) {

        try {
            // Build request body safely
            Map<String, Object> body = new HashMap<>();
            body.put("sender", Map.of(
                    "email", "demomailforstudy@gmail.com",
                    "name", "E-Waste System"
            ));
            body.put("to", List.of(Map.of("email", to)));
            body.put("subject", subject);
            body.put("htmlContent", content);

            String json = mapper.writeValueAsString(body);

            Request request = new Request.Builder()
                    .url("https://api.brevo.com/v3/smtp/email")
                    .post(RequestBody.create(json, JSON))
                    .addHeader("api-key", apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.out.println("Email failed: " + response.body().string());
                } else {
                    System.out.println("Email sent successfully to " + to);
                }
            }

        } catch (Exception e) {
            System.out.println("Email exception: " + e.getMessage());
        }
    }
}
