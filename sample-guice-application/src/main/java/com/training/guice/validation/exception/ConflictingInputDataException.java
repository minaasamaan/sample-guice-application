/**
 * 
 */
package com.training.guice.validation.exception;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

/**
 * @author Mina
 *
 */
public class ConflictingInputDataException extends ClientErrorException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 * @param status
	 */
	public ConflictingInputDataException(String message) {
		super(message, Response.Status.CONFLICT);
	}

}
