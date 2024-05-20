package bank.bankApp.controllers;

import bank.bankApp.repositories.IBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankController {

    @Autowired
    private IBankRepository bankRepository;
}
