package bank.bankApp.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Account_ID")
    private Long id;
    @Column(name = "Number")
    private String number;
    @Column(name = "Type")
    private String type;
    @Column(name = "Balance")
    private Double balance;
    @ManyToOne
    @JoinColumn(name = "User_ID")
    private User user;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL , fetch = FetchType.EAGER)
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<AccountTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<AccountTransaction> transactions) {
        this.transactions = transactions;
    }
}
