package bank.bankApp.models;

import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

public class TransactionModel extends RepresentationModel<TransactionModel> {

    private Long id;
    private Date date;
    private Double amount;
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
