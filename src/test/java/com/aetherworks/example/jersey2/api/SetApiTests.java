/*
 * Copyright 2014, AetherWorks LLC.
 */

package com.aetherworks.example.jersey2.api;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
	private final GenericType<Set<String>> STRING_SET_RETURN_TYPE = new GenericType<Set<String>>() {
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
	 * Attempt to call <tt>add</tt> but without the required parameter.
	 */
	@Test
	public void addCallNullParameter() {
		final Response response = target("set/add").request(MediaType.APPLICATION_JSON_TYPE).get();
		assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
	}

	/**
	 * Make calls to {@link SetResource#getAll()} and checks that the returned values are as expected.
	 */
	@Test
	public void getCallEmptySet() {
		checkGetCallResponse(performGetCall(), new HashSet<String>());
	}

	@Test
	public void getCallSingleValue() {
		final Set<String> valuesToStore = new HashSet<String>();

		addValuesAndCheckStored(valuesToStore);
	}

	@Test
	public void getCallManyValues() {
		final Set<String> valuesToStore = new HashSet<String>(Arrays.asList("a", "n", "g", "u", "s", "m", "a", "c"));
		addValuesAndCheckStored(valuesToStore);
	}

	/**
	 * Add the given values to the {@link SetResource} and then check that {@link SetResource#getAll()} returns those
	 * values as expected.
	 */
	private void addValuesAndCheckStored(final Set<String> valuesToStore) {
		addValues(valuesToStore);
		checkGetCallResponse(performGetCall(), valuesToStore);
	}

	/**
	 * Add the given values to the store by calling the {@link SetResource#addSingle(javax.ws.rs.core.UriInfo, String)}
	 * method.
	 */
	private void addValues(final Set<String> valuesToStore) {
		for (final String value : valuesToStore) {
			checkAddCallResponse(performAddCall(value), true);
		}
	}

	/**
	 * Check that the response to {@link SetResource#getAll()} is as expected.
	 */
	private void checkGetCallResponse(final Response callResponse, final Set<String> expectedResponse) {
		assertEquals(Response.Status.OK.getStatusCode(), callResponse.getStatus());
		assertEquals(expectedResponse, callResponse.readEntity(STRING_SET_RETURN_TYPE));
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

	/**
	 * Execute the {@link SetResource#addSingle(javax.ws.rs.core.UriInfo, String)} call.
	 */
	private Response performGetCall() {
		final String pathToCall = "set/get";
		return target(pathToCall).request(MediaType.APPLICATION_JSON_TYPE).get();
	}
}
