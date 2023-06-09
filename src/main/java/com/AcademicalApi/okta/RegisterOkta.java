package com.AcademicalApi.okta;

import com.AcademicalApi.objects.Account;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Properties;

import static com.AcademicalApi.helpers.ConfigFile.config;


public class RegisterOkta {
    private static final Properties config = config();
    private static final String AUTH_URL = config.getProperty("AUTH_URL");
    private static final String AUTH_TOKEN = config.getProperty("AUTH_TOKEN");
    private static final String APP_ID = config.getProperty("APP_ID");

    public static HttpResponse registerUserToAuthProvider(Account account) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(AUTH_URL + "/api/v1/users?active=true");

        request.setHeader("Authorization", AUTH_TOKEN);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");

        String jsonBody = """
            {
              "profile": {
                "firstName": "%s",
                "lastName": "%s",
                "email": "%s",
                "login": "%s"
              },
              "credentials": {
                "password": {
                  "value": "%s"
                }
              }
            }
        """;
        jsonBody = String.format(
                jsonBody,
                account.firstName(),
                account.lastName(),
                account.username(),
                account.username(),
                account.password()
        );

        StringEntity jsonBodyWithCreds = new StringEntity(jsonBody);
        jsonBodyWithCreds.setContentType("application/json");
        request.setEntity(jsonBodyWithCreds);

        return client.execute(request);
    }

    public static HttpResponse assignUserToApp(String userId) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(AUTH_URL + "/api/v1/apps/" + APP_ID + "/users/" + userId);

        request.setHeader("Authorization", AUTH_TOKEN);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");

        return client.execute(request);
    }
}
