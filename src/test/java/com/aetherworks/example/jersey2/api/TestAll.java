/*
 * Copyright 2014, AetherWorks LLC.
 */

package com.aetherworks.example.jersey2.api;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test runner to run all specified test classes.
 * 
 * @author Angus Macdonald (amacdonald@aetherworks.com)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ SetApiTests.class, MockedSetApiTests.class })
public class TestAll {
	// Empty: run with JUnit.
}
