/**
 * 
 */
package com.training.guice.validation.engine;

/**
 * @author Mina
 *
 */
public interface RulesCollection<Subject extends ValidatableObject> {

	/**
	 * @param object
	 * @param operation
	 */
	void applyRules(Subject object, Class<? extends ValidatableOperation> operation);
}
