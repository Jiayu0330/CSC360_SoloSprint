package views;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.MainBPView;
import models.BusinessPlan;
import models.CNTRAssessment;
import models.MyRemote;
import models.MyRemoteClient;
import models.MyRemoteImpl;
import models.Section;

/* 
 * This one is going to test the first task, which is to compare two business plans.
 * 
 * I'm going to show two business plans when they are the same.
 * Then modify one plan and see what happens.
 * 
 * Different sections will be marked with yellow background.
 * 
 * Please try several times if there is EmptyNodeQueryException.
 * Since sometimes robot clicks wrong place!
 */


@ExtendWith(ApplicationExtension.class)
public class TestTask1
{
	static MyRemoteImpl server;
	static MyRemoteClient client;
	static BusinessPlan plan;
	
	Stage stage;
	
	@BeforeAll
	static void initialize() throws RemoteException
	{
		try
		{

			Registry registry = LocateRegistry.createRegistry(1099);
			server = new MyRemoteImpl();
			
	    	MyRemote stub = (MyRemote) UnicastRemoteObject.exportObject(server, 0);
			registry.rebind("MyRemote", stub);
			MyRemote serverInterface=(MyRemote) registry.lookup("MyRemote");
			client=new MyRemoteClient();
			
			System.err.println("Server ready");
			
			serverInterface.addObserver(client);
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//server = new MyRemoteImpl();
//		client = new MyRemoteClient(server);
		//client = new MyRemoteClient();
		
		plan = new CNTRAssessment();
		Section current = plan.root;
		current.setContent("root");
		String comment = "comment for root";
		current.addComment("", comment);
		plan.addSection(current);
		current.getChildren().get(1).setContent("goal2");;
		current = current.getChildren().get(0);
		current.setContent("goal");
		current.addChild(new Section("Program Goals and Student Learning Objective"));
		current.getChildren().get(0).setContent("objective1");
		current.getChildren().get(1).setContent("objective2");
		plan.setDepartment("CSC");
		plan.setYear("2020");
		//Registry registry = LocateRegistry.createRegistry(1099);
		server.getStoredBP().add(plan);
		
		BusinessPlan plan2 = new CNTRAssessment();
		Section current2 = plan2.root;
		current2.setContent("root");
		plan2.addSection(current2);
		current2.getChildren().get(1).setContent("goal2");;
		current2 = current2.getChildren().get(0);
		current2.setContent("goal");
		current2.addChild(new Section("Program Goals and Student Learning Objective"));
		current2.getChildren().get(0).setContent("objective1");
		current2.getChildren().get(1).setContent("objective2");
		plan2.setDepartment("CSC");
		plan2.setYear("2021");
		plan2.isEditable = false;
		plan2.setEdit("No");
		server.getStoredBP().add(plan2);	
		server.addPerson("", "", "CSC", true);
		server.addPerson("Iris", "password", "CSC", true);
		
		//client = new MyRemoteClient();
		client.setServer(server);
	}
	
	
	@Start
	private void start(Stage stage)
	{
		this.stage = stage;
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainBPView.class.getResource("../views/loginPage.fxml"));
			BorderPane view = loader.load();
			LoginController cont = loader.getController();
			cont.setModel(client);
			
			Scene s = new Scene(view);
			stage.setScene(s);
			stage.show();
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	void test(FxRobot robot)
	{		
		try
		{
			//enter username and password
			robot.clickOn("#username");
			robot.write("Iris");
			robot.clickOn("#password");
			robot.write("password");
			Thread.sleep(1000);
			
			//login
			robot.clickOn("#loginButton");
			Thread.sleep(1000);
			
			//compare two plans when they are exactly the same
			robot.clickOn("#compareButton");
			Thread.sleep(1000);
			
			//enter years		
			robot.clickOn("#year1");
			robot.write("2020");
			robot.clickOn("#year2");
			robot.write("2021");
			Thread.sleep(1000);
			robot.clickOn("#compare_confirm");
			Thread.sleep(1000);
			
			//go back to the previous page
			robot.clickOn("#show_back");
			Thread.sleep(1000);
			
			//select a plan to edit
			@SuppressWarnings("unchecked")
			TableView<BusinessPlan> tableV = (TableView<BusinessPlan>)robot.lookup("#tableView").queryAs(TableView.class);	    
			tableV.getSelectionModel().clearAndSelect(0);
			robot.clickOn("#cancelButton"); //view button
			Thread.sleep(1000);
			
			//hard code the location of table item
//			Node node = ((Parent) robot.lookup("#tableView").query()).getChildrenUnmodifiable().get(0);
//			Bounds bounds = node.localToScreen(node.getBoundsInLocal());
//			int x = (int) bounds.getMinX();
//			int y = (int) bounds.getMinY();
//			
//			robot.moveTo(x+20, y+40);
//			robot.clickOn();
//			Thread.sleep(1000);
//			robot.clickOn("#cancelButton"); //view button
//			Thread.sleep(1000);
			
			//edit the plan
			@SuppressWarnings("unchecked")
			TreeView<Section> treeV = (TreeView<Section>)robot.lookup("#treeView").queryAs(TreeView.class);	    
			treeV.getSelectionModel().clearAndSelect(1);
			robot.clickOn("#selectButton");
			Thread.sleep(1000);
			
			//hard code
//			Node tree = robot.lookup("#treeView").query();
//			Bounds bounds1 = tree.localToScreen(tree.getBoundsInLocal());
//			int x1 = (int) bounds1.getMinX();
//			int y1 = (int) bounds1.getMinY();
//			
//		    robot.moveTo(x1+35, y1+15);
//		    robot.doubleClickOn();
//		    Thread.sleep(1000);
//		    robot.moveTo(x1+40, y1+30);
//			robot.clickOn();
//			Thread.sleep(1000);

			
			//change root content
			robot.clickOn("#contentText");
			robot.write(" changed");
			Thread.sleep(1000);
			
			//add an empty section
//			@SuppressWarnings("unchecked")
//			TreeView<Section> treeV1 = (TreeView<Section>)robot.lookup("#treeView").queryAs(TreeView.class);	    
			treeV.getSelectionModel().clearAndSelect(0);
			
//			robot.moveTo(x1+35, y1+15);
//			robot.clickOn();
			robot.clickOn("#selectButton");
			Thread.sleep(1000);
			robot.clickOn("#contentText");
			robot.write(". Going to add another branch");
			Thread.sleep(1000);
			robot.clickOn("#addButton");
			Thread.sleep(1000);
			robot.clickOn("#newContentText");
			robot.write("I'm a new section!");
			Thread.sleep(1000);
			robot.clickOn("#confirmButton");
			Thread.sleep(1000);
			robot.clickOn("#closeButton");
			Thread.sleep(1000);
			robot.clickOn("#closeButton");
			Thread.sleep(1000);
			
			//compare after the change
			robot.clickOn("#compareButton");
			Thread.sleep(1000);	
			robot.clickOn("#year1");
			robot.write("2020");
			robot.clickOn("#year2");
			robot.write("2021");
			Thread.sleep(1000);
			robot.clickOn("#compare_confirm");
			Thread.sleep(10000); //wait 10 sec

			
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
	}
}
	