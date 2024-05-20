package bank.bankApp.controllers;

import bank.bankApp.repositories.IEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @Autowired
    private IEmployeeRepository employeeRepository;

}
