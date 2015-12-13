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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Brian
 */
public class Institution implements Externalizable {
    private final IntegerProperty id;
    private final StringProperty institutionName;
    private final IntegerProperty userId;
    //private ObservableList<Account> accountList;
    
    public Institution(){
        this.id = new SimpleIntegerProperty(this, "id", 0);
        this.institutionName = new SimpleStringProperty(this, "institutionName", ""); 
        this.userId = new SimpleIntegerProperty(this, "userId", 0);
        //this.accountList = FXCollections.observableArrayList();
    }
    
//    public Institution(String institutionName) {
//        this();
//        this.setInstitutionName(institutionName);       
//    }
//
    public Institution(String institutionName, Integer userId) {
        this();
        
        this.setInstitutionName(institutionName);
        this.setUserId(userId);
    }
    
    public Institution(Integer id, String institutionName, Integer userId) {
        this();
        
        this.setId(id);
        this.setInstitutionName(institutionName);
        this.setUserId(userId);
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
        
    public final String getInstitutionName() { 
        return institutionName.get(); 
    }
    
    public final void setInstitutionName( String value ) {
        institutionName.set(value); 
    }
    
    public final StringProperty getInstitutionNameProperty() { 
        return institutionName; 
    }

    public final Integer getUserId() {
        return userId.get();
    }
    
    public final void setUserId( Integer value ) {
        userId.set(value);
    }
    
    public IntegerProperty getUserIdProperty() {
        return userId;
    }
    
    @Override
    public String toString() {
        return String.format("Institution{ %d %s %d }", 
                this.getId(), this.getInstitutionName(), this.getUserId());
    }

//    public void addAccount(Account account) {
//        accountList.add(account);
//    }
//    
//    ObservableList<Account> getAccountList() {
//        return accountList;
//    }
    
    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getInstitutionName());
        
        //ArrayList<Account> tmp = new ArrayList<>(accountList);
        //out.writeObject(tmp);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setInstitutionName((String) in.readObject());
        
        //ArrayList<Account> tmp = (ArrayList<Account>) in.readObject();
        //accountList = FXCollections.observableArrayList(tmp);
    }
    
} // Institution
