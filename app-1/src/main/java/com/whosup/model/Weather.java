package com.whosup.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(value = "weather")
@JsonInclude(Include.NON_NULL)
public class Weather {

	@PrimaryKey
	@JsonIgnore
	private WeatherKey pk;

	@Column(value = "sampled_latlong")
	private String latLong;

	@Column(value = "weather_main")
	private String main;

	@Column(value = "weather_description")
	private String description;

	@Column(value = "main_temp_min")
	private Double minTemp;

	@Column(value = "main_temp_max")
	private Double maxTemp;

	@Column(value = "wind_speed")
	private Double windSpeed;

	public Weather() {

	}

	public Weather(WeatherKey pk, String latLong, String main,
			String description, Double minTemp, Double maxTemp, Double windSpeed) {
		this.pk = pk;
		this.latLong = latLong;
		this.main = main;
		this.description = description;
		this.minTemp = minTemp;
		this.maxTemp = maxTemp;
		this.windSpeed = windSpeed;
	}

	public WeatherKey getPk() {
		return pk;
	}

	public void setPk(WeatherKey pk) {
		this.pk = pk;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(Double minTemp) {
		this.minTemp = minTemp;
	}

	public Double getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(Double maxTemp) {
		this.maxTemp = maxTemp;
	}

	public Double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(Double windSpeed) {
		this.windSpeed = windSpeed;
	}

}
