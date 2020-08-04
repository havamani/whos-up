package com.whosup.service;

import java.util.List;
import java.util.UUID;

import com.whosup.model.User;
import com.whosup.model.UserContact;
import com.whosup.model.UserContactRequest;
import com.whosup.model.request.NetworkRequest;
import com.whosup.model.response.ContactCount;
import com.whosup.model.response.Domain;
import com.whosup.model.response.MyCrowd;
import com.whosup.model.response.Profile;
import com.whosup.model.response.Status;
import com.whosup.model.response.Suggestion;

public interface NetworkService {

	Status updateProfilePicture(User user, String picturePath);

	Status updateUserLocation(User user, NetworkRequest request);

	Status addEmail(User user, String emailID);

	Status verifyEmail(User user, String emailID, String code);

	ContactCount getContactCount(UUID userID);

	List<UserContact> contacts(UUID userID, String filterName);

	List<UserContactRequest> contactResponse(NetworkRequest request);

	Status updateProfile(User user, NetworkRequest profile);

	Suggestion importMobile(UUID userID, NetworkRequest contact);

	Suggestion importGmail(UUID userID, NetworkRequest contact);

	List<UserContactRequest> requestList(UUID userID);

	Suggestion suggestion(UUID userID);

	Profile friendProfile(UUID userID, UUID contactID);

	Status signOut(User user);

	MyCrowd selectCrowd(UUID userID, NetworkRequest crowdRequest);

	Status setFavourite(UUID userID, UUID contactId, Boolean isFavourite);

	Status removeEmail(User user, String emailID);

	Status addMobile(User user, String mobileID);

	Status verifyMobile(User user, String mobileID, String code);

	Suggestion contactRequest(User user, NetworkRequest contactRequest);

	Domain emailDomain(User user);

	Suggestion inviteContact(User user, String id);

	void contactChangeAfterNewId(User user, String id);

	List<User> contactSearch(UUID userID, String search);
	
	Status autoFriend(User user, UUID contactID);

	Status deleteSuggestion(UUID userID, UUID contactId, String mobileOrEmailId);
	
}