package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	CECConfiguratorTests.class,
	ChangeSetTests.class,
	EmailBuilderTests.class,
	EmailSortingTests.class,
	EmailTests.class,
	FolderFactoryTests.class,
	FolderTests.class,
	HierarchyTests.class,
	InputValidationTests.class,
	MeetingBuilderTests.class,
	PlaceholderHelperTests.class,
	RecipientsTests.class,
	RuleTests.class,
	RuleXMLDaoTests.class,
	SearchTests.class,
	ServerMeetingDataImplTests.class,
	TemplateImplTests.class,
	TemplateServiceTests.class,
	TemplateXMLDaoTests.class,
		
})

public class UnitTests {

}
