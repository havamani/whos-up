package com.whosup.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Suggestion {

	private String status;
	private List<SuggestList> response;

	public Suggestion() {

	}

	public Suggestion(String status) {
		super();
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<SuggestList> getResponse() {
		return response;
	}

	public void setResponse(List<SuggestList> response) {
		this.response = response;
	}

}
