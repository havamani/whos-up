//package com.whosup.resource;
//
//import static com.jayway.restassured.RestAssured.given;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.junit.Test;
//
//import com.amazonaws.util.json.JSONException;
//import com.google.gson.Gson;
//import com.jayway.restassured.path.json.JsonPath;
//import com.jayway.restassured.response.Response;
//
//import groovyx.net.http.ContentType;
//import net.sf.json.JSONObject;
//
//public class InteractResourceTest extends AbstractResourceTest{
//	private static String MODULE = "/interact";
//	
//	/***
//	 * This API is used to view the event.
//	 * 
//	 * @param accessToken
//	 *            - Whosup access token
//	 * @return list of events
//	 */
//	
//	@Test
//	public void testView() throws JSONException{
//		String eveId="ca094092-f0e8-4bbf-99c6-a64638ef8341";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				System.out.println(json);
//				assertEquals("39ecf321-16e9-49c2-a6af-b16993392fd6",jp.get("users[0].userID"));
//				break;
//			}		
//		}
//	}
//	
//	/***
//	 * This API is used to delete the event.
//	 * 
//	 * @param request
//	 *            - Whosup access token,eventID
//	 * @return Status
//	 */
//	
//    @Test
//	public void testDeleteEvent() throws JSONException{
//		String eveId="373acb09-f267-4abf-97dd-976340f19fe9";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject(); 
//		ob.put("response",res.jsonPath().get("remaining.eventDetails.pk.eventID"));
//		@SuppressWarnings("unchecked")
//		List<String> list = (ArrayList<String>) ob.get("response");
//		for(int i=0;i<list.size();i++)
//		{
//			if(list.get(i).equals(eveId))
//			{
//					JSONObject json=new JSONObject();
//					json.put("accessToken",this.access_token);
//					json.put("eventId",eveId);
//					Response res2 = given()
//						    .contentType(ContentType.JSON).body(json.toString()).
//						       when().
//						          post(MODULE+"/delete/event");
//					if(res2.getStatusCode()!=200)
//					{
//						assertEquals("400",res2.getStatusCode());
//					}
//					else
//					{
//						assertEquals("Success",res2.jsonPath().get("status"));
//					}
//			}
//		}
//	}
//    
//    /***
//	 * This API is used to reject the organizer.
//	 * 
//	 * @param request
//	 *            - Whosup access token,eventID
//	 * @return Status
//	 */
//	
//    @Test
//	public void testRejected() throws JSONException{
//		String eveId="ca094092-f0e8-4bbf-99c6-a64638ef8341";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index = 0 ; index < list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if((jp.get("isThereOrganizer").toString())=="true" 
//					&& jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				JSONObject json2=new JSONObject();
//				json2.put("accessToken",this.access_token);
//				json2.put("eventId",eveId);
//				Response res2 = given()
//					    .contentType(ContentType.JSON).body(json2.toString()).
//					       when().
//					          post(MODULE+"/organizer/reject");
//				assertEquals(200,res2.getStatusCode());
//			}		
//		}
//	}
//	
//    /***
//	 * This API is used to accept the organizer.
//	 * 
//	 * @param request
//	 *            - Whosup access token,eventID
//	 * @return Status
//	 */
//    
//	@Test
//	public void testConfirmed() throws JSONException{
//		int flag=0;
//		String eveId="8219439e-642a-4f53-a278-dd7b38622041";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if((jp.get("isThereOrganizer").toString())=="false" && jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				flag=1;
//				JSONObject json2=new JSONObject();
//				json2.put("accessToken",this.access_token);
//				json2.put("eventId",eveId);
//				Response res2 = given()
//					    .contentType(ContentType.JSON).body(json2.toString()).
//					       when().
//					          post(MODULE+"/organizer/accept");
//				System.out.println(res2.asString());
////				assertEquals(200,res2.getStatusCode());
//				break;
//			}
//		}
//		if(flag==0)
//		{
//			JSONObject json2=new JSONObject();
//			json2.put("accessToken",this.access_token);
//			json2.put("eventId",eveId);
//			Response res2 = given()
//				    .contentType(ContentType.JSON).body(json2.toString()).
//				       when().
//				          post(MODULE+"/organizer/accept");
//			System.out.println(res2.asString());
////			assertEquals(400,res2.getStatusCode());
//		}
//	}
//	
//	/***
//	 * This API is used to resign the organizer.
//	 * 
//	 * @param request
//	 *            - Whosup access token,eventID
//	 * @return Status
//	 */
//	
//	@Test
//	public void testNotConfirmed() throws JSONException{
//		int flag=0;
//		String eveId="3be297ce-c48b-4d22-810e-e8c3b003e637";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				flag=1;
//				JSONObject json2=new JSONObject();
//				json2.put("accessToken",this.access_token);
//				json2.put("eventId",eveId);
//				Response res2 = given()
//					    .contentType(ContentType.JSON).body(json2.toString()).
//					       when().
//					          post(MODULE+"/organizer/resign");
//				assertEquals(200,res2.getStatusCode());
//			}
//		}
//		if(flag==0)
//		{
//			JSONObject json2=new JSONObject();
//			json2.put("accessToken",this.access_token);
//			json2.put("eventId",eveId);
//			Response res2 = given()
//				    .contentType(ContentType.JSON).body(json2.toString()).
//				       when().
//				          post(MODULE+"/organizer/resign");
//			assertEquals(400,res2.getStatusCode());
//		}
//	}
//	
//	/***
//	 * This API is used to conform the event request.
//	 * 
//	 * @param 
//	 *       - Whosup access token,eventID
//	 *       
//	 * @return ConfirmRequest
//	 */
//	
//	@Test
//	public void testConfirmRequest() throws JSONException{
//		String eveId="3be297ce-c48b-4d22-810e-e8c3b003e637";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				Response res2 = given()
//					    .contentType(ContentType.JSON).
//					       when().
//					          get(MODULE+"/view?access_token="+this.access_token+"&event_id="+eveId);
//				assertEquals(200,res2.getStatusCode());
//			}
//		}
//	}
//	
//	/***
//	 * This API is used to see the event settings.
//	 * 
//	 * @param 
//	 *       - Whosup access token,eventID
//	 *       
//	 * @return EventSettingResponse
//	 */
//	@Test
//	public void testView1() throws JSONException{
//		String eveId="3be297ce-c48b-4d22-810e-e8c3b003e637";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				Response res2 = given()
//					    .contentType(ContentType.JSON).
//					       when().
//					          get(MODULE+"/event/settings?access_token="+this.access_token+"&event_id="+eveId);
//				assertEquals(200,res2.getStatusCode());
//			}
//		}
//	}
//	
//	/***
//	 * This API is used to edit the event settings.
//	 * 
//	 * @param 
//	 *       - Whosup access token,eventID
//	 *       
//	 * @return Status
//	 */
//	@Test
//	public void testEditSettings() throws JSONException{
//		String eveId="3be297ce-c48b-4d22-810e-e8c3b003e637";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				JSONObject json2=new JSONObject();
//				json2.put("accessToken",this.access_token);
//				json2.put("eventId",eveId);
//				Response res2 = given()
//					    .contentType(ContentType.JSON).body(json2.toString()).
//					       when().
//					          post(MODULE+"/event/advanced");
//				if(res2.getStatusCode()!=200)
//				{
//					assertEquals(400,res2.getStatusCode());
//				}
//				else
//				{
//					assertEquals("Success",res2.jsonPath().get("status"));
//				}
//			}
//		}
//	}
//	
//	/***
//	 * This API is view picture list of event.
//	 * 
//	 * @param 
//	 *       - Whosup access token,eventID
//	 *       
//	 * @return Status
//	 */
//	@Test
//	public void testGetPictures() throws JSONException{
//		String eveId="8219439e-642a-4f53-a278-dd7b38622041";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				Response res2 = given()
//					    .contentType(ContentType.JSON).
//					       when().
//					          get(MODULE+"/picture/list?access_token="+this.access_token+"&event_id="+eveId);
//				assertEquals(200,res2.getStatusCode());
//			}
//		}
//	}
//	
//	/***
//	 * This API is get feedback.
//	 * 
//	 * @param 
//	 *       - Whosup access token,eventID
//	 *       
//	 * @return Status
//	 */
//	
//	@Test
//	public void testGetFeedback() throws JSONException{
//		String eveId="8219439e-642a-4f53-a278-dd7b38622041";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				Response res2 = given()
//					    .contentType(ContentType.JSON).
//					       when().
//					          get(MODULE+"/feedback/get?access_token="+this.access_token+"&event_id="+eveId);
//				assertEquals(200,res2.getStatusCode());	
//			}
//		}
//	}
//	
//	/***
//	 * This API is set feedback.
//	 * 
//	 * @param 
//	 *       - Whosup access token,eventID
//	 *       
//	 * @return Status
//	 */
//	@Test
//	public void testFeedback() throws JSONException{
//		String eveId="8219439e-642a-4f53-a278-dd7b38622041";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				int rating=4;
//				JSONObject json2=new JSONObject();
//				json2.put("accessToken",this.access_token);
//				json2.put("eventId",eveId);
//				json2.put("rating",rating);
//				json2.put("issue","some problem");
//				Response res2 = given()
//					    .contentType(ContentType.JSON).body(json2.toString()).
//					       when().
//					          post(MODULE+"/feedback");
//				assertEquals(200,res2.getStatusCode());	
//			}
//		}
//	}
//	
//	/***
//	 * This API is used to booking the event.
//	 * 
//	 * @param 
//	 *       - Whosup access token,eventID and required fields.
//	 *       
//	 * @return Status
//	 * @throws ParseException 
//	 */
//	
//	@Test
//	public void tesBooking() throws JSONException, ParseException{
//		String eveId="4dd1b82c-7548-43f7-a7ca-800b190e92e7";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			Date d =format.parse("2016-05-01"); 
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				JSONObject json2=new JSONObject();
//				json2.put("accessToken",this.access_token);
//				json2.put("eventId",eveId);
//				json2.put("description","Junit test");
//				json2.put("webLink","");
//				json2.put("smallPhotoPath","https://s3-eu-west-1.amazonaws.com/idea-images/dafa89aa-ea08-481a-8c56-fec57fd8da62_thumbnail.png");
//				json2.put("largePhotoPath","https://s3-eu-west-1.amazonaws.com/idea-images/dafa89aa-ea08-481a-8c56-fec57fd8da62_main.png");
//				json2.put("eventDate",new Date());
//				json2.put("eventStartTime",new Date());
//				json2.put("eventEndTime",d);
//				json2.put("isPaid","false");
//				json2.put("costToSplit","");
//				json2.put("isFixedAmount","false");
//				json2.put("minPlaces","2");
//				json2.put("maxPlaces","4");
//				json2.put("isCancel","false");
//				Response res2 = given()
//					    .contentType(ContentType.JSON).body(json2.toString()).
//					       when().
//					          post(MODULE+"/booking");
//				if(res2.getStatusCode()!=200)
//				{
//					assertEquals(400,res2.getStatusCode());
//				}
//				else
//				{
//					assertEquals("Success",res2.jsonPath().get("status"));
//				}
//			}
//		}
//	}
//	
//	/***
//	 * This API is used to update event location.
//	 * 
//	 * @param 
//	 *       - Whosup access token,eventID and required fields.
//	 *       
//	 * @return Status
//	 */
//	@Test
//	public void testEventLocation() throws JSONException{
//		String eveId="8219439e-642a-4f53-a278-dd7b38622041";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				JSONObject json2=new JSONObject();
//				json2.put("accessToken",this.access_token);
//				json2.put("eventId",eveId);
//				json2.put("chosenLocation","51.5286417,-0.100912");
//				json2.put("chosenLocationAddress","");
//				json2.put("chosenLocCountry","GB");
//				json2.put("chosenLocAdmin1","England");
//				json2.put("chosenLocAdmin2","Gt Lon");
//				json2.put("chosenLocAdmin3","London Borough of Islington");
//				Response res2 = given()
//					    .contentType(ContentType.JSON).body(json2.toString()).
//					       when().
//					          post(MODULE+"/location");
//				assertTrue("Success".equals(res2.jsonPath().get("status")));
//			}
//		}
//	}
//	
//	/***
//	 * This API is used to conform event.
//	 * 
//	 * @param 
//	 *       - Whosup access token,eventID.
//	 *       
//	 * @return Status
//	 */
//	
//	@Test
//	public void testConfirmEvent() throws JSONException{
//		String eveId="8219439e-642a-4f53-a278-dd7b38622041";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				JSONObject json2=new JSONObject();
//				json2.put("accessToken",this.access_token);
//				json2.put("eventId",eveId);
//				Response res2 = given()
//					    .contentType(ContentType.JSON).body(json2.toString()).
//					       when().
//					          post(MODULE+"/event/confirm");
//				assertTrue("Success".equals(res2.jsonPath().get("status")));
//			}
//		}
//	}
//	
//	/***
//	 * This API is used to unConform event.
//	 * 
//	 * @param 
//	 *       - Whosup access token,eventID.
//	 *       
//	 * @return Status
//	 */
//	@Test
//	public void testUnConfirmEvent() throws JSONException{
//		String eveId="8219439e-642a-4f53-a278-dd7b38622041";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				JSONObject json2=new JSONObject();
//				json2.put("accessToken",this.access_token);
//				json2.put("eventId",eveId);
//				Response res2 = given()
//					    .contentType(ContentType.JSON).body(json2.toString()).
//					       when().
//					          post(MODULE+"/event/unconfirm");
//				assertTrue("Success".equals(res2.jsonPath().get("status")));
//			}
//		}
//	}
//	
//	/***
//	 * This API is used to delete user from event.
//	 * 
//	 * @param 
//	 *       - Whosup access token,eventID.
//	 *       
//	 * @return Status
//	 */
//	
//	@Test
//	public void testDeleteUser() throws JSONException{
//		String eveId="8219439e-642a-4f53-a278-dd7b38622041";
//		String delUserId="39ecf321-16e9-49c2-a6af-b16993392fd6";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				JSONObject json2=new JSONObject();
//				json2.put("accessToken",this.access_token);
//				json2.put("eventId",eveId);
//				json2.put("deleteUserId",delUserId);
//				Response res2 = given()
//					    .contentType(ContentType.JSON).body(json2.toString()).
//					       when().
//					          post(MODULE+"/delete/user");
//				if(res2.getStatusCode()!=200)
//				{
//					assertEquals(400,res2.getStatusCode());
//				}
//				else
//				{
//					assertEquals(200,res2.getStatusCode());
//				}
//			}
//		}
//	}
//	
//	/***
//	 * This API is used to add the picture.
//	 * 
//	 * @param 
//	 *       - Whosup access token,eventID and picturePath.
//	 *       
//	 * @return Status
//	 */
//	
//	@Test
//	public void testAddPicture() throws JSONException{
//		String eveId="8219439e-642a-4f53-a278-dd7b38622041";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				JSONObject json2=new JSONObject();
//				json2.put("accessToken",this.access_token);
//				json2.put("eventId",eveId);
//				json2.put("picturePath","https://s3-eu-west-1.amazonaws.com/idea-images/b21cc7af-6cdd-419b-85f6-5dd01ba1b238_thumbnail.png");
//				Response res2 = given()
//					    .contentType(ContentType.JSON).body(json2.toString()).
//					       when().
//					          post(MODULE+"/picture/add");
//				assertEquals(200,res2.getStatusCode());
//			}
//		}
//	}
//	
//	/***
//	 * This API is used to edit max place and min place in event.
//	 * 
//	 * @param 
//	 *       - Whosup access token,eventID,maxPlace and minPlace.
//	 *       
//	 * @return Status
//	 */
//	@Test
//	public void testEditPlaces() throws JSONException{
//		String eveId="8219439e-642a-4f53-a278-dd7b38622041";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				JSONObject json2=new JSONObject();
//				json2.put("accessToken",this.access_token);
//				json2.put("eventId",eveId);
//				json2.put("minPlaces","0");
//				json2.put("maxPlaces","5");
//				Response res2 = given()
//					    .contentType(ContentType.JSON).body(json2.toString()).
//					       when().
//					          post(MODULE+"/places/edit");
//				if(res2.getStatusCode()!=200)
//				{
//					assertEquals(400,res2.getStatusCode());
//				}
//				else
//				{
//					assertEquals(200,res2.getStatusCode());
//				}
//			}
//		}
//	}
//	
//	@Test
//	public void testGatherAgain() throws JSONException{
//		String eveId="8219439e-642a-4f53-a278-dd7b38622041";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				JSONObject json2=new JSONObject();
//				json2.put("accessToken",this.access_token);
//				json2.put("eventId",eveId);
//				Response res2 = given()
//					    .contentType(ContentType.JSON).body(json2.toString()).
//					       when().
//					          post(MODULE+"/gather");
//				System.out.println(res2.asString());
//			}
//		}
//	}
//	
//	/***
//	 * This API is used to edit the event date,start time and end time.
//	 * 
//	 * @param 
//	 *       - Whosup access token,eventID,eventDate,eventStartTime and eventEndTime.
//	 *       
//	 * @return Status
//	 */
//	@Test
//	public void testEventDate() throws JSONException, ParseException{
//		String eveId="8219439e-642a-4f53-a278-dd7b38622041";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/view?access_token="+this.access_token);
//		JSONObject ob = new JSONObject();
//		ob.put("view",res.jsonPath().get("remaining"));
//		List<?> list=(List<?>) ob.get("view");
//		for(int index=0 ; index<list.size() ; index++)
//		{
//			String json = new Gson().toJson(list.get(index));
//			JsonPath jp = new JsonPath(json);
//			if(jp.get("eventDetails.pk.eventID").equals(eveId))
//			{
//				String date_s = "18 01 2011 00:00:00";
//				Date date=new Date();
//				SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
//				Date d =format.parse(date_s); 
//				JSONObject json2=new JSONObject();
//				json2.put("accessToken",this.access_token);
//				json2.put("eventId",eveId);
//				json2.put("eventDate",d);
//				json2.put("eventStartTime",d);
//				json2.put("eventEndTime",d);
//				Response res2 = given()
//					    .contentType(ContentType.JSON).body(json2.toString()).
//					       when().
//					          post(MODULE+"/date");
//				System.out.println(res2.asString());
//			}
//		}
//	}
//	
//	@Test
//	public void test() throws JSONException, ParseException{
//		String eveId="8219439e-642a-4f53-a278-dd7b38622041";
//		Response res = given()
//			    .contentType(ContentType.JSON).
//			       when().
//			          get(MODULE+"/test?access_token="+this.access_token);
//		System.out.println(res.asString());
//	}
//}
