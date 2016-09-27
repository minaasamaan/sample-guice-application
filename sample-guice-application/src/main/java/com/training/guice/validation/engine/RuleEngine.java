/**
 * 
 */
package com.training.guice.validation.engine;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.training.guice.dto.exercise.ExerciseDto;
import com.training.guice.validation.rules.ExerciseRules;

/**
 * @author Mina
 *
 */

@Singleton
public final class RuleEngine {

	private static final Map<Class<? extends ValidatableObject>, RulesCollection<? extends ValidatableObject>> rules = new HashMap<Class<? extends ValidatableObject>, RulesCollection<? extends ValidatableObject>>();

	private ExerciseRules exerciseRules;

	@Inject
	public RuleEngine(ExerciseRules exerciseRules) {
		this.exerciseRules = exerciseRules;
		buildAll();
	}

	/**
	 * 
	 */
	private void buildAll() {
		rules.put(ExerciseDto.class, exerciseRules);
	}

	public <Subject extends ValidatableObject, Operation extends ValidatableOperation> void validate(Subject object,
			Class<Operation> operation) {
		@SuppressWarnings("unchecked")
		RulesCollection<Subject> rulesCollection = (RulesCollection<Subject>) rules.get(object.getClass());
		rulesCollection.applyRules(object, operation);
	}

}
