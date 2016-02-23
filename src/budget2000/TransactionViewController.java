/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * FXML Controller class
 *
 * @author Brian
 */
public class TransactionViewController implements Initializable {

    private static final Logger logger = Logger.getGlobal();

    private BudgetData budgetData;
    private TransactionDbAdapter mTransactionDbAdapter;
    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    @FXML
    private TableView<Transaction> transactionTableView;
    @FXML
    private TableColumn<Transaction, String> TransactionNameCol;
    @FXML
    private TableColumn<Transaction, String> TransactionDisplayNameCol;
    @FXML
    private TableColumn<Transaction, Long> TransactionDateCol;
    @FXML
    private TableColumn<Transaction, Double> TransactionAmountCol;
    
    // TODO: What do i use here
    @FXML
    private TableColumn<Tag, String> TransactionTagCol;

    final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logger.info("");

        TransactionDateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
        TransactionNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        TransactionDisplayNameCol.setCellValueFactory(new PropertyValueFactory<>("DisplayName"));
        TransactionAmountCol.setCellValueFactory(new PropertyValueFactory<>("Amount"));

        transactionTableView.setItems(transactionList);

        // propagate transactions selections
        transactionTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            tableSelection(newValue);
        });

        // Custom rendering of the table cell.
        TransactionDateCol.setCellFactory(column -> {
            return new TableCell<Transaction, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format date.
                        setText(dateFormat.format(LocalDate.ofEpochDay(item)));

//                        // Style all dates in March with a different color.
//                        if (item.getMonth() == Month.MARCH) {
//                            setTextFill(Color.CHOCOLATE);
//                            setStyle("-fx-background-color: yellow");
//                        } else {
//                            setTextFill(Color.BLACK);
//                            setStyle("");
//                        }
                    }
                }
            };
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
        mTransactionDbAdapter.createConnection();
        mTransactionDbAdapter.createDatabase();

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
                    
                    t.setAccountId(budgetData.getSelectedAccount());
                    int transactionId = mTransactionDbAdapter.createTransaction(t);
                    t.setId(transactionId);
                    
                    transactionList.add(t); // need to add to DB? or have list rebuild from DB?

                    transactionTableView.getSelectionModel().select(t);

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
    protected void editTransaction(ActionEvent event) {
        logger.info("");

        Transaction transaction = transactionTableView.getSelectionModel().getSelectedItem();

        TransactionDialog dialog = new TransactionDialog(transaction);
        dialog.setTitle("Edit Transaction");
        dialog.setHeaderText("<header text>");

        Optional<Transaction> result = dialog.showAndWait();

        // Add to data store and set it as table selection
        result.ifPresent(newTransaction -> {
            logger.info("edit Trans " + newTransaction);

//            newTransaction.setAccountId(budgetData.getSelectedAccount());
//
//            int transactionId = mTransactionDbAdapter.createTransaction(newTransaction);
//            newTransaction.setId(transactionId);
//            transactionList.add(newTransaction); // need to add to DB? or have list rebuild from DB?
//
//            transactionTableView.getSelectionModel().select(newTransaction);
        });
    }

    @FXML
    protected void addTransaction(ActionEvent event) {
        logger.info("");

        TransactionDialog dialog = new TransactionDialog();
        dialog.setTitle("New Transaction");
        dialog.setHeaderText("<header text>");

        //Optional<Pair<String, String>> result = dialog.showAndWait();
        //Optional<Pair<Transaction,ArrayList<Tag>>> result = dialog.showAndWait();
        Optional<Pair<Transaction,ArrayList<String>>> result = dialog.showAndWait();

        // Add to data store and set it as table selection
        result.ifPresent(resultPair -> {
            logger.info("newTrans " + resultPair);
 System.out.println("transaction=" + resultPair.getKey() + ", tag=" + resultPair.getValue());
 
            Transaction newTransaction = resultPair.getKey();
            ArrayList<String> tags = resultPair.getValue();
            
            newTransaction.setAccountId(budgetData.getSelectedAccount());

            int transactionId = mTransactionDbAdapter.createTransaction(newTransaction);
            newTransaction.setId(transactionId);
            
            createTags(newTransaction, tags);
            
            transactionList.add(newTransaction); // need to add to DB? or have list rebuild from DB?

            transactionTableView.getSelectionModel().select(newTransaction);
        });

    } // addTransaction
    
    private void createTags(Transaction transaction, ArrayList<String> tags){
        TransactionTagDbAdapter ttDbAdapter = new TransactionTagDbAdapter();
        ttDbAdapter.createConnection();
        ttDbAdapter.createDatabase();
        
        TagDbAdapter tagDbAdapter = new TagDbAdapter();
        tagDbAdapter.createConnection();
        tagDbAdapter.createDatabase();
                
        for (String stringTag : tags) {
           Tag tag = new Tag(stringTag);
           
           // verfiy doesn't already exist or will DB not accept dupes?
           
           int tagId = tagDbAdapter.createTag(tag);
           
           TransactionTag tt = new TransactionTag(transaction.getId(), tagId);
           
           int ttId = ttDbAdapter.createTransactionTag(tt);
        }
    }

    private void update() {
        Integer accountId = budgetData.getSelectedAccount();

        transactionList.setAll(FXCollections.observableArrayList(mTransactionDbAdapter.getAllForAccount(accountId)));
        transactionTableView.getSelectionModel().selectFirst();
    }

//    private void accountSelected(PropertyChangeEvent evt) {
//        Integer i = (Integer) evt.getNewValue();
//
//        logger.info("i = " + i);
//
//        update();
//    }
} // TransactionViewController
