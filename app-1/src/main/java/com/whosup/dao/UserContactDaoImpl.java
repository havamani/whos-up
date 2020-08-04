package com.whosup.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.User;
import com.whosup.model.UserContact;
import com.whosup.model.UserContactKey;
import com.whosup.model.UserVerifiedDomain;

public class UserContactDaoImpl implements UserContactDao {

	@Autowired
	private CassandraOperations cassandraOperations;
	@Autowired
	private UserVerifiedDomainDao userVerifiedDomainDao;
	@Autowired
	private UserDao userDao;

	static Logger log = Logger.getLogger(UserContactDaoImpl.class.getName());

	@Override
	public UserContact findByContactId(UUID userId, UUID contactId) {
		if (userId == null || contactId == null)
			return null;
		Select select = QueryBuilder.select().from("user_contact")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.eq("contact_user_id", contactId));
		UserContact userContact = cassandraOperations.selectOne(select,
				UserContact.class);
		return userContact;
	}

	@Override
	public List<UserContact> findByContactIds(UUID userId, List<UUID> contactIds) {
		if (userId == null || contactIds == null)
			return new ArrayList<UserContact>();
		Select select = QueryBuilder.select().from("user_contact")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.in("contact_user_id", contactIds.toArray()));
		List<UserContact> userContacts = cassandraOperations.select(select,
				UserContact.class);
		return userContacts;
	}

	@Override
	public List<UUID> findEventSharedUserIds(UUID userId, List<UUID> contactIds) {
		if (userId == null || contactIds == null || contactIds.isEmpty())
			return new ArrayList<UUID>();
		Select select = QueryBuilder.select().from("user_contact")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.in("contact_user_id", contactIds.toArray()));
		List<UserContact> userContacts = cassandraOperations.select(select,
				UserContact.class);
		if (userContacts != null && userContacts.size() > 0) {
			List<UUID> userIds = new ArrayList<UUID>();
			for (UserContact u : userContacts) {
				if (u.getShareInterest())
					userIds.add(u.getContactUserID());
			}
			return userIds;
		}
		return new ArrayList<UUID>();
	}

	@Override
	public List<UserContact> findOtherContactsByUser(List<UUID> userIds,
			UUID contactUserId) {
		if (userIds == null || userIds.isEmpty() || contactUserId == null)
			return new ArrayList<UserContact>();
		Select select = QueryBuilder.select().from("user_contact")
				.allowFiltering();
		select.where(QueryBuilder.in("user_id", userIds.toArray())).and(
				QueryBuilder.eq("contact_user_id", contactUserId));
		List<UserContact> userContact = cassandraOperations.select(select,
				UserContact.class);
		return userContact;
	}

	@Override
	public Long findContactCount(UUID userId) {
		if (userId == null)
			return 0L;
		String select = "SELECT count(*) FROM user_contact WHERE user_id="
				+ userId + " AND connection_status_id = 0";
		Long count = cassandraOperations.queryForObject(select, Long.class);
		return count;
	}

	@Override
	public Long findFacebookCount(UUID userId) {
		if (userId == null)
			return 0L;
		String select = "SELECT count(*) FROM user_contact WHERE user_id="
				+ userId
				+ " AND connection_status_id = 0 AND is_connected_via_fb = true ALLOW FILTERING";
		Long count = cassandraOperations.queryForObject(select, Long.class);
		return count;
	}

	@Override
	public Long findFacebookStarredCount(UUID userId) {
		if (userId == null)
			return 0L;
		String select = "SELECT count(*) FROM user_contact WHERE user_id="
				+ userId
				+ " AND connection_status_id = 0 AND is_connected_via_fb = true AND is_starred = true ALLOW FILTERING";
		Long count = cassandraOperations.queryForObject(select, Long.class);
		return count;
	}

	@Override
	public Long findRequestCount(UUID userId) {
		if (userId == null)
			return 0L;
		String select = "SELECT count(*) FROM user_contact WHERE user_id="
				+ userId + " AND connection_status_id = 1";
		Long count = cassandraOperations.queryForObject(select, Long.class);
		return count;
	}

	@Override
	public List<UserContact> findContacts(UUID userId) {
		if (userId == null)
			return new ArrayList<UserContact>();
		Select select = QueryBuilder.select().from("user_contact")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.eq("connection_status_id", new Integer(0)));
		List<UserContact> userContact = cassandraOperations.select(select,
				UserContact.class);
		return userContact;
	}

	@Override
	public List<UserContact> findOtherContacts(UUID userId) {
		if (userId == null)
			return new ArrayList<UserContact>();
		Select select = QueryBuilder.select().from("user_contact")
				.allowFiltering();
		select.where(QueryBuilder.eq("contact_user_id", userId));
		List<UserContact> userContact = cassandraOperations.select(select,
				UserContact.class);
		return userContact;
	}

	@Override
	public List<UserContact> findContactsByFb(UUID userId) {
		if (userId == null)
			return new ArrayList<UserContact>();
		Select select = QueryBuilder.select().from("user_contact")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.eq("is_connected_via_fb", new Boolean(true)));
		List<UserContact> userContact = cassandraOperations.select(select,
				UserContact.class);
		return userContact;
	}

	@Override
	public List<UserContact> findContactsByLn(UUID userId) {
		if (userId == null)
			return new ArrayList<UserContact>();
		Select select = QueryBuilder.select().from("user_contact")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder
						.eq("is_connected_via_linked_in", new Boolean(true)));
		List<UserContact> userContact = cassandraOperations.select(select,
				UserContact.class);
		return userContact;
	}

	@Override
	public List<UserContact> findContactsByEmailDomain(UUID userId) {
		if (userId == null)
			return new ArrayList<UserContact>();
		Select select = QueryBuilder.select().from("user_contact")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId)).and(
				QueryBuilder.eq("is_connected_via_email_domain", new Boolean(
						true)));
		List<UserContact> userContact = cassandraOperations.select(select,
				UserContact.class);
		return userContact;
	}

	@Override
	public List<UserContact> findMyCrowd(UUID userId) {
		if (userId == null)
			return new ArrayList<UserContact>();
		Select select = QueryBuilder.select().from("user_contact");
		select.where(QueryBuilder.eq("user_id", userId));
		List<UserContact> userContacts = cassandraOperations.select(select,
				UserContact.class);
		if (userContacts != null && userContacts.size() > 0) {
			List<UserContact> filtered = new ArrayList<UserContact>();
			for (UserContact u : userContacts) {
				if (u.getIsStarred())
					filtered.add(u);
			}
			return filtered;
		} else
			return userContacts;
	}

	@Override
	public List<UserContact> findContactsByNetwork(UUID userId,
			List<UUID> contactUserIDs) {
		if (userId == null || contactUserIDs == null
				|| contactUserIDs.isEmpty())
			return new ArrayList<UserContact>();
		Select select = QueryBuilder.select().from("user_contact")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId))
				.and(QueryBuilder.in("contact_user_id",
						contactUserIDs.toArray()))
				.and(QueryBuilder.eq("connection_status_id", new Integer(0)));
		List<UserContact> userContact = cassandraOperations.select(select,
				UserContact.class);
		return userContact;
	}

	@Override
	public List<UserContact> findContactsByWhosUp(UUID userId) {
		if (userId == null)
			return new ArrayList<UserContact>();
		Select select = QueryBuilder.select().from("user_contact")
				.allowFiltering();
		select.where(QueryBuilder.eq("user_id", userId))
				.and(QueryBuilder.eq("is_connected_via_whosup", new Boolean(
						true)))
				.and(QueryBuilder.eq("connection_status_id", new Integer(0)));
		List<UserContact> userContact = cassandraOperations.select(select,
				UserContact.class);
		return userContact;
	}

	@Override
	public List<UUID> findCommonFriends(UUID userId, UUID contactID) {
		if (userId == null || contactID == null)
			return new ArrayList<UUID>();
		Select select = QueryBuilder.select("contact_user_id").from(
				"user_contact");
		select.where(QueryBuilder.eq("user_id", userId));
		List<UserContact> userContact = cassandraOperations.select(select,
				UserContact.class);
		if (userContact != null) {
			List<UUID> userIds = new ArrayList<UUID>();
			for (UserContact u : userContact) {
				userIds.add(u.getContactUserID());
			}
			select = QueryBuilder.select().from("user_contact");
			select.where(QueryBuilder.eq("user_id", contactID)).and(
					QueryBuilder.in("contact_user_id", userIds.toArray()));
			userContact = cassandraOperations.select(select, UserContact.class);
			List<UUID> commomUserIds = new ArrayList<UUID>();
			for (UserContact u : userContact) {
				commomUserIds.add(u.getContactUserID());
			}
			return commomUserIds;
		}
		return null;
	}

	@Override
	public List<UserContact> findContactsByGatherFrom(UUID userId,
			List<String> gatherFrom) {
		Map<UUID, UserContact> contactsMap = new HashMap<UUID, UserContact>();
		if (userId == null || gatherFrom == null)
			return new ArrayList<UserContact>();
		if (gatherFrom.contains("fb")) {
			Set<UserContact> contact = new HashSet<UserContact>(
					findContactsByFb(userId));
			for (UserContact user : contact) {
				contactsMap.put(user.getPk().getContactUserID(), user);
			}
		}
		if (gatherFrom.contains("ln")) {
			Set<UserContact> contact = new HashSet<UserContact>(
					findContactsByLn(userId));
			for (UserContact user : contact) {
				contactsMap.put(user.getPk().getContactUserID(), user);
			}
		}
		if (gatherFrom.contains("whosup")) {
			Set<UserContact> contact = new HashSet<UserContact>(
					findContactsByWhosUp(userId));
			for (UserContact user : contact) {
				contactsMap.put(user.getPk().getContactUserID(), user);
			}
		}
		if (gatherFrom.contains("my_net")) {
			Set<UserContact> contact = new HashSet<UserContact>(
					findContactsByEmailDomain(userId));
			List<UserContact> contactsToFilter = findContactsByGatherFrom(
					userId, Arrays.asList("fb", "ln", "whosup"));
			List<UserContact> userContacts = findSameDomainContacts(userId,
					new ArrayList<>(contact), contactsToFilter);
			for (UserContact user : userContacts) {
				contactsMap.put(user.getPk().getContactUserID(), user);
			}
		}
		return new ArrayList<UserContact>(contactsMap.values());
	}

	private List<UserContact> findSameDomainContacts(UUID userID,
			List<UserContact> userContacts, List<UserContact> contactsToFilter) {
		contactsToFilter.addAll(userContacts);
		UserVerifiedDomain userdomain = userVerifiedDomainDao
				.findByUser(userID);
		List<UUID> sameDomainUserIDs = new ArrayList<UUID>();
		if (userdomain != null) {
			sameDomainUserIDs = userVerifiedDomainDao
					.findUsersByDomain(userdomain.getDomainName());
			if (sameDomainUserIDs.contains(userID)) {
				sameDomainUserIDs.remove(userID);
			}
		}
		for (UUID usersInSameDomain : sameDomainUserIDs) {
			boolean ifUserIsNotThere = true;
			for (UserContact u : contactsToFilter) {
				if (u.getPk().getContactUserID().equals(usersInSameDomain)) {
					ifUserIsNotThere = false;
					break;
				}
			}
			if (ifUserIsNotThere) {
				User newUser = userDao.findById(usersInSameDomain);
				if (newUser != null) {
					UserContact newUserContact = new UserContact();
					UserContactKey newUserContactkey = new UserContactKey();
					newUserContactkey.setUserID(usersInSameDomain);
					newUserContactkey.setContactUserID(newUser.getUserID());
					newUserContact.setPk(newUserContactkey);
					newUserContact.setContactFullName(newUser.getFullName());
					newUserContact.setContactPhotoPath(newUser.getPhotoPath());
					newUserContact.setIsConnectedViaEmailDomain(true);
					newUserContact.setIsStarred(false);
					userContacts.add(newUserContact);
				}
			}
		}
		return userContacts;
	}

	@Override
	public void save(List<UserContact> contacts) {
		if (contacts != null && !contacts.isEmpty())
			cassandraOperations.insert(contacts);
	}

	@Override
	public void save(UserContact contact) {
		if (contact != null)
			cassandraOperations.insert(contact);
	}

	@Override
	public void delete(UserContact contact) {
		if (contact != null)
			cassandraOperations.delete(contact);
	}

	@Override
	public void update(UserContact contact) {
		if (contact != null)
			cassandraOperations.update(contact);
	}

	@Override
	public void update(List<UserContact> contact) {
		if (contact != null)
			cassandraOperations.update(contact);
	}

}
