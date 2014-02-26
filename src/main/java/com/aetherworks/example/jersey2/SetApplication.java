package com.aetherworks.example.jersey2;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.aetherworks.example.jersey2.exception.InvalidRequestExceptionMapper;

/**
 * This class is specified in the <tt>web.xml</tt> file, and is used when Jersey is loaded to determine where resources
 * (API classes) are located, and for any other dependencies.
 * 
 * @author Angus Macdonald (amacdonald@aetherworks.com)
 */
public class SetApplication extends ResourceConfig {

	/**
	 * This is the constructor called by the application server when loading the application (as specified by the
	 * web.xml).
	 */
	public SetApplication() {
		this(new SetCallHandler());
	}

	/**
	 * This is the constructor called directly by unit tests. This allows us to pass in a mocked version of the
	 * {@link SetCallHandler} to just test the functionality of the API and not the handler itself.
	 */
	public SetApplication(final SetCallHandler setCallHandler) {
		/*
		 * Register the mapping between internal exceptions and their outward facing messages.
		 */
		register(InvalidRequestExceptionMapper.class);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(setCallHandler).to(SetCallHandler.class);
			}
		});

		/*
		 * Specify where resource classes are located. These are the classes that constitute the API.
		 */
		packages(true, "com.aetherworks.example.jersey2.api");
	}
}