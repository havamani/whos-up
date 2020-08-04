package com.whosup.common;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AppException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public AppException(AppErrorMessage message) {
		super(Response.status(Response.Status.BAD_REQUEST).entity(message)
				.type(MediaType.APPLICATION_JSON).build());
	}
	
}
