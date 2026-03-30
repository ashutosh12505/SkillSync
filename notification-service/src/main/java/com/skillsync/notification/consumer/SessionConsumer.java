package com.skillsync.notification.consumer;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.skillsync.notification.config.RabbitConfig;
import com.skillsync.notification.service.EmailService;

@Component
public class SessionConsumer {
	
	@Autowired
	private EmailService emailService;

	@RabbitListener(queues = RabbitConfig.QUEUE_NAME)
	public void receiveMessage(String message) {

	    System.out.println("MESSAGE RECEIVED: " + message);
	    emailService.sendEmail("ashutosh12501@gmail.com", message);
	}
}