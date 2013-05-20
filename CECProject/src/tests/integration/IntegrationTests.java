package tests.integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	EmailModelAndDaoIntegrationTests.class,
	EmailServiceModelAndDaoIntegrationTest.class,
	FolderServiceModelAndDaoIntegrationTests.class })

public class IntegrationTests {

}
