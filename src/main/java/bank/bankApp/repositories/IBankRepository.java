package bank.bankApp.repositories;

import bank.bankApp.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBankRepository extends JpaRepository<Bank, Long> {

}
