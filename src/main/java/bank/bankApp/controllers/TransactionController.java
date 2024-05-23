package bank.bankApp.controllers;

import bank.bankApp.exceptions.AccountNotFoundException;
import bank.bankApp.exceptions.TransactionNotFoundException;
import bank.bankApp.models.Account;
import bank.bankApp.models.AccountTransaction;
import bank.bankApp.repositories.IAccountRepository;
import bank.bankApp.repositories.ITransactionRepository;
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


/**
 * Controller for managing transactions.
 */
@RestController
@RequestMapping(path = "/transactions")
@Tag(name = "Controller for the transaction class")
public class TransactionController {

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private IAccountRepository accountRepository;

    /**
     * Retrieves all transactions.
     *
     * @return a list of all transactions.
     */
    @Operation(summary = "Returns all transactions", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all transactions",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountTransaction.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve transactions",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
    @GetMapping
    public ResponseEntity<?> getTransactions() {
        try {
            return ResponseEntity.ok().body(transactionRepository.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve transactions");
        }
    }

    /**
     * Retrieves all transactions associated with the given account ID.
     *
     * @param accountId the ID of the account.
     * @return a list of transactions for the specified account.
     */
    @Operation(summary = "Returns transactions by account ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found transactions",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountTransaction.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve transactions",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
    @GetMapping(path = "/account/{accountId}")
    public ResponseEntity<?> getAccountTransactions(@PathVariable Long accountId) {
        try {
            return ResponseEntity.ok().body(transactionRepository.findAllByAccountId(accountId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve transactions with accountId: " + accountId);
        }
    }

    /**
     * Retrieves a transaction based on the provided transaction ID.
     *
     * @param transactionId the ID of the transaction.
     * @return the transaction details.
     */
    @Operation(summary = "Returns a transaction by ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the transaction",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountTransaction.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve transaction",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
    @GetMapping(path = "/{transactionId}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long transactionId) {
        try {
            AccountTransaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new TransactionNotFoundException(transactionId));
            return ResponseEntity.ok().body(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve transaction.");
        }
    }

    /**
     * Creates a new transaction for the specified account ID.
     *
     * @param newTransaction the transaction details.
     * @param accountId      the ID of the account.
     * @return a confirmation message.
     */
    @Operation(summary = "Adds a new transaction", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to add transaction",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
    @PostMapping(path = "/{accountId}")
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

    /**
     * Updates a transaction's information based on the provided transaction ID.
     *
     * @param newTransaction the new transaction details.
     * @param id             the ID of the transaction to update.
     * @return a confirmation message.
     */
    @Operation(summary = "Replaces a transaction by ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction replaced successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to replace transaction",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> replaceTransactionById(@RequestBody AccountTransaction newTransaction, @PathVariable Long id) {
        try {
            AccountTransaction transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> new TransactionNotFoundException(id));

            transaction.setType(newTransaction.getType());
            transaction.setAmount(newTransaction.getAmount());
            transaction.setDate(newTransaction.getDate());
            transactionRepository.save(transaction);
            return ResponseEntity.ok().body("Transaction replaced!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to replace transaction.");
        }
    }

    /**
     * Removes a transaction based on the provided transaction ID.
     *
     * @param id the ID of the transaction to delete.
     * @return a confirmation message.
     */
    @Operation(summary = "Deletes a transaction by ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to delete transaction",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteTransactionById(@PathVariable Long id) {
        try {
            transactionRepository.findById(id)
                    .orElseThrow(() -> new TransactionNotFoundException(id));
            transactionRepository.deleteById(id);
            return ResponseEntity.ok().body("Transaction deleted!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete transaction.");
        }
    }
}
