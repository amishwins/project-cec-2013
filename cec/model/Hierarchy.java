package cec.model;


/**
 * Hierarchy interface. Only contains one method to load the hierarchy
 * and its return type is a collection of folders. This is used for easy
 * tree model building
 *
 */
public interface Hierarchy {

	public abstract Iterable<Folder> loadHierarchy();

}