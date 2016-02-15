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
import java.util.function.Supplier;
import java.util.logging.Level;
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

    public void setSelectedUser(User user) {
        logger.info("");

        Integer old = mSelectedUser;

        if (user == null) {
            mSelectedUser = 0;
            setSelectedInstitution(null);
        } else {
            mSelectedUser = user.getId();
        }

        PropertyChangeEvent evt = new PropertyChangeEvent(this, USER_SELECTION, old, mSelectedUser);
        pcsUser.firePropertyChange(evt);
    }

    public void setSelectedInstitution(Institution institution) {
        logger.info("");

        Integer old = mSelectedInstitution;

        if (institution == null) {
            mSelectedInstitution = 0;
            setSelectedAccount(null);
        } else {
            mSelectedInstitution = institution.getId();
        }

        PropertyChangeEvent evt = new PropertyChangeEvent(this, INSTITUTION_SELECTION, old, mSelectedInstitution);
        pcsInstitution.firePropertyChange(evt);
    }

    public void setSelectedAccount(Account account) {
        logger.info("");

        Integer old = mSelectedAccount;

        if (account == null) {
            mSelectedAccount = 0;
            setSelectedTransaction(null);
        } else {
            mSelectedAccount = account.getId();
        }

        PropertyChangeEvent evt = new PropertyChangeEvent(this, ACCOUNT_SELECTION, old, mSelectedAccount);
        pcsAccount.firePropertyChange(evt);
    }

    void setSelectedTransaction(Transaction transaction) {
        logger.info("");

        Integer old = mSelectedTransaction;
        
        if (transaction == null) {
            mSelectedTransaction = 0;
        } else {
            mSelectedTransaction = transaction.getId();
        }

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
