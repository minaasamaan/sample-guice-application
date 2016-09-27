/**
 * 
 */
package com.training.guice.manager.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.training.guice.helper.CollectionUtil;
import com.training.guice.jpa.dao.ExerciseDao;
import com.training.guice.jpa.domain.Exercise;
import com.training.guice.manager.UserManager;
import com.training.guice.valueobject.UserVo;

/**
 * @author Mina
 *
 */
@Singleton
public class UserManagerImpl implements UserManager {

	/**
	 * - RUNNING - 2 - CYCLING - 2 - SWIMMING - 3 - ROWING - 2 - WALKING - 1 -
	 * CIRCUIT_TRAINING - 4 - STRENGTH_TRAINING - 3 - FITNESS_COURSE - 2 -
	 * SPORTS - 3 - OTHER - 1 *
	 */
	private enum ExerciseType {
		RUNNING("2"), CYCLING("2"), SWIMMING("3"), ROWING("2"), WALKING("1"), CIRCUIT_TRAINING("4"), STRENGTH_TRAINING(
				"3"), FITNESS_COURSE("2"), SPORTS("3"), OTHER("1");

		private String multiplication;

		private ExerciseType(String multiplication) {
			this.multiplication = multiplication;
		}

		/**
		 * @return the multiplication
		 */
		String getMultiplication() {
			return multiplication;
		}
	}

	@Inject
	private ExerciseDao exerciseDao;

	/*
	 * - the points are calculated as follows: - A user gets points for each
	 * exercise he has completed in the past 4 weeks. - A user gets one point
	 * per minute of the duration of the exercise plus the burnt kilo calories.
	 * - Each time a user performs the same type of exercise again it is worth
	 * 10% less (Make sure to look at the newest exercises first). Example: A
	 * user ran 4 times in the past 4 weeks (let's say once per week). This
	 * weeks run is worth 100%. The oldest run is worth only 70% of the
	 * calculated points for the exercise. - An exercise can't count for less
	 * than 0 points.
	 * 
	 */
	@Override
	public List<Long> getRanking(List<Long> userIds) {
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();

		prepareStartEndDates(startDate, endDate);

		List<Exercise> exercises = exerciseDao.findByUserIdListBetweenStartAndEndDates(userIds, startDate.getTime(),
				endDate.getTime());

		BigInteger calculatedPoints;
		int exerciseCompletedMinutes;
		Map<Long, UserVo> userVoMap = new HashMap<>();

		for (Exercise exercise : exercises) {
			exerciseCompletedMinutes = exercise.getDuration() / 60; // Only
																	// completed
																	// minutes
																	// count as
																	// per
																	// description
			calculatedPoints = new BigInteger(ExerciseType.valueOf(exercise.getType().name()).getMultiplication());

			// total points= exercise type multiply factor* (kcal+ duration in
			// minutes)

			calculatedPoints = calculatedPoints.multiply(new BigInteger(exercise.getCalories().toString())
					.add(new BigInteger(String.valueOf(exerciseCompletedMinutes))));

			if (userVoMap.containsKey(exercise.getUserId())) {
				userVoMap.get(exercise.getUserId()).updatePoints(calculatedPoints, exercise.getType());
			} else {
				userVoMap.put(exercise.getUserId(),
						new UserVo(exercise.getUserId(), calculatedPoints, exercise.getType()));
			}
		}
		Set<Long> rankedIdSet = CollectionUtil.sortByValueReverseOrder(userVoMap).keySet();

		// Check if user ids left behind (i.e. has no exercises).
		if (rankedIdSet.size() != userIds.size()) {
			Set<Long> usersHasNoExercises = new HashSet<>(userIds);
			usersHasNoExercises.removeAll(rankedIdSet);
			List<Long> allIdList = new ArrayList<>(rankedIdSet);
			// Append to end of the list
			allIdList.addAll(usersHasNoExercises);
			return allIdList;
		}
		return new ArrayList<>(rankedIdSet);
	}

	/**
	 * @param startDate
	 * @param endDate
	 */
	private void prepareStartEndDates(Calendar startDate, Calendar endDate) {
		startDate.add(Calendar.WEEK_OF_MONTH, -4);
		startDate.set(Calendar.HOUR, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MILLISECOND, 0);

		endDate.set(Calendar.HOUR, 23);
		endDate.set(Calendar.MINUTE, 59);
		endDate.set(Calendar.SECOND, 59);
		endDate.set(Calendar.MILLISECOND, 999);
	}
}
