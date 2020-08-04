package com.whosup.resource;

import static com.jayway.restassured.RestAssured.get;

import java.util.UUID;

import org.junit.Before;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/webapp/WEB-INF/cassandra.properties"})
public class AbstractResourceTest {
	
	protected UUID access_token;
	@Before
	public void setUp(){
		RestAssured.basePath = "https://whosup.io";
		this.access_token = UUID.fromString(getAccessToken());
	}
	
	private String getAccessToken() {
		Response res = get("/network/fb/login?fb_access_token="
				+ "CAAGNZCvbfW1ABAOA1VwymPeZCBIwPoR1OmUI4hOB1pJ2zA1DsK4NesEbPBS4qdDZBntKP7ieREZBY0wZBCP92LG83wizBXZAsV9manclJCdAzEQsBRZCoKsKphKwKX2wFY6D5sSpZCV7T6S9KbtqJsZAxf9IrKRhswD48rACk9ZBY4hGDrdzlTayGilSIDJuFOR4UZD&"
				+ "device_ID=mFZ3nOuGTOo:APA91bGHo6RixEA1aW6_i23RMuocFSH0INV1JNrGvU5s87V7SPaZFkA5U3LPcHjHWFLjy4KnXtXH7M0x2SWquHW2L4r7blxbVLWeWWfk7vpnEK1eTnD4yZUJyZwhZpawuZt7u9hsF4N5&"
				+ "deviceType=iOS");
		JsonPath jp = new JsonPath(res.asString());
		return jp.get("accessToken");
	}
}
