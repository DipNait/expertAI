package it.alex;

import ai.expert.nlapi.security.*;
import ai.expert.nlapi.security.Authenticator;
import ai.expert.nlapi.v2.API;
import ai.expert.nlapi.v2.cloud.Analyzer;
import ai.expert.nlapi.v2.cloud.AnalyzerConfig;
import ai.expert.nlapi.v2.cloud.Categorizer;
import ai.expert.nlapi.v2.cloud.CategorizerConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class ConfigurationBean {

    @Value("${expertai.username}")
    String username;

    @Value("${expertai.password}")
    String password;

    @Value("${expertai.auth.url}")
    String authUrl;

    public static final MediaType CONTENT = MediaType.get("application/json");

    @Bean
    public Analyzer analyzer() throws Exception {

        Response response;
        RequestBody body;

        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);

        body = RequestBody.create(json.toString(),CONTENT);

        Request request = new Request.Builder().url(authUrl).post(body).build();
        String token = "";
        try {
            OkHttpClient httpClient = new OkHttpClient.Builder().readTimeout(Long.parseLong("5000"), TimeUnit.SECONDS).build();
            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                log.error("Error calling WS for retrieve the TOKEN: TOKEN NOT RETRIEVED");
            } else {
                token = response.body().string();
                response.close();
            }
        } catch (IOException ex) {
            log.error("Exception e :",ex);
        }

        Credential credential = new Credential(username,password,token);
        Authenticator authenticator = new BasicAuthenticator(credential);

        Authentication authentication = new Authentication(authenticator);
        return new Analyzer(AnalyzerConfig.builder()
                .withVersion(API.Versions.V2)
                .withContext("standard")
                .withLanguage(API.Languages.en)
                .withAuthentication(authentication)
                .build());
    }

    @Bean
    public Categorizer categorizer() throws Exception {

        Response response;
        RequestBody body;

        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);

        body = RequestBody.create(json.toString(),CONTENT);

        Request request = new Request.Builder().url(authUrl).post(body).build();
        String token = "";
        try {
            OkHttpClient httpClient = new OkHttpClient.Builder().readTimeout(Long.parseLong("5000"), TimeUnit.SECONDS).build();
            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                log.error("Error calling WS for retrieve the TOKEN: TOKEN NOT RETRIEVED");
            } else {
                token = response.body().string();
                response.close();
            }
        } catch (IOException ex) {
            log.error("Exception e :",ex);
        }

        Credential credential = new Credential(username,password,token);
        Authenticator authenticator = new BasicAuthenticator(credential);

        Authentication authentication = new Authentication(authenticator);
        return new Categorizer(CategorizerConfig.builder()
                .withVersion(API.Versions.V2)
                .withTaxonomy("iptc")
                .withLanguage(API.Languages.en)
                .withAuthentication(authentication)
                .build());
    }

}
