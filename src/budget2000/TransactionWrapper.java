/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author brian
 */
public class TransactionWrapper {

    private final ObjectProperty<Transaction> transaction = new SimpleObjectProperty<>();
    private final ListProperty<Tag> tags = new SimpleListProperty<>();

    public ObservableList getTags() {
        return tags.get();
    }
    
    public String getTagList() {
        StringBuilder sb = new StringBuilder();
        for (Tag tag : tags ){
            sb.append(tag.getName()).append(" ");
        }
        return sb.toString();
        
    }

    public void setTags(ObservableList value) {
        tags.set(value);
    }

    public ListProperty tagsProperty() {
        return tags;
    }
    

    public Transaction getTransaction() {
        return transaction.get();
    }

    public void setTransaction(Transaction value) {
        transaction.set(value);
    }

    public ObjectProperty transactionProperty() {
        return transaction;
    }
}
