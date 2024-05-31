package bank.bankApp.models;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class AccountModel extends RepresentationModel<AccountModel> {
    private Long id;
    private String number;
    private String type;
    private Double balance;
    private Long userId;
    private List<AccountTransaction> transactions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<AccountTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<AccountTransaction> transactions) {
        this.transactions = transactions;
    }
}
