package com.training.guice.jpa.dao;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.training.guice.helper.JpqlHelper;
import com.training.guice.jpa.domain.Exercise;
import com.training.guice.jpa.domain.Enums.ExerciseType;
import com.training.guice.validation.exception.BusinessEntityNotFoundException;

@Transactional
public class ExerciseDaoImpl extends AbstractBaseDao<Exercise>implements ExerciseDao {

	@Inject
	ExerciseDaoImpl(final Provider<EntityManager> entityManagerProvider) {
		super(entityManagerProvider, Exercise.class);
	}

	@Nonnull
	@Override
	public List<Exercise> findByDescription(@Nullable String description) {
		if (description == null) {
			return Collections.emptyList();
		}

		description = description.toLowerCase();

		try {
			return getEntityManager().createQuery("SELECT e FROM Exercise e WHERE LOWER(e.description) = :description")
					.setParameter("description", description).getResultList();
		} catch (NoResultException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public Long findByUserIdAndStartDateExceptExerciseId(long userId, Date startDate, Long exerciseId) {
		return (Long) getEntityManager()
				.createQuery(
						"SELECT COUNT(id) FROM Exercise e WHERE e.id <> :exerciseId AND e.userId = :userId AND DATEADD('ss' , e.duration , e.startTime ) >= :startDate ")
				.setParameter("userId", userId).setParameter("startDate", startDate)
				.setParameter("exerciseId", exerciseId).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Exercise> findByUserIdOptionalTypeAndDate(long userId, ExerciseType type, Date date) {
		StringBuilder queryBuilder = new StringBuilder("SELECT e FROM Exercise e WHERE userId = :userId");

		if (null != type) {
			queryBuilder.append(" AND type=:type");
		}

		if (null != date) {
			queryBuilder.append(" AND YEAR(e.startTime) =:year");
			queryBuilder.append(" AND MONTH(e.startTime) =:month");
			queryBuilder.append(" AND DAY(e.startTime) =:day");
		}

		Query query = getEntityManager().createQuery(queryBuilder.toString());
		query.setParameter("userId", userId);

		if (null != type) {
			query.setParameter("type", type);
		}

		if (null != date) {
			Calendar dateFilter = Calendar.getInstance();
			dateFilter.setTime(date);
			query.setParameter("year", dateFilter.get(Calendar.YEAR));
			query.setParameter("month", dateFilter.get(Calendar.MONTH) + 1);
			query.setParameter("day", dateFilter.get(Calendar.DAY_OF_MONTH));
		}

		List<Exercise> exercises = query.getResultList();

		if (exercises.isEmpty()) {
			throw new BusinessEntityNotFoundException("No records found matching the given criteria");
		}
		return exercises;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.training.guice.jpa.dao.ExerciseDao#
	 * findByUserIdListBetweenStartAndEndDates(java.util.List, java.util.Date,
	 * java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Exercise> findByUserIdListBetweenStartAndEndDates(List<Long> userIds, Date startDate, Date endDate) {
		StringBuilder queryBuilder = new StringBuilder(
				// "SELECT * FROM Exercise e WHERE DATEADD('ss' , e.duration ,
				// e.startTime) BETWEEN( :startDate , :endDate ) AND ");
				"SELECT e FROM Exercise e WHERE DATEADD('ss' , e.duration , e.startTime) >= :startDate AND DATEADD('ss' , e.duration , e.startTime) <= :endDate  AND ");

		Map<String, List<Long>> idParams = JpqlHelper.handleInClauseLimitation(userIds, queryBuilder, "userId");
		queryBuilder.append(" order by e.startTime desc");
		Query query = getEntityManager().createQuery(queryBuilder.toString());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		for (Entry<String, List<Long>> paramEntry : idParams.entrySet()) {
			query.setParameter(paramEntry.getKey(), paramEntry.getValue());
		}
		List<Exercise> exercises = query.getResultList();

		if (exercises.isEmpty()) {
			throw new BusinessEntityNotFoundException("No records found matching the given criteria");
		}
		return exercises;
	}

}