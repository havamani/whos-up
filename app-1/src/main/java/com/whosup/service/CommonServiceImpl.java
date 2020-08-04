package com.whosup.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.crypto.RsaSHA256Signer;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
//import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.joda.time.Instant;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.gson.JsonObject;
//import com.jayway.restassured.response.Response;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.whosup.common.AppErrorMessage;
import com.whosup.common.AppException;
import com.whosup.dao.EventDao;
import com.whosup.dao.IdeaDao;
import com.whosup.model.Event;
import com.whosup.model.User;
import com.whosup.model.request.ContactDetail;
import com.whosup.model.request.NetworkRequest;
import com.whosup.model.response.Status;

public class CommonServiceImpl implements CommonService {

	@Autowired
	private AmazonS3 amazonS3;
	@Autowired
	private IdeaDao ideaDao;
	@Autowired
	private EventDao eventDao;

	private static final String TWILIO_SID = "ACad163a082b6f704bac4b3a2a5fece7f6";
	private static final String TWILIO_TOKEN = "b5fa05ce3365d48327a1944000a565e2";
	private static final String KAFKA_CONSUMER = "172.31.26.21:9092";
	private static final String LAYER_TOKEN = "Bearer ZrpviHsTO8zkY7R3NIiLqT1d1nIc5dstcOdn7l3gNhlILyjg";
	private static final String GCM_TOKEN = "AIzaSyDZmFtzkm3hxpFiMxyHL8ffJ2MKHBa3cDE";
	private static final String OPEN_WEATHER_API = "abf561e8cd3de0f88cc03854c72a9e60";

	@Override
	public void sendMail(String emailID, String Subject, String message) {
		try {
			Properties mailProperties = new Properties();
			mailProperties.load(this.getClass().getResourceAsStream(
					"/mail-settings.properties"));
			Session session = Session.getDefaultInstance(mailProperties,
					new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(mailProperties
									.getProperty("mail.smtp.user"),
									mailProperties
											.getProperty("email.password"));
						}
					});
			Message mailMessage = new MimeMessage(session);

			mailMessage.setFrom(new InternetAddress(mailProperties
					.getProperty("mail.smtp.from")));
			mailMessage.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(emailID));
			mailMessage.setSubject(Subject);

			mailMessage.setContent(message, "text/plain");

			Transport.send(mailMessage);
		} catch (Exception e) {
			throw new AppException(new AppErrorMessage("400",
					"Email sending Failed"));
		}
	}

	@Override
	public NetworkRequest locationDetails(String location) {
		NetworkRequest locationDetails = new NetworkRequest();
		JSONObject json = new JSONObject();
		String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ location
				+ "&result_type=administrative_area_level_2|administrative_area_level_3&key=AIzaSyBHCj1JynkQ7fwJCjprDRsKMhl7EN1Zrz8";
		try {
			URL requestMethod = new URL(url);
			HttpURLConnection urlConnection = (HttpURLConnection) requestMethod
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				json = new JSONObject(sb.toString());
				if (json.getJSONArray("results").length() > 0
						&& json.getString("status").equals("OK")) {
					JSONArray addrComponent = json
							.getJSONArray("results")
							.getJSONObject(
									json.getJSONArray("results").length() - 1)
							.getJSONArray("address_components");
					if(addrComponent.length() == 4) {
					locationDetails.setAdmin1(addrComponent.getJSONObject(2)
							.getString("short_name"));
					locationDetails.setAdmin2(addrComponent.getJSONObject(1)
							.getString("short_name"));
					locationDetails.setAdmin3(addrComponent.getJSONObject(0)
							.getString("short_name"));
					locationDetails.setCountry(addrComponent.getJSONObject(3)
							.getString("short_name"));
					} else {
						locationDetails.setAdmin1(addrComponent.getJSONObject(1)
								.getString("short_name"));
						locationDetails.setAdmin2(addrComponent.getJSONObject(0)
								.getString("short_name"));
						locationDetails.setCountry(addrComponent.getJSONObject(2)
								.getString("short_name"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return locationDetails;

	}

	@Override
	public Status message(String code, String mobileNumber) {
		String result;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("To", mobileNumber));
		params.add(new BasicNameValuePair("From", "whosup"));
		params.add(new BasicNameValuePair("Body", "WhosUp Verification Code: "
				+ code));
		try {
			TwilioRestClient client = new TwilioRestClient(TWILIO_SID,
					TWILIO_TOKEN);
			MessageFactory messageFactory = client.getAccount()
					.getMessageFactory();
			result = messageFactory.create(params).getSid();
		} catch (TwilioRestException e) {
			throw new AppException(new AppErrorMessage("400",
					"Invlaid Mobile Number"));
		}
		return new Status(result);
	}

	@Override
	public JSONObject getCurrentWeather(String latLong) {

		String url = "http://api.openweathermap.org/data/2.5/weather?lat="
				+ latLong.substring(0, latLong.indexOf(",")) + "&lon="
				+ latLong.substring(latLong.indexOf(",") + 1) + "&APPID="
				+ OPEN_WEATHER_API;
		JSONObject json = new JSONObject();
		try {
			URL addUser = new URL(url);
			HttpURLConnection newConnection = (HttpURLConnection) addUser
					.openConnection();
			newConnection.setRequestMethod("GET");
			newConnection.connect();
			if (newConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						newConnection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				json = new JSONObject(sb.toString());
			}
			return json;
		} catch (Exception e) {
			throw new AppException(new AppErrorMessage("400",
					"Internal Server Error"));
		}
	}

	@Override
	public Status kafkaProducerUser(UUID userId) {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONSUMER);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");

		JSONObject msg = new JSONObject();
		msg.put("userId", userId);

		try {
			System.out.println("starting user kafka..");
			KafkaProducer<String, String> producer = new KafkaProducer<String, String>(
					props);
			producer.send(new ProducerRecord<>("refresh_discover", userId
					.toString(), msg.toString()));
			producer.close();
			System.out.println("user send to kafka..");
			return new Status("Success");
		} catch (Exception e) {
			System.out.println("Error sending user to kafka..");
			return new Status("Failure");
		}
	}

	@Override
	public Status kafkaProducerBuzz(UUID userId, UUID eventId, Boolean isGather) {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONSUMER);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");

		JSONObject msg = new JSONObject();
		msg.put("buzzTime", new Date());
		msg.put("userId", userId);
		msg.put("eventId", eventId);
		msg.put("isGather", isGather);

		try {
			System.out.println("starting buzz kafka..");
			KafkaProducer<String, String> producer = new KafkaProducer<String, String>(
					props);
			producer.send(new ProducerRecord<>("buzz", userId.toString(), msg
					.toString()));
			producer.close();
			System.out.println("buzz send to kafka..");
			return new Status("Success");
		} catch (Exception e) {
			System.out.println("Error sending buzz to kafka..");
			return new Status("Failure");
		}
	}

	@Override
	public List<ContactDetail> gmailContacts(String accessToken) {
		String url = "https://www.google.com/m8/feeds/contacts/default/full?alt=json&oauth_token="
				+ accessToken;
		try {
			URL requestMethod = new URL(url);
			HttpURLConnection urlConnection = (HttpURLConnection) requestMethod
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				JSONArray jsonArray = new JSONObject(sb.toString())
						.getJSONObject("feed").getJSONArray("entry");
				List<ContactDetail> cons = new ArrayList<ContactDetail>();
				for (int i = 0; i < jsonArray.length(); i++) {
					if (jsonArray.getJSONObject(i).optJSONArray("gd$email") != null) {
						ContactDetail con = new ContactDetail();
						con.setId(jsonArray.getJSONObject(i)
								.getJSONArray("gd$email").getJSONObject(0)
								.getString("address"));
						con.setUserName(jsonArray.getJSONObject(i)
								.getJSONObject("title").getString("$t"));
						con.setFile(jsonArray.getJSONObject(i)
								.getJSONArray("link").getJSONObject(0)
								.getString("href"));
						cons.add(con);
					}
				}
				return cons;
			} else
				return new ArrayList<ContactDetail>();
		} catch (Exception e) {
			throw new AppException(new AppErrorMessage("400",
					"Internal Server Error"));
		}
	}

	@Override
	public String uploadImage(InputStream upload,
			FormDataContentDisposition contentDispositionHeader) {
		String name;
		if (contentDispositionHeader != null)
			name = UUID.randomUUID().toString() + "-"
					+ contentDispositionHeader.getFileName();
		else
			name = UUID.randomUUID().toString() + ".jpg";
		ObjectMetadata objectMetadata = new ObjectMetadata();
		try {
			PutObjectRequest request = new PutObjectRequest("whosup-server",
					name, upload, objectMetadata);
			amazonS3.putObject(request);
			amazonS3.setObjectAcl("whosup-server", name,
					CannedAccessControlList.PublicRead);
		} catch (Exception e) {
			return "Failure";
		}
		String path = "https://whosup-server.s3.amazonaws.com/" + name;
		return path;
	}

	@Override
	public String createChatRoom(Event event) {

		String url = "https://api.layer.com/apps/f0a2b518-52d3-11e5-994c-1c6821004889/conversations";
		try {
			JSONObject request = new JSONObject();
			request.put("participants", event.getInterestedUserIDs());
			request.put("distinct", false);
			JSONObject info = new JSONObject();
			JSONObject title = new JSONObject();
			title.put("title", event.getTitle());
			info.put("info", title);
			request.put("metadata", info);
			URL room = new URL(url);
			HttpURLConnection newConnection = (HttpURLConnection) room
					.openConnection();
			newConnection.setRequestMethod("POST");
			newConnection.setRequestProperty("Authorization", LAYER_TOKEN);
			newConnection.setRequestProperty("Accept",
					"application/vnd.layer+json; version=1.0");
			newConnection
					.setRequestProperty("Content-Type", "application/json");
			newConnection.setUseCaches(false);
			newConnection.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream(
					newConnection.getOutputStream());
			wr.writeBytes(request.toString());
			wr.flush();
			wr.close();
			newConnection.connect();
			if (newConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						newConnection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				request = new JSONObject(sb.toString());
				String chatID = request.optString("url").substring(
						request.optString("url").lastIndexOf("/") + 1);
				return chatID;
			} else
				return "Failed to create room";
		} catch (Exception e) {
			return "server error";
		}
	}

	@Override
	public String addUser(UUID chatID, UUID user) {

		String url = "https://api.layer.com/apps/f0a2b518-52d3-11e5-994c-1c6821004889/conversations/"
				+ chatID;
		try {
			JSONArray request = new JSONArray();
			JSONObject add = new JSONObject();
			add.put("operation", "add");
			add.put("property", "participants");
			add.put("value", user);
			request.put(add);

			URL addUser = new URL(url);
			HttpURLConnection newConnection = (HttpURLConnection) addUser
					.openConnection();
			newConnection.setRequestMethod("POST");
			newConnection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
			newConnection.setRequestProperty("Authorization", LAYER_TOKEN);
			newConnection.setRequestProperty("Accept",
					"application/vnd.layer+json; version=1.0");
			newConnection.setRequestProperty("Content-Type",
					"application/vnd.layer-patch+json");
			newConnection.setUseCaches(false);
			newConnection.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream(
					newConnection.getOutputStream());
			wr.writeBytes(request.toString());
			wr.flush();
			wr.close();
			newConnection.connect();
			if (newConnection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT)
				return "added a user";
			else
				return "Failed to add user";
		} catch (Exception e) {
			return "server error";
		}
	}

	@Override
	public String removeUser(UUID chatID, UUID user) {
		
		String url = "https://api.layer.com/apps/f0a2b518-52d3-11e5-994c-1c6821004889/conversations/"
				+ chatID;
		try {
			JSONArray request = new JSONArray();
			JSONObject remove = new JSONObject();
			remove.put("operation", "remove");
			remove.put("property", "participants");
			remove.put("value", user);
			request.put(remove);

			URL addUser = new URL(url);
			HttpURLConnection newConnection = (HttpURLConnection) addUser
					.openConnection();
			newConnection.setRequestMethod("POST");
			newConnection.setRequestProperty("Authorization", LAYER_TOKEN);
			newConnection.setRequestProperty("Accept",
					"application/vnd.layer+json; version=1.0");
			newConnection.setRequestProperty("Content-Type",
					"application/vnd.layer-patch+json");
			newConnection.setUseCaches(false);
			newConnection.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream(
					newConnection.getOutputStream());
			wr.writeBytes(request.toString());
			wr.flush();
			wr.close();
			newConnection.connect();
			if (newConnection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT)
				return "Removed a user";
			else
				return "Failed to remove user";
		} catch (Exception e) {
			return "server error";
		}
	}

	@Override
	public String createUser(String nonce, UUID userID) throws Exception {
		Properties properties = new Properties();
		properties.load(this.getClass()
				.getResourceAsStream("/layer.properties"));
		long expTimeInMilliSec = 1000L * 60L * 5L;

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RandomAccessFile raf = new RandomAccessFile("/home/admin/layer.pk8",
				"r");
		System.out.println("in");
		byte[] privateBytes = new byte[(int) raf.length()];
		try {
			raf.readFully(privateBytes);
		} catch (IOException ioe) {
			throw new AppException(new AppErrorMessage("400",
					"Error reading key"));
		} finally {
			raf.close();
		}
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateBytes);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

		Calendar cal = Calendar.getInstance();
		RsaSHA256Signer signer = new RsaSHA256Signer(null, null,
				(RSAPrivateKey) privateKey);
		JsonToken token = new JsonToken(signer);
		JsonObject header = token.getHeader();

		header.addProperty("typ", "JWT");
		header.addProperty("alg", "RS256");
		header.addProperty("cty", "layer-eit;v=1");
		header.addProperty("kid", properties.getProperty("keyID"));

		token.setParam("iss", properties.getProperty("providerID"));
		token.setParam("prn", userID.toString());
		token.setIssuedAt(new Instant(cal.getTimeInMillis()));
		token.setExpiration(new Instant(cal.getTimeInMillis()
				+ expTimeInMilliSec));
		token.setParam("nce", nonce);

		return token.serializeAndSign();
	}

	@Override
	public Boolean notify(User user, String message) throws IOException {
		Sender sender = new Sender(GCM_TOKEN);
		com.google.android.gcm.server.Message msg = new com.google.android.gcm.server.Message.Builder()
				.delayWhileIdle(true).addData("message", message).build();
		System.out.println(msg);
		System.out.println("Rxd : " + user.getDeviceID());
		Result result = sender.send(msg, user.getDeviceID(), 2);
		System.out.println("Messaging : " + result);
		return true;
	}

	@Override
	public String uploadSmallImage(InputStream upload) {
		String name;
		name = UUID.randomUUID().toString() + "thumb.jpg";
		ObjectMetadata objectMetadata = new ObjectMetadata();
		try {
			PutObjectRequest request = new PutObjectRequest("whosup-server",
					name, upload, objectMetadata);
			amazonS3.putObject(request);
			amazonS3.setObjectAcl("whosup-server", name,
					CannedAccessControlList.PublicRead);
		} catch (Exception e) {
			return "Failure";
		}
		String path = "https://whosup-server.s3.amazonaws.com/" + name;
		return path;
	}

	/*@Override
	public String confirmedMessage(String chatID, User user, String message) {
		String url = "https://api.layer.com/apps/f0a2b518-52d3-11e5-994c-1c6821004889/conversations/e5cffc57-a7e7-42c0-8973-67f3c7196b0a/messages";
		try {
			URL addUser = new URL(url);
			HttpURLConnection newConnection = (HttpURLConnection) addUser
					.openConnection();
			newConnection.setRequestMethod("POST");
			newConnection.setDoOutput(true);
			newConnection.setDoInput(true);
			newConnection.setRequestProperty("Authorization", LAYER_TOKEN);
			newConnection.setRequestProperty("Accept",
					"application/vnd.layer+json; version=1.0");
			newConnection
					.setRequestProperty("Content-Type", "application/json");
			newConnection.setUseCaches(false);*/

			/*
			 * HttpClient client = HttpClientBuilder.create().build(); HttpPost
			 * post = new HttpPost(url);
			 * post.setHeader("Authorization",LAYER_TOKEN);
			 * post.setHeader("Accept",
			 * "application/vnd.layer+json; version=1.0");
			 * post.setHeader("Content-Type","application/json"); HttpResponse
			 * newConnection = client.execute(post);
			 */

		/*	DataOutputStream wr = new DataOutputStream(
					((URLConnection) newConnection).getOutputStream());
			JSONArray request = new JSONArray();
			JSONObject userDet = new JSONObject();
			userDet.put("Name", "Tester");
			JSONObject partsDet = new JSONObject();
			partsDet.put("body", message);
			partsDet.put("mime_type", "text/plain");
			request.put(partsDet);
			JSONObject mess = new JSONObject();
			mess.put("sender", userDet);
			mess.put("parts", request);
			wr.writeBytes(mess.toString());
			wr.flush();
			wr.close();
			((URLConnection) newConnection).connect();
			if (((HttpURLConnection) newConnection).getResponseCode() == HttpURLConnection.HTTP_CREATED)
				return "Message Created";
			else
				return "Failure";
		} catch (Exception e) {
			return "failure";
		}
	}*/
	
	@Override
	public String confirmedMessage(String chatID, User user, String message){
		String url = "https://api.layer.com/apps/f0a2b518-52d3-11e5-994c-1c6821004889/conversations/e5cffc57-a7e7-42c0-8973-67f3c7196b0a/messages";
		String s = null;
		try{
			HttpClient client = new DefaultHttpClient();
			
//			client.getParams().setParameter("Name","Tester");
//			client.getParams().setParameter("body",message);
//			client.getParams().setParameter("mime_type","text/plain");
//			client.getParams().setParameter("Content-Type","application/json");
			
			JSONArray request = new JSONArray();
			JSONObject userDet = new JSONObject();
			userDet.put("Name",  "Tester");
			JSONObject partsDet = new JSONObject();
			partsDet.put("body", message);
			partsDet.put("mime_type", "text/plain");
			request.put(partsDet);
			JSONObject mess = new JSONObject();
			mess.put("sender",userDet);
			mess.put("parts",request);
			
			StringEntity se = new StringEntity(mess.toString());
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
			HttpPost post = new HttpPost(url);
			
			post.setEntity(se);
			post.addHeader("Accept","application/vnd.layer+json;version=1.1");
			post.addHeader("Authorization",LAYER_TOKEN);
			post.addHeader("Content-Type","application/json");
			
			HttpResponse response = (HttpResponse) client.execute(post);
			s=String.valueOf(response.getStatusLine().getStatusCode());
			
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_UNPROCESSABLE_ENTITY)
			return "ok";
		}
		catch(Exception e){
			return "failurecat";
		}
		return s;
	}
}
