/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author brian
 */
public class TransactionDetailWrapper {
    private final StringProperty tag;
    private final DoubleProperty amount;

    public TransactionDetailWrapper(){
        this.tag = new SimpleStringProperty(this, "tag", "");
        this.amount = new SimpleDoubleProperty(this, "amount", 0);
    }
    
    public TransactionDetailWrapper( TransactionDetailWrapper ref ) {
        this.tag = new SimpleStringProperty(this, "tag",ref.getTag());
        this.amount = new SimpleDoubleProperty(this, "amount", ref.getAmount());
    }
    
    public TransactionDetailWrapper(String tag, Double amount) {
        this();
        
        this.setTag(tag);
        this.setAmount(amount);
    }
    
    public final String getTag() {
        return tag.get();
    }
    
    public final void setTag(String value) {
        tag.set(value);
    }

    public final StringProperty getTagProperty() {
        return tag;
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
}
