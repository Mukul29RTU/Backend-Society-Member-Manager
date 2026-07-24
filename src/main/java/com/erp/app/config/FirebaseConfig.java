//package com.erp.app.config;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//import java.io.InputStream;
//
//@Configuration
//public class FirebaseConfig {
//
//    @PostConstruct
//    public void initialize() {
//        try {
//            // Load the JSON file from the resources folder
//            InputStream serviceAccount = getClass().getClassLoader()
//                    .getResourceAsStream("serviceAccountKey.json");
//
//            if (serviceAccount == null) {
//                throw new RuntimeException("serviceAccountKey.json not found in resources!");
//            }
//
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//
//            // Initialize only if no apps exist to avoid "app already exists" errors
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options);
//                System.out.println("Firebase Admin SDK Initialized Successfully!");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}