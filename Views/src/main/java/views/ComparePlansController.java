package views;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.BusinessPlan;
import models.MyRemoteClient;

public class ComparePlansController
{
	int year1;
	int year2;
	MyRemoteClient client;
	
    @FXML
    private TextField firstYearTextField;

    @FXML
    private TextField secondYearTextField;
    
    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;
	

    
    public void setModel(BusinessPlan plan, MyRemoteClient client)
	{
    	this.client = client;
		
	}
    
    @FXML
    void onClickConfirm(ActionEvent event) 
    {
    	//get input years
    	String s_year1 = firstYearTextField.textProperty().getValue();
    	String s_year2 = secondYearTextField.textProperty().getValue();
    	
    	if (s_year1.isEmpty() || s_year2.isEmpty())
    	{
    		Alert a = new Alert(AlertType.INFORMATION); 
    		a.setContentText("Please enter valid years."); 
    		a.show();
    	}
    	else
    	{
    		year1 = Integer.parseInt(s_year1);
        	year2 = Integer.parseInt(s_year2);
        	
        	if (year1 == year2)
        	{
        		Alert a = new Alert(AlertType.INFORMATION); 
        		a.setContentText("Please enter two differet years."); 
        		a.show();
        	}
        	
        	else 
        	{
        		boolean is_comparable1 = client.askForBP(year1);
            	boolean is_comparable2 = client.askForSecondBP(year2);
            	
            	System.out.println(is_comparable1);
            	System.out.println(is_comparable2);
            	
            	if (is_comparable1 == true && is_comparable2 == true)
            	{
            		client.compareTwoBPs();
         
            		try {
                    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/ShowTwoPlansView.fxml"));
                        Parent root = (Parent) loader.load();
                        Stage stage = new Stage();
                        stage.setTitle("Show Two Plans");
                        stage.setScene(new Scene(root));  
                        stage.show();
                        
                        ShowTwoPlansController cont = loader.getController();
                        //System.out.println("1" + client.getCurrentBP());
            			//System.out.println("2" + client.getCurrentBP2());
            			cont.setModel(client.getCurrentBP());
            			cont.setModel2(client.getCurrentBP2());
            			
            			//close current window
            			Stage currentStage = (Stage) confirmButton.getScene().getWindow();
            			currentStage.close();
            			
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
            	}
            	else
            	{
            		Alert a = new Alert(AlertType.INFORMATION); 
            		a.setContentText("Please enter correct years."); 
            		a.show();
            	}
        	}		
    	}
    	
    }
    
    //close current window
    @FXML
    void onClickCancel(ActionEvent event) 
    {
    	Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    
}
