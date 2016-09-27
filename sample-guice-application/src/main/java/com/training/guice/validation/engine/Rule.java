/**
 * 
 */
package com.training.guice.validation.engine;

/**
 * Functional interface to be implemented by any business rule
 * 
 * @author Mina
 *
 */
@FunctionalInterface
public interface Rule<Subject extends ValidatableObject> {

	/**
	 * Executing the business logic for the rule implementation on a specific
	 * subject validatable object.
	 */
	void execute(Subject subject);
}
