package com.whosup.dao;

import java.util.List;
import java.util.UUID;

import com.whosup.model.User;

public interface UserDao {

	User findById(UUID id);

	List<User> findUserByIds(List<UUID> userIds);

	List<UUID> findByName(String search);

	void save(User user);

	UUID update(User user);

	List<User> findBySearch(String search,UUID userID);
	
	List<UUID> getAllUsers();

}