/*
 * Copyright 2014, AetherWorks LLC.
 */

package com.aetherworks.example.jersey2.api;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.aetherworks.example.jersey2.SetApplication;
import com.aetherworks.example.jersey2.SetCallHandler;
import com.aetherworks.example.jersey2.exception.InvalidRequestException;
import com.aetherworks.example.jersey2.exception.InvalidRequestExceptionMapper;

@RunWith(MockitoJUnitRunner.class)
public class MockedSetApiTests extends JerseyTest {

	/**
	 * Used to specify the format expected for the return type of the add call.
	 */
	private final GenericType<Boolean> BOOLEAN_RETURN_TYPE = new GenericType<Boolean>() {
	};

	/**
	 * The handler used in the {@link SetResource}. We are going to use {@link Mockito} to mock this handler for some
	 * tests, to just check the API call itself.
	 */
	private SetCallHandler setCallHandler;

	@Override
	protected Application configure() {
		setCallHandler = Mockito.mock(SetCallHandler.class);

		return new SetApplication(setCallHandler);
	}

	@Override
	@After
	public void tearDown() throws Exception {
		/*
		 * The super-class tearDown method takes down the server, so it has to be called.
		 */
		super.tearDown();

		/*
		 * Reset the mocking on this object so that the field can be safely re-used between tests.
		 */
		Mockito.reset(setCallHandler);
	}

	/**
	 * Makes a call to add, and checks that the expected value is returned. The {@link SetCallHandler} is mocked, so we
	 * control the return type.
	 */
	@Test
	public void mockedAddCall() throws InvalidRequestException {
		/*
		 * Set up. This code sets up mocking to return true when the SetCallHandler#add() method is called with the
		 * given value.
		 */
		final String value = "MyTest1";
		final boolean returnValue = true;
		when(setCallHandler.add(value)).thenReturn(returnValue);

		/*
		 * Perform call. This is set up as a get request, so the parameter is added to the path of that request.
		 */
		final String pathToCall = "set/add/" + value;
		final Response responseWrapper = target(pathToCall).request(MediaType.APPLICATION_JSON_TYPE).get();

		/*
		 * Manage response. Check that the status code is correct (so we know the call worked), and then check that the
		 * value returned is as expected.
		 */
		assertEquals(Response.Status.OK.getStatusCode(), responseWrapper.getStatus());

		assertEquals(returnValue, responseWrapper.readEntity(BOOLEAN_RETURN_TYPE));
	}

	/**
	 * Mock an exception being thrown and check that the {@link InvalidRequestException} mapper at
	 * {@link InvalidRequestExceptionMapper} is being used correctly.
	 */
	@Test
	public void exceptionMapping() throws InvalidRequestException {
		/*
		 * Set up. This code sets up mocking to throw an exception when the SetCallHandler#add() method is called with
		 * the given value.
		 */
		final String value = "MyTest1";
		when(setCallHandler.add(value)).thenThrow(new InvalidRequestException("Mocked StartupException failure."));

		/*
		 * Perform call.
		 */
		final String pathToCall = "set/add/" + value;
		final Response responseWrapper = target(pathToCall).request(MediaType.APPLICATION_JSON_TYPE).get();

		/*
		 * Manage response. We expect an exception to be thrown, so we check that the expected error code is returned.
		 */
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), responseWrapper.getStatus());

		final String returnedErrorMessage = responseWrapper.readEntity(String.class);
		assertEquals(InvalidRequestExceptionMapper.ERROR_MESSAGE, returnedErrorMessage);
	}
}
