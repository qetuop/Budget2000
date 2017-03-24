/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import java.io.File;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.controlsfx.control.CheckComboBox;

/**
 * FXML Controller class
 *
 * @author Brian
 */
public class TransactionViewController implements Initializable {

    private static final Logger logger = Logger.getGlobal();

    private BudgetData budgetData;

    private TransactionDbAdapter mTransactionDbAdapter;
    private TransactionTagDbAdapter ttDbAdapter;
    private TagDbAdapter tagDbAdapter;

    private ObservableList<TransactionWrapper> transactionWrapperList = FXCollections.observableArrayList();

    @FXML
    private TableView<TransactionWrapper> transactionTableView;
    @FXML
    private TableColumn<TransactionWrapper, String> TransactionNameCol;
    @FXML
    private TableColumn<TransactionWrapper, String> TransactionDisplayNameCol;
    @FXML
    private TableColumn<TransactionWrapper, String> TransactionDateCol;
    @FXML
    private TableColumn<TransactionWrapper, String> TransactionAmountCol;
    // TODO: What do i use here
    @FXML
    //private TableColumn<String[], String> TransactionTagCol;
    private TableColumn<TransactionWrapper, String> TransactionTagCol;
    
    @FXML
    private TextField transactionFilterSearch;
    @FXML
    private CheckComboBox transactionTypeCombo;
    @FXML
    private ChoiceBox transactionRangeChoice;

    final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    final ObservableList dateRangeList = FXCollections.observableArrayList(
            "30 Days", "60 Days", "90 Days", "Custom Range");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logger.info("");

        TransactionNameCol.setCellValueFactory((TableColumn.CellDataFeatures<TransactionWrapper, String> p)
                -> (ObservableValue<String>) p.getValue().getTransaction().getNameProperty());

        TransactionDisplayNameCol.setCellValueFactory((TableColumn.CellDataFeatures<TransactionWrapper, String> p)
                -> (ObservableValue<String>) p.getValue().getTransaction().getDisplayNameProperty());

//        TransactionAmountCol.setCellValueFactory((TableColumn.CellDataFeatures<TransactionWrapper, Number> p)
//                -> (ObservableValue<Number>) p.getValue().getTransaction().getAmmountProperty());
        TransactionAmountCol.setCellValueFactory((TableColumn.CellDataFeatures<TransactionWrapper, String> p)
                -> {
            SimpleStringProperty property = new SimpleStringProperty();
            NumberFormat numberFormat = NumberFormat.getInstance();
            property.setValue(numberFormat.format(p.getValue().getTransaction().getAmmountProperty().doubleValue()));
            return property;
        });

        TransactionTagCol.setCellValueFactory(new PropertyValueFactory<TransactionWrapper, String>("tagList"));

        // TODO: please make this right
        TransactionDateCol.setCellValueFactory((TableColumn.CellDataFeatures<TransactionWrapper, String> p)
                -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(dateFormat.format(LocalDate.ofEpochDay(p.getValue().getTransaction().getDateProperty().longValue())));
            return property;
        });

//        // Custom rendering of the table cell.
//        TransactionDateCol.setCellFactory(column -> {
//            return new TableCell<TransactionWrapper, Number>() {
//                @Override
//                protected void updateItem(Number item, boolean empty) {
//                    super.updateItem(item, empty);
//
//                    if (item == null || empty) {
//                        setText(null);
//                        setStyle("");
//                    } else {
//                        // Format date.
//                        setText(dateFormat.format(LocalDate.ofEpochDay(item.longValue())));
//                    }
//                }
//            };
//        });
        transactionTableView.setItems(transactionWrapperList);

        // propagate transactions selections
        transactionTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if ( newValue != null ) {
                tableSelection(newValue.getTransaction());
            }
        });
        
        transactionRangeChoice.setItems(dateRangeList);
        transactionRangeChoice.getSelectionModel().selectFirst();
        transactionRangeChoice.setTooltip(new Tooltip("Select the range to display"));
        transactionRangeChoice.getSelectionModel().selectedIndexProperty().addListener( 
            (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                System.out.println("range = " + dateRangeList.get(newValue.intValue()));
        });


    } // initialize

    void setBudgetData(BudgetData budgetData) {
        this.budgetData = budgetData;

        // handle ACCOUNT selection (from other tab) - set the institution list to this user's list
        budgetData.addAccountPropertyChangeListener(evt -> {
            update();
        });

        init();
    }

    private void init() {
        logger.info("");

        mTransactionDbAdapter = new TransactionDbAdapter();
        ttDbAdapter = new TransactionTagDbAdapter();
        tagDbAdapter = new TagDbAdapter();

        // query all DB items into the list and set the Tableview to this list, select first item
        update();

    } // init

    private void tableSelection(Transaction selectedTransaction) {;
        logger.info("selected Transaction = " + selectedTransaction);
        budgetData.setSelectedTransaction(selectedTransaction);
    }

    @FXML
    protected void contextMenuRequested() {
        logger.info("");
    }

    @FXML
    protected void importTransaction(ActionEvent event) {
        logger.info("");
        ClassLoader cl = this.getClass().getClassLoader();
        this.getClass().getResource("sample.csv");

        Stage stage = (Stage) transactionTableView.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
//        fileChooser.setInitialDirectory( new File(System.getProperty("user.home")) ); 
        //fileChooser.setInitialDirectory(new File("C:\\Users\\Brian\\Documents\\NetBeansProjects\\Budget"));
        fileChooser.setInitialDirectory(new File("."));

        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Text CSV", "*.csv", "*.CSV"),
                new ExtensionFilter("All Files", "*.*"));

        List<File> list = fileChooser.showOpenMultipleDialog(stage);
        if (list != null) {
            for (File file : list) {
                Importer i = new Importer();

                ArrayList<Transaction> newTransList = i.readData(file);
                newTransList.stream().forEach((t) -> {

                    // see if this transaction already exists (account, name, date, amount)
                    // for this account
                    t.setAccountId(budgetData.getSelectedAccount());
                    //System.out.println(t + " exists = " + mTransactionDbAdapter.exists(t) );
                    if ( mTransactionDbAdapter.exists(t) == false ) {
                    
                        t.setAccountId(budgetData.getSelectedAccount());
                        int transactionId = mTransactionDbAdapter.createTransaction(t);
                        t.setId(transactionId);

                        TransactionWrapper tw = new TransactionWrapper();
                        tw.setTransaction(t);
                        tw.setTags(FXCollections.observableArrayList());

                        transactionWrapperList.add(tw); // need to add to DB? or have list rebuild from DB?

                        //transactionTableView.getSelectionModel().select(t);
                    }
                });
            }
        }

//        File file = fileChooser.showOpenDialog(stage);
//        if (file != null) {
//            Importer i = new Importer();
//            ArrayList<Transaction> transactionList;
//            transactionList = i.readData(file);
//            transactionList.stream().forEach((t) -> {
//                budgetData.getSelectedAccount().addTransaction(t);
//            });
//        }
    }

     @FXML
    protected void onFilterApply(ActionEvent event) {
        logger.info("");
    }
    
    @FXML
    protected void editTransaction(ActionEvent event) {
        logger.info("");

        TransactionWrapper tw = transactionTableView.getSelectionModel().getSelectedItem();
        if ( tw == null ) {
            return;
        }
        
        Transaction selectedTransaction = tw.getTransaction();

        // This should *NOT* modify the selectedTransaction
        TransactionDialog dialog = new TransactionDialog(selectedTransaction);
        dialog.setTitle("Edit Transaction");
        dialog.setHeaderText("<header text>");

        Optional<Pair<Transaction, ArrayList<String>>> result = dialog.showAndWait();

        // Add to data store and set it as table selection
        result.ifPresent(resultPair -> {
            logger.info("Edit Transaction: " + resultPair);

            Transaction editTransaction = resultPair.getKey();
            ArrayList<String> stringTags = resultPair.getValue();

            // TODO: if Display name or tag changed - prompt user to update 
            // similar transactions (same name) if yes do them as well
            Transaction origTrans = tw.getTransaction();            
            List<String> origTagList = tw.getTagList();

             // tw modified in function
            updateTransactionWrapper(tw, editTransaction, stringTags);
            
            transactionTableView.getSelectionModel().select(tw);
                        
            // compare origTw values with new ones
            // If they displayName is different, then all other similar items need
            // to update their displayName
            if ( editTransaction.getDisplayName().equals(origTrans.getDisplayName()) == false ) {

                // get list of all transactions with same Name
                ArrayList<Transaction> matched = mTransactionDbAdapter.getAllForName(origTrans.getName());

                // TODO can i do a DB update and do all at once instead of indivudialy

                // does this Name already have an existing mapping?

                // prompt user to update - TODO display listing of items
                 
                // update
                for ( Transaction updateTrans : matched ) {
                    updateTrans.setDisplayName(editTransaction.getDisplayName());
                    mTransactionDbAdapter.updateTransaction(updateTrans);
                }

                // add mapping to TBD table - TODO: what?!?
                // so "GIANT 0196 GAITHERSBURG MD" maps to "Giant" and all future
                // additions of "GIANT 0196 GAITHERSBURG MD" would automaticaly get
                // "Giant" --> Enhacment #29
            }
            
            // update Tags if changed - TODO: combine with Name check
            if ( origTagList.equals(tw.getTagList()) == false ) {
                // get the correct TW for this transaction                    
                for ( TransactionWrapper transWrap : transactionWrapperList ) {
                    if ( transWrap.getTransaction().getName().equals(editTransaction.getName()) ) {
                        updateTransactionTags(transWrap, transWrap.getTransaction(), stringTags);
                    }
                }
            }

            // update the table
            update();
        });
    } // editTransaction

    // tw = original transWrapper (trans + tags)
    // transaction = updated transaction info
    // stringTags = whatever came out of the edit dialog
    private void updateTransactionWrapper(TransactionWrapper tw, Transaction transaction, ArrayList<String> stringTags) {
        // need to check for success and handle failure
        mTransactionDbAdapter.updateTransaction(transaction);
        
        // set the new values
        tw.setTransaction(transaction);

        // must call updateTransactionTags - check for changes
        updateTransactionTags(tw, transaction, stringTags);

    }

    private void updateTransactionTags(TransactionWrapper tw, Transaction transaction, ArrayList<String> stringTags) {
        logger.info("");

        // TODO: do comparisons and shit
        for (Object o : tw.getTags()) {
            Tag tag = (Tag) o;

            // CASCADE will delete from TT table
            tagDbAdapter.delete(tag.getId());
        }

        ArrayList<Tag> tags = new ArrayList<>();

        for (String stringTag : stringTags) {
            Tag tag = new Tag(stringTag);
            tagDbAdapter.createTag(tag); // verfiy doesn't already exist or will DB not accept dupes?

            tags.add(tag);

            TransactionTag tt = new TransactionTag(transaction.getId(), tag.getId());
            ttDbAdapter.createTransactionTag(tt);
        }

        tw.setTags(FXCollections.observableArrayList(tags));
    }

    @FXML
    protected void addTransaction(ActionEvent event) {
        logger.info("");

        TransactionDialog dialog = new TransactionDialog();
        dialog.setTitle("New Transaction");
        dialog.setHeaderText("<header text>");

        Optional<Pair<Transaction, ArrayList<String>>> result = dialog.showAndWait();

        // Add to data store and set it as table selection
        result.ifPresent(resultPair -> {
            logger.info("New Transaction: " + resultPair);

            Transaction newTransaction = resultPair.getKey();
            ArrayList<String> stringTags = resultPair.getValue();

            newTransaction.setAccountId(budgetData.getSelectedAccount());
            TransactionWrapper tw = createTransactionWrapper(newTransaction, stringTags);

            transactionWrapperList.add(tw);
            transactionTableView.getSelectionModel().select(tw);
        });

    } // addTransaction

    private TransactionWrapper createTransactionWrapper(Transaction transaction, ArrayList<String> stringTags) {
        logger.info("");

        // need to check for success and handle failure
        mTransactionDbAdapter.createTransaction(transaction);

        ArrayList<Tag> tags = createTransactionTags(transaction, stringTags);
        TransactionWrapper tw = new TransactionWrapper(transaction, tags);

        return tw;
    }

    /**
     * This will take a Transaction object and list of strings and create
     * TransactionTag entries and return a list of Tag objects
     *
     * @param transaction
     * @param stringTags
     * @return
     */
    private ArrayList<Tag> createTransactionTags(Transaction transaction, ArrayList<String> stringTags) {
        logger.info("");

        ArrayList<Tag> tags = new ArrayList<>();

        for (String stringTag : stringTags) {
            Tag tag = new Tag(stringTag);
            tagDbAdapter.createTag(tag); // verfiy doesn't already exist or will DB not accept dupes?
            tags.add(tag);
            System.out.println("ttDbAdapter.conn " + ttDbAdapter.conn);
            TransactionTag tt = new TransactionTag(transaction.getId(), tag.getId());
            ttDbAdapter.createTransactionTag(tt);
        }

        return tags;
    }

//    public ArrayList<Tag> getTags(Integer transactionId) {
//        //Integer transactionId = budgetData.getSelectedTransaction();
//        TagDbAdapter tagDbAdapter = new TagDbAdapter();
//
//        ArrayList<Tag> tags = tagDbAdapter.getAll();
//        return tags;
//    }
    private void update() {
        Integer accountId = budgetData.getSelectedAccount();   
        
        // need to create a tmp list to set the actual one to, TODO: is there another way?
        ObservableList<TransactionWrapper>  tmpTransactionWrapperList = FXCollections.observableArrayList();
        
        // get transactions
        ArrayList<Transaction> transactions = mTransactionDbAdapter.getAllForAccount(accountId);

        for (Transaction transaction : transactions) {

            // get TransactionTags
            ArrayList<TransactionTag> ttList = ttDbAdapter.getAllForTransaction(transaction.getId());

            // get Tags
            ArrayList<Tag> tags = new ArrayList<>();

            // Tags
            for (TransactionTag tt : ttList) {
                tags.add(tagDbAdapter.getTag(tt.getTagId()));
            }

            TransactionWrapper tw = new TransactionWrapper();
            tw.setTransaction(transaction);
            tw.setTags(FXCollections.observableArrayList(tags));
            tmpTransactionWrapperList.add(tw);
        }
        
        // This will maintain the sorting order
        transactionTableView.getItems().clear();
        transactionTableView.getItems().addAll(tmpTransactionWrapperList);
        transactionTableView.sort();
  
        //transactionWrapperList.setAll(tmpTransactionWrapperList);
        //transactionTableView.getSelectionModel().selectFirst();
    }

} // TransactionViewController
