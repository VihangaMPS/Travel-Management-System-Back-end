package lk.travel.customerservice.repo;

import lk.travel.customerservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {
    Customer findByEmail(String email);
}
