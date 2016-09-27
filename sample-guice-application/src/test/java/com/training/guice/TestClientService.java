package com.training.guice;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.training.guice.dto.exercise.ExerciseDto;
import com.training.guice.jpa.domain.Enums;
import com.training.guice.jpa.domain.Exercise;
import com.training.guice.rest.ExerciseService;

public class TestClientService {

	private static final Logger log = LoggerFactory.getLogger(TestClientService.class);

	private final ExerciseService exerciseService;
	private final ModelMapper modelMapper;

	@Inject
	public TestClientService(final ExerciseService exerciseService, final ModelMapper modelMapper) {
		this.exerciseService = exerciseService;
		this.modelMapper = modelMapper;
	}

	/**
	 * Get the exercise for a given exerciseId.
	 *
	 * @param exerciseId
	 *            id to search
	 * @return the exercise for the given exerciseId
	 * @throws RuntimeException
	 *             if there is an error please throw an appropriate exception
	 *             here
	 */
	@Nonnull
	public Exercise getExercise(@Nullable final Long exerciseId) {
		return exerciseService.getExerciseById(exerciseId);
	}

	/**
	 * Get the exercises with the given description.
	 *
	 * @param description
	 *            description to search
	 * @return the exercises for the given description
	 * @throws RuntimeException
	 *             if there is an error please throw an appropriate exception
	 *             here
	 */
	@Nonnull
	public List<Exercise> getExerciseByDescription(@Nullable final String description) {
		return exerciseService.getExerciseByDescription(description);
	}

	/**
	 * Persists a given exercise.
	 *
	 * @param exercise
	 *            to persist
	 * @return the persisted exercise with created id
	 * @throws RuntimeException
	 *             if there is an error please throw an appropriate exception
	 *             here
	 */
	@Nonnull
	public Exercise createExercise(@Nullable final Exercise exercise) {
		try {
			return modelMapper.map(exerciseService.createExercise(modelMapper.map(exercise, ExerciseDto.class)),
					Exercise.class);
		} catch (Throwable e) {
			handleThrowable(e);
		}
		return null;
	}

	/**
	 * Updates an existing exercise.
	 *
	 * @param exercise
	 *            to update
	 * @return the updated exercise
	 * @throws RuntimeException
	 *             if there is an error please throw an appropriate exception
	 *             here
	 */
	@Nonnull
	public Exercise updateExercise(@Nullable final Exercise exercise) {
		try {
			return modelMapper.map(exerciseService.updateExercise(modelMapper.map(exercise, ExerciseDto.class)),
					Exercise.class);
		} catch (Throwable e) {
			handleThrowable(e);
		}
		return null;
	}

	/**
	 * Deletes an exercise with the given exerciseId.
	 *
	 * @param exerciseId
	 *            id to delete
	 * @throws RuntimeException
	 *             if there is an error please throw an appropriate exception
	 *             here
	 */
	public void deleteExercise(@Nullable final Long exerciseId) {
		try {
			exerciseService.deleteExercise(exerciseId);
		} catch (Throwable e) {
			handleThrowable(e);
		}
	}

	/**
	 * Returns a list of exercises for a specific user and some filter items
	 * (type + date).
	 *
	 * @param userId
	 *            who did the exercise
	 * @param exerciseType
	 *            filter: type of the exercise
	 * @param date
	 *            filter: date ("yyyy-MM-dd") of the exercise
	 * @return a list of exercises
	 * @throws ParseException
	 * @throws RuntimeException
	 *             if there is an error please throw an appropriate exception
	 *             here
	 */
	@Nonnull
	public List<Exercise> getExercises(@Nullable final Long userId, @Nullable final Enums.ExerciseType exerciseType,
			@Nullable final String date) {
		try {
			Type listType = new TypeToken<List<Exercise>>() {
			}.getType();
			return modelMapper.map(exerciseService.getExercises(userId, exerciseType, date), listType);
		} catch (Throwable e) {
			handleThrowable(e);
		}
		return null;
	}

	/**
	 * Calculate the ranking for the given user ids. The Calculation based on
	 * the exercises the user has done. The first in the list is the user with
	 * the highest score.
	 *
	 * @param userIds
	 *            list of user ids
	 * @return the user list orders by there calculated exercise points
	 * @throws RuntimeException
	 *             if there is an error please throw an appropriate exception
	 *             here
	 */
	@Nonnull
	public List<Long> getRanking(@Nullable final List<Long> userIds) {
		try {
			return exerciseService.getRanking(userIds);

		} catch (Throwable e) {
			handleThrowable(e);
		}
		return userIds;
	}

	void handleThrowable(Throwable throwable) {
		if (throwable instanceof ConstraintViolationException) {
			StringBuilder message = new StringBuilder("Request validation failed with the following errors: ");

			((ConstraintViolationException) throwable).getConstraintViolations()
					.forEach(violation -> message.append(violation.getMessage()).append(" \n "));
			log.debug(message.toString());
		} else if (throwable instanceof IllegalArgumentException) {
			log.debug("Malformed REST request. " + throwable.getMessage(), throwable);
		} else if (throwable instanceof EntityNotFoundException || throwable instanceof NoResultException) {
			log.debug(Response.Status.NOT_FOUND.getStatusCode() + ": " + throwable.getMessage(), throwable);
		} else if (throwable instanceof WebApplicationException) {
			final WebApplicationException waex = (WebApplicationException) throwable;
			log.debug(waex.getResponse().getStatus() + ": " + waex.getMessage(), throwable);
		} else
			log.error("Unexpected exception in REST interface.", throwable);

		if (throwable instanceof RuntimeException) {
			throw (RuntimeException) throwable;
		} else {
			throw new RuntimeException(throwable.getMessage(), throwable);
		}
	}
}
