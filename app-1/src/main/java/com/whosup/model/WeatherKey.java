package com.whosup.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class WeatherKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKeyColumn(name = "admin_3", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String admin3;
	@PrimaryKeyColumn(name = "admin_2", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private String admin2;
	@PrimaryKeyColumn(name = "admin_1", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
	private String admin1;
	@PrimaryKeyColumn(name = "country", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
	private String country;
	@PrimaryKeyColumn(name = "date_submitted", ordinal = 4, type = PrimaryKeyType.CLUSTERED)
	private Date dateSubmitted;

	public WeatherKey() {

	}

	public WeatherKey(String admin3, String admin2, String admin1,
			String country, Date dateSubmitted) {
		this.admin3 = admin3;
		this.admin2 = admin2;
		this.admin1 = admin1;
		this.country = country;
		this.dateSubmitted = dateSubmitted;
	}

	public String getAdmin3() {
		return admin3;
	}

	public void setAdmin3(String admin3) {
		this.admin3 = admin3;
	}

	public String getAdmin2() {
		return admin2;
	}

	public void setAdmin2(String admin2) {
		this.admin2 = admin2;
	}

	public String getAdmin1() {
		return admin1;
	}

	public void setAdmin1(String admin1) {
		this.admin1 = admin1;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getDateSubmitted() {
		return dateSubmitted;
	}

	public void setDateSubmitted(Date dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((admin1 == null) ? 0 : admin1.hashCode());
		result = prime * result + ((admin2 == null) ? 0 : admin2.hashCode());
		result = prime * result + ((admin3 == null) ? 0 : admin3.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((dateSubmitted == null) ? 0 : dateSubmitted.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeatherKey other = (WeatherKey) obj;
		if (admin1 == null) {
			if (other.admin1 != null)
				return false;
		} else if (!admin1.equals(other.admin1))
			return false;
		if (admin2 == null) {
			if (other.admin2 != null)
				return false;
		} else if (!admin2.equals(other.admin2))
			return false;
		if (admin3 == null) {
			if (other.admin3 != null)
				return false;
		} else if (!admin3.equals(other.admin3))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (dateSubmitted == null) {
			if (other.dateSubmitted != null)
				return false;
		} else if (!dateSubmitted.equals(other.dateSubmitted))
			return false;
		return true;
	}

}
