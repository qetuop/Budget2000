/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

/**
 *
 * @author brian
 */
public class Context {
    private final static Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }

    private Integer userId = 0;
    private Integer institutionId = 0;
    private Integer accountId = 0;
    private Integer transactionId = 0;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

   
}
