/**
 * 
 */
package com.training.guice.validation.exception;

import javax.ws.rs.NotFoundException;

/**
 * @author Mina
 *
 */
public class BusinessEntityNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public BusinessEntityNotFoundException(String message) {
		super(message);
	}
}
