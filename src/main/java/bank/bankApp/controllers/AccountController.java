package bank.bankApp.controllers;

import bank.bankApp.exceptions.AccountNotFoundException;
import bank.bankApp.exceptions.UserNotFoundException;
import bank.bankApp.models.Account;
import bank.bankApp.models.User;
import bank.bankApp.models.AccountTransaction;
import bank.bankApp.repositories.IAccountRepository;
import bank.bankApp.repositories.ITransactionRepository;
import bank.bankApp.repositories.IUserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing accounts.
 */
@RestController
@RequestMapping(path = "/accounts")
@Tag(name = "Controller for the account class")
public class AccountController {

    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ITransactionRepository transactionRepository;

    /**
     * Retrieves a list of all accounts in the system.
     *
     * @return a list of all accounts.
     */
    @Operation(
            summary = "Returns all accounts",
            description = "Retrieves a list of all accounts in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all accounts",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve accounts",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
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

    /**
     * Retrieves all accounts associated with the given user ID.
     *
     * @param userId the ID of the user.
     * @return a list of accounts for the specified user.
     */
    @Operation(
            summary = "Returns accounts by user ID",
            description = "Retrieves all accounts associated with the given user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the accounts",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve accounts",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
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

    /**
     * Retrieves an account based on the provided account ID.
     *
     * @param accountId the ID of the account.
     * @return the account details.
     */
    @Operation(
            summary = "Returns an account by ID",
            description = "Retrieves an account based on the provided account ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the account",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve account",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
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

    /**
     * Creates a new account for the specified user ID.
     *
     * @param newAccount the account details.
     * @param userId     the ID of the user.
     * @return a confirmation message.
     */
    @Operation(
            summary = "Adds a new account",
            description = "Creates a new account for the specified user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to add account",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
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

    /**
     * Updates an account's information based on the provided account ID.
     *
     * @param newAccount the new account details.
     * @param id         the ID of the account to update.
     * @return a confirmation message.
     */
    @Operation(
            summary = "Replaces an account by ID",
            description = "Updates an account's information based on the provided account ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account replaced successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to replace account",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
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

    /**
     * Removes an account and its associated transactions based on the provided account ID.
     *
     * @param id the ID of the account to delete.
     * @return a confirmation message.
     */
    @Operation(
            summary = "Deletes an account by ID",
            description = "Removes an account and its associated transactions based on the provided account ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to delete account",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
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
