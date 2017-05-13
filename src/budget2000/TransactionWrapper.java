/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author brian
 */
public class TransactionWrapper {

    private final ObjectProperty<Transaction> transaction;
    private final ListProperty<Tag> tags;

    public TransactionWrapper(){
        transaction = new SimpleObjectProperty<>();
        tags = new SimpleListProperty<>();
    }
    
    public TransactionWrapper(Transaction t, ArrayList<Tag> tags){
        this();
        
        setTransaction(t);
        setTags(FXCollections.observableArrayList(tags));
    }
    
    public ObservableList getTags() {
        return tags.get();
    }
    
    public String getTagListAsString() {
        StringBuilder sb = new StringBuilder();
        for (Tag tag : tags ){
            sb.append(tag.getName()).append(" ");
        }
        return sb.toString().trim();
        
    }
    
    public List<String> getTagList() {
        ArrayList<String> tagList = new ArrayList<>();
        
        for (Tag tag : tags ){
            tagList.add(tag.getName());
        }
        return tagList;  
    }
    public Set<String> getTagSet() {
        HashSet<String> tagSet = new HashSet();
        
        for (Tag tag : tags ){
            tagSet.add(tag.getName());
        }
        return tagSet;  
    }
    

    public final void setTags(ObservableList value) {
        tags.set(value);
    }

    public ListProperty tagsProperty() {
        return tags;
    }
    

    public Transaction getTransaction() {
        return transaction.get();
    }

    public final void setTransaction(Transaction value) {
        transaction.set(value);
    }

    public ObjectProperty transactionProperty() {
        return transaction;
    }
}
