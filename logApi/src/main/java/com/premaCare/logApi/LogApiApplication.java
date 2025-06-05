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
						new FileInputStream("logApi/src/main/resources/premacare-75bc7-firebase-adminsdk-fbsvc-999d0be7c2.json")
				))
				.build();
		FirebaseApp.initializeApp(options);

		SpringApplication.run(LogApiApplication.class, args);
	}
}

