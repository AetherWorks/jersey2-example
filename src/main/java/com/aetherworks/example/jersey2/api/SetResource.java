/*
 * Copyright 2014, AetherWorks LLC.
 */

package com.aetherworks.example.jersey2.api;

import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.aetherworks.example.jersey2.SetCallHandler;
import com.aetherworks.example.jersey2.SetApplication;
import com.aetherworks.example.jersey2.exception.InvalidRequestException;
import com.aetherworks.example.jersey2.exception.InvalidRequestExceptionMapper;

/**
 * An example API, which models some {@link Set} operations, allowing someone to add strings, and to query the state of
 * the set.
 * <p>
 * Jersey knows this is a resource class, because it is annotated with a {@link Path}, and because our
 * {@link SetApplication} has registered the package as a location for resources.
 * 
 * @author Angus Macdonald (amacdonald@aetherworks.com)
 */
@Path("/set")
public class SetResource {
	private final static Logger LOGGER = Logger.getLogger(SetResource.class.getName());

	/**
	 * This class is dynamically loaded by Jersey, so if we want it to interact with other objects in our project, we
	 * need to inject these dependencies. To do this, i've used the {@link Inject} annotation.
	 */
	@Inject
	private SetCallHandler callHandler;

	public SetResource() {
	}

	/**
	 * Add a given value to the set of {@link String} objects being stored by this resource.
	 * <p>
	 * This is a put call, so it can't be queried from a browser. <a
	 * href="http://localhost:8080/jersey2-example/v1/set/put/myvalue">Example test URI</a>.
	 * 
	 * @param headers
	 *        Parameters add with the context annotation aren't required, but they allow you to get extra information
	 *        about the call. You can also do this to get the {@link Locale} of the caller. <a
	 *        href="http://codahale.com/what-makes-jersey-interesting-injection-providers/">This post</a> by Coda Hale
	 *        is interesting, but the abstract classes he creates refer to Jersey V1 code.
	 * @param value
	 *        This is annotated with {@link PathParam}, meaning it is a value that is in the calling path. I have used a
	 *        {@link String} type, but you can use more complex types, such as <a
	 *        href="http://codahale.com/what-makes-jersey-interesting-parameter-classes/">in this Coda Hale example</a>,
	 *        which parses the string passed into the complex object's constructor.
	 * @return The return value is parsed into JSON (as I've specified that as the media type in the annotation). To do
	 *         this, the returned type must have some state to parse (public or annotated fields), and have an empty
	 *         constructor.
	 * @throws InvalidRequestException
	 *         Rather than throwing a messy exception message to the caller, this is mapped to a cleaner HTTP exception,
	 *         using the {@link InvalidRequestExceptionMapper} and a mapping specified in {@link SetApplication}.
	 */
	@PUT
	@Path("/add/{value}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSingle(@Context final UriInfo headers, @PathParam("value") final String value) throws InvalidRequestException {
		LOGGER.log(Level.INFO, "Call to " + headers.getPath());

		final boolean added = callHandler.add(value);
		return Response.status(Response.Status.OK).entity(added).build();
	}

	/**
	 * This is an example of a put request where the message is in the body of the request, rather than in the path, as
	 * is the case with {@link #addSingle(UriInfo, String)}.
	 * 
	 * @param values
	 *        Any parameters that are not marked with {@link PathParam} are expected in the body of the put request.
	 *        These are automatically parsed from JSON.
	 * @return This call doesn't return anything in the body of the message, but it sends back an HTTP 200 message
	 *         indicating the call was successful.
	 */
	@PUT
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addMultiple(final Set<String> values) throws InvalidRequestException {
		callHandler.add(values);
		return Response.status(Response.Status.OK).build();
	}

	/**
	 * Get the set of all {@link String} objects stored so far. This is a get request, so it can be called from the
	 * browser with this <a href="http://localhost:8080/jersey2-example/v1/set/get">example test URI</a>.
	 * <p>
	 * This call doesn't use the {@link Context} headers parameter to show that it is not needed. Removing this has no
	 * effect on the external API.
	 * 
	 * @return The set that is returned is automatically parsed into JSON, as we have specified this through the
	 *         {@link Produces} annotation.
	 */
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() throws InvalidRequestException {
		final Set<String> set = callHandler.getSetCopy();
		return Response.status(Response.Status.OK).entity(set).build();
	}
}
