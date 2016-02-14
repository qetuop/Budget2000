/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
    private static final Logger logger = Logger.getGlobal();
    
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
        logger.info("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO    
    }    
    
    protected void init() {
        logger.info("");
        
        // handle USER selection
        budgetData.addUserPropertyChangeListener( evt -> { tableSelection(evt); } );
        budgetData.addInstitutionPropertyChangeListener(evt -> { tableSelection(evt); } );
        budgetData.addAccountPropertyChangeListener(evt -> { tableSelection(evt); } );
        budgetData.addTransactionPropertyChangeListener(evt -> { tableSelection(evt); } );
        
        userSelected(0);
        institutionSelected(0);
        accountSelected(0);
        transactionSelected(0);
    }
    
    @FXML
    protected void fileSaveSelected(Event event) {
        logger.info("");        
        mainApp.save();
    }
    
    @FXML
    protected void fileOpenSelected(Event event) {
        logger.info("");        
        mainApp.load();
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
    
    private void tableSelection(PropertyChangeEvent evt) {        
        Integer i = (Integer)evt.getNewValue(); 
        String prop = evt.getPropertyName();
        
        switch (prop) {
            case BudgetData.USER_SELECTION: {
                userSelected(i);
                break;
            }
            case BudgetData.INSTITUTION_SELECTION: {
                institutionSelected(i);
                break;
            }
            case BudgetData.ACCOUNT_SELECTION: {
                accountSelected(i);
                break;
            }
            case BudgetData.TRANSACTION_SELECTION: {
                transactionSelected(i);
                break;                
            }
        }
    } // tableSelection

    private void userSelected(Integer i) {        
       
        if ( i == 0 ) {
            institutionsTab.setDisable(true);
        }
        else {
            institutionsTab.setDisable(false);
        }
        
        // propagate selection
        //institutionSelected(i);
    }

    private void institutionSelected(Integer i) { 
        
        // TODO: how to gray out tab
        
        if ( i == 0 ) {
            accountsTab.setDisable(true);;
        }
        else {
            accountsTab.setDisable(false);
        }
        
        // propagate selection
        //institutionSelected(i);
    }

    private void accountSelected(Integer i) { 
       
        // TODO: how to gray out tab
        
        if ( i == 0 ) {
            transactionsTab.setDisable(true);;
        }
        else {
            transactionsTab.setDisable(false);
        }
        
        // propagate selection
        //institutionSelected(i);
    }
    
    private void transactionSelected(Integer i) { 
        //Integer i = (Integer)evt.getNewValue();    
        // TODO: how to gray out tab              
    }
    
}
