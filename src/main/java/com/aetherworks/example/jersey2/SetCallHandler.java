package com.aetherworks.example.jersey2;

import java.util.HashSet;
import java.util.Set;

import com.aetherworks.example.jersey2.api.SetResource;

/**
 * The internals of the application, called by the {@link SetResource} to interact with internal application state.
 * <p>
 * An instance of this class is injected into the {@link SetResource}.
 * 
 * @author Angus Macdonald (amacdonald@aetherworks.com)
 */
public class SetCallHandler {
	private final Set<String> internalSet = new HashSet<>();

	public boolean add(final String value) {
		return internalSet.add(value);
	}

	public void add(final Set<String> values) {
		internalSet.addAll(values);
	}

	public Set<String> getSetCopy() {
		return new HashSet<>(internalSet);
	}
}
