package com.premaCare.logApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class LogApiApplication {
	public static void main(String[] args) throws Exception {
		// Initialize Firebase
		FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(
						new FileInputStream("/Volumes/T7/bachelor2025/logApiV2/logApi/src/main/resources/firebase-service-accountV2.json")
				))
				.build();
		FirebaseApp.initializeApp(options);

		SpringApplication.run(LogApiApplication.class, args);
	}
}

