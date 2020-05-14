package models;

import java.io.Serializable;

public class VMOSA extends BusinessPlan implements Serializable
{
	private static final long serialVersionUID = 3349885006404797374L;
	public String[] sectionNames = {"Vision", "Mission","Objective","Strategy","Action Plan"};
	public String[] getSectionNames() {
		return sectionNames;
	}

	public void setSectionNames(String[] sectionNames) {
		this.sectionNames = sectionNames;
	}

	public VMOSA()
	{
		// create a complete tree
		type.setValue("VMOSA");
		
		root = new Section("Vision");
		Section strategy = new Section("Strategy");
		Section action = new Section("Action Plan");
		strategy.addChild(action);
		Section objective = new Section("Objective");
		objective.addChild(strategy);
		Section mission = new Section("Mission");
		mission.addChild(objective);
		root.addChild(mission);
		mission.setParent(root);
		objective.setParent(mission);
		strategy.setParent(objective);
		action.setParent(strategy);
		setHeight(5);
		

	}

	public void addSection (Section parent) 
	{
		while (parent.name.getValue() != "Action Plan")
		{
			if (parent.name.getValue() == "Vision")
			{
				Section child = new Section("Mission");
				child.setParent(parent);
				parent.addChild(child);
				parent = child;
			} 
			else if (parent.name.getValue() == "Mission")
			{
				Section child = new Section("Objective");
				child.setParent(parent);
				parent.addChild(child);
				parent = child;
			} 
			else if (parent.name.getValue() == "Objective")
			{
				Section child = new Section("Strategy");
				child.setParent(parent);
				parent.addChild(child);
				parent = child;
			} 
			else if (parent.name.getValue() == "Strategy")
			{
				Section child = new Section("Action Plan");
				child.setParent(parent);
				parent.addChild(child);
				parent = child;
			} 
			else 
			{
				System.out.println("ERROR: INVALID SECTION! ! !");
				return;
			}
		}

	}




	
}
