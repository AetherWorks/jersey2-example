/*
 * Copyright 2014, AetherWorks LLC.
 */

package com.aetherworks.example.jersey2.api;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.mockito.Mockito;

import com.aetherworks.example.jersey2.SetApplication;
import com.aetherworks.example.jersey2.SetCallHandler;
import com.aetherworks.example.jersey2.exception.InvalidRequestException;

/**
 * End-to-end test class, which makes API calls to {@link SetResource} and checks that the returned values are as
 * expected.
 * <p>
 * If this were a real project it would make more sense to test the functionality of the {@link SetCallHandler} class
 * directly.
 * <p>
 * All Jersey test classes must extends {@link JerseyTest}, and should override the configure method to specify how to
 * set up the {@link Application}. This tells the container how to set up our servlet.
 * 
 * @author Angus Macdonald (amacdonald@aetherworks.com)
 */
public class SetApiTests extends JerseyTest {

	/**
	 * Used to specify the format expected for the return type of the add call.
	 */
	private final GenericType<Boolean> BOOLEAN_RETURN_TYPE = new GenericType<Boolean>() {
	};

	/**
	 * The handler used in the {@link SetResource}. We are going to use {@link Mockito} to mock this handler for some
	 * tests, to just check the API call itself.
	 */
	@Override
	protected Application configure() {
		return new SetApplication();
	}

	/**
	 * Makes multiple calls to {@link SetResource#addSingle(javax.ws.rs.core.UriInfo, String)} and checks that the
	 * return type is correct.
	 */
	@Test
	public void repeatedAddCalls() throws InvalidRequestException {
		checkAddCallResponse(performAddCall("MyTest1"), true);
		checkAddCallResponse(performAddCall("MyTest1"), false);
		checkAddCallResponse(performAddCall("MyTest2"), true);
		checkAddCallResponse(performAddCall("MyTest3"), true);
		checkAddCallResponse(performAddCall("MyTest2"), false);
	}

	/**
	 * Manage response. Check that the status code is correct (so we know the call worked), and then check that the
	 * value returned is as expected.
	 */
	private void checkAddCallResponse(final Response responseWrapper, final boolean expectedResponse) {
		assertEquals(Response.Status.OK.getStatusCode(), responseWrapper.getStatus());
		assertEquals(expectedResponse, responseWrapper.readEntity(BOOLEAN_RETURN_TYPE));
	}

	/**
	 * Execute the {@link SetResource#addSingle(javax.ws.rs.core.UriInfo, String)} call.
	 */
	private Response performAddCall(final String value) {
		final String pathToCall = "set/add/" + value;
		return target(pathToCall).request(MediaType.APPLICATION_JSON_TYPE).get();
	}
}
