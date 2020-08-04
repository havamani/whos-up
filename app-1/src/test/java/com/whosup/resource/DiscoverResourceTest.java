//package com.whosup.resource;
//
//import static com.jayway.restassured.RestAssured.get;
//import static com.jayway.restassured.RestAssured.given;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//import groovyx.net.http.ContentType;
//
//import java.io.IOException;
//
//import org.junit.Ignore;
//import org.junit.Test;
//import org.json.JSONArray;
//
//import com.amazonaws.util.json.JSONException;
//import com.amazonaws.util.json.JSONObject;
//import com.jayway.restassured.response.Response;
//
//public class DiscoverResourceTest extends AbstractResourceTest implements
//		Cloneable {
//
//	private static String MODULE = "/discover";
//
//	/***
//	 * This is App landing page API, it provides list of ideas and events
//	 * 
//	 * @param accessToken
//	 *            - Whosup access token
//	 * @return list of ideas and events
//	 */
//	@Ignore
//	public void testEventList() {
//		Response res = get(MODULE + "/list?access_token=" + this.access_token
//				+ "&location=51.5286417,-0.100912");
//		String s = res.jsonPath().get("ideaSource1[0].inbox.title");
//		assertTrue("Gadgets we can make".equals(s.trim()));
//
//		Response res2 = get(MODULE + "/list?access_token=" + this.access_token
//				+ "&location=51.5286417,-0.100912");
//		String s2 = res2.jsonPath().get("ideaSource1[0].inbox.pk.eventID");
//		assertTrue("be1c690f-2fbc-42ff-90e6-03f7aa5d0f38".equals(s2));
//	}
//
//	@Ignore
//	public void testEventListNullLocation() {
//		Response res = get(MODULE + "/list?access_token=" + this.access_token);
//		assertEquals(400, res.getStatusCode());
//	}
//
//	@Ignore
//	public void testDiscoverScroll() {
//		long l = 180000L;
//		Response res = get(MODULE + "/reload?access_token=" + this.access_token
//				+ "&location=\"\"&time_in_mills=" + l);
//		if (res.getStatusCode() != 200) {
//			assertEquals("Please provide location",
//					res.jsonPath().get("message"));
//		} else {
//			assertEquals("1459447248475",
//					res.jsonPath().get("eventSource1[0].eventExpiry"));
//		}
//	}
//
//	@Ignore
//	public void testDiscoverReloadNullLocation() {
//		Response res = get(MODULE + "/keywords?access_token="
//				+ this.access_token + "&location=" + null);
//		assertTrue("{}".equals(res.asString()));
//	}
//
//	@Ignore
//	public void testDiscoverReload() {
//		Response res = get(MODULE + "/keywords?access_token="
//				+ this.access_token + "&location=51.5286417,-0.100912");
//		assertEquals(200, res.getStatusCode());
//	}
//
//	/***
//	 * This API filters ideas and events based on given search items or it will
//	 * provide list of ideas and events under particular category
//	 * 
//	 * Searches can be performed by Date, Day, friday, today soccer, 12/12/2015
//	 * etc..
//	 * 
//	 * @param accessToken
//	 *            - whosup access token
//	 * @param search
//	 *            - search term
//	 * @param categoryID
//	 *            - UUID of category
//	 * @return list of events and ideas
//	 */
//	@Ignore
//	public void testSearch() throws JSONException {
//		Response res = given()
//				.contentType(ContentType.JSON)
//				.when()
//				.get(MODULE
//						+ "/search?access_token="
//						+ this.access_token
//						+ "&location="
//						+ null
//						+ "&search=down&searchDate=19-02-2016&searchAfterEve=gh");
//		if (res.getStatusCode() != 200) {
//			assertEquals("Please provide location",
//					res.jsonPath().get("message"));
//		} else {
//			assertEquals(200, res.getStatusCode());
//		}
//
//	}
//
//	/***
//	 * This API adds a user into an event after user buzzed a event
//	 * 
//	 * @param request
//	 *            - access token and eventId are required
//	 * @return success or failure message
//	 */
//	@Ignore
//	public void testCount() {
//		Response res = get(MODULE + "/details?access_token="
//				+ this.access_token
//				+ "&idea_id=837ac0f9-c03e-47ad-b429-601f396bcc24");
//		int a = (Integer) res.jsonPath().getInt("networkCount");
//		assertEquals(10, a);
//	}
//
//	/***
//	 * This API provides list of users under advance for choosing user who can
//	 * see event after creation. list of users will be send based items selected
//	 * under gatherfrom button while creating event
//	 * 
//	 * @param request
//	 *            access_token and gatherfrom list which may contain(fb, ln,
//	 *            whosup, my_net)
//	 * @return list of users
//	 */
//	@Ignore
//	public void testGatherFrom() throws JSONException {
//		JSONObject json = new JSONObject();
//		String gathForm[] = { "fb", "ln", "my_net", "whosup" };
//		json.put("accessToken", this.access_token);
//		json.put("gatherFrom", gathForm);
//
//		Response res = given().contentType("application/json")
//				.body(json.toString()).when().post(MODULE + "/event/advance");
//		assertEquals(
//				"https://whosup-server.s3.amazonaws.com/75683d79-369d-4aea-91b0-1ef7adb53692.jpg",
//				res.jsonPath().get("contactPhotoPath[2]"));
//	}
//
//	/***
//	 * This API used for creating a event from idea and creating own events
//	 * 
//	 * @param request
//	 *            - access_token and fields given while creating a event
//	 * @return success or failure message
//	 */
//	@Test
//	public void testCreateEvent() throws JSONException {
//		JSONObject json = new JSONObject();
//		String conID[] = { "24835840-5e5b-4377-941e-af3b226b6ff1",
//				"de6a7d87-dfb2-4aca-914c-1be5cbaa8d37",
//				"8316002a-952d-476c-bea3-f32fcc4f741c",
//				"856815b0-eeb3-45a4-be23-97d1bf4486bb",
//				"2afbea9a-df79-42a1-ba2a-5ff95a6858e8",
//				"8421c988-4745-4e8b-8751-8716f3329d79",
//				"5e3ea859-6771-4a76-9f9c-ccb45f09d7ac",
//				"39ecf321-16e9-49c2-a6af-b16993392fd6" };
//
//		String gathForm[] = { "fb", "ln", "my_net", "whosup" };
//		json.put("accessToken", this.access_token);
//		json.put("contactIDs", conID);
//		json.put("description", "JUnit testing.");
//		json.put("eventDate", "");
//		json.put("eventEndTime", "");
//		json.put("eventName", "Test Event from JUNIT");
//		json.put("eventStartTime", "");
//		json.put("femaleOnly", "false");
//		json.put("gatherAddress", "");
//		json.put("gatherFrom", gathForm);
//		json.put("gatherLocation", "19.017336,72.856340");
//		json.put("gatherRadius", "10");
//		json.put("ideaID", "837ac0f9-c03e-47ad-b429-601f396bcc24");
//		json.put("isPublic", "false");
//		json.put(
//				"largePhotoPath",
//				"https://s3-eu-west-1.amazonaws.com/idea-images/837ac0f9-c03e-47ad-b429-601f396bcc24_main.jpeg");
//		json.put("maxAge", "25");
//		json.put("maxPlaces", "0");
//		json.put("minAge", "18");
//		json.put("minPlaces", "0");
//		json.put("myCrowdOnly", "false");
//		json.put(
//				"smallPhotoPath",
//				"https://s3-eu-west-1.amazonaws.com/idea-images/837ac0f9-c03e-47ad-b429-601f396bcc24_thumbnail.jpeg");
//		json.put("webLink", "");
//
//		Response res = given().contentType("application/json")
//				.body(json.toString()).when().post(MODULE + "/event/create");
//		assertEquals(200, res.getStatusCode());
//	}
//
//	/***
//	 * This API returns Event settings and details
//	 * 
//	 * @param accessToken
//	 *            - whosup access token
//	 * @param eventId
//	 *            - event's uuid for which settings and details are needed
//	 * @return response contains event details
//	 */
//
//	@Ignore
//	public void testEventView() throws JSONException, IOException {
//
//		String eventId = "ec2177a9-3bd7-4133-bf18-017da91cb384";
//		Response res = given()
//				.contentType(ContentType.JSON)
//				.when()
//				.get(MODULE + "/event/view?access_token=" + this.access_token
//						+ "&event_id=" + eventId);
//		if (res.getStatusCode() != 200) {
//			assertEquals(400, res.getStatusCode());
//		} else {
//			String s = (String) res.jsonPath().getString(
//					"event.interestedUserIDs[0]");
//			assertTrue("39ecf321-16e9-49c2-a6af-b16993392fd6".equals(s));
//		}
//	}
//
//	/***
//	 * This API adds a user into an event after user buzzed a event
//	 * 
//	 * @param request
//	 *            - access token and eventId are required
//	 * @return success or failure message
//	 */
//	@Ignore
//	public void testBuzz() throws JSONException {
//		JSONObject json = new JSONObject();
//		json.put("accessToken", "3319b338-aec6-4ba1-a678-c4902755c880");
//		json.put("eventId", "ec2177a9-3bd7-4133-bf18-017da91cb384");
//		Response res = given().contentType("application/json")
//				.body(json.toString()).when().post(MODULE + "/event/buzz");
//		if (res.getStatusCode() != 200) {
//			assertEquals(400, res.getStatusCode());
//		} else {
//			assertEquals(200, res.getStatusCode());
//		}
//	}
//
//	/***
//	 * This API gives list of users other than users send under "/event/advance"
//	 * to select few users from other categories
//	 * 
//	 * @param request
//	 *            - access_token and other gatherfrom list which may contain(fb,
//	 *            ln, whosup, my_net)
//	 * @return list of users
//	 */
//	@Ignore
//	public void testAdd() throws JSONException {
//		JSONObject json = new JSONObject();
//		String gathForm[] = { "fb", "ln", "my_net", "whosup" };
//		json.put("accessToken", this.access_token);
//		json.put("gatherFrom", gathForm);
//
//		Response res = given().contentType("application/json")
//				.body(json.toString()).when().post(MODULE + "/event/add");
//		assertEquals("Melki Ct", res.jsonPath().get("contactFullName[0]"));
//	}
//
//	@Ignore
//	public void testPushNotification() throws JSONException {
//		Response res = given()
//				.contentType(ContentType.JSON)
//				.when()
//				.get(MODULE + "/event/notification?access_token="
//						+ this.access_token
//						+ "&event_id=ec2177a9-3bd7-4133-bf18-017da91cb384");
//		assertTrue("200".equals(res.jsonPath().get("status")));
//	}
//}
