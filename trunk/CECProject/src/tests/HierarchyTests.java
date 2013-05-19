package tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cec.model.Folder;
import cec.model.Hierarchy;

public class HierarchyTests {

	HierarchyCUT hierarchy;
	List<Folder> systemFolders;
	
	
	@Before
	public void setUp() throws Exception {
		hierarchy = new HierarchyCUT();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void showLoadHierarchyWorks() {
		systemFolders = (List<Folder>)hierarchy.loadHierarchy();
		 assertEquals(systemFolders.get(0).getPath(), "emails/Inbox");
		 assertEquals(systemFolders.get(1).getPath(), "emails/Drafts");
		 assertEquals(systemFolders.get(2).getPath(), "emails/Outbox");
		 assertEquals(systemFolders.get(3).getPath(), "emails/Sent");
	}
	
}
class HierarchyCUT extends Hierarchy{
	
	@Override
	protected void getSubFoldersFromSystemFolder(Folder systemFolder){
		return;
	}
}
