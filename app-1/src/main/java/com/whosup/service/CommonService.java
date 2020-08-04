package com.whosup.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.whosup.model.Event;
import com.whosup.model.User;
import com.whosup.model.request.ContactDetail;
import com.whosup.model.request.NetworkRequest;
import com.whosup.model.response.Status;

public interface CommonService {

	String uploadImage(InputStream upload,
			FormDataContentDisposition contentDispositionHeader);

	String createChatRoom(Event event);

	String addUser(UUID event, UUID user);

	String removeUser(UUID event, UUID user);

	List<ContactDetail> gmailContacts(String accessToken);

	Status message(String code, String mobileNumber);

	void sendMail(String emailID, String Subject, String message);

	Boolean notify(User user, String message) throws IOException;

	Status kafkaProducerUser(UUID userId);

	Status kafkaProducerBuzz(UUID userId, UUID eventId, Boolean isGather);

	String createUser(String nonce, UUID userID) throws Exception;

	JSONObject getCurrentWeather(String latLong);

	NetworkRequest locationDetails(String location);

	String uploadSmallImage(InputStream upload);
	
	String confirmedMessage(String chatId, User user, String message); 

}
