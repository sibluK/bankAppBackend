package bank.bankApp.controllers;

import bank.bankApp.exceptions.UserNotFoundException;
import bank.bankApp.models.Account;
import bank.bankApp.models.AccountTransaction;
import bank.bankApp.models.User;
import bank.bankApp.repositories.IAccountRepository;
import bank.bankApp.repositories.ITransactionRepository;
import bank.bankApp.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private ITransactionRepository transactionRepository;

    @GetMapping
    public ResponseEntity<?> getUsers() {
        try {
            return ResponseEntity.ok().body(userRepository.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve users.");
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve user.");
        }
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User newUser) {
        try {
            userRepository.save(newUser);
            return ResponseEntity.ok().body("User added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add user.");
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> replaceUserById(@RequestBody User newUser, @PathVariable Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));

            user.setUsername(newUser.getUsername());
            user.setPassword(newUser.getPassword());
            user.setEmail(newUser.getEmail());
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            userRepository.save(user);
            return ResponseEntity.ok().body("User replaced!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to replace user.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));

            List<Account> accounts = user.getAccounts();
            for (Account account : accounts) {
                List<AccountTransaction> transactions = account.getTransactions();
                for (AccountTransaction transaction : transactions) {
                    transactionRepository.delete(transaction);
                }
                accountRepository.delete(account);
            }

            userRepository.deleteById(id);

            return ResponseEntity.ok().body("User deleted!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete user.");
        }
    }

}
