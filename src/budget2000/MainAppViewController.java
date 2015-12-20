/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;

/**
 *
 * @author brian
 */
public class MainAppViewController implements Initializable {
    //@FXML Node usersTab;
    //@FXML private UserViewController usersTabController;
    private MainApp mainApp;
    private BudgetData budgetData;
    
    @FXML TabPane tabPane; // must match exactly fx:id="tabPane" in fxml file
    
    @FXML Node usersTab;
    @FXML private UserViewController usersTabController;
    
    @FXML Node institutionsTab;
    @FXML private InstitutionViewController institutionsTabController;
    
    @FXML Node accountsTab;
    @FXML private AccountViewController accountsTabController;
    
    @FXML Node transactionsTab;
    @FXML private TransactionViewController transactionsTabController;
    
    
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO    
    }    
    
    protected void init() {
        System.out.println("MAVC::init()");
        
        // handle USER selection
        //budgetData.addUserPropertyChangeListener( evt -> { userSelected(evt); } );
    }
    
    void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    void setBudgetData(BudgetData budgetData) {
        this.budgetData = budgetData;    

        usersTabController.setBudgetData(budgetData);
        institutionsTabController.setBudgetData(budgetData);
        accountsTabController.setBudgetData(budgetData);
        transactionsTabController.setBudgetData(budgetData);
        
        init();
    }

    private void userSelected(PropertyChangeEvent evt) {
        
        Integer i = (Integer)evt.getNewValue();
        
        System.out.println("MainAppViewController::userSelected: " + i);
        
        if ( i == 0 ){
            ;
        }
        else {
            ;//tabPane.getSelectionModel().select(0);
            institutionsTab.setDisable(true);
        }
    }
    
}
