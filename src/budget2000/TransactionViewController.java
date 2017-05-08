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
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
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
    
    // ------- Filter Panel ----------
    // TODO: replace with SET?  don't have to check for existing items? -->combobox wants a list
    private ObservableList<String> filterTags = FXCollections.observableArrayList("ALL");
    
    @FXML
    private TextField transactionFilterSearch;
    
    //@FXML
    private CheckComboBox transactionTypeCombo;  // TODO this currently won't bind!
    
    @FXML
    private ChoiceBox transactionRangeChoice;
    
    @FXML
    private FlowPane filterTopFlow;

    final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    
    final ObservableList dateRangeList = FXCollections.observableArrayList(
            "30 Days", "60 Days", "90 Days", "Custom Range");    
    
    @FXML
    private DatePicker pickerStart;
    @FXML
    private DatePicker pickerEnd;
    // ------- Filter Panel ----------
    
    
    // ------- Details Panel ----------
    private ObservableList<TransactionDetailWrapper> transactionDetailWrapperList = FXCollections.observableArrayList();
    
    @FXML
    private TableView<TransactionDetailWrapper> transactionDetailTableView;
    
    @FXML
    private TableColumn<TransactionDetailWrapper, String> transactionDetailTagCol;
    
    @FXML
    private TableColumn<TransactionDetailWrapper, String> transactionDetailAmountCol;
    
    // ------- Details Panel ----------
    
    

    public TransactionViewController() {
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logger.info("");
        
        // ------- Details Pane ----------
       
        transactionDetailTagCol.setCellValueFactory(new PropertyValueFactory<>("tag"));
        //transactionDetailAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        // OR THIS?
        transactionDetailAmountCol.setCellValueFactory((TableColumn.CellDataFeatures<TransactionDetailWrapper, String> p)
                -> {
            SimpleStringProperty property = new SimpleStringProperty();
            NumberFormat numberFormat = NumberFormat.getInstance();
            property.setValue(numberFormat.format(p.getValue().getAmmountProperty().doubleValue()));
            return property;
        });
        
        transactionDetailTableView.setItems(transactionDetailWrapperList);       
        // ------- Details Pane ----------

        
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

        // ------- Filter Panel ----------
        transactionRangeChoice.setItems(dateRangeList);
        transactionRangeChoice.getSelectionModel().selectFirst();
        transactionRangeChoice.setTooltip(new Tooltip("Select the range to display"));
        transactionRangeChoice.getSelectionModel().selectedIndexProperty().addListener( 
            (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                System.out.println("range = " + dateRangeList.get(newValue.intValue()));
                
                if (newValue.intValue() == 3 ) {
                    pickerStart.setDisable(false);
                    pickerEnd.setDisable(false);
                }
                else {
                    pickerStart.setDisable(true);
                    pickerEnd.setDisable(true);
                }
        });
        
        // default start end to Today / - 30days
        pickerStart.setValue(LocalDateTime.now().toLocalDate().plus(Period.of(0, 0, -30)));
        pickerEnd.setValue(LocalDateTime.now().toLocalDate());
   
        pickerStart.setDisable(true);
        pickerEnd.setDisable(true);
          
        
        // TODO: can't curretnly bind the control in FXML, this is a workaround - needs set() function?
        // https://bitbucket.org/controlsfx/controlsfx/issues/537/add-setitems-method-to-checkcombobox        
        transactionTypeCombo = new CheckComboBox<>(filterTags);
        
        // TODO: where/how to call this?
        //transactionTypeCombo.getCheckModel().check(0);
         
        // TODO: eventully have list update as checked and not only on Apply button
        transactionTypeCombo.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {
                System.out.println("onChanged: " + transactionTypeCombo.getCheckModel().getCheckedItems());   
                
                // TODO: update details pane with tag selection
                //updateDetails();
                
                // TODO: for now use the Apply button press to do the filtering
                // onFilterApply
            }            
        });           
        filterTopFlow.getChildren().add(transactionTypeCombo);                
        
       

        // ------- Filter Panel ----------
        
        
    } // initialize
    
    void updateDetails() {
        logger.info("");
        ObservableList<String> checkedTags = transactionTypeCombo.getCheckModel().getCheckedItems();
        
        // update tag col with checked items - clear out existing list or check for dupes
        transactionDetailWrapperList.clear();
        
        for (String tagName : checkedTags) {
            TransactionDetailWrapper tdw = new TransactionDetailWrapper(tagName, 0.0);
            System.out.println("tdw="+tdw.getTag() + ":" + tdw);
            transactionDetailWrapperList.add(tdw);
            
            ArrayList<TransactionTag> transTags;
            
            // If tag = "ALL" get all
            if ( tagName.compareTo("ALL") == 0 ) {
                transTags = ttDbAdapter.getAll();     
            }
            else {
                // get tag id
                Tag tag = tagDbAdapter.getTagByName(tagName);
            
                // get all transactionTag  with this tag ID
                transTags = ttDbAdapter.getAllForTag(tag.getId());
            }
            
            System.out.println("transTags size= " + transTags.size());
              
    
            // calculate total amount for this tag (and date range)
            //ArrayList<Transaction> matched = mTransactionDbAdapter.getTransaction();
        }                       
        // udate amount col with value
        
    } // updateDetails
    
    @FXML
    protected void onFilterApply(ActionEvent event) {
        logger.info("");
        
        // FILTER on Keyword / Date
        System.out.println("Filter = " + transactionFilterSearch.getText());
        
        // FILTER on Date range
        System.out.println("range = " + transactionRangeChoice.getSelectionModel().getSelectedIndex()
                + ", " + transactionRangeChoice.getSelectionModel().getSelectedItem());
        

        transactionDetailWrapperList.clear();
        
        ////////////////
        // FILTER on Date
        ////////////////
        
        
        
        // Simple date input 30, 60, 90, Custom
        int rangeIndex = transactionRangeChoice.getSelectionModel().getSelectedIndex();

        LocalDate startDate = LocalDate.MIN;
        LocalDate endDate = LocalDateTime.now().toLocalDate();
                
        long startDateLong = 0;
        long endDateLong   = endDate.toEpochDay();
        
        
        switch (rangeIndex) {
            case 0:
                startDate = endDate.plus(Period.of(0, 0, -30));
                break;
            case 1:
                startDate = endDate.plus(Period.of(0, 0, -60));
                break;
            case 2:
                startDate = endDate.plus(Period.of(0, 0, -90));
                break;
            case 3:
                startDate = pickerStart.getValue();
                endDate = pickerEnd.getValue();
                break;
            default:
                break;
        }
        
        startDateLong = startDate.toEpochDay();
        endDateLong   = endDate.toEpochDay();
        
        
        System.out.println("startDate: " + startDate + " : " + startDateLong);
        System.out.println("endDate:   " + endDate + " : " + endDateLong);
        
        
        // convert to ??
        
        
        
        ////////////////
        // FILTER on Tag
        ////////////////
        System.out.println(transactionTypeCombo.getCheckModel().getCheckedItems()); 
        ObservableList<String> checkedFilterTags = transactionTypeCombo.getCheckModel().getCheckedItems();
        for ( String tagStr : checkedFilterTags ){
            
            ArrayList<TransactionTag> transTags;
            
            // Get list of all TransTag mappings for this Tag
            if ( tagStr.compareTo("ALL") == 0 ) {
                transTags = ttDbAdapter.getAll();     
            }
            else {
                // get tag id
                Tag tag = tagDbAdapter.getTagByName(tagStr);
            
                // get all transactionTag  with this tag ID
                transTags = ttDbAdapter.getAllForTag(tag.getId());
            }
                       
            Double currAmount = 0.0;
            
            // get all transactions this TT mapping refers to - sum the amounts
            for (TransactionTag transTag : transTags ) {
                Integer transId = transTag.getTransactionId();
                Transaction trans = mTransactionDbAdapter.getTransaction(transId);
                
                if ( trans != null && ( trans.getDate() >= startDateLong && trans.getDate() <= endDateLong) ) {
                    System.out.println("Trans = " + trans.getDisplayName() + ", value=" + trans.getAmount());
                    currAmount += trans.getAmount();
                    
            /*
                    // TODO - how to update observable list element and trigger event?
                    // Get the TDW line this tag refers to 
                    TransactionDetailWrapper tdw = transactionDetailWrapperList.stream()
                        .filter(wrap -> wrap.getTag().equals(tagStr))
                        .findFirst()
                        .get();
                    
                    System.out.println("IMMA EDIT THIS: " + tdw.getTag() + ":" + tdw);
                    
                    tdw.setAmount(currAmount);
                    System.out.println("amount="+currAmount +", tdw=" + tdw.getAmount());
                    transactionDetailWrapperList.add(tdw);
 */
                }
                else {
                    System.out.println("null trans??, transId=" + transId + ", transTagId="+transTag.getId());
                }
            } // for each TT mapping
            
            TransactionDetailWrapper tdw = new TransactionDetailWrapper(tagStr, currAmount);
            System.out.println("tdw="+tdw.getTag() + ":" + tdw);
            transactionDetailWrapperList.add(tdw);
        }
                
    } // onFilterApply
    

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
        
        // populate the Tag Fliter combo
        ArrayList<Tag> tags = tagDbAdapter.getAll();
        for (Tag tag : tags) {
                if ( !filterTags.contains(tag.getName())) {
                    filterTags.add(tag.getName());
                }
            }

        // query all DB items into the list and set the Tableview to this list, select first item
        update();

    } // init

    private void tableSelection(Transaction selectedTransaction) {
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
    protected void editTransaction(ActionEvent event) {
        logger.info("");

        TransactionWrapper tw = transactionTableView.getSelectionModel().getSelectedItem();
        if ( tw == null ) {
            return;
        }
        
        Transaction selectedTransaction = tw.getTransaction();
        
        System.out.println("EDITING TRANS = " + selectedTransaction.getDisplayName() );

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

             // Updte *this* transaction - TODO, need to verify this function works as expected
             // I think the tags part will be updated below - not sure if good idea
            updateTransactionWrapper(tw, editTransaction, stringTags);
            
            // TODO - what is this for?
            transactionTableView.getSelectionModel().select(tw);
                        
            // compare origTw values with new ones
            // If they displayName is different, then all other similar items need
            // to update their displayName
            if ( editTransaction.getDisplayName().compareTo(origTrans.getDisplayName()) != 0 ) {

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
            } // DisplayName changed
            
            // update Tags if changed - TODO: combine with Name check?
            if ( origTagList.equals(stringTags) == false ) {    
                ArrayList<Tag> tags = createTransactionTags(tw.getTransaction(), stringTags);
                
                // TODO: need to apply to all similar entries
                
                // get list of all transactions with same Name
                ArrayList<Transaction> matched = mTransactionDbAdapter.getAllForName(origTrans.getName());

                // get all TT for each transaction
                for ( Transaction trans : matched ) {
                    ArrayList<TransactionTag> transTags = this.ttDbAdapter.getAllForTransaction(trans.getId());
                    
                    // Delete each TT
                    for ( TransactionTag tTag : transTags ) {
                        ttDbAdapter.delete(tTag.getId());
                    }
                    
                    // Add new tag
                    createTransactionTags(trans, stringTags);
                    
                    // pray
                }
            } // Tag changed

            // update the table
            update();
        });
    } // editTransaction

    // tw = original transWrapper (trans + tags)
    // transaction = updated transaction info
    // stringTags = whatever came out of the edit dialog
    private void updateTransactionWrapper(TransactionWrapper tw, Transaction transaction, ArrayList<String> stringTags) {
        logger.info("");
        // need to check for success and handle failure
        mTransactionDbAdapter.updateTransaction(transaction);
        
        // set the new values
        tw.setTransaction(transaction);

        // TODO: Why don't i need this?
        
        // must call updateTransactionTags - check for changes
       // updateTransactionTags(tw, transaction, stringTags);
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
     * It will delete any existing TransactionTags
     *
     * @param transaction
     * @param stringTags
     * @return
     */
    private ArrayList<Tag> createTransactionTags(Transaction transaction, ArrayList<String> stringTags) {
        logger.info("");
        ArrayList<Tag> tags = new ArrayList<>();
        
        Set<String> hs = new HashSet<>();
        hs.addAll(stringTags);
        stringTags.clear();
        stringTags.addAll(hs);
        
        // delete all TransTag mappings for this transaction
       // TODO: see if it will auto delete orphened tags --> NO, but i don't
       // want it to.  TAGS should persist, even if no transaction uses them
       ttDbAdapter.deleteTransaction(transaction.getId());

        for (String stringTag : stringTags) {
            Tag tag = new Tag(stringTag);

            // Check for existing Tag
            Tag tmpTag = tagDbAdapter.getTagByName(stringTag);
            
            if ( tmpTag == null ) {
                tagDbAdapter.createTag(tag);   
                
                 if ( !filterTags.contains(tag.getName())) {
                    filterTags.add(tag.getName());
                 }    
                 
            }
            else {
                tag = tmpTag;
            }
            
            tags.add(tag);
  
            // Don't allow multiple TT  entries :  Trans -> (TT) -> Tag
            // a trans should only link to a particular tag once
            // TODO: can likely remove this if i'm ALWAYS deleting TTs above
            Boolean exists = ttDbAdapter.exists(transaction, tag);

            if ( !exists ) {
                TransactionTag tt = new TransactionTag(transaction.getId(), tag.getId());
                ttDbAdapter.createTransactionTag(tt);
            }
        } // for each stringTag

        return tags;
    }
    
    // WHAT is the main purpose of this function?  When should it be called?
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
