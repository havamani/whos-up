package com.whosup.dao;

import com.whosup.model.Weather;
import com.whosup.model.request.NetworkRequest;

public interface WeatherDao {

	Weather selectRecentWeather(NetworkRequest request);

	void save(Weather weather);

}
