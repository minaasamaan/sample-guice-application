/**
 * 
 */
package com.training.guice.validation.rules;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.training.guice.dto.exercise.ExerciseDto;
import com.training.guice.jpa.dao.ExerciseDao;
import com.training.guice.jpa.domain.Exercise;
import com.training.guice.validation.engine.Rule;
import com.training.guice.validation.engine.impl.AbstractRulesCollection;
import com.training.guice.validation.exception.ConflictingInputDataException;
import com.training.guice.validation.groups.CRUDGroup.Create;
import com.training.guice.validation.groups.CRUDGroup.Update;

/**
 * @author Mina
 *
 */
@Singleton
public class ExerciseRules extends AbstractRulesCollection<ExerciseDto> {

	@Inject
	private ExerciseDao exerciseDao;

	@Override
	protected void buildRuleSet() {
		// Create operation validations
		Set<Rule<ExerciseDto>> createRules = new HashSet<Rule<ExerciseDto>>();
		createRules.add(new NoOverlappingInTime());
		registerRuleSet(Create.class, createRules);

		// Update operation validations
		Set<Rule<ExerciseDto>> updateRules = new HashSet<Rule<ExerciseDto>>();
		updateRules.add(new NoOverlappingInTime());
		updateRules.add(new UserIdAndTypeCannotBeChanged());
		registerRuleSet(Update.class, updateRules);

	}

	/**
	 * - During exercise persisting it should be checked that there is no other
	 * exercise already present for the user id, in the period (start +
	 * duration) where the new exercise will take place. If this is the case
	 * return an HTTP status code `Conflict` with appropriate error message.
	 */
	class NoOverlappingInTime implements Rule<ExerciseDto> {

		/**
		 * {@inheritDoc}}
		 */
		@Override
		public void execute(ExerciseDto dto) {
			// Assumption: No cached/old exercises to be added via calling
			// channels, only new
			// exercises
			// which achieves the criteria of having their start date after the
			// end
			// dates of all past exercises
			Long overlaps = exerciseDao.findByUserIdAndStartDateExceptExerciseId(dto.getUserId(), dto.getStartTime(),
					dto.getId());
			if (overlaps > 0) {
				throw new ConflictingInputDataException(
						"Input exercise start date is (overlapping with/or behind in time of) existing exercises.");
			}
		}
	}

	/**
	 * - While updating an exercise, the user id and type shouldn't change.
	 * 
	 */
	class UserIdAndTypeCannotBeChanged implements Rule<ExerciseDto> {

		/**
		 * {@inheritDoc}}
		 */
		@Override
		public void execute(ExerciseDto exerciseDto) {
			Exercise exercise = exerciseDao.findById(exerciseDto.getId());
			if (exercise == null) {
				throw new EntityNotFoundException(
						"Entity to be updated with id:" + exerciseDto.getId() + " does not exist.");
			}
			if (!exercise.getUserId().equals(exerciseDto.getUserId())
					|| !exercise.getType().equals(exerciseDto.getType())) {
				throw new ConflictingInputDataException(
						"Can't change neither user id, nor type while updating exercise.");
			}
		}

	}

}
