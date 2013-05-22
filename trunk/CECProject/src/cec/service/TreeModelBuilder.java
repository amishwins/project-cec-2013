package cec.service;

import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * 
 * @author a_gala
 */
public class TreeModelBuilder {
	private DefaultTreeModel cecModel;

	public TreeModelBuilder(DefaultMutableTreeNode root) {
		cecModel = new DefaultTreeModel(root);
	}

	public DefaultTreeModel buildTreeNodeFromFileList(Iterable<String> hierarchy) {

		for (String folder : hierarchy) {
			buildTreeFromFolders(folder);
		}
		return cecModel;
	}

	private void buildTreeFromFolders(final String relativeFolderPath) {

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) cecModel
				.getRoot();

		String[] folders = relativeFolderPath.split("/");

		DefaultMutableTreeNode node = root;

		for (String folder : folders) {

			int index = subFolderIndex(node, folder);

			if (index < 0) {

				DefaultMutableTreeNode subFolder = new DefaultMutableTreeNode(
						folder);
				node.insert(subFolder, node.getChildCount());
				node = subFolder;
			}

			else {
				node = (DefaultMutableTreeNode) node.getChildAt(index);
			}
		}
	}

	private int subFolderIndex(final DefaultMutableTreeNode parentFolder,
			final String subFolderPath) {
		Enumeration<DefaultMutableTreeNode> subFolders = parentFolder
				.children();
		DefaultMutableTreeNode subFolder = null;
		int index = -1;

		while (subFolders.hasMoreElements() && index < 0) {
			subFolder = subFolders.nextElement();

			if (subFolder.getUserObject() != null
					&& subFolderPath.equals(subFolder.getUserObject())) {
				index = parentFolder.getIndex(subFolder);
			}
		}

		return index;
	}
}
