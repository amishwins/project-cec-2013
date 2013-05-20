package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.integration.EmailModelAndDaoIntegrationTests;
import tests.integration.EmailServiceModelAndDaoIntegrationTest;
import tests.integration.FolderServiceModelAndDaoIntegrationTests;

@RunWith(Suite.class)
@SuiteClasses({ 
	CECConfiguratorTests.class,
	EmailBuilderTests.class, 
	EmailSortingTests.class,
	EmailTests.class,
	FolderFactoryTests.class,
	FolderTests.class,
	HierarchyTests.class,
	
	EmailModelAndDaoIntegrationTests.class,
	EmailServiceModelAndDaoIntegrationTest.class,
	FolderServiceModelAndDaoIntegrationTests.class 	})

public class AllTests {
}

