package views;

import java.io.IOException;
import java.rmi.RemoteException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.MainBPView;
import models.BusinessPlan;
import models.CNTRAssessment;
import models.MyRemoteClient;
import models.MyRemoteImpl;
import models.Section;

/* 
 * This one is going to test the second task, which is to add and remove comments.
 * 
 * I'm going to add some comments and remove some.
 * 
 * Please try several times if there is EmptyNodeQueryException.
 * Since sometimes robot clicks wrong place!
 */


@ExtendWith(ApplicationExtension.class)
public class TestTask2
{
	static MyRemoteImpl server;
	static MyRemoteClient client;
	
	Stage stage;
	
	@BeforeAll
	static void initialize() throws RemoteException
	{
		server = new MyRemoteImpl();
//		client = new MyRemoteClient(server);
		client = new MyRemoteClient();
		
		BusinessPlan plan = new CNTRAssessment();
		Section current = plan.root;
		current.setContent("root");
		String comment = "comment for root";
		current.addComment("Someone", comment);
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
			
			//select a plan
			@SuppressWarnings("unchecked")
			TableView<BusinessPlan> tableV = (TableView<BusinessPlan>)robot.lookup("#tableView").queryAs(TableView.class);	    
			tableV.getSelectionModel().clearAndSelect(0);
			robot.clickOn("#cancelButton"); //view button
			Thread.sleep(1000);
//			
//			Node node = ((Parent) robot.lookup("#tableView").query()).getChildrenUnmodifiable().get(0);
//			Bounds bounds = node.localToScreen(node.getBoundsInLocal());
//			int x = (int) bounds.getMinX();
//			int y = (int) bounds.getMinY();
//			
//			robot.moveTo(x+20, y+40);
//			robot.clickOn();
//			Thread.sleep(1000);

			
			//select a section
			@SuppressWarnings("unchecked")
			TreeView<Section> treeV = (TreeView<Section>)robot.lookup("#treeView").queryAs(TreeView.class);	    
			treeV.getSelectionModel().clearAndSelect(0);
			robot.clickOn("#selectButton");
			Thread.sleep(1000);
			
//			Node tree = robot.lookup("#treeView").query();
//			Bounds bounds1 = tree.localToScreen(tree.getBoundsInLocal());
//			int x1 = (int) bounds1.getMinX();
//			int y1 = (int) bounds1.getMinY();
//			
//		    robot.moveTo(x1+35, y1+15);
//			robot.clickOn();
//			Thread.sleep(1000);

			
			//add some comments for the section
			//comment 1
			robot.clickOn("#addComment");
			Thread.sleep(1000);
			robot.write("new comment 1");
			Thread.sleep(1000);
			robot.clickOn("#okButton");
			Thread.sleep(1000);
			
			//comment 2
			robot.clickOn("#addComment");
			Thread.sleep(1000);
			robot.write("new comment 2");
			Thread.sleep(1000);
			robot.clickOn("#okButton");
			Thread.sleep(1000);
			
			//remove comments
			@SuppressWarnings("unchecked")
			ListView<Section> listV = (ListView<Section>)robot.lookup("#listView").queryAs(ListView.class);	    
			listV.getSelectionModel().clearAndSelect(0);
			Thread.sleep(1000);
			robot.clickOn("#deleteComment");
			Thread.sleep(1000);
			
			listV.getSelectionModel().clearAndSelect(1);
			Thread.sleep(1000);
			robot.clickOn("#deleteComment");
			Thread.sleep(1000);
			
//			Node list = robot.lookup("#listView").query();
//			Bounds bounds2 = list.localToScreen(list.getBoundsInLocal());
//			int x2 = (int) bounds2.getMinX() + 10;
//			int y2 = (int) bounds2.getMinY() + 15;
//	
//		    robot.moveTo(x2, y2);
//			robot.clickOn();



			
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
	}
}
	