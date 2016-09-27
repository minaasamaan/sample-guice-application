/**
 * 
 */
package com.training.guice.validation.groups;

import com.training.guice.validation.engine.ValidatableOperation;

/**
 * @author Mina
 *
 */
public interface CRUDGroup extends ValidationGroup {
	/**
	 * @author Mina
	 *
	 */
	public interface Create extends ValidatableOperation {

	}

	/**
	 * @author Mina
	 *
	 */
	public interface Update extends ValidatableOperation {

	}
}
