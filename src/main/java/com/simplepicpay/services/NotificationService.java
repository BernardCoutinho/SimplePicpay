package com.simplepicpay.services;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.simplepicpay.domain.user.User;
import com.simplepicpay.dtos.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Autowired
    private RestTemplate restTemplate;

    public void sendNotificatiom(User user, String message) throws Exception{

//        String email = user.getEmail();
//        NotificationDTO notificationRequest = new NotificationDTO(email, message);
//        ResponseEntity<String> notificationResponse = restTemplate.postForEntity("(http://o4d9z.mocklab.io/notify", notificationRequest, String.class);
//
//        if(!(notificationResponse.getStatusCode() == HttpStatus.OK)){
//            System.out.println("Error sending notification");
//            throw new Exception("Notification service is down.");
//        }
        System.out.println(message);

    }
}
