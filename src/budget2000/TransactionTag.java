/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author brian
 */
public class TransactionTag {

    private final IntegerProperty id;
    private final IntegerProperty transactionId;
    private final IntegerProperty tagId;

    public TransactionTag() {
        this.id = new SimpleIntegerProperty(this, "id", 0);
        this.transactionId = new SimpleIntegerProperty(this, "transactionId", 0);
        this.tagId = new SimpleIntegerProperty(this, "tagId", 0);
    }

    public TransactionTag(Integer id, Integer transactionId, Integer tagId) {
        this();

        this.setId(id);
        this.setTransactionId(transactionId);
        this.setTagId(tagId);

    }
    
    public TransactionTag(Integer transactionId, Integer tagId) {
        this();

        this.setTransactionId(transactionId);
        this.setTagId(tagId);

    }

    public final int getId() {
        return id.get();
    }

    public final void setId(int value) {
        id.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public final int getTransactionId() {
        return transactionId.get();
    }

    public final void setTransactionId(int value) {
        transactionId.set(value);
    }

    public IntegerProperty transactionIdProperty() {
        return transactionId;
    }

    public final int getTagId() {
        return tagId.get();
    }

    public final void setTagId(int value) {
        tagId.set(value);
    }

    public IntegerProperty tagIdProperty() {
        return tagId;
    }

    @Override
    public String toString() {
        return "TransactionTag{" + "id=" + id + ", transactionId=" + transactionId + ", tagId=" + tagId + '}';
    }

} // Tag
