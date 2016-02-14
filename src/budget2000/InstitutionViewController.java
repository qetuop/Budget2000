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
        logger.info("");

        InstitutionNameCol.setCellValueFactory(new PropertyValueFactory<>("InstitutionName"));// must be ?similar? to POJO field
        AccountCol.setCellValueFactory(new PropertyValueFactory<>("AccountName")); // must be ?similar? to POJO field

    }

    void setBudgetData(BudgetData budgetData) {
        this.budgetData = budgetData;
        init();
    }

    private void init() {
        logger.info("");

        mInstitutionDbAdapter = new InstitutionDbAdapter();
        mInstitutionDbAdapter.createConnection();
        mInstitutionDbAdapter.createDatabase();

        institutionTableView.setItems(institutionList);

        // set the table up with initial data
        //setTable();
        // handle USER selection (from other tab) - set the institution list to this user's list
        budgetData.addUserPropertyChangeListener(evt -> {
            userSelected(evt);
        });

        // handle INSTITUTION table selection events
        institutionTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (institutionTableView.getSelectionModel().getSelectedItem() != null) {

                Institution selectedInstitution = institutionTableView.getSelectionModel().getSelectedItem();
                budgetData.setSelectedInstitution(selectedInstitution.getId());

                // link institution view - Right hand side table - future growth
                //institutionAccountTableView.setItems(selectedInstitution.getAccountList());
            }
        });

        // query all DB items into the list and set the Tableview to this list, select first item
        update();
        
    } // init

    @FXML
    protected void addInstitution(ActionEvent event) {
        logger.info("");

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
            institution.setUserId(budgetData.getSelectedUser());

            int instutitionId = mInstitutionDbAdapter.createInstitution(institution);
            institution.setId(instutitionId);
            institutionList.add(institution); // need to add to DB? or have list rebuild from DB?

            institutionTableView.getSelectionModel().select(institution);
        });

    } // addInstitution

    private void update() {
        Integer userId = budgetData.getSelectedUser();

        institutionList.setAll(FXCollections.observableArrayList(mInstitutionDbAdapter.getAllForUser(userId)));
        //institutionTableView.setItems(institutionList);

        institutionTableView.getSelectionModel().selectFirst();
    }

    private void userSelected(PropertyChangeEvent evt) {
        Integer i = (Integer) evt.getNewValue();

        logger.info("i = " + i);

        update();
    }

} // InstitutionViewController
