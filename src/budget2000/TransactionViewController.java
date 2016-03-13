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
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
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
    private TableColumn<TransactionWrapper, Number> TransactionDateCol;
    @FXML
    private TableColumn<TransactionWrapper, Number> TransactionAmountCol;
    // TODO: What do i use here
    @FXML
    //private TableColumn<String[], String> TransactionTagCol;
    private TableColumn<TransactionWrapper, String> TransactionTagCol;

    final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");

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

        TransactionDateCol.setCellValueFactory((TableColumn.CellDataFeatures<TransactionWrapper, Number> p)
                -> (ObservableValue<Number>) p.getValue().getTransaction().getDateProperty());

        TransactionAmountCol.setCellValueFactory((TableColumn.CellDataFeatures<TransactionWrapper, Number> p)
                -> (ObservableValue<Number>) p.getValue().getTransaction().getAmmountProperty());

        TransactionTagCol.setCellValueFactory(new PropertyValueFactory<TransactionWrapper, String>("tagList"));

//        TransactionDateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
//        TransactionNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
//        TransactionDisplayNameCol.setCellValueFactory(new PropertyValueFactory<>("DisplayName"));
//        TransactionAmountCol.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        transactionTableView.setItems(transactionWrapperList);

        // propagate transactions selections
        transactionTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            tableSelection(newValue.getTransaction());
        });
//
//        // Custom rendering of the table cell.
//        TransactionDateCol.setCellFactory(column -> {
//            return new TableCell<Transaction, Long>() {
//                @Override
//                protected void updateItem(Long item, boolean empty) {
//                    super.updateItem(item, empty);
//
//                    if (item == null || empty) {
//                        setText(null);
//                        setStyle("");
//                    } else {
//                        // Format date.
//                        setText(dateFormat.format(LocalDate.ofEpochDay(item)));
//                    }
//                }
//            };
//        });

//        TransactionTagCol.setCellFactory(column -> {
//            return new TableCell<Tag, String>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//
//                    if (item == null || empty) {
//                        System.out.println("whaa");
//                        
//                    } else {
//                        System.out.println("huh");
//                    }
//                }
//            };
//        });
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

                    t.setAccountId(budgetData.getSelectedAccount());
                    int transactionId = mTransactionDbAdapter.createTransaction(t);
                    t.setId(transactionId);

                    TransactionWrapper tw = new TransactionWrapper();
                    tw.setTransaction(t);
                    tw.setTags(FXCollections.observableArrayList());

                    transactionWrapperList.add(tw); // need to add to DB? or have list rebuild from DB?

                    //transactionTableView.getSelectionModel().select(t);
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

        TransactionWrapper tw = transactionTableView.getSelectionModel().getSelectedItem();
        Transaction transaction = tw.getTransaction();

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
        Optional<Pair<Transaction, ArrayList<String>>> result = dialog.showAndWait();

        // Add to data store and set it as table selection
        result.ifPresent(resultPair -> {
            logger.info("newTrans " + resultPair);

            Transaction newTransaction = resultPair.getKey();
            ArrayList<String> tags = resultPair.getValue();

            newTransaction.setAccountId(budgetData.getSelectedAccount());

            // need to check for success and handle failure
            int transactionId = mTransactionDbAdapter.createTransaction(newTransaction);

            createTags(newTransaction, tags);

            TransactionWrapper tw = new TransactionWrapper();
            tw.setTransaction(newTransaction);
            tw.setTags(FXCollections.observableArrayList(tags));

            transactionWrapperList.add(tw);; // need to add to DB? or have list rebuild from DB?

            transactionTableView.getSelectionModel().select(tw);
        });

    } // addTransaction

    private void createTags(Transaction transaction, ArrayList<String> tags) {

        for (String stringTag : tags) {
            Tag tag = new Tag(stringTag);
            tagDbAdapter.createTag(tag); // verfiy doesn't already exist or will DB not accept dupes?

            TransactionTag tt = new TransactionTag(transaction.getId(), tag.getId());
            ttDbAdapter.createTransactionTag(tt);
        }
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

        // get transactions
        ArrayList<Transaction> transactions = mTransactionDbAdapter.getAllForAccount(accountId);

        for (Transaction t : transactions) {

            // get TransactionTags
            ArrayList<TransactionTag> ttList = ttDbAdapter.getAllForTransaction(t.getId());

            // get Tags
            ArrayList<Tag> tags = new ArrayList<>();
            
            for (TransactionTag tt : ttList) {

                tags.add(tagDbAdapter.getTag(tt.getTagId()));
                
                // merge together
                TransactionWrapper tw = new TransactionWrapper();
                tw.setTransaction(t);
                tw.setTags(FXCollections.observableArrayList(tags));

                transactionWrapperList.add(tw);
            }
        }

        transactionTableView.getSelectionModel().selectFirst();
    }

} // TransactionViewController
