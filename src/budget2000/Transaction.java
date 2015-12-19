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
import java.util.Date;
import java.util.TimeZone;
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
    private final StringProperty description;
    private final IntegerProperty accountId;
    private final LongProperty date;
    private final DoubleProperty amount;

    public Transaction() {
        this.id = new SimpleIntegerProperty(this, "id", 0);
        this.description = new SimpleStringProperty(this, "description", "");
        this.accountId = new SimpleIntegerProperty(this, "accountId", 0);
        this.date = new SimpleLongProperty(this, "date", 0);
        this.amount = new SimpleDoubleProperty(this, "amount", 0);
    }

    public Transaction(Integer id, Long date, String description, Integer accountId, Double amount) {
        this();

        this.setId(id);
        this.setDescription(description);
        this.setAccountId(accountId);
        this.setDate(date);
        this.setAmount(amount);
    }
    
    public Transaction(Integer id, Long date, String description, Integer accountId) {
        this();

        this.setId(id);
        this.setDescription(description);
        this.setAccountId(accountId);
        this.setDate(date);
    }

    public Transaction(Long date, String description, Integer accountId) {
        this();

        this.setDescription(description);
        this.setAccountId(accountId);
        this.setDate(date);
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

    public final String getDescription() {
        return description.get();
    }

    public final void setDescription(String value) {
        description.set(value);
    }

    public final StringProperty getDescriptionProperty() {
        return description;
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
        DateFormat df = DateFormat.getDateInstance();
        df.setTimeZone(TimeZone.getTimeZone("EST"));
        String d = df.format(new Date(getDate()));

        return String.format("Transaction{ %d %d (%s) \"%s\" %s }", 
                this.getId(), getDate(), d, getDescription(), getAccountId());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getDescription());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setDescription((String) in.readObject());
    }

} // Account
