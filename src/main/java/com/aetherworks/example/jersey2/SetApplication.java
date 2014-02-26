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

	public SetApplication() {

		/*
		 * Register the mapping between internal exceptions and their outward facing messages.
		 */
		register(InvalidRequestExceptionMapper.class);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(new SetCallHandler()).to(SetCallHandler.class);
			}
		});

		/*
		 * Specify where resource classes are located. These are the classes that constitute the API.
		 */
		packages(true, "com.aetherworks.example.jersey2.api");

	}
}