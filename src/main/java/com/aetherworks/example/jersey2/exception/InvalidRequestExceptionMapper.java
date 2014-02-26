package com.aetherworks.example.jersey2.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.aetherworks.example.jersey2.SetApplication;
import com.aetherworks.example.jersey2.api.SetResource;

/**
 * This class specifies a mapping between an internal {@link InvalidRequestException}, and the message returned to the
 * caller of the API when that exception is thrown back to the API interface in {@link SetResource}.
 * <p>
 * The mapping between {@link InvalidRequestException} and this class is specified in {@link SetApplication}.
 * 
 * @author Angus Macdonald (amacdonald@aetherworks.com)
 */
@Provider
public class InvalidRequestExceptionMapper implements ExceptionMapper<InvalidRequestException> {
	public static final String ERROR_MESSAGE = "This request was no good.";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response toResponse(final InvalidRequestException e) {
		return Response.status(Response.Status.BAD_REQUEST).entity(ERROR_MESSAGE).build();
	}

}
