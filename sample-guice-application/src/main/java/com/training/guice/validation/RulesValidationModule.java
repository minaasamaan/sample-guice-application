/**
 * 
 */
package com.training.guice.validation;

import com.google.inject.AbstractModule;
import com.training.guice.validation.engine.RuleEngine;
import com.training.guice.validation.rules.ExerciseRules;

/**
 * @author Mina
 *
 */
public class RulesValidationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(RuleEngine.class);
		bind(ExerciseRules.class);
		// MapBinder<? extends TypeLiteral<Class<? extends ValidatableObject>>,
		// ? extends TypeLiteral<RulesCollection<? extends ValidatableObject>>>
		// mapbinder = MapBinder
		// .newMapBinder(binder(), new TypeLiteral<Class<? extends
		// ValidatableObject>>() {
		// }.getClass(), new TypeLiteral<RulesCollection<? extends
		// ValidatableObject>>() {
		// }.getClass());
		//
		// mapbinder.addBinding(new TypeLiteral<Class<ExerciseDto>>() {
		// }).to(ExerciseRules.class);
	}

}
