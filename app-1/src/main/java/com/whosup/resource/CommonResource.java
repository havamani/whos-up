package com.whosup.resource;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.whosup.common.AppErrorMessage;
import com.whosup.common.AppException;
import com.whosup.dao.UserAccessTokenDao;
import com.whosup.dao.UserDao;
import com.whosup.model.User;
import com.whosup.model.request.NetworkRequest;
import com.whosup.model.response.Status;
import com.whosup.service.CommonService;

@Path("/common")
@Component
public class CommonResource {

	@Autowired
	private CommonService commonService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserAccessTokenDao userAccessTokenDao;

	@POST
	@Produces("application/json")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/upload")
	public Status createEvent(
			@FormDataParam("file") InputStream upload,
			@FormDataParam("file") FormDataContentDisposition contentDispositionHeader)
			throws FileNotFoundException {

		String path = commonService.uploadImage(upload,
				contentDispositionHeader);
		Status status = new Status();
		status.setPath(path);
		return status;
	}

	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/layer/token")
	public Status createEvent(NetworkRequest request) {

		Status status = new Status();
		String token = "";
		try {
			token = commonService.createUser(request.getNonce(),
					validateAccessToken(request.getAccessToken()).getUserID());
		} catch (Exception e) {
			return status;
		}
		status.setLayerToken(token);
		return status;
	}

	private User validateAccessToken(UUID accessToken) {
		User user = userDao.findById(userAccessTokenDao
				.findUserIdByAccessToken(accessToken));
		if (user != null) {
			if (user.getAccessTokenExpireDate().before(new Date()))
				throw new AppException(new AppErrorMessage("400",
						"Your Access Token has been Expired"));
			return user;
		} else
			throw new AppException(new AppErrorMessage("101",
					"Given Token is invalid"));
	}
	
}