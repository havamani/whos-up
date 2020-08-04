package com.whosup.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.Weather;
import com.whosup.model.request.NetworkRequest;

public class WeatherDaoImpl implements WeatherDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Override
	public Weather selectRecentWeather(NetworkRequest request) {
		if (request.getAdmin2() == null || request.getAdmin3() == null)
			return null;
		Select select = QueryBuilder.select().from("weather");
		select.where(QueryBuilder.eq("admin_3", request.getAdmin3())).and(
				QueryBuilder.eq("admin_2", request.getAdmin2()));
		List<Weather> weathers = cassandraOperations.select(select,
				Weather.class);
		if (weathers != null && weathers.size() > 0)
			return weathers.get(0);
		else
			return null;
	}

	@Override
	public void save(Weather weather) {
		if (weather != null)
			cassandraOperations.insert(weather);
	}

}
