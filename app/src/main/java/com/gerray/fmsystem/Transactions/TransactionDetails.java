package com.gerray.fmsystem.Transactions;

public class TransactionDetails {
    public String transactionID;
    public String transactionCode;
    public String payerID;
    public String payeeID;
    public String cost;
    public String transactionDescription;
    public String transactionDate;

    public TransactionDetails() {
    }

    public TransactionDetails(String transactionID, String transactionCode, String payerID, String payeeID, String cost, String transactionDescription, String transactionDate) {
        this.transactionID = transactionID;
        this.transactionCode = transactionCode;
        this.payerID = payerID;
        this.payeeID = payeeID;
        this.cost = cost;
        this.transactionDescription = transactionDescription;
        this.transactionDate = transactionDate;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getPayerID() {
        return payerID;
    }

    public void setPayerID(String payerID) {
        this.payerID = payerID;
    }

    public String getPayeeID() {
        return payeeID;
    }

    public void setPayeeID(String payeeID) {
        this.payeeID = payeeID;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
}
