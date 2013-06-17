package tests.integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	EmailModelAndDaoIntegrationTests.class,
	EmailServiceModelAndDaoIntegrationTests.class,
	FolderServiceModelAndDaoIntegrationTests.class,
	MeetingServiceModelAndDaoIntegrationTests.class,
	TemplateModelAndDaoIntegrationTests.class,
	RuleSetModelAndDaoIntegrationTests.class })

public class IntegrationTests {

}
