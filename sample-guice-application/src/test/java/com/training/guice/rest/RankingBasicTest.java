package com.training.guice.rest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import com.google.inject.Inject;
import com.training.guice.AbstractIntegrationTest;
import com.training.guice.TestClientService;
import com.training.guice.jpa.domain.Enums;
import com.training.guice.jpa.domain.Exercise;
import com.training.guice.validation.exception.BusinessEntityNotFoundException;

public class RankingBasicTest extends AbstractIntegrationTest {

	@Inject
	private TestClientService testClientService;

	@Test
	public void testRangingList() {
		final long userId1 = 20L;
		final long userId2 = 21L;

		final Exercise exercise1ToInsert = new Exercise();
		exercise1ToInsert.setDescription("Coding Task");
		exercise1ToInsert.setDuration(14400);
		exercise1ToInsert.setDistance(0);
		exercise1ToInsert.setCalories(500);
		exercise1ToInsert.setStartTime(Calendar.getInstance().getTime());
		exercise1ToInsert.setType(Enums.ExerciseType.OTHER);
		exercise1ToInsert.setUserId(userId1);

		final Exercise persistedExercise1 = testClientService.createExercise(exercise1ToInsert);
		assertNotNull(persistedExercise1);
		assertNotNull(persistedExercise1.getId());

		final Exercise exercise2ToInsert = new Exercise();
		exercise2ToInsert.setDescription("Onsite Interview");
		exercise2ToInsert.setDuration(7200);
		exercise2ToInsert.setDistance(1500);
		exercise2ToInsert.setCalories(700);
		exercise2ToInsert.setStartTime(Calendar.getInstance().getTime());
		exercise2ToInsert.setType(Enums.ExerciseType.OTHER);
		exercise2ToInsert.setUserId(userId2);

		final Exercise persistedExercise2 = testClientService.createExercise(exercise2ToInsert);
		assertNotNull(persistedExercise2);
		assertNotNull(persistedExercise2.getId());

		final List<Long> ranking = testClientService.getRanking(Arrays.asList(userId1, userId2));
		assertNotNull(ranking);
		assertThat(ranking.size(), is(2));
		assertThat(ranking.get(0), is(userId2));
		assertThat(ranking.get(1), is(userId1));
	}

	@Test
	public void testMoreThanOneExerciseDifferentTypesPerUser() {
		final long userId1 = 22;
		final long userId2 = 23;

		final Exercise exercise1User1ToInsert = new Exercise();
		exercise1User1ToInsert.setDescription("Coding Task1");
		exercise1User1ToInsert.setDuration(10800);
		exercise1User1ToInsert.setDistance(0);
		exercise1User1ToInsert.setCalories(1000);
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		exercise1User1ToInsert.setStartTime(yesterday.getTime());
		exercise1User1ToInsert.setType(Enums.ExerciseType.CIRCUIT_TRAINING);
		exercise1User1ToInsert.setUserId(userId1);

		final Exercise persistedExercise1User1 = testClientService.createExercise(exercise1User1ToInsert);
		assertNotNull(persistedExercise1User1);
		assertNotNull(persistedExercise1User1.getId());

		final Exercise exercise2User1ToInsert = new Exercise();
		exercise2User1ToInsert.setDescription("Coding Task2");
		exercise2User1ToInsert.setDuration(14400);
		exercise2User1ToInsert.setDistance(0);
		exercise2User1ToInsert.setCalories(1000);
		exercise2User1ToInsert.setStartTime(Calendar.getInstance().getTime());
		exercise2User1ToInsert.setType(Enums.ExerciseType.SPORTS);
		exercise2User1ToInsert.setUserId(userId1);

		final Exercise persistedExercise2User1 = testClientService.createExercise(exercise2User1ToInsert);
		assertNotNull(persistedExercise2User1);
		assertNotNull(persistedExercise2User1.getId());

		final Exercise exercise1User2ToInsert = new Exercise();
		exercise1User2ToInsert.setDescription("Coding Task3");
		exercise1User2ToInsert.setDuration(108000);
		exercise1User2ToInsert.setDistance(0);
		exercise1User2ToInsert.setCalories(2000);
		Calendar threeWeeksAgo = Calendar.getInstance();
		threeWeeksAgo.add(Calendar.WEEK_OF_MONTH, -3);
		exercise1User2ToInsert.setStartTime(threeWeeksAgo.getTime());
		exercise1User2ToInsert.setType(Enums.ExerciseType.CIRCUIT_TRAINING);
		exercise1User2ToInsert.setUserId(userId2);

		final Exercise persistedExercise1User2 = testClientService.createExercise(exercise1User2ToInsert);
		assertNotNull(persistedExercise1User2);
		assertNotNull(persistedExercise1User2.getId());

		final Exercise exercise2User2ToInsert = new Exercise();
		exercise2User2ToInsert.setDescription("Coding Task4");
		exercise2User2ToInsert.setDuration(14400);
		exercise2User2ToInsert.setDistance(0);
		exercise2User2ToInsert.setCalories(1000);
		exercise2User2ToInsert.setStartTime(Calendar.getInstance().getTime());
		exercise2User2ToInsert.setType(Enums.ExerciseType.SPORTS);
		exercise2User2ToInsert.setUserId(userId2);

		final Exercise persistedExercise2User2 = testClientService.createExercise(exercise2User2ToInsert);
		assertNotNull(persistedExercise2User2);
		assertNotNull(persistedExercise2User2.getId());

		final List<Long> ranking = testClientService.getRanking(Arrays.asList(userId1, userId2));
		assertNotNull(ranking);
		assertThat(ranking.size(), is(2));
		assertThat(ranking.get(0), is(userId2));
		assertThat(ranking.get(1), is(userId1));
	}

	@Test
	public void testMoreThanOneExerciseSameTypesPerUser() {
		final long userId1 = 24;
		final long userId2 = 25;

		final Exercise exercise1User1ToInsert = new Exercise();
		exercise1User1ToInsert.setDescription("Coding Task1");
		exercise1User1ToInsert.setDuration(10800);
		exercise1User1ToInsert.setDistance(0);
		exercise1User1ToInsert.setCalories(1000);
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		exercise1User1ToInsert.setStartTime(yesterday.getTime());
		exercise1User1ToInsert.setType(Enums.ExerciseType.CIRCUIT_TRAINING);
		exercise1User1ToInsert.setUserId(userId1);

		final Exercise persistedExercise1User1 = testClientService.createExercise(exercise1User1ToInsert);
		assertNotNull(persistedExercise1User1);
		assertNotNull(persistedExercise1User1.getId());

		final Exercise exercise2User1ToInsert = new Exercise();
		exercise2User1ToInsert.setDescription("Coding Task2");
		exercise2User1ToInsert.setDuration(14400);
		exercise2User1ToInsert.setDistance(0);
		exercise2User1ToInsert.setCalories(1000);
		exercise2User1ToInsert.setStartTime(Calendar.getInstance().getTime());
		exercise2User1ToInsert.setType(Enums.ExerciseType.SPORTS);
		exercise2User1ToInsert.setUserId(userId1);

		final Exercise persistedExercise2User1 = testClientService.createExercise(exercise2User1ToInsert);
		assertNotNull(persistedExercise2User1);
		assertNotNull(persistedExercise2User1.getId());

		final Exercise exercise1User2ToInsert = new Exercise();
		exercise1User2ToInsert.setDescription("Coding Task3");
		exercise1User2ToInsert.setDuration(10800);
		exercise1User2ToInsert.setDistance(0);
		exercise1User2ToInsert.setCalories(1000);
		Calendar threeWeeksAgo = Calendar.getInstance();
		threeWeeksAgo.add(Calendar.WEEK_OF_MONTH, -3);
		exercise1User2ToInsert.setStartTime(threeWeeksAgo.getTime());
		exercise1User2ToInsert.setType(Enums.ExerciseType.SPORTS);
		exercise1User2ToInsert.setUserId(userId2);

		final Exercise persistedExercise1User2 = testClientService.createExercise(exercise1User2ToInsert);
		assertNotNull(persistedExercise1User2);
		assertNotNull(persistedExercise1User2.getId());

		final Exercise exercise2User2ToInsert = new Exercise();
		exercise2User2ToInsert.setDescription("Coding Task4");
		exercise2User2ToInsert.setDuration(14400);
		exercise2User2ToInsert.setDistance(0);
		exercise2User2ToInsert.setCalories(1000);
		exercise2User2ToInsert.setStartTime(Calendar.getInstance().getTime());
		exercise2User2ToInsert.setType(Enums.ExerciseType.SPORTS);
		exercise2User2ToInsert.setUserId(userId2);

		final Exercise persistedExercise2User2 = testClientService.createExercise(exercise2User2ToInsert);
		assertNotNull(persistedExercise2User2);
		assertNotNull(persistedExercise2User2.getId());

		final List<Long> ranking = testClientService.getRanking(Arrays.asList(userId1, userId2));
		assertNotNull(ranking);
		assertThat(ranking.size(), is(2));
		assertThat(ranking.get(0), is(userId1));
		assertThat(ranking.get(1), is(userId2));
	}

	@Test
	public void testByUserWithNoExercises() {
		final long userId1 = 26L;
		final long userId2 = 27L;

		final Exercise exercise1ToInsert = new Exercise();
		exercise1ToInsert.setDescription("Coding Task");
		exercise1ToInsert.setDuration(14400);
		exercise1ToInsert.setDistance(0);
		exercise1ToInsert.setCalories(500);
		exercise1ToInsert.setStartTime(Calendar.getInstance().getTime());
		exercise1ToInsert.setType(Enums.ExerciseType.OTHER);
		exercise1ToInsert.setUserId(userId1);

		final Exercise persistedExercise1 = testClientService.createExercise(exercise1ToInsert);
		assertNotNull(persistedExercise1);
		assertNotNull(persistedExercise1.getId());

		final List<Long> ranking = testClientService.getRanking(Arrays.asList(userId1, userId2));
		assertNotNull(ranking);
		assertThat(ranking.size(), is(2));
		assertThat(ranking.get(0), is(userId1));
		assertThat(ranking.get(1), is(userId2));
	}

	@Test(expected = BusinessEntityNotFoundException.class)
	public void testFailAllUsersWithNoExercises() {
		final long userId1 = 28;
		final long userId2 = 29L;
		testClientService.getRanking(Arrays.asList(userId1, userId2));
	}

	@Test
	public void testSameExerciseTypeMultiplicationFactor() {
		final long userId1 = 30L;
		final long userId2 = 31L;
		final long userId3 = 32L;

		// Points achieved should be same for users 1 & 2!
		createExercisesForUser(userId1, 15);
		createExercisesForUser(userId2, 11);
		createExercisesForUser(userId3, 8);

		final List<Long> ranking = testClientService.getRanking(Arrays.asList(userId1, userId2, userId3));
		assertNotNull(ranking);
		assertThat(ranking.size(), is(3));
		assertThat(ranking.get(2), is(userId3));
	}

	/**
	 * @param userId
	 * @param i
	 */
	private void createExercisesForUser(long userId, int noOfExercises) {
		Exercise exerciseToInsert;
		Exercise persistedExercise;

		for (int i = 0; i < noOfExercises; i++) {
			exerciseToInsert = new Exercise();
			exerciseToInsert.setDescription("Coding Task" + i);
			exerciseToInsert.setDuration(14400);
			exerciseToInsert.setDistance(0);
			exerciseToInsert.setCalories(500);
			Calendar startDate = Calendar.getInstance();
			startDate.add(Calendar.WEEK_OF_MONTH, -3);
			startDate.add(Calendar.DAY_OF_MONTH, i);
			exerciseToInsert.setStartTime(startDate.getTime());
			exerciseToInsert.setType(Enums.ExerciseType.FITNESS_COURSE);
			exerciseToInsert.setUserId(userId);

			persistedExercise = testClientService.createExercise(exerciseToInsert);
			assertNotNull(persistedExercise);
			assertNotNull(persistedExercise.getId());
		}
	}

	@Test
	public void outOfEvaluationRangeExercise() {
		final long userId1 = 33L;
		final long userId2 = 34L;

		final Exercise exercise1ToInsert = new Exercise();
		exercise1ToInsert.setDescription("Coding Task");
		exercise1ToInsert.setDuration(14400);
		exercise1ToInsert.setDistance(0);
		exercise1ToInsert.setCalories(500);
		exercise1ToInsert.setStartTime(Calendar.getInstance().getTime());
		exercise1ToInsert.setType(Enums.ExerciseType.OTHER);
		exercise1ToInsert.setUserId(userId1);

		final Exercise persistedExercise1 = testClientService.createExercise(exercise1ToInsert);
		assertNotNull(persistedExercise1);
		assertNotNull(persistedExercise1.getId());

		final Exercise exercise2ToInsert = new Exercise();
		exercise2ToInsert.setDescription("Onsite Interview");
		exercise2ToInsert.setDuration(7200);
		exercise2ToInsert.setDistance(1500);
		exercise2ToInsert.setCalories(700);
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.WEEK_OF_MONTH, -4);
		startDate.add(Calendar.DAY_OF_MONTH, -1);
		exercise2ToInsert.setStartTime(startDate.getTime());
		exercise2ToInsert.setType(Enums.ExerciseType.OTHER);
		exercise2ToInsert.setUserId(userId2);

		final Exercise persistedExercise2 = testClientService.createExercise(exercise2ToInsert);
		assertNotNull(persistedExercise2);
		assertNotNull(persistedExercise2.getId());

		final List<Long> ranking = testClientService.getRanking(Arrays.asList(userId1, userId2));
		assertNotNull(ranking);
		assertThat(ranking.size(), is(2));
		assertThat(ranking.get(0), is(userId1));
		assertThat(ranking.get(1), is(userId2));
	}
}
