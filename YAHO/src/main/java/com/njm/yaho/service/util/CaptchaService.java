package com.njm.yaho.service.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

@Service
public class CaptchaService {

    private final String SECRET_KEY = "6LfUSyArAAAAAKod3sOkOR-jlBszRa6TlWwZKvN3";

    public boolean verifyCaptcha(String token) {
        try {
            String url = "https://www.google.com/recaptcha/api/siteverify";
            HttpClient client = HttpClient.newHttpClient();

            String body = "secret=" + SECRET_KEY + "&response=" + token;

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body().contains("\"success\": true");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

