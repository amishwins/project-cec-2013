package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	CECConfiguratorTests.class, 
	EmailBuilderTests.class,
	EmailSortingTests.class, 
	EmailTests.class, 
	FolderFactoryTests.class,
	FolderTests.class, 
	InputValidationTests.class,
	HierarchyTests.class,
	MeetingBuilderTests.class})

public class UnitTests {

}
