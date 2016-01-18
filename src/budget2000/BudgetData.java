/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Brian
 */
public class BudgetData {
    private static final Logger logger = Logger.getGlobal();

//    private ObservableList<User> userList = FXCollections.observableArrayList();
//
//    private User selectedUser = new User();
//    private Institution selectedInstitution = new Institution();
//    private Account selectedAccount = new Account();
//    private Transaction selectedTransaction = new Transaction();
    private Integer mSelectedUser = 0;
    private Integer mSelectedInstitution = 0;
    private Integer mSelectedAccount = 0;
    private Integer mSelectedTransaction = 0;

    // selection vars
    public static final String USER_SELECTION = "user_selection";
    public static final String INSTITUTION_SELECTION = "institution_selection";
    public static final String ACCOUNT_SELECTION = "account_selection";
    public static final String TRANSACTION_SELECTION = "transaction_selection";

    private final PropertyChangeSupport pcsUser = new PropertyChangeSupport(this);
    private final PropertyChangeSupport pcsInstitution = new PropertyChangeSupport(this);
    private final PropertyChangeSupport pcsAccount = new PropertyChangeSupport(this);
    private final PropertyChangeSupport pcsTransaction = new PropertyChangeSupport(this);

    public BudgetData() {

    }

    public Integer getSelectedUser() {
        return mSelectedUser;
    }

    public Integer getSelectedInstitution() {
        return mSelectedInstitution;
    }

    public Integer getSelectedAccount() {
        return mSelectedAccount;
    }

    public Integer getSelectedTransaction() {
        return mSelectedTransaction;
    }

    public void setSelectedUser(Integer i) {
        logger.info(i.toString());
        
        Integer old = mSelectedUser;
        mSelectedUser = i;

        PropertyChangeEvent evt = new PropertyChangeEvent(this, USER_SELECTION, old, mSelectedUser);
        
        // un set next lower class - should the data class do this or a controller class? MAVC?
        if ( i == 0 )
            setSelectedInstitution(0);

        // TODO: should this happen before the above
        pcsUser.firePropertyChange(evt);
    }

    public void setSelectedInstitution(Integer i) {
        logger.info(i.toString());
        
        Integer old = mSelectedInstitution;
        mSelectedInstitution = i;

        PropertyChangeEvent evt = new PropertyChangeEvent(this, INSTITUTION_SELECTION, old, mSelectedInstitution);
        
        // un set next lower class - should the data class do this or a controller class? MAVC?
        if ( i == 0 )
            setSelectedAccount(0);
        
        pcsInstitution.firePropertyChange(evt);
    }

    public void setSelectedAccount(Integer i) {
        logger.info(i.toString());
        
        Integer old = mSelectedAccount;
        mSelectedAccount = i;

        PropertyChangeEvent evt = new PropertyChangeEvent(this, ACCOUNT_SELECTION, old, mSelectedAccount);
        
        // un set next lower class - should the data class do this or a controller class? MAVC?
        if ( i == 0 )
            setSelectedTransaction(0);
        
        pcsAccount.firePropertyChange(evt);
    }

    void setSelectedTransaction(Integer i) {
        logger.info(i.toString());
        
        Integer old = mSelectedTransaction;
        mSelectedTransaction = i;

        PropertyChangeEvent evt = new PropertyChangeEvent(this, TRANSACTION_SELECTION, old, mSelectedTransaction);
        pcsTransaction.firePropertyChange(evt);
    }

    public void addUserPropertyChangeListener(PropertyChangeListener listener) {
        pcsUser.addPropertyChangeListener(listener);
    }

    public void removeUserPropertyChangeListener(PropertyChangeListener listener) {
        pcsUser.removePropertyChangeListener(listener);
    }

    public void addInstitutionPropertyChangeListener(PropertyChangeListener listener) {
        pcsInstitution.addPropertyChangeListener(listener);
    }

    public void removeInstitutionPropertyChangeListener(PropertyChangeListener listener) {
        pcsInstitution.removePropertyChangeListener(listener);
    }

    public void addAccountPropertyChangeListener(PropertyChangeListener listener) {
        pcsAccount.addPropertyChangeListener(listener);
    }

    public void removeAccountPropertyChangeListener(PropertyChangeListener listener) {
        pcsAccount.removePropertyChangeListener(listener);
    }

    public void addTransactionPropertyChangeListener(PropertyChangeListener listener) {
        pcsTransaction.addPropertyChangeListener(listener);
    }

    public void removeTransactionPropertyChangeListener(PropertyChangeListener listener) {
        pcsTransaction.removePropertyChangeListener(listener);
    }
} // BudgetData
