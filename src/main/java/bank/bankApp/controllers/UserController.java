package bank.bankApp.controllers;

import bank.bankApp.assemblers.UserModelAssembler;
import bank.bankApp.exceptions.UserNotFoundException;
import bank.bankApp.models.Account;
import bank.bankApp.models.AccountTransaction;
import bank.bankApp.models.User;
import bank.bankApp.models.UserModel;
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
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import java.util.List;
import java.util.stream.Collectors;


/**
 * Controller for managing users.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Controller for the user class")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private ITransactionRepository transactionRepository;
    @Autowired
    private UserModelAssembler userAssembler;

    /**
     * Retrieves all users.
     *
     * @return a list of all users.
     */
    @Operation(
            summary = "Returns all users",
            description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all users",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve users",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
    @GetMapping
    public ResponseEntity<?> getUsers() {
        try {
            List<User> users = userRepository.findAll();
            List<UserModel> userModels = users.stream()
                    .map(userAssembler::toModel)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(CollectionModel.of(userModels, linkTo(methodOn(UserController.class).getUsers()).withSelfRel()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve users.");
        }
    }

    /**
     * Retrieves a user based on their ID.
     *
     * @param id the ID of the user.
     * @return the user details.
     */
    @Operation(
            summary = "Returns a user by their id",
            description = "Retrieves a user based on the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
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

    /**
     * Creates a new user with the provided details.
     *
     * @param newUser the details of the new user.
     * @return a confirmation message.
     */
    @Operation(
            summary = "Adds a new user",
            description = "Creates a new user with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to add user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
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

    /**
     * Updates a user's information based on their ID.
     *
     * @param newUser the new user details.
     * @param id      the ID of the user to update.
     * @return a confirmation message.
     */
    @Operation(
            summary = "Replaces a user by their id",
            description = "Updates a user's information based on the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User replaced successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to replace user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
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

    /**
     * Deletes a user based on their ID, along with associated accounts and transactions.
     *
     * @param id the ID of the user to delete.
     * @return a confirmation message.
     */
    @Operation(
            summary = "Deletes a user by their id",
            description = "Removes a user and their associated accounts and transactions based on the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Failed to delete user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
    })
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
