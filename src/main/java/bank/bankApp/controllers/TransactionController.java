package bank.bankApp.controllers;

import bank.bankApp.exceptions.AccountNotFoundException;
import bank.bankApp.exceptions.TransactionNotFoundException;
import bank.bankApp.exceptions.UserNotFoundException;
import bank.bankApp.models.Account;
import bank.bankApp.models.AccountTransaction;
import bank.bankApp.models.User;
import bank.bankApp.repositories.IAccountRepository;
import bank.bankApp.repositories.ITransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private IAccountRepository accountRepository;

    @GetMapping(path = "/transactions/{accountId}")
    public ResponseEntity<?> getAccountTransactions(@PathVariable Long accountId) {
        try {
            return ResponseEntity.ok().body(transactionRepository.findAllByAccountId(accountId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve transactions with accountId: " + accountId);
        }
    }

    @PostMapping(path = "/transactions/{accountId}")
    public ResponseEntity<?> addTransaction(@RequestBody AccountTransaction newTransaction, @PathVariable Long accountId) {
        try {

            Account account = accountRepository.findById(accountId)
                            .orElseThrow(() -> new AccountNotFoundException(accountId));
            newTransaction.setAccount(account);
            account.getTransactions().add(newTransaction);
            transactionRepository.save(newTransaction);
            accountRepository.save(account);

            return ResponseEntity.ok().body("Transaction added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add transaction.");
        }
    }

    @GetMapping(path = "/transactions/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) {
        try {
            AccountTransaction transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> new TransactionNotFoundException(id));
            return ResponseEntity.ok().body(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve transaction.");
        }
    }

    @PutMapping(path = "/transactions/{id}")
    public ResponseEntity<?> replaceTransactionById(@RequestBody AccountTransaction newTransaction, @PathVariable Long id) {
        try {
            AccountTransaction transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> new TransactionNotFoundException(id));

            transaction.setType(newTransaction.getType());
            transaction.setAmount(newTransaction.getAmount());
            transaction.setDate(newTransaction.getDate());
            transaction.setAccount(newTransaction.getAccount());
            transactionRepository.save(transaction);
            return ResponseEntity.ok().body("Transaction replaced!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to replace transaction.");
        }
    }

    @DeleteMapping(path = "/transactions/{id}")
    public ResponseEntity<?> deleteTransactionById(@PathVariable Long id) {
        try {
            transactionRepository.deleteById(id);
            return ResponseEntity.ok().body("Transaction deleted!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete transaction.");
        }
    }


}
