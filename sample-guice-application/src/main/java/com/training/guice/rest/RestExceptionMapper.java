package com.training.guice.rest;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.guice.dto.ErrorDto;

/**
 * Maps Java exceptions to HTTP error responses.
 *
 * https://jersey.java.net/documentation/latest/representations.html#d0e6665
 */
@Provider
public class RestExceptionMapper implements ExceptionMapper<Exception> {

	private static final Logger log = LoggerFactory.getLogger(RestExceptionMapper.class);

	private final ObjectMapper objectMapper;

	public RestExceptionMapper() {
		objectMapper = ObjectMapperProvider.newObjectMapper();
	}

	@Override
	public Response toResponse(final Exception ex) {
		if (ex instanceof ConstraintViolationException) {
			StringBuilder message = new StringBuilder("Request validation failed with the following errors: ");

			((ConstraintViolationException) ex).getConstraintViolations()
					.forEach(violation -> message.append(violation.getMessage()).append(" \n "));
			log.debug(message.toString());
			return errorResponse(Response.Status.BAD_REQUEST.getStatusCode(), message.toString());
		}

		if (ex instanceof IllegalArgumentException) {
			log.debug(ex.getMessage());
			return errorResponse(Response.Status.BAD_REQUEST.getStatusCode(),
					"Malformed REST request. " + ex.getMessage());
		}

		if (ex instanceof EntityNotFoundException || ex instanceof NoResultException) {
			log.debug(ex.getMessage());
			return errorResponse(Response.Status.NOT_FOUND.getStatusCode(), ex.getMessage());
		}

		if (ex instanceof WebApplicationException) {
			log.debug(ex.getMessage());
			final WebApplicationException waex = (WebApplicationException) ex;
			return errorResponse(waex.getResponse().getStatus(), waex.getMessage());
		}

		log.error("Unexpected exception in REST interface.", ex);
		return errorResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
				"Unexpected error in the REST interface.");
	}

	/**
	 * Build a JSON error response based on a HTTP status code.
	 *
	 * @param statusCode
	 *            HTTP status code.
	 * @param errorValue
	 *            optional error message.
	 * @return a Response object.
	 */
	private Response errorResponse(final int statusCode, final String errorValue) {
		log.debug("Error response: " + statusCode + ", " + errorValue);

		final ErrorDto errorDto = new ErrorDto(statusCode, errorValue);

		return Response.status(statusCode).entity(toJson(errorDto)).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	private String toJson(final ErrorDto errorDto) {
		String errorJson = null;
		try {
			errorJson = objectMapper.writeValueAsString(errorDto);
		} catch (final Exception e) {
			log.error("Could not create error JSON.", e);
		}
		return errorJson;
	}
}
