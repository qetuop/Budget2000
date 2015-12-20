/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * FXML Controller class
 *
 * @author Brian
 */
public class UserViewController implements Initializable {
    private static final Logger logger = Logger.getGlobal();

    private BudgetData budgetData; // will be set from main controller
    private UserDbAdapter mUserDbAdapter;
    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> FirstNameCol;   // User = data in tableview? String = field in that class?
    @FXML
    private TableColumn<User, String> LastNameCol;

    @FXML
    public TableView<Institution> userInstitutionTableView;
    @FXML
    private TableColumn<Institution, String> InstitutionCol;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("UVC::initialize()");
        FirstNameCol.setCellValueFactory(new PropertyValueFactory<>("FirstName")); // FirstName or firstName work?
        LastNameCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));

//        InstitutionCol.setCellValueFactory(new PropertyValueFactory<>("InstitutionName"));
        mUserDbAdapter = new UserDbAdapter();
        mUserDbAdapter.createConnection();
        userList.setAll(FXCollections.observableArrayList(mUserDbAdapter.getUsers()));

        userTableView.setItems(userList);
       
        //init();

    }

    protected void init() {
        System.out.println("UVC::init()");

        // handle USER table selection events
        userTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (userTableView.getSelectionModel().getSelectedItem() != null) {

                User selectedUser = userTableView.getSelectionModel().getSelectedItem();
                budgetData.setSelectedUser(selectedUser.getId());

                System.out.println("UVC::selected user now = " + selectedUser.getFirstName());

                // link institution view - Right hand side table
//                userInstitutionTableView.setItems(selectedUser.getInstitutionList());
            }
        });

        // done the first time through
        userTableView.getSelectionModel().selectFirst();

    } // init

    @FXML
    protected void addUser(ActionEvent event) {
        System.out.println("UVC::addUser");

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("New User");
        dialog.setHeaderText("<header text>");

        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the first and last labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField firstName = new TextField();
        firstName.setPromptText("Frist Name");
        TextField lastName = new TextField();
        lastName.setPromptText("Last Name");

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstName, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastName, 1, 1);

        // Enable/Disable ok button depending on whether a first name was entered.
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        firstName.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the first field by default.
        Platform.runLater(() -> firstName.requestFocus());

        // Convert the result to a first-last-pair when the ok button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(firstName.getText(), lastName.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        // Add user to data store and set it as table selection
        result.ifPresent(firstLastName -> {
            User newUser = new User(firstLastName.getKey(), firstLastName.getValue());

            int userId = mUserDbAdapter.createUser(newUser);
            newUser.setId(userId);
            userList.add(newUser); // need to add to DB? or have list rebuild from DB?

            userTableView.getSelectionModel().select(newUser);
            //Context.getInstance().setUserId(userId);
            budgetData.setSelectedUser(userId);
        });
    }

    void setBudgetData(BudgetData budgetData) {
        this.budgetData = budgetData;
        init();
    }
    
} // UserViewController
