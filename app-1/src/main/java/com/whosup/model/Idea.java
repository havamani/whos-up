package com.whosup.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "idea")
@JsonInclude(Include.NON_NULL)
public class Idea extends Model {

	@PrimaryKey(value = "idea_id")
	private UUID ideaID;

	private String title;

	private String description;

	@Column(value = "original_photo_url")
	private String photoPath;

	@Column(value = "photo_640_1000_path")
	private String largePhotoPath;

	@Column(value = "photo_300_300_path")
	private String smallPhotoPath;

	private Integer gender;

	private Integer age;

	@Column(value = "typical_group_size")
	private Integer groupSize;

	@Column(value = "typical_group_size_generic")
	private Integer groupSizeGeneric;

	@Column(value = "typical_group_size_specific_lower")
	private Integer groupSizeLower;

	@Column(value = "typical_group_size_specific_upper")
	private Integer groupSizeHigher;

	@Column(value = "primary_category")
	private String primaryCategory;

	@Column(value = "secondary_category")
	private String secondaryCategory;

	private List<Integer> weather;

	@Column(value = "facility_requirement")
	private Integer facilityRequired;

	@Column(value = "facility_generic")
	private String facilityGeneric;

	@Column(value = "facility_specific")
	private String facilitySpecific;

	@Column(value = "facility_specific_googleid")
	private String facilitySpecificGoogleId;

	@Column(value = "when_type")
	private Integer whenType;

	@Column(value = "when_generic_day")
	private Integer whenDay;

	@Column(value = "when_generic_time")
	private Integer whenTime;

	@Column(value = "when_specific")
	private List<Date> whenSpecific;

	@Column(value = "organizational_difficulty")
	private Integer organizationalDifficulty;

	@Column(value = "time_to_organize")
	private String timeToOrganize;

	@Column(value = "typical_price")
	private Integer typicalPrice;

	@Column(value = "typical_price_lower")
	private BigDecimal typicalPriceLower;

	@Column(value = "typical_price_upper")
	private BigDecimal typicalPriceUpper;

	@Column(value = "tag_environment")
	private String tagsEnvironment;

	@Column(value = "tag_experience")
	private String tagsExperience;

	@Column(value = "tag_people")
	private String tagsPeople;

	@Column(value = "idea_keywords")
	private List<String> keywords;
	
	@Column(value = "web_link")
	private String webLink;

	public Idea() {

	}

	public Idea(UUID ideaID, String title, String description) {
		this.ideaID = ideaID;
		this.title = title;
		this.description = description;
	}

	public UUID getIdeaID() {
		return ideaID;
	}

	public void setIdeaID(UUID ideaID) {
		this.ideaID = ideaID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getLargePhotoPath() {
		return largePhotoPath;
	}

	public void setLargePhotoPath(String largePhotoPath) {
		this.largePhotoPath = largePhotoPath;
	}

	public String getSmallPhotoPath() {
		return smallPhotoPath;
	}

	public void setSmallPhotoPath(String smallPhotoPath) {
		this.smallPhotoPath = smallPhotoPath;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(Integer groupSize) {
		this.groupSize = groupSize;
	}

	public Integer getGroupSizeGeneric() {
		return groupSizeGeneric;
	}

	public void setGroupSizeGeneric(Integer groupSizeGeneric) {
		this.groupSizeGeneric = groupSizeGeneric;
	}

	public Integer getGroupSizeLower() {
		return groupSizeLower;
	}

	public void setGroupSizeLower(Integer groupSizeLower) {
		this.groupSizeLower = groupSizeLower;
	}

	public Integer getGroupSizeHigher() {
		return groupSizeHigher;
	}

	public void setGroupSizeHigher(Integer groupSizeHigher) {
		this.groupSizeHigher = groupSizeHigher;
	}

	public String getPrimaryCategory() {
		return primaryCategory;
	}

	public void setPrimaryCategory(String primaryCategory) {
		this.primaryCategory = primaryCategory;
	}

	public String getSecondaryCategory() {
		return secondaryCategory;
	}

	public void setSecondaryCategory(String secondaryCategory) {
		this.secondaryCategory = secondaryCategory;
	}

	public List<Integer> getWeather() {
		return weather;
	}

	public void setWeather(List<Integer> weather) {
		this.weather = weather;
	}

	public Integer getFacilityRequired() {
		return facilityRequired;
	}

	public void setFacilityRequired(Integer facilityRequired) {
		this.facilityRequired = facilityRequired;
	}

	public String getFacilityGeneric() {
		return facilityGeneric;
	}

	public void setFacilityGeneric(String facilityGeneric) {
		this.facilityGeneric = facilityGeneric;
	}

	public String getFacilitySpecific() {
		return facilitySpecific;
	}

	public void setFacilitySpecific(String facilitySpecific) {
		this.facilitySpecific = facilitySpecific;
	}

	public String getFacilitySpecificGoogleId() {
		return facilitySpecificGoogleId;
	}

	public void setFacilitySpecificGoogleId(String facilitySpecificGoogleId) {
		this.facilitySpecificGoogleId = facilitySpecificGoogleId;
	}

	public Integer getWhenType() {
		return whenType;
	}

	public void setWhenType(Integer whenType) {
		this.whenType = whenType;
	}

	public Integer getWhenDay() {
		return whenDay;
	}

	public void setWhenDay(Integer whenDay) {
		this.whenDay = whenDay;
	}

	public Integer getWhenTime() {
		return whenTime;
	}

	public void setWhenTime(Integer whenTime) {
		this.whenTime = whenTime;
	}

	public List<Date> getWhenSpecific() {
		return whenSpecific;
	}

	public void setWhenSpecific(List<Date> whenSpecific) {
		this.whenSpecific = whenSpecific;
	}

	public Integer getOrganizationalDifficulty() {
		return organizationalDifficulty;
	}

	public void setOrganizationalDifficulty(Integer organizationalDifficulty) {
		this.organizationalDifficulty = organizationalDifficulty;
	}

	public String getTimeToOrganize() {
		return timeToOrganize;
	}

	public void setTimeToOrganize(String timeToOrganize) {
		this.timeToOrganize = timeToOrganize;
	}

	public Integer getTypicalPrice() {
		return typicalPrice;
	}

	public void setTypicalPrice(Integer typicalPrice) {
		this.typicalPrice = typicalPrice;
	}

	public BigDecimal getTypicalPriceLower() {
		return typicalPriceLower;
	}

	public void setTypicalPriceLower(BigDecimal typicalPriceLower) {
		this.typicalPriceLower = typicalPriceLower;
	}

	public BigDecimal getTypicalPriceUpper() {
		return typicalPriceUpper;
	}

	public void setTypicalPriceUpper(BigDecimal typicalPriceUpper) {
		this.typicalPriceUpper = typicalPriceUpper;
	}

	public String getTagsEnvironment() {
		return tagsEnvironment;
	}

	public void setTagsEnvironment(String tagsEnvironment) {
		this.tagsEnvironment = tagsEnvironment;
	}

	public String getTagsExperience() {
		return tagsExperience;
	}

	public void setTagsExperience(String tagsExperience) {
		this.tagsExperience = tagsExperience;
	}

	public String getTagsPeople() {
		return tagsPeople;
	}

	public void setTagsPeople(String tagsPeople) {
		this.tagsPeople = tagsPeople;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getWebLink() {
		return webLink;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}
	
	

}
