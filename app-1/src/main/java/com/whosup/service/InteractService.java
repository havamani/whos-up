package com.whosup.service;

import java.util.Date;
import java.util.UUID;

import com.whosup.model.User;
import com.whosup.model.request.InteractRequest;
import com.whosup.model.response.ConfirmRequest;
import com.whosup.model.response.EventSettingResponse;
import com.whosup.model.response.Status;
import com.whosup.model.response.UserEvent;

public interface InteractService {

	UserEvent interactView(UUID userID);

	EventSettingResponse eventSetting(UUID eventId);

	Status eventDate(InteractRequest request);

	Status deleteEvent(UUID userID, UUID eventId);

	Status deleteUser(UUID userID, UUID eventId, UUID deleteUserId);

	Status gatherAgain(UUID userId, UUID eventId);

	Status bookingConfirm(User user, InteractRequest request);

	Status editPlaces(UUID userId, InteractRequest request);

	Status bookingCancel(User user, UUID eventId);

	ConfirmRequest confirmRequest(UUID userID, UUID eventId);

	Status resignOrganizer(UUID userID, UUID eventId);

	Status acceptOrganizer(UUID userID, UUID eventId);

	Status unConfirmEvent(UUID userID, UUID eventId);

	Status reject(UUID userID, UUID eventId);

	Status addPicture(UUID userID, InteractRequest request);

	Status likePicture(UUID eventId, Date pictureDate, UUID userId);

	Status unlikePicture(UUID eventId, Date pictureDate, UUID userId);

	Status confirmEvent(User user, UUID eventId);

	Status getFeedback(UUID userID, UUID eventId);

	Status feedback(UUID userID, InteractRequest request);

	Status eventLocation(InteractRequest request, UUID userId);

	Status getPicture(UUID eventId, UUID userId);

	Status editSettings(InteractRequest request, UUID userId);

}