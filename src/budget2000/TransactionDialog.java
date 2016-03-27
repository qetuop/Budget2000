/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 *
 * @author brian
 */
public class TransactionDialog extends Dialog {

    GridPane grid = new GridPane();
    DatePicker datePicker = new DatePicker();
    TextField name = new TextField();
    TextField displayName = new TextField();
    TextField amount = new TextField();
    TextField tagTF = new TextField();
    
    Transaction transaction = new Transaction();

    ArrayList<String> tags = new ArrayList<>();

    TextFormatter<String> amountFormatter = new TextFormatter<String>(change -> {
        change.setText(change.getText().replaceAll("[^\\d+(\\.\\d{0,2})?$]", ""));
        return change;
    });

    // Edit this transaction - need to retain some values, ex: _id
    TransactionDialog(Transaction transactionIn) {
        init();

        // must use copy!  orig value needs to be retained
        transaction = new Transaction(transactionIn);
        
        LocalDate locatDate = LocalDate.ofEpochDay(transaction.getDate());
        datePicker.setValue(locatDate);
        name.setText(transaction.getName());
        displayName.setText(transaction.getDisplayName());
        amount.setText(Double.toString(transaction.getAmount()));

        TransactionTagDbAdapter ttDbAdapter = new TransactionTagDbAdapter();
        TagDbAdapter tagDbAdapter = new TagDbAdapter();

        ArrayList<TransactionTag> objects = ttDbAdapter.getAllForTransaction(transaction.getId());
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (TransactionTag tt : objects) {
            Tag tag = tagDbAdapter.getTag(tt.getTagId());
            sb.append(delim).append(tag.getName());
            delim = ",";
        }

        tagTF.setText(sb.toString());
    }

    TransactionDialog() {
        init();
    }

    private void init() {
        // Set the button types.
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        amount.setTextFormatter(amountFormatter);
        name.setPromptText("Transaction Name");
        displayName.setPromptText("Display Name");
        amount.setPromptText("Transaction Amount");

        datePicker.setValue(LocalDate.now());
        amount.setText("0.0");

        // Create the first and last labels and fields.        
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Transaction Date:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Transaction Name:"), 0, 1);
        grid.add(name, 1, 1);
        grid.add(new Label("Display Name:"), 0, 2);
        grid.add(displayName, 1, 2);
        grid.add(new Label("Transaction Amount:"), 0, 3);
        grid.add(amount, 1, 3);
        grid.add(new Label("Tags:"), 0, 4);
        grid.add(tagTF, 1, 4);

        getDialogPane().setContent(grid);

        // Convert the result to a Transaction when the ok button is clicked.
        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {

                DecimalFormat decimalFormat = new DecimalFormat();
                decimalFormat.setParseBigDecimal(true);
                BigDecimal bigDecimal = BigDecimal.ZERO;
                try {
                    bigDecimal = (BigDecimal) decimalFormat.parse(amount.getText());
                } catch (ParseException ex) {
                    Logger.getLogger(TransactionViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                transaction.setName(name.getText());
                transaction.setDisplayName(displayName.getText());
                transaction.setAmount(bigDecimal.doubleValue());
                transaction.setDate(datePicker.getValue().toEpochDay());

                // returns an empty string --> array of size 1 --> not good
                //String[] items = tagTF.getText().split("\\s+",-1);//.split(",");
                // is this really the cleanest way?
                String[] items = Arrays.asList(
                        tagTF.getText().split(" "))
                        .stream()
                        .filter(str -> !str.isEmpty())
                        .collect(Collectors.toList())
                        .toArray(new String[0]);
                tags = new ArrayList<>(Arrays.asList(items));

                return new Pair<>(transaction, tags);
            }
            return null;
        });
    }

} // TransactionDialog
