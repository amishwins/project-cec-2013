package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.integration.IntegrationTests;

@RunWith(Suite.class)
@SuiteClasses({
	UnitTests.class,
	IntegrationTests.class
})

public class AllTests {
}

