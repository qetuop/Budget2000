/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author brian
 */
public class TransactionDetailWrapper {
    private final StringProperty tag;

    public TransactionDetailWrapper(){
        this.tag = new SimpleStringProperty(this, "tag", "");
    }
    
    public TransactionDetailWrapper( TransactionDetailWrapper ref ) {
        this.tag = new SimpleStringProperty(this, "tag",ref.getTag());
    }
    
    public TransactionDetailWrapper(String tag) {
        this();
        
        this.setTag(tag);
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
}
