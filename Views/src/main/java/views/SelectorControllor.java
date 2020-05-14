package views;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.MainBPView;

import models.BusinessPlan;
import models.ConfirmationInterface;
import models.MyRemoteClient;


public class SelectorControllor {
	//model 2 is only used for test
	BusinessPlan plan;
	MyRemoteClient client;
	Stage stage;
	ConfirmationInterface model2;
	//All the table columns 
	@FXML 
	private TableView<BusinessPlan> tableView;
	@FXML 
	private TableColumn<BusinessPlan,String> year;
	@FXML 
	private TableColumn<BusinessPlan,String> department;
    @FXML
    private TableColumn<BusinessPlan, String> Editability;
    @FXML
    private TableColumn<BusinessPlan, String> Type;
    //this is called when the user click the create new business plan button and then a new window will pop up 
	@FXML
    void createNewBP(ActionEvent event)
	{
		if(model2 == null)
		{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainBPView.class.getResource("../views/CreateNewBPView.fxml"));
		BorderPane pane;
		try {
			pane = loader.load();
    		CreateNewBPViewController cont = loader.getController();
    		cont.setModel(client);
    		cont.setStage(stage);
    		Scene sc = new Scene(pane);
    		stage.setScene(sc);
    		stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		else
		{	//for testing
			model2.confirmation();
		}
    			
    }
	public void setStage(Stage newStage)
	{	//pass stage between different views
		stage = newStage;
	}
	public void setModel(MyRemoteClient client)
	{
		if(model2 == null)//avoid null pointer exception
		{
		this.client = client;
		createTable();
		}

	}
	//called when the user selects a BP and then tries to view it.

	@FXML
	void onClickView(ActionEvent event) 
	{	
		if(model2 == null)
		{
		//try {
		//get selected BP
		
		BusinessPlan current = tableView.getSelectionModel().getSelectedItem();
		client.setCurrentBP(current);
		
		
//		System.out.println(client.getLoginPerson());
//		System.out.println(current.getCurrentP());
		if (client.getLoginPerson().getUsername() != current.getCurrentPersonName()
				&& current.getCurrentPersonName() != null)
		{
			//System.out.println("Oops " + current.getCurrentPersonName() + " is editing this bp");
			
    		Alert a = new Alert(AlertType.INFORMATION); 
    		a.setContentText("Oops " + current.getCurrentPersonName() + " is editing this bp. Please come back later!"); 
    		a.show();
            Button okButton1 = (Button) a.getDialogPane().lookupButton(ButtonType.OK);
            okButton1.setId("okButton1");
		}
		else
		{
			current.setCurrentPersonName(client.getLoginPerson().getUsername());
			//client.getServer().notifyObservers(current.getCurrentPersonName());
			System.out.println(client.getLoginPerson().getUsername() + " is editing");
			
			
			//pop up editable page
			if(current.isEditable)
	    	{
	    		FXMLLoader loader = new FXMLLoader();
	    		loader.setLocation(MainBPView.class.getResource("../views/BPView.fxml"));
	    		BorderPane pane;
				try {
					pane = loader.load();
		    		BPViewController cont = loader.getController();
		    		//cont.setModel(client.getCurrentBP());
		    		cont.setModel(current,client);
		    		cont.setPane(pane);
		    		cont.setStage(stage);
		    		Scene sc = new Scene(pane);
		    		stage.setScene(sc);
		    		stage.show();
				} catch (IOException e) {

					e.printStackTrace();
				}

	    	}
			//pop up noneditable page
	    	else
	    	{
	    		FXMLLoader loader = new FXMLLoader();
	    		loader.setLocation(MainBPView.class.getResource("../views/NonEditableView.fxml"));
	    		BorderPane pane;
				try {
					pane = loader.load();
		    		NonEditableViewController cont = loader.getController();
		    		//cont.setModel(client.getCurrentBP());
		    		cont.setModel(current,client);
		    		cont.setPane(pane);
		    		cont.setStage(stage);
		    		Scene sc = new Scene(pane);
		    		stage.setScene(sc);
		    		stage.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
	    	//}
	    	}
			}
		}
		

//	    catch(Exception e) 
//		{
//	    	System.out.println("Please Select a Business Plan");
//	    }
		}
		else
		{
			model2.cancel();
		}
    	
}
	  //create table of BPs
	  public void createTable()
	  {
		  year.setCellValueFactory(new PropertyValueFactory<BusinessPlan, String>("year"));
		  department.setCellValueFactory(new PropertyValueFactory<BusinessPlan, String>("department"));
		  Editability.setCellValueFactory(new PropertyValueFactory<BusinessPlan, String>("edit"));
		  Type.setCellValueFactory(new PropertyValueFactory<BusinessPlan, String>("type"));
		  ArrayList<BusinessPlan> plans = client.getServer().getStoredBP();
		  ObservableList<BusinessPlan> newPlans = FXCollections.observableArrayList();
		  for(int i = 0; i<plans.size(); i++)
		  {
			  newPlans.add(plans.get(i));
		  }
		  tableView.setItems(newPlans);
	  }
	//set model2 for test
	public void setModel2(ConfirmationInterface model2) {
		this.model2 = model2;
		
	}
	
	//a window that asks for two BPs will pop up when you click this button
    @FXML
    void onClickCompare(ActionEvent event) {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/ComparePlansView.fxml"));
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle("Compare Two Plans");
            stage.setScene(new Scene(root1, 500, 400));  
            stage.show();
            
            ComparePlansController cont = loader.getController();
			cont.setModel(plan, client);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
       
    }
    

	 

}

