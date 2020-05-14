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
 * This one is going to test the second task, which is to add and remove comments.
 * 
 * I'm going to add some comments and remove some.
 * 
 * Please try several times if there is EmptyNodeQueryException.
 * Since sometimes robot clicks wrong place!
 */


@ExtendWith(ApplicationExtension.class)
public class TestTask3
{
	static MyRemoteImpl server;
	static MyRemoteClient client;
	static MyRemoteClient client2;
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
			
			//create another client
			MyRemote serverInterface2=(MyRemote) registry.lookup("MyRemote");
			client2=new MyRemoteClient();
			
			System.err.println("Server ready");
			
			//add clients to server
			serverInterface.addObserver(client);
			serverInterface2.addObserver(client2);
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//server = new MyRemoteImpl();
		//client = new MyRemoteClient();
		
		plan = new CNTRAssessment();
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
		//server = new MyRemoteImpl();
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
		
		//client = new MyRemoteClient();
		client.setServer(server);
		client2.setServer(server);
		
		server.addPerson("someone", "", "CSC", true);
		server.addPerson("", "", "CSC", true);
		server.addPerson("Iris", "1", "CSC", true);
		
		//add client to server
		//server.addObserver(client);
		
		
		//assume Iris is editing plan
		//login
		client2.askForLogin("Iris", "1");
		
		//select a plan
		client2.setCurrentBP(plan);
		
		plan.setCurrentPersonName("Iris");
		
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
			robot.write("");
			robot.clickOn("#password");
			robot.write("");
			Thread.sleep(1000);
			
			//login
			robot.clickOn("#loginButton");
			Thread.sleep(1000);
			
			//select a plan
			@SuppressWarnings("unchecked")
			TableView<BusinessPlan> tableV = (TableView<BusinessPlan>)robot.lookup("#tableView").queryAs(TableView.class);	    
			tableV.getSelectionModel().clearAndSelect(0);
			robot.clickOn("#cancelButton"); //view button
			Thread.sleep(5000);
			
			//cannot view bc Iris is editing the plan
			robot.clickOn("#okButton1");
			Thread.sleep(5000);
			
			//Iris finished editing
			System.out.println("Client2 logout. Client1 can view the plan now");
			client2.logOut();
			
			//can view now
			tableV.getSelectionModel().clearAndSelect(0);
			robot.clickOn("#cancelButton");
			Thread.sleep(5000);

			

			
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
	}

}
	