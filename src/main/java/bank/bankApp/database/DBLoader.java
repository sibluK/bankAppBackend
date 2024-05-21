package bank.bankApp.database;

import bank.bankApp.models.Account;
import bank.bankApp.models.AccountTransaction;
import bank.bankApp.models.User;
import bank.bankApp.repositories.IAccountRepository;
import bank.bankApp.repositories.ITransactionRepository;
import bank.bankApp.repositories.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Date;

@Configuration
public class DBLoader {
    private static final Logger log = LoggerFactory.getLogger(DBLoader.class);

    @Bean
    CommandLineRunner initDatabase(IUserRepository userRepository, IAccountRepository accountRepository, ITransactionRepository transactionRepository) {

        return args -> {
            User user1 = new User();
            user1.setUsername("username1");
            user1.setPassword("123");
            user1.setEmail("username1@gmail.com");
            user1.setFirstName("name1");
            user1.setLastName("lastName2");

            User user2 = new User();
            user2.setUsername("username2");
            user2.setPassword("password2");
            user2.setEmail("username2@gmail.com");
            user2.setFirstName("name2");
            user2.setLastName("lastName2");

            userRepository.saveAll(Arrays.asList(user1, user2));

            Account account1 = new Account();
            account1.setNumber("123456");
            account1.setType("Checking");
            account1.setBalance(1000.0);
            account1.setUser(user1);

            Account account2 = new Account();
            account2.setNumber("654321");
            account2.setType("Savings");
            account2.setBalance(5000.0);
            account2.setUser(user1);

            Account account3 = new Account();
            account3.setNumber("789012");
            account3.setType("Checking");
            account3.setBalance(2000.0);
            account3.setUser(user2);

            accountRepository.saveAll(Arrays.asList(account1, account2, account3));

            AccountTransaction transaction1 = new AccountTransaction();
            transaction1.setDate(new Date());
            transaction1.setAmount(100.0);
            transaction1.setType("Deposit");
            transaction1.setAccount(account1);

            AccountTransaction transaction2 = new AccountTransaction();
            transaction2.setDate(new Date());
            transaction2.setAmount(50.0);
            transaction2.setType("Withdrawal");
            transaction2.setAccount(account1);

            AccountTransaction transaction3 = new AccountTransaction();
            transaction3.setDate(new Date());
            transaction3.setAmount(200.0);
            transaction3.setType("Deposit");
            transaction3.setAccount(account2);

            AccountTransaction transaction4 = new AccountTransaction();
            transaction4.setDate(new Date());
            transaction4.setAmount(150.0);
            transaction4.setType("Withdrawal");
            transaction4.setAccount(account3);

            transactionRepository.saveAll(Arrays.asList(transaction1, transaction2, transaction3, transaction4));

            log.info("Database has been initialized with sample data");
        };
    }
}
