package bank.bankApp.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Bank")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Bank_ID")
    private Long id;
    @Column(name = "Name")
    private String name;
    @Column(name = "Location")
    private String location;
    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private List<Employee> employees;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
