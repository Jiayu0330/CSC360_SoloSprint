package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.BusinessPlan;
import models.MyRemoteClient;
import models.Section;

public class ShowTwoPlansController
{
	
	BusinessPlan plan1;
	BusinessPlan plan2;
	MyRemoteClient client;
	
	@FXML
    private VBox VBox1;

    @FXML
    private Label yearLabel1;

    @FXML
    private VBox VBox2;

    @FXML
    private Label yearLabel2;
    
    @FXML
    private Button backButton;


    public void setModel(BusinessPlan plan1)
	{
		this.plan1 = plan1;
		setContent(plan1.root, VBox1);
		yearLabel1.textProperty().setValue(plan1.getYear());
	}

	public void setModel2(BusinessPlan plan2)
	{
		this.plan2 = plan2;
		setContent(plan2.root, VBox2);
		yearLabel2.textProperty().setValue(plan2.getYear());
	}
	
	private void setContent(Section current, VBox v)
    {
		TextArea area= new TextArea();
    	if(current.children.isEmpty())
    	{		
    		area.setEditable(false);
    		area.textProperty().set(current.content);
    		//Bindings.bindBidirectional(area.textProperty(),current.getContent());
    		v.getChildren().add(area);

    	}
    	else
    	{
    		area.setEditable(false);
    		v.getChildren().add(area);
    		area.textProperty().set(current.content);
    		//Bindings.bindBidirectional(area.textProperty(),current.getContent());
    		for(int i = 0; i<current.children.size(); i++)
    		{
    			setContent(current.getChildren().get(i), v);
    			
    		}
    	}
    	
    	if(current.isDiff() == true)
    	{
    		area.setStyle("-fx-control-inner-background:yellow");
    	}
    }

	@FXML
    void onClickBack(ActionEvent event) 
	{
		Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
	
}
