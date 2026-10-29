package kigali.duka.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.duka.rw.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    // B1
    List<Customer> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    // C4
    @Query("SELECT DISTINCT e.customer FROM CreditEntry e WHERE e.shopkeeper.id = :id")
    List<Customer> ofShopkeeper(@Param("id") UUID id);
}
