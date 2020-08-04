package com.whosup.resource;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.jayway.restassured.response.Response;

public class NetworkResourceTest extends AbstractResourceTest {

	private static String MODULE = "/network";
	
	/***
	 * User's profile details will be provided if valid access Token is provided
	 * 
	 * @param accessToken
	 *            - whosup access Token
	 * @return Profile details of user
	 */
	@Ignore
	public void testSettings() {
		Response res = get(MODULE + "/settings?access_token="+ this.access_token);
		Assert.assertEquals(200, res.getStatusCode());
	}
	/***
	 * This API is used for updating profile picture of a user
	 * 
	 * @param accessToken
	 *            - whosup access Token
	 * @param picturePath
	 *            - path of picture used to update profile picture
	 * @return success or failure message
	 */
	 @Ignore
	 public void testUpdateProfilePicture() throws JSONException{
		 JSONObject obj= new JSONObject();
		 obj.put("accessToken",this.access_token);
		 obj.put("picturePath","C:\\Users\\New System4\\Pictures\\OredooImages\\drugstore.jpg");
				
		 Response res = given()
				    .contentType("application/json").body(obj.toString()).
				       when().
				          post(MODULE + "/profile/picture");
		 assertEquals(200, res.getStatusCode());
		
	 }
	 
	 /***
		 * This API is used to get list of contacts of a user and contacts will be
		 * filter based on given param
		 * 
		 * @param accessToken
		 *            - whosup access Token
		 * @param filterName
		 *            - filter param will be "fb", "ln", "all", "my_nte",
		 *            "my_crowd", "whosup"
		 * @return list of contacts
		 */
	@Ignore
	public void testContacts() throws JSONException {
		Response res = get(MODULE + "/contacts?access_token="+ this.access_token + "&filter_name=ln");
		assertEquals(200, res.getStatusCode());
		JSONArray obj = new JSONArray();
		obj.put(res);
		assertEquals(1, obj.length());
	}
	
	@Ignore
	public void testContactsInvalidFilterName() {
		Response res = get(MODULE + "/contacts?access_token="+ this.access_token);
		assertEquals(400, res.getStatusCode());
	}
	
	@Ignore
	public void testContactSearch() throws JSONException {
		Response res = get(MODULE + "/contact/search?access_token="+ this.access_token + "&search=Sai Teja");
		assertEquals(200, res.getStatusCode());
	}
	
	/***
	 * This API used for saving user's favourite contacts If User is already
	 * Friend then save into user's favourite contacts else if User tries to
	 * make Fav from the Domain list, we make autofriend to the user and
	 * Favourite
	 * 
	 * @param request
	 *            - require fields are accessToken, contactId and favourite or
	 *            not
	 * @return success or failure message
	 */
	
	 @Ignore
	 public void testStarContact() throws JSONException{
		 JSONObject obj= new JSONObject();
		 obj.put("accessToken",this.access_token);
		 obj.put("contactId","8316002a-952d-476c-bea3-f32fcc4f741c");
		 obj.put("isFavourite","true");
			 
		 Response res = given()	
			   .contentType("application/json")
			     .body(obj.toString()).
			        when()
			          .post(MODULE + "/starred");
		assertEquals(200, res.getStatusCode());
	 }
	 
	 /***
		 * user can get their saved emailDomain from this API
		 * 
		 * @param accessToken
		 *            - whosup access Token
		 * @return EmailDomain with no. of users in same domain will be provided
		 */

	 @Ignore
	 public void testEmailDomains() throws JSONException{
		 Response res = get(MODULE + "/emaildomain?access_token=" +this.access_token);
		 assertEquals(200, res.getStatusCode());
	 }
	 
	 /***
		 * This API adds email domain or mobile number depending upon tag field in
		 * request param and sends verification code
		 * 
		 * @param request
		 *            - id, tag and accesstoken are required fields
		 * @return success or failure message
		 */
	 @Ignore
	 public void testAdd() throws JSONException{
		 JSONObject obj= new JSONObject();
		 obj.put("accessToken",this.access_token);
		 obj.put("contactId","39ecf321-16e9-49c2-a6af-b16993392fd6");
		 obj.put("tag","email");
				
		 Response res = given()
				    .contentType("application/json").body(obj.toString()).
				       when().
				          post(MODULE + "/add");
		 assertEquals(200, res.getStatusCode());
	 }
	 
	 /***
		 * This API verifies email domain or mobile number depending upon tag field
		 * in request param
		 * 
		 * @param request
		 *            - id, tag, verification code and accesstoken are required
		 *            fields
		 * @return success or failure message
		 * @throws Exception
		 */
	 @Ignore
	 public void testVerify() throws JSONException{
		 JSONObject obj= new JSONObject();
		 obj.put("accessToken",this.access_token);
		 obj.put("contactId","de6a7d87-dfb2-4aca-914c-1be5cbaa8d37");
		 obj.put("tag", "email");
				
		 Response res = given()
				    .contentType("application/json").body(obj.toString()).
				       when().
				          post(MODULE +"/verify");
		 assertEquals(200, res.getStatusCode());
	 }
	 
	 @Ignore
	 public void testAddWithoutEmail() throws JSONException{
		 Response res = get(MODULE + "/betaDomain?access_token=" +this.access_token);
		 assertEquals(200, res.getStatusCode());
	 }
	 
	 /***
		 * This API user for removing current email domain
		 * 
		 * @param request
		 *            - access token and emailID
		 * @return success or failure message
		 */
	 @Ignore
	 public void testRemoveEmail() throws JSONException{
		 JSONObject obj= new JSONObject();
		 obj.put("accessToken",this.access_token);
		 obj.put("id","betatest@earlyaccessbeta.com");
			
		 Response res = given()
			 .contentType("application/json").body(obj.toString()).
			 when().
			 post(MODULE + "/domain/remove");
		assertEquals(200, res.getStatusCode());
	 }
	 
	 /***
		 * This API provides user's no of contacts and no of contact requests
		 * 
		 * @param accessToken
		 *            - whosup access token
		 * @return contact count and request count
		 */
	 @Ignore
	 public void testContactDetails() throws JSONException{
		 Response res = get(MODULE + "/contact/count?access_token=" +
		 this.access_token);
		 assertEquals(200, res.getStatusCode());
	 }
	 
	 /***
		 * For accepting and rejecting a contact request this APi is used
		 * 
		 * @param request
		 *            - access token, contactId and accept/reject
		 * @return success or failure message
		 */
	 @Ignore
	 public void testContactResponse() throws JSONException{
		 JSONObject obj= new JSONObject();
		 obj.put("accessToken",this.access_token);
		 obj.put("contactId","856815b0-eeb3-45a4-be23-97d1bf4486bb");
		 obj.put("accept","true");
				
		 Response res = given()
				    .contentType("application/json").body(obj.toString()).
				       when().
				          post(MODULE + "/contact/response");
		assertEquals(200, res.getStatusCode());
	 }
	 
	 /***
		 * This API is used to send contact request to a user inside whosup
		 * 
		 * @param request
		 *            - access token and contactId
		 * @return success or failure message
		 */
	 @Ignore
	 public void testContactRequest() throws JSONException{
		 JSONObject obj= new JSONObject();
		 obj.put("accessToken",this.access_token);
		 obj.put("contactId"," c4dfb748-a384-4d3c-8b59-4bf807c62f2a ");
				
		 Response res = given()
				    .contentType("application/json").body(obj.toString()).
				       when().
				          post(MODULE + "/contact/request");
		 assertEquals(200, res.getStatusCode());
	 }
	 
	 /***
		 * To get user's list of contact request this API is used
		 * 
		 * @param accessToken
		 *            - whosup access token
		 * @return success or failure message
		 */
	 
	 @Ignore
	 public void testRequestList() throws JSONException{
		 Response res = get(MODULE + "/contact/requestlist?access_token=" +this.access_token);
		 JSONArray obj = new JSONArray();
		 obj.put(res);
		 assertEquals(1, obj.length());
	}
	 
	 /***
		 * To invite a user's friend to whosup using their email ID
		 * 
		 * @param request
		 *            - access token and contactId
		 * @return success or failure message
		 */
	 @Ignore
	 public void testInviteContact() throws JSONException{
		 JSONObject obj= new JSONObject();
		 obj.put("accessToken",this.access_token);
		 obj.put("contactId","856815b0-eeb3-45a4-be23-97d1bf4486bb");
				
		 Response res = given()
				    .contentType("application/json").body(obj.toString()).
				       when().
				          post(MODULE + "/profile/picture");
		 assertEquals(200, res.getStatusCode());
	 }
	 
	 /***
		 * This API is used to get profile details of friend and their common
		 * interest, friends
		 * 
		 * @param accessToken
		 *            - whosup access token
		 * @param contactId
		 *            - contact user's ID
		 * @return friends profile and common interest
		 */
	 @Ignore
	 public void testFriendProfile() throws JSONException{
		 Response res = get(MODULE + "/friendProfile?access_token=" +
		 this.access_token+"&contactId=8316002a-952d-476c-bea3-f32fcc4f741c");
		 assertEquals(200,res.getStatusCode());
	 }
	 
	 /***
		 * This API is used to save list of favourite contacts in user's contact
		 * list
		 * 
		 * @param request
		 *            - accesstoken and list of selected favourite users
		 * @return list of users
		 */
	 
	 @Ignore
	 public void testMyCrowd() throws JSONException{
		 JSONObject obj= new JSONObject();
		 obj.put("accessToken",this.access_token);
		 obj.put("contactId","de6a7d87-dfb2-4aca-914c-1be5cbaa8d37");
				
		 Response res = given()
				    .contentType("application/json").body(obj.toString()).
				       when().
				          post(MODULE + "/myCrowd");
		 assertEquals(200, res.getStatusCode());
	 }
	 
	@Ignore
	public void testAppStoreLink() throws JSONException{
		Response res = get(MODULE + "/appStore/link?access_token=" +this.access_token);
		assertEquals(200,res.getStatusCode());
	}
	 
	@Ignore
	public void testUser() throws JSONException {
		Response res = get(MODULE + "/getUser?access_token="+ this.access_token);
		assertEquals(200, res.getStatusCode());
	} 
	
	@Ignore
	public void testContactSearchNoSearch() throws JSONException {
		Response res = get(MODULE+"/contact/search?access_token="+this.access_token);
		assertEquals(400, res.getStatusCode());
	}
	
    /*Here we need gmail access code from client side*/
	 @Ignore
	 public void testCheck() throws JSONException{
	 JSONObject obj= new JSONObject();
	 obj.put("accessToken",this.access_token);
	 	try
		 {
			 Response res = given()
			    .contentType("application/json").body(obj.toString()).
			       when().
			          post(MODULE + "/mail");
			 assertEquals(200, res.getStatusCode());
		 }
		 catch(Exception e)
		 {
			 System.out.println("Error");
		 }
	 }
	 
	 @Ignore
	 public void testUpdateSettings() throws JSONException{
		 List<UUID> l=new ArrayList<UUID>();
		 JSONObject obj= new JSONObject();
		 obj.put("accessToken",this.access_token);
		 obj.put("IsAvailableSearch","false");
		 obj.put("isSilent","false");
		 obj.put("sendEmail","false");
	 //    obj.put("systemLanguage",UUID.randomUUID());
	//	 obj.put("additionalLanguage",l);
		 obj.put("systemLanguage","null");
		 obj.put("additionalLanguage","null");
		 try
		 {
			 Response res = given()
			    .contentType("application/json").body(obj.toString()).
			       when().
			          post(MODULE + "/settings/update");
			 assertEquals(200, res.getStatusCode());
		 }
		 catch(Exception e)
		 {
			 System.out.println("Error");
		 }
	 }
	 
	 /*Here we neen to send suggestion to friend user*/
	 @Ignore
	 public void testDeleteSuggestion(){
		Response res=get(MODULE+"/import/delete?access_token="+this.access_token
				+"&contact_id=856815b0-eeb3-45a4-be23-97d1bf4486bb&mobile_or_email_id=vignesh@softsuave.com "); 
		assertEquals(200,res.getStatusCode());
		String json=res.toString();
		System.out.println(json);
	 }
	 
	 @Ignore
	 public void testLinkedLogin() throws JSONException{
	 JSONObject obj= new JSONObject();
	 obj.put("accessToken",this.access_token);
	 obj.put("linkedInAccessToken"," ");
	 obj.put("deviceID","mFZ3nOuGTOo:APA91bGHo6RixEA1aW6_i23RMuocFSH0INV1JNrGvU5s87V7SPaZFkA5U3LPcHjHWFLjy4KnXtXH7M0x2SWquHW2L4r7blxbVLWeWWfk7vpnEK1eTnD4yZUJyZwhZpawuZt7u9hsF4N5");
		 try
		 {
			 Response res = given()
			    .contentType("application/json").body(obj.toString()).
			       when().
			          post(MODULE + "/profile/picture");
			 assertEquals(200, res.getStatusCode());
			 String responseBody = res.toString();
			 System.out.println(responseBody);
		 }
		 catch(Exception e)
		 {
			 System.out.println("Error");
		 }
	 }
}