/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

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
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

/**
 * FXML Controller class
 *
 * @author Brian
 */
class AccountDialog extends ChoiceDialog {

    AccountDialog(String title, List<String> choices) {

        //ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        setTitle(title);
        //setHeaderText("Look, a Choice Dialog");
        setContentText("Choose your Account:");
        getItems().addAll(choices);

        // TODO: how to handle this, gray OK button?
        if (choices.isEmpty()) {
            ;
        } else {
            setSelectedItem(choices.get(0));
        }
    }
}

public class AccountViewController implements Initializable {

    private static final Logger logger = Logger.getGlobal();

    private BudgetData budgetData;
    private AccountDbAdapter mAccountDbAdapter;
    private ObservableList<Account> accountList = FXCollections.observableArrayList();

    @FXML
    private TableView<Account> accountTableView;
    @FXML
    private TableColumn<Transaction, String> AccountNameCol;

    // right side table/col
    @FXML
    public TableView<Transaction> accountTransactionTableView;
    @FXML
    private TableColumn<Transaction, String> TransactionCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("AVC::initialize()");
        AccountNameCol.setCellValueFactory(new PropertyValueFactory<>("AccountName"));

        TransactionCol.setCellValueFactory(new PropertyValueFactory<>("TransactionName"));

        mAccountDbAdapter = new AccountDbAdapter();
        mAccountDbAdapter.createConnection();
        accountList.setAll(FXCollections.observableArrayList(mAccountDbAdapter.getAll()));

        accountTableView.setItems(accountList);

        //init();
    }

    private void init() {
        System.out.println("AVC::init()");

        // set the table up with initial data
        //setTable();
        // handle INSTITUTION selection (from other tab) - set the institution list to this user's list
//        budgetData.addInstitutionPropertyChangeListener(evt -> {
//            setTable();
//        });
        // propagate account selections
        accountTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (accountTableView.getSelectionModel().getSelectedItem() != null) {

                Account selectedAccount = accountTableView.getSelectionModel().getSelectedItem();
                budgetData.setSelectedAccount(selectedAccount.getId());

                // link institution view - Right hand side table
                //accountTransactionTableView.setItems(selectedAccount.getTransactionList());
            }
        });

        // done the first time through
        accountTableView.getSelectionModel().selectFirst();

    } // init

    @FXML
    protected void addAccount(ActionEvent event) {
        System.out.println("AVC::addAccount()");

        // TODO - move this somewhere - DB table or simple config file?
        // query current institution here and pass in list?
        List<String> choices = new ArrayList<>();
        choices.add("Savings");
        choices.add("Checking");
        choices.add("Credit Card");
        choices.add("Stock");

        AccountDialog dialog = new AccountDialog("Add Account", choices);

        // The Java 8 way to get the response value (with lambda expression).
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(accountName -> {
            System.out.println("Your choice: " + accountName);

            Account account = new Account();
            account.setAccountName(accountName);
            account.setInstitutionId(budgetData.getSelectedInstitution());

            int accountId = mAccountDbAdapter.createAccount(account);
            account.setId(accountId);
            accountList.add(account); // need to add to DB? or have list rebuild from DB?

            accountTableView.getSelectionModel().select(account);
        });

    } // addAccount

    @FXML
    protected void deleteAccount(ActionEvent event) {
        logger.info("");

        // TODO - this will need to be queried from this users current Institutions
        //or the currelty selected account
        Account selectedAccount = accountTableView.getSelectionModel().getSelectedItem();

        List<String> choices = new ArrayList<>();
        choices.add(selectedAccount.getAccountName());

        AccountDialog dialog = new AccountDialog("Delete Account", choices);
        dialog.setHeaderText("Are you sure?");

        // The Java 8 way to get the response value (with lambda expression).
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(accountName -> {
            System.out.println("Your choice: " + accountName);

            mAccountDbAdapter.delete(selectedAccount.getId());
            update();
        });

    } // addAccount

    private void update() {
        accountList.setAll(FXCollections.observableArrayList(mAccountDbAdapter.getAll()));
        accountTableView.setItems(accountList);
    }

    void setBudgetData(BudgetData budgetData) {
        this.budgetData = budgetData;
        init();
    }

//    private void setTable() {
//        Institution institution = budgetData.getSelectedInstitution();
//
//        if (institution != null) {
//            ObservableList<Account> accountList = institution.getAccountList();
//            accountTableView.setItems(accountList);
//
//            // link institution view - Right hand side table
//            //accountTransactionTableView.setItems(selectedAccount.getTransactionList());
//        }
//    }
} // AccountViewController
