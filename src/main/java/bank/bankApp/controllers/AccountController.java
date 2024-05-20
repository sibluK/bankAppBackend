package bank.bankApp.controllers;

import bank.bankApp.exceptions.AccountNotFoundException;
import bank.bankApp.exceptions.UserNotFoundException;
import bank.bankApp.models.Account;
import bank.bankApp.models.User;
import bank.bankApp.repositories.IAccountRepository;
import bank.bankApp.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private IUserRepository userRepository;

    @GetMapping(path = "/accounts/user/{userId}")
    public ResponseEntity<?> getUserAccounts(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok().body(accountRepository.findAllByUserId(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve accounts with userId: " + userId);
        }
    }

    @GetMapping(path = "/accounts/{accountId}")
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

    @PostMapping(path = "/accounts/{userId}")
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

    @PutMapping(path = "/accounts/{id}")
    public ResponseEntity<?> replaceAccountById(@RequestBody Account newAccount, @PathVariable Long id) {
        try {
            Account account = accountRepository.findById(id)
                    .orElseThrow(() -> new AccountNotFoundException(id));

            account.setNumber(newAccount.getNumber());
            account.setType(newAccount.getType());
            account.setBalance(newAccount.getBalance());
            account.setUser(newAccount.getUser());
            account.setTransactions(newAccount.getTransactions());
            accountRepository.save(account);
            return ResponseEntity.ok().body("Account replaced!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to replace account.");
        }
    }

    @DeleteMapping(path = "/accounts/{id}")
    public ResponseEntity<?> deleteAccountById(@PathVariable Long id) {
        try {
            accountRepository.deleteById(id);
            return ResponseEntity.ok().body("Account deleted!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete account.");
        }
    }


}
