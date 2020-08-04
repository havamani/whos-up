package com.whosup.dao;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.datastax.driver.core.Row;
import com.whosup.model.Idea;
import com.whosup.model.request.NetworkRequest;

public interface IdeaDao {

	Idea findById(UUID ideaId);

	List<Idea> findAllIdeas();

	List<Idea> findAll(int ideaSize);

	Map<Idea, Double> getIdeaFromSource1(List<UUID> contactUserIds,
			String search);

	Map<Idea, Double> getIdeaFromSource2(String domain, String search);

	Map<Idea, Double> getIdeaFromSource3(String admin_level_1,
			String admin_level_2, String admin_level_3, String country,
			String search);

	Map<Idea, Double> getIdeaFromSource4(String country, String search);

	List<Idea> findByIds(List<UUID> ideaIds, String search);

	UUID updateStarredIdeaMomentum(UUID contactId, UUID ideaId);

	UUID updateIdeaDomainMomentum(UUID ideaId, String companyDomain);

	UUID updateIdeaLocationMomentum(UUID ideaId, NetworkRequest locationDeatils);

	void ideaDomainUpdate(UUID ideaId, String companyDomain, Row one);

	Row selectIdeaDomainMomentum(UUID ideaId, String companyDomain);

	void ideaLocationUpdate(UUID ideaId, String country, Row one);

	Row selectIdeaLocation(UUID ideaId, NetworkRequest locationDetails);

	Map<Idea, Double> findBySearch(String search);

	List<Idea> findRandomIdeas(int ideaSize);

}
