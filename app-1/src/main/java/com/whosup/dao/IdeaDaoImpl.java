package com.whosup.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.whosup.model.Idea;
import com.whosup.model.request.NetworkRequest;
import com.whosup.service.MomentumProperties;

public class IdeaDaoImpl implements IdeaDao {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Autowired
	@Qualifier("analytics")
	private Session session;

	@Override
	public Idea findById(UUID ideaId) {
		if (ideaId == null)
			return null;
		Select select = QueryBuilder.select().from("idea");
		select.where(QueryBuilder.eq("idea_id", ideaId));
		Idea idea = cassandraOperations.selectOne(select, Idea.class);
		return idea;
	}

	@Override
	public List<Idea> findAllIdeas() {
		Select select = QueryBuilder.select().all().from("idea");
		List<Idea> ideas = cassandraOperations.select(select, Idea.class);
		return ideas;
	}

	@Override
	public List<Idea> findAll(int ideaSize) {
		Select select = QueryBuilder.select().all().from("idea")
				.limit(ideaSize);
		List<Idea> ideas = cassandraOperations.select(select, Idea.class);
		return ideas;

	}

	@Override
	public Map<Idea, Double> getIdeaFromSource1(List<UUID> contactUserIds,
			String search) {
		if (contactUserIds == null || contactUserIds.isEmpty())
			return new HashMap<Idea, Double>();
		Select select = QueryBuilder.select().from("user_starred_idea_stats");
		select.where(QueryBuilder.in("user_id", contactUserIds.toArray()));
		List<Row> userStarredIdea = session.execute(select).all();
		List<UUID> userStarredIdeaIds = new ArrayList<UUID>();
		Map<Idea, Double> ideasMomentum = new HashMap<Idea, Double>();
		Map<UUID, Double> ideaIdsMomentum = new HashMap<UUID, Double>();
		for (Row r : userStarredIdea) {
			ideaIdsMomentum.put(r.getUUID("idea_id"), r.getDouble("momentum"));
			userStarredIdeaIds.add(r.getUUID("idea_id"));
		}
		List<Idea> ideas = new ArrayList<Idea>();
		if (search == null)
			ideas = findByIds(userStarredIdeaIds, null);
		else
			ideas = findByIds(userStarredIdeaIds, search);
		for (Idea i : ideas) {
			ideasMomentum.put(i, ideaIdsMomentum.get(i.getIdeaID()));
		}
		return ideasMomentum;
	}

	@Override
	public Map<Idea, Double> getIdeaFromSource2(String domain, String search) {
		if (domain == null || domain.isEmpty())
			return new HashMap<Idea, Double>();
		JSONObject json = new JSONObject();
		json.put("q", "company_domain:" + domain);
		Select select = QueryBuilder.select().from("idea_domain_stats");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Row> idea_domain_stats = session.execute(select).all();
		List<UUID> companyDomainIdeaIds = new ArrayList<UUID>();
		Map<Idea, Double> ideasMomentum = new HashMap<Idea, Double>();
		Map<UUID, Double> ideaIdsMomentum = new HashMap<UUID, Double>();
		for (Row r : idea_domain_stats) {
			ideaIdsMomentum.put(r.getUUID("idea_id"), r.getDouble("momentum"));
			companyDomainIdeaIds.add(r.getUUID("idea_id"));
		}
		List<Idea> ideas = new ArrayList<Idea>();
		if (search == null)
			ideas = findByIds(companyDomainIdeaIds, null);
		else
			ideas = findByIds(companyDomainIdeaIds, search);
		for (Idea i : ideas) {
			ideasMomentum.put(i, ideaIdsMomentum.get(i.getIdeaID()));
		}
		return ideasMomentum;
	}

	@Override
	public Map<Idea, Double> getIdeaFromSource3(String admin_level_1,
			String admin_level_2, String admin_level_3, String country,
			String search) {

		if (admin_level_1 == null || admin_level_1.isEmpty()
				&& admin_level_2 == null || admin_level_2.isEmpty()
				&& admin_level_3 == null || admin_level_3.isEmpty()
				&& country == null || country.isEmpty()) {
			return new HashMap<Idea, Double>();
		}
		JSONObject json = new JSONObject();
		json.put("q", "admin_level_1:(" + admin_level_1 + ") admin_level_2:("
				+ admin_level_2 + ") admin_level_3:(" + admin_level_3
				+ ") country:(" + country + ")");
		Select select = QueryBuilder.select().from("idea_location_stats");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Row> idea_location_stats = session.execute(select).all();
		List<UUID> locationIdeaIds = new ArrayList<UUID>();
		Map<Idea, Double> ideasMomentum = new HashMap<Idea, Double>();
		Map<UUID, Double> ideaIdsMomentum = new HashMap<UUID, Double>();
		for (Row r : idea_location_stats) {
			ideaIdsMomentum.put(r.getUUID("idea_id"), r.getDouble("momentum"));
			locationIdeaIds.add(r.getUUID("idea_id"));
		}
		List<Idea> ideas = new ArrayList<Idea>();
		if (search == null)
			ideas = findByIds(locationIdeaIds, null);
		else
			ideas = findByIds(locationIdeaIds, search);
		for (Idea i : ideas) {
			ideasMomentum.put(i, ideaIdsMomentum.get(i.getIdeaID()));
		}
		return ideasMomentum;
	}

	@Override
	public Map<Idea, Double> getIdeaFromSource4(String country, String search) {
		if (country == null || country.isEmpty())
			return new HashMap<Idea, Double>();
		JSONObject json = new JSONObject();
		json.put("q",
				"country:(" + country + ") random:(" + new Date().getTime()
						+ " asc) ");
		Select select = QueryBuilder.select().from("country_idea_selection");
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Row> country_idea_selection = session.execute(select).all();
		List<UUID> countryIdeaIds = new ArrayList<UUID>();
		Map<Idea, Double> ideasMomentum = new HashMap<Idea, Double>();
		Map<UUID, Double> ideaIdsMomentum = new HashMap<UUID, Double>();
		for (Row r : country_idea_selection) {
			ideaIdsMomentum.put(r.getUUID("idea_id"), r.getDouble("momentum"));
			countryIdeaIds.add(r.getUUID("idea_id"));
		}
		List<Idea> ideas = new ArrayList<Idea>();
		if (search == null)
			ideas = findByIds(countryIdeaIds, null);
		else
			ideas = findByIds(countryIdeaIds, search);
		for (Idea i : ideas) {
			ideasMomentum.put(i, ideaIdsMomentum.get(i.getIdeaID()));
		}
		return ideasMomentum;
	}

	@Override
	public List<Idea> findByIds(List<UUID> ideaIds, String search) {
		List<Idea> ideas = new ArrayList<Idea>();
		if (search == null) {
			if (ideaIds == null || ideaIds.isEmpty())
				return new ArrayList<Idea>();
			Select select = QueryBuilder.select().from("idea");
			select.where(QueryBuilder.in("idea_id", ideaIds.toArray()));
			ideas = cassandraOperations.select(select, Idea.class);
			return ideas;
		} else {
			if (ideaIds == null || ideaIds.isEmpty())
				return new ArrayList<Idea>();
			JSONObject json = new JSONObject();
			json.put("q", "idea_id:("
					+ ideaIds.toString().replace(",", " ").replace("[", "")
							.replace("]", "") + ") AND idea_keywords:("
					+ search + "~0.8) description:(" + search + "~0.8) title:("
					+ search + "~0.8) primary_category:(" + search
					+ "~0.8) secondary_category:(" + search
					+ "~0.8) time_to_organize:(" + search
					+ "~0.8) facility_generic:(" + search
					+ "~0.8) facility_specific:(" + search
					+ "~0.8) tag_environment:(" + search
					+ "~0.8) tag_experience:(" + search + "~0.8) tag_people:("
					+ search + "~0.8)");
			Select select = QueryBuilder.select().from("idea");
			select.where(QueryBuilder.eq("solr_query", json.toString()));
			ideas = cassandraOperations.select(select, Idea.class);
			return ideas;
		}
	}

	@Override
	public UUID updateStarredIdeaMomentum(UUID contactId, UUID ideaId) {
		if (contactId == null || ideaId == null)
			return null;
		Select select = QueryBuilder.select().from("user_starred_idea_stats");
		select.where(QueryBuilder.eq("user_id", contactId)).and(
				QueryBuilder.eq("idea_id", ideaId));
		Row one = session.execute(select).one();
		if (one != null && one.getUUID("user_id") != null
				&& one.getUUID("idea_id") != null) {
			MomentumProperties momentum = new MomentumProperties(
					one.getDouble("mass"), one.getDouble("momentum"),
					one.getDouble("compound_effect"),
					MomentumProperties.dateDiff(
							one.getDate("time_momentum_last_updated"),
							new Date()));
			MomentumProperties updatedValue = momentum
					.updateStarredIdeaMomentum(MomentumProperties.dateDiff(
							one.getDate("time_momentum_last_updated"),
							new Date()));
			String cql = "update analytics.user_starred_idea_stats SET momentum = "
					+ updatedValue.getMomentum()
					+ ", compound_effect = "
					+ updatedValue.getCompoundEffect()
					+ ",time_momentum_last_updated ="
					+ new Date().getTime()
					+ " where user_id = "
					+ contactId
					+ " AND idea_id = "
					+ ideaId;
			session.execute(cql);
		} else {
			String cql = "insert into analytics.user_starred_idea_stats "
					+ "(user_id, idea_id, compound_effect, first_buzzed_date, "
					+ "mass, momentum, time_momentum_last_updated) values ("
					+ contactId + ", " + ideaId + ", 0.0,"
					+ new Date().getTime() + ", 1.0, 0.0,"
					+ new Date().getTime() + ")";
			session.execute(cql);
		}
		return ideaId;
	}

	@Override
	public UUID updateIdeaDomainMomentum(UUID ideaId, String companyDomain) {
		Row one = selectIdeaDomainMomentum(ideaId, companyDomain);
		if (one != null && one.getUUID("idea_id") != null
				&& one.getString("company_domain") != null) {
			ideaDomainUpdate(ideaId, companyDomain, one);
		} else {
			String cql = "insert into analytics.idea_domain_stats"
					+ "(company_domain, idea_id, compound_effect, first_buzzed_date, "
					+ "mass, momentum, time_momentum_last_updated) values ('"
					+ companyDomain + "', " + ideaId + ", 0.0,"
					+ new Date().getTime() + ", 1.0, 0.0,"
					+ new Date().getTime() + ")";
			session.execute(cql);
		}
		return ideaId;
	}

	@Override
	public UUID updateIdeaLocationMomentum(UUID ideaId,
			NetworkRequest locationDetails) {
		Row one = selectIdeaLocation(ideaId, locationDetails);
		if (one != null && one.getUUID("idea_id") != null
				&& one.getString("country") != null
				&& one.getString("admin_level_1") != null
				&& one.getString("admin_level_2") != null
				&& one.getString("admin_level_3") != null)
			ideaLocationUpdate(ideaId, locationDetails.getCountry(), one);
		else {
			String cql = "insert into analytics.idea_location_stats "
					+ "(idea_id, country, admin_level_1, admin_level_2, "
					+ "admin_level_3, mass, compound_effect, count_buzzes, "
					+ "female_age_compound_effects, female_age_momentums,"
					+ "female_time_age_momentums_last_updated, first_buzzed_date,  "
					+ "male_age_compound_effects, male_age_momentums, "
					+ "male_time_age_momentums_last_updated, momentum, time_momentum_last_updated) values ("
					+ ideaId + ", '" + locationDetails.getCountry() + "', '"
					+ locationDetails.getAdmin1() + "', '"
					+ locationDetails.getAdmin2() + "', '"
					+ locationDetails.getAdmin3() + "',1.0,0.0, 0,{0}, {0},"
					+ new Date().getTime() + ", " + new Date().getTime()
					+ "{0}, {0}, " + new Date().getTime() + ", 0.0, "
					+ new Date().getTime() + ")";
			session.execute(cql);
		}
		return ideaId;
	}

	@Override
	public void ideaDomainUpdate(UUID ideaId, String companyDomain, Row one) {
		Double dateDiff = MomentumProperties.dateDiff(
				one.getDate("time_momentum_last_updated"), new Date());
		MomentumProperties momentum = new MomentumProperties(
				one.getDouble("mass"), one.getDouble("momentum"),
				one.getDouble("compound_effect"), dateDiff);
		MomentumProperties updatedValue = momentum
				.updateIdeaDomainMomentum(dateDiff);
		String cql = "update analytics.idea_domain_stats SET momentum = "
				+ updatedValue.getMomentum() + ", compound_effect = "
				+ updatedValue.getCompoundEffect()
				+ ",time_momentum_last_updated =" + new Date().getTime()
				+ " where idea_id = " + ideaId + " AND company_domain = '"
				+ companyDomain + "'";
		session.execute(cql);
	}

	@Override
	public Row selectIdeaDomainMomentum(UUID ideaId, String companyDomain) {
		Select select = QueryBuilder.select().from("idea_domain_stats");
		select.where(QueryBuilder.eq("idea_id", ideaId)).and(
				QueryBuilder.eq("company_domain", companyDomain));
		return session.execute(select).one();
	}

	@Override
	public Row selectIdeaLocation(UUID ideaId, NetworkRequest locationDetails) {
		Select select = QueryBuilder.select().from("idea_location_stats");
		select.where(QueryBuilder.eq("idea_id", ideaId))
				.and(QueryBuilder.eq("country", locationDetails.getCountry()))
				.and(QueryBuilder.eq("admin_level_1",
						locationDetails.getAdmin1()))
				.and(QueryBuilder.eq("admin_level_1",
						locationDetails.getAdmin2()))
				.and(QueryBuilder.eq("admin_level_1",
						locationDetails.getAdmin3()));
		System.out.println("QUERY "+select.getQueryString()+"\n"+select.toString());
		return session.execute(select).one();
	}
	@Override
	public void ideaLocationUpdate(UUID ideaId, String country, Row one) {
		Double dateDiff = MomentumProperties.dateDiff(
				one.getDate("time_momentum_last_updated"), new Date());
		MomentumProperties momentum = new MomentumProperties(
				one.getDouble("mass"), one.getDouble("momentum"),
				one.getDouble("compound_effect"), dateDiff);
		MomentumProperties updateValue = momentum
				.updateIdeaLocationMomentum(dateDiff);
		String cql = "update analytics.idea_location_stats SET compound_effect = "
				+ updateValue.getCompoundEffect()
				+ ", mass = "
				+ updateValue.getMass()
				+ ", momentum = "
				+ updateValue.getMomentum()
				+ ", time_momentum_last_updated = "
				+ new Date();
		session.execute(cql);
	}

	@Override
	public Map<Idea, Double> findBySearch(String search) {
		JSONObject json = new JSONObject();
		json.put("q", "idea_keywords:(" + search + "~0.8) description:("
				+ search + "~0.8) title:(" + search
				+ "~0.8) primary_category:(" + search
				+ "~0.8) secondary_category:(" + search
				+ "~0.8) time_to_organize:(" + search
				+ "~0.8) facility_generic:(" + search
				+ "~0.8) facility_specific:(" + search
				+ "~0.8) tag_environment:(" + search + "~0.8) tag_experience:("
				+ search + "~0.8) tag_people:(" + search + "~0.8)");
		Select select = QueryBuilder.select().from("idea");
		select.where(QueryBuilder.eq("solr_query", json.toString()));

		Map<Idea, Double> ideasMomentum = new HashMap<Idea, Double>();
		List<Idea> ideas = cassandraOperations.select(select, Idea.class);
		for (Idea i : ideas) {
			ideasMomentum.put(i, 1.0);
		}
		return ideasMomentum;
	}

	@Override
	public List<Idea> findRandomIdeas(int ideaSize) {
		JSONObject json = new JSONObject();
		json.put("q", "*:*");
		json.put("sort","random_"+(new Random().nextInt(378)+1)+" desc");
		Select select = QueryBuilder.select().all().from("idea")
				.limit(ideaSize);
		select.where(QueryBuilder.eq("solr_query", json.toString()));
		List<Idea> ideas = cassandraOperations.select(select, Idea.class);
		return ideas;
	}
}
