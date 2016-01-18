/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Brian
 */
public class InstitutionViewController implements Initializable {

    private static Logger logger = Logger.getGlobal();

    private BudgetData budgetData; // will be set from main controller
    private InstitutionDbAdapter mInstitutionDbAdapter;
    private ObservableList<Institution> institutionList = FXCollections.observableArrayList();
    
    // left side table/col
    @FXML
    private TableView<Institution> institutionTableView;
    @FXML
    private TableColumn<Institution, String> InstitutionNameCol;

    // right side table/col
    @FXML
    public TableView<Account> institutionAccountTableView;
    @FXML
    private TableColumn<Account, String> AccountCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("IVC::initialize()");
        
        InstitutionNameCol.setCellValueFactory(new PropertyValueFactory<>("InstitutionName"));// must be ?similar? to POJO field
        AccountCol.setCellValueFactory(new PropertyValueFactory<>("AccountName")); // must be ?similar? to POJO field

        mInstitutionDbAdapter = new InstitutionDbAdapter();
        mInstitutionDbAdapter.createConnection();
        mInstitutionDbAdapter.createDatabase();

        institutionTableView.setItems(institutionList);
    }

    private void init() {
        System.out.println("IVC::init()");

        // set the table up with initial data
        //setTable();
        // handle USER selection (from other tab) - set the institution list to this user's list
        budgetData.addUserPropertyChangeListener(evt -> { userSelected(evt); });

        // handle INSTITUTION table selection events
        institutionTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (institutionTableView.getSelectionModel().getSelectedItem() != null) {

                Institution selectedInstitution = institutionTableView.getSelectionModel().getSelectedItem();
                budgetData.setSelectedInstitution(selectedInstitution.getId());

                // link institution view - Right hand side table - future growth
                //institutionAccountTableView.setItems(selectedInstitution.getAccountList());
            }
        });

        // done the first time through
        institutionTableView.getSelectionModel().selectFirst();

    } // init

    @FXML
    protected void addInstitution(ActionEvent event) {
        System.out.println("IVC::addInstitution()");

        // TODO - move this somewhere
        List<String> choices = new ArrayList<>();
        choices.add("Navy Federal");
        choices.add("Vanguard");

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Add Institution");
        dialog.setHeaderText("Look, a Choice Dialog");
        dialog.setContentText("Choose your Institution:");

        // The Java 8 way to get the response value (with lambda expression).
        Optional<String> result = dialog.showAndWait();

        // Add institution to data store and set it as table selection
        result.ifPresent(institutionName -> {
            Institution institution = new Institution();
            institution.setInstitutionName(institutionName);

            int instutitionId = mInstitutionDbAdapter.createInstitution(institution);
            institution.setId(instutitionId);
            institutionList.add(institution); // need to add to DB? or have list rebuild from DB?

            //institutionData.add(institution);
            //budgetData.getSelectedUser().addInstitution(institution);
            institutionTableView.getSelectionModel().select(institution);
            //Context.getInstance().setInstitutionId(instutitionId);

        });

    } // addInstitution

    void setBudgetData(BudgetData budgetData) {
        this.budgetData = budgetData;
        init();
    }

//    private void setTable() {
//        Integer userId = budgetData.getSelectedUser();
//        User user = new User();
//        
//        if (user != null) {
//            ObservableList<Institution> institutionList = user.getInstitutionList();
//            institutionTableView.setItems(institutionList);
//
//            // link institution view - Right hand side table
//            //institutionAccountTableView.setItems(userData.getSelectedUser().getInstitutionData().getAccountData().getAccountList());
//        }
//    }
    private void userSelected(PropertyChangeEvent evt) {
        Integer i = (Integer) evt.getNewValue();

         //logger.info("i = " + i);     
    }

} // InstitutionViewController
