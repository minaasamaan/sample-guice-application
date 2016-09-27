package com.training.guice.rest;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.NotFoundException;

import org.apache.bval.guice.Validate;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.training.guice.dto.exercise.ExerciseDto;
import com.training.guice.jpa.dao.ExerciseDao;
import com.training.guice.jpa.domain.Exercise;
import com.training.guice.jpa.domain.Enums.ExerciseType;
import com.training.guice.manager.UserManager;
import com.training.guice.validation.engine.RuleEngine;
import com.training.guice.validation.groups.CRUDGroup.Create;
import com.training.guice.validation.groups.CRUDGroup.Update;

@Singleton
public class ExerciseServiceImpl implements ExerciseService {

	private static final Logger log = LoggerFactory.getLogger(ExerciseServiceImpl.class);

	private final ExerciseDao exerciseDao;

	private final ModelMapper modelMapper;

	private final RuleEngine ruleEngine;

	private final UserManager userManager;

	@Inject
	ExerciseServiceImpl(final ExerciseDao exerciseDao, final ModelMapper mapper, RuleEngine ruleEngine,
			UserManager userManager) {
		this.exerciseDao = exerciseDao;
		this.modelMapper = mapper;
		this.ruleEngine = ruleEngine;
		this.userManager = userManager;
	}

	@Nonnull
	@Override
	public Exercise getExerciseById(@Nonnull final Long exerciseId) {
		log.debug("Get exercise by id.");

		final Exercise exercise = exerciseDao.findById(exerciseId);
		if (exercise == null) {
			throw new NotFoundException("Exercise with id = " + exerciseId + " could not be found.");
		}

		return exercise;
	}

	@Nonnull
	@Override
	public List<Exercise> getExerciseByDescription(@Nonnull final String description) {
		log.debug("Get exercise by description.");

		return exerciseDao.findByDescription(description);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	@Validate(groups = { Create.class })
	public ExerciseDto createExercise(@Valid ExerciseDto exerciseDto) {
		ruleEngine.validate(exerciseDto, Create.class);
		Exercise exercise = exerciseDao.create(modelMapper.map(exerciseDto, Exercise.class));
		return modelMapper.map(exercise, ExerciseDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Validate(groups = { Update.class })
	public ExerciseDto updateExercise(ExerciseDto exerciseDto) {
		ruleEngine.validate(exerciseDto, Update.class);
		Exercise exercise = exerciseDao.update(modelMapper.map(exerciseDto, Exercise.class));
		return modelMapper.map(exercise, ExerciseDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Validate
	public void deleteExercise(@NotNull Long exerciseId) {
		exerciseDao.deleteById(exerciseId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Validate
	public List<ExerciseDto> getExercises(@NotNull Long userId, ExerciseType type, String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFilter = null;
		try {
			dateFilter = df.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Bad date format was passed to the service.");
		}
		List<Exercise> exercises = exerciseDao.findByUserIdOptionalTypeAndDate(userId, type, dateFilter);
		Type listType = new TypeToken<List<ExerciseDto>>() {
		}.getType();
		return modelMapper.map(exercises, listType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Validate
	public List<Long> getRanking(@NotNull List<Long> userIds) {
		return userManager.getRanking(userIds);
	}
}
