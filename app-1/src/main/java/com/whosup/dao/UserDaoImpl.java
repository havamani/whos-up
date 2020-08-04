package com.whosup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.User;
import com.whosup.model.UserAccessToken;

public class UserDaoImpl implements UserDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Autowired
	private UserAccessTokenDao userAccessTokenDao;

	@Override
	public User findById(UUID id) {
		if (id == null)
			return null;
		return cassandraOperations.selectOneById(User.class, id);
	}

	@Override
	public List<UUID> findByName(String search) {
		JSONObject json = new JSONObject();
		json.put("q", "full_name:(" + search + "~0.8)");
		Select select = QueryBuilder.select().from("user");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<User> users = cassandraOperations.select(select, User.class);
		if (users != null && users.size() > 0) {
			List<UUID> userIds = new ArrayList<UUID>();
			for (User u : users) {
				userIds.add(u.getUserID());
			}
			return userIds;
		} else
			return new ArrayList<UUID>();
	}

	@Override
	public List<User> findBySearch(String search, UUID userID) {
		JSONObject json = new JSONObject();
		json.put("q", "full_name:(" + search + "~0.8) facebook_email:("
				+ search + "~0.8) verified_domain_email:(" + search
				+ "~0.5) verified_mobile_number:(" + search
				+ ") linked_in_email:(" + search + "~0.8)" + " NOT user_id:"
				+ userID);
		Select select = QueryBuilder.select().from("user");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<User> users = cassandraOperations.select(select, User.class);
		if (users != null && users.size() > 0) {
			return users;
		} else
			return new ArrayList<User>();

	}

	@Override
	public List<User> findUserByIds(List<UUID> userIds) {
		if (userIds == null || userIds.isEmpty())
			return null;
		Select select = QueryBuilder.select().from("user");
		select.where(QueryBuilder.in("user_id", userIds.toArray()));
		List<User> user = cassandraOperations.select(select, User.class);
		return user;
	}

	@Override
	public void save(User user) {
		if (user != null) {
			user.setUserID(User.getUUID());
			cassandraOperations.insert(user);
			userAccessTokenDao.save(new UserAccessToken(user.getUserID(), user
					.getAccessToken()));
		}
	}

	@Override
	public UUID update(User user) {
		if (user != null) {
			cassandraOperations.update(user);
			UserAccessToken userToken = userAccessTokenDao.findByUserId(user
					.getUserID());
			if (userToken != null) {
				userToken.setAccessToken(user.getAccessToken());
				userAccessTokenDao.update(userToken);
			}
			return user.getUserID();
		} else
			return null;
	}
	
	@Override
	public List<UUID> getAllUsers(){
		Select select = QueryBuilder.select().from("user");
		List<User> allUsers = cassandraOperations.select(select, User.class);
		List<UUID> userIds = new ArrayList<>();
		if (allUsers == null || allUsers.isEmpty())
			return new ArrayList<UUID>();
		for(User u:allUsers)
			userIds.add(u.getUserID());
		return userIds;
	}
	

}