/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Brian
 */
public class Account implements Externalizable {

    private final IntegerProperty id;
    private final StringProperty accountName;
    private final IntegerProperty institutionId;
    //private ObservableList<Transaction> transactionList;

    public Account() {
        this.id = new SimpleIntegerProperty(this, "id", 0);
        this.accountName = new SimpleStringProperty(this, "accountName", "");
        this.institutionId = new SimpleIntegerProperty(this, "institutionId", 0);
        //this.transactionList = FXCollections.observableArrayList();
    }

    public Account(String accountName, Integer institutionId) {
        this();

        this.setAccountName(accountName);
        this.setInstitutionId(institutionId);
    }
    
    public Account(Integer id, String accountName, Integer institutionId) {
        this();

        this.setId(id);
        this.setAccountName(accountName);
        this.setInstitutionId(institutionId);
    }

    public final Integer getId() {
        return id.get();
    }
    
    public final void setId( Integer value ) {
        id.set(value);
    }
    
    public IntegerProperty getIdProperty() {
        return id;
    }
    
    public final String getAccountName() {
        return accountName.get();
    }

    public final void setAccountName(String value) {
        accountName.set(value);
    }

    public final StringProperty getAccountNameProperty() {
        return accountName;
    }

    public final Integer getInstitutionId() {
        return institutionId.get();
    }

    public final void setInstitutionId(Integer value) {
        institutionId.set(value);
    }

    public IntegerProperty getInstitutionIdProperty() {
        return institutionId;
    }
    
    @Override
    public String toString() {
        return String.format("Account{ %d %s %d }", 
                this.getId(), this.getAccountName(), this.getInstitutionId());
    }

//    public void addTransaction(Transaction account) {
//        transactionList.add(account);
//    }
//
//    ObservableList<Transaction> getTransactionList() {
//        return transactionList;
//    }
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getAccountName());

        //ArrayList<Transaction> tmp = new ArrayList<>(transactionList);
        //out.writeObject(tmp);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setAccountName((String) in.readObject());

        //ArrayList<Transaction> tmp = (ArrayList<Transaction>) in.readObject();
        //transactionList = FXCollections.observableArrayList(tmp);
    }

} // Account
