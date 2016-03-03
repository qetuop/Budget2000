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
import java.time.LocalDate;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Brian
 */
public class Transaction implements Externalizable {

//    private LongProperty transactionDate;
//    private LongProperty postedDate;
//    private StringProperty description;
//    private DoubleProperty amount;
//    private static DoubleProperty id;
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty displayName;
    private final IntegerProperty accountId;
    private final LongProperty date;
    private final DoubleProperty amount;

    public Transaction() {
        this.id = new SimpleIntegerProperty(this, "id", 0);
        this.name = new SimpleStringProperty(this, "name", "");
        this.displayName = new SimpleStringProperty(this, "displayName", "");
        this.accountId = new SimpleIntegerProperty(this, "accountId", 0);
        this.date = new SimpleLongProperty(this, "date", 0);
        this.amount = new SimpleDoubleProperty(this, "amount", 0);
    }

    public Transaction(Integer id, Long date, String name, String displayName, Integer accountId, Double amount) {
        this();

        this.setId(id);
        this.setName(name);
        this.setDisplayName(displayName);
        this.setAccountId(accountId);
        this.setDate(date);
        this.setAmount(amount);
    }

    public final Integer getId() {
        return id.get();
    }

    public final void setId(Integer value) {
        id.set(value);
    }

    public IntegerProperty getIdProperty() {
        return id;
    }

    public final String getName() {
        return name.get();
    }

    public final void setName(String value) {
        name.set(value);
    }

    public final StringProperty getNameProperty() {
        return name;
    }
    
    public final String getDisplayName() {
        return displayName.get();
    }

    public final void setDisplayName(String value) {
        displayName.set(value);
    }

    public final StringProperty getDisplayNameProperty() {
        return displayName;
    }

    public final Integer getAccountId() {
        return accountId.get();
    }

    public final void setAccountId(Integer value) {
        accountId.set(value);
    }

    public IntegerProperty getAccountIdProperty() {
        return accountId;
    }
   
    public final Long getDate() {
        return date.get();
    }

    public final void setDate(Long value) {
        date.set(value);
    }

    public LongProperty getDateProperty() {
        return date;
    }

    public final Double getAmount() {
        return amount.get();
    }
    
    public final void setAmount(Double value) {
        amount.set(value);
    }
    
    public DoubleProperty getAmmountProperty() {
        return amount;
    }
    
    

    @Override
    public String toString() {
        //DateFormat df = DateFormat.getDateInstance();
        //df.setTimeZone(TimeZone.getTimeZone("EST"));
        //String d = df.format(new Date(getDate()));
        LocalDate localDate = LocalDate.ofEpochDay(getDate());
                
        return String.format("Transaction{ %d %d %d (%s) \"%s\" \"%s\" }", 
                this.getId(), getAccountId(), getDate(), localDate, getName(), getDisplayName());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        //out.writeObject(getName());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        //setDescription((String) in.readObject());
    }

} // Account
