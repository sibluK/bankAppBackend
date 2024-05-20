package bank.bankApp.repositories;

import bank.bankApp.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmployeeRepository extends JpaRepository<Employee, Long>{

}
