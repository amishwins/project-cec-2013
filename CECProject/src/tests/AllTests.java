package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.integration.EmailModelAndDaoIntegrationTests;

@RunWith(Suite.class)
@SuiteClasses({ 
	CECConfiguratorTests.class,
	EmailBuilderTests.class, 
	EmailSortingTests.class,
	EmailTests.class,
	FolderFactoryTests.class,
	FolderTests.class,
	EmailModelAndDaoIntegrationTests.class })

public class AllTests {
}

