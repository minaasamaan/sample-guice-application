package com.training.guice.jpa.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.training.guice.jpa.domain.Exercise;
import com.training.guice.jpa.domain.Enums.ExerciseType;

public interface ExerciseDao extends BaseDao<Exercise> {

	/**
	 * Returns a list of exercises with the given description
	 *
	 * @param description
	 *            of the exercise
	 * @return filters list of exercise
	 */
	@Nonnull
	List<Exercise> findByDescription(@Nullable String description);

	/**
	 * @param userId
	 * @param startDate
	 * @param long1
	 * @return
	 */
	Long findByUserIdAndStartDateExceptExerciseId(long userId, Date startDate, Long exerciseId);

	/**
	 * @param description
	 * @return
	 */
	List<Exercise> findByUserIdOptionalTypeAndDate(long userId, ExerciseType type, Date date);

	List<Exercise> findByUserIdListBetweenStartAndEndDates(List<Long> userIds, Date startDate, Date endDate);

}
