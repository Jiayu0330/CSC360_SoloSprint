package models;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Section implements Serializable
{

	private static final long serialVersionUID = 3004879294918214266L;
	StringProperty name = new SimpleStringProperty();
	StringProperty content = new SimpleStringProperty();
	Section parent = null;
	public ArrayList<Section> children =  new ArrayList<Section>();
	boolean diff = false;
	//ArrayList<String> comment =  new ArrayList<String>();
	ObservableList<String> comments =FXCollections.observableArrayList();

	
	public void addComment(String userName, String comment)
	{
		comments.add(userName + ": " + comment);
	}
	
	public void deleteComment(String selectedComment)
	{
		comments.remove(selectedComment);
	}

	public boolean isDiff()
	{
		return diff;
	}

	public ObservableList<String> getComments()
	{
		return comments;
	}

	public void setComments(ObservableList<String> comments)
	{
		this.comments = comments;
	}

	public void setDiff(boolean diff)
	{
		this.diff = diff;
	}

	@Override
	public String toString() {
		return name.getValue();
	}

	// default constructor for XML
	public Section()
	{
		this("default");
		content.setValue("");
		
	}

	public Section(String name)
	{
		this.name.setValue(name);
	}

	// getters and setters
	public Section getParent()
	{
		return parent;
	}

	public void setParent(Section parent)
	{
		this.parent = parent;
	}

	// this is used for XMl encoder, but we can never change the children of a
	// Section
	public void setChildren(ArrayList<Section> children)
	{
		this.children = children;
	}

	public String getName()
	{
		return name.getValue();
	}

	// this is used for XML encoder, but we can never change the name of a Section
	public void setName(String name)
	{
		this.name.setValue(name);
	}

	public ArrayList<Section> getChildren()
	{
		return children;
	}

	public StringProperty getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content.setValue(content);
	}
	// end of getters and setters

	// add child to the array list
	public void addChild(Section child)
	{
		children.add(child);
	}

	// remove child from the array list
	public void deleteChild(Section child)
	{
		children.remove(child);
	}
}
