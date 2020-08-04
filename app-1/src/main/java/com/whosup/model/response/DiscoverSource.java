package com.whosup.model.response;

import java.util.List;

public class DiscoverSource {

	private List<DiscoverList> ideaSource1;
	private List<DiscoverList> ideaSource2;
	private List<DiscoverList> ideaSource3;
	private List<DiscoverList> ideaSource4;
	private List<DiscoverList> ideaSource5;
	private List<DiscoverList> eventSource1;
	private List<DiscoverList> eventSource2;
	private List<DiscoverList> eventSource3;
	private List<DiscoverList> eventSource4;
	private List<DiscoverList> deletedEvents;

	private List<String> eventBriteWebLinks;
	private String searchBy;

	public String getSearchBy() {
		return searchBy;
	}

	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}

	public List<String> getEventBriteWebLinks() {
		return eventBriteWebLinks;
	}

	public void setEventBriteWebLinks(List<String> eventBriteWebLinks) {
		this.eventBriteWebLinks = eventBriteWebLinks;
	}

	public List<DiscoverList> getIdeaSource1() {
		return ideaSource1;
	}

	public List<DiscoverList> getDeletedEvents() {
		return deletedEvents;
	}

	public void setDeletedEvents(List<DiscoverList> deletedEvents) {
		this.deletedEvents = deletedEvents;
	}

	public void setIdeaSource1(List<DiscoverList> ideaSource1) {
		this.ideaSource1 = ideaSource1;
	}

	public List<DiscoverList> getIdeaSource2() {
		return ideaSource2;
	}

	public void setIdeaSource2(List<DiscoverList> ideaSource2) {
		this.ideaSource2 = ideaSource2;
	}

	public List<DiscoverList> getIdeaSource3() {
		return ideaSource3;
	}

	public void setIdeaSource3(List<DiscoverList> ideaSource3) {
		this.ideaSource3 = ideaSource3;
	}

	public List<DiscoverList> getIdeaSource4() {
		return ideaSource4;
	}

	public void setIdeaSource4(List<DiscoverList> ideaSource4) {
		this.ideaSource4 = ideaSource4;
	}

	public List<DiscoverList> getEventSource1() {
		return eventSource1;
	}

	public void setEventSource1(List<DiscoverList> eventSource1) {
		this.eventSource1 = eventSource1;
	}

	public List<DiscoverList> getEventSource2() {
		return eventSource2;
	}

	public void setEventSource2(List<DiscoverList> eventSource2) {
		this.eventSource2 = eventSource2;
	}

	public List<DiscoverList> getEventSource3() {
		return eventSource3;
	}

	public void setEventSource3(List<DiscoverList> eventSource3) {
		this.eventSource3 = eventSource3;
	}

	public List<DiscoverList> getEventSource4() {
		return eventSource4;
	}

	public void setEventSource4(List<DiscoverList> eventSource4) {
		this.eventSource4 = eventSource4;
	}

	public List<DiscoverList> getIdeaSource5() {
		return ideaSource5;
	}

	public void setIdeaSource5(List<DiscoverList> ideaSource5) {
		this.ideaSource5 = ideaSource5;
	}

}
