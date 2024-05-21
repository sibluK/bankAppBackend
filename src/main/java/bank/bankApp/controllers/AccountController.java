package bank.bankApp.controllers;

import bank.bankApp.exceptions.AccountNotFoundException;
import bank.bankApp.exceptions.UserNotFoundException;
import bank.bankApp.models.Account;
import bank.bankApp.models.User;
import bank.bankApp.models.AccountTransaction;
import bank.bankApp.repositories.IAccountRepository;
import bank.bankApp.repositories.ITransactionRepository;
import bank.bankApp.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {

    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ITransactionRepository transactionRepository;

    @GetMapping
    public ResponseEntity<?> getAccounts() {
        try {
            return ResponseEntity.ok().body(accountRepository.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve accounts");
        }
    }
    @GetMapping(path = "/user/{userId}")
    public ResponseEntity<?> getUserAccounts(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok().body(accountRepository.findAllByUserId(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve accounts with userId: " + userId);
        }
    }

    @GetMapping(path = "/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable Long accountId) {
        try {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new AccountNotFoundException(accountId));
            return ResponseEntity.ok().body(account);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve account.");
        }
    }

    @PostMapping(path = "/{userId}")
    public ResponseEntity<?> addAccount(@RequestBody Account newAccount, @PathVariable Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));

            newAccount.setUser(user);
            user.getAccounts().add(newAccount);

            userRepository.save(user);
            accountRepository.save(newAccount);

            return ResponseEntity.ok().body("Account added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add account.");
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> replaceAccountById(@RequestBody Account newAccount, @PathVariable Long id) {
        try {
            Account account = accountRepository.findById(id)
                    .orElseThrow(() -> new AccountNotFoundException(id));

            account.setNumber(newAccount.getNumber());
            account.setType(newAccount.getType());
            account.setBalance(newAccount.getBalance());
            accountRepository.save(account);
            return ResponseEntity.ok().body("Account replaced!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to replace account.");
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteAccountById(@PathVariable Long id) {
        try {
            Account account = accountRepository.findById(id)
                    .orElseThrow(() -> new AccountNotFoundException(id));

            List<AccountTransaction> transactions = account.getTransactions();
            for (AccountTransaction transaction : transactions) {
                transactionRepository.delete(transaction);
            }
            accountRepository.deleteById(id);
            return ResponseEntity.ok().body("Account deleted!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete account.");
        }
    }
}
