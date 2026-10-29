package kigali.duka.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.duka.rw.domain.CreditEntry;
import kigali.duka.rw.domain.Repayment;

public interface RepaymentRepository extends JpaRepository<Repayment, UUID> {

    // E3
    boolean existsByCreditEntry(CreditEntry creditEntry);

    // E2
    @Query("SELECT d FROM Repayment d "
         + "WHERE d.creditEntry.customer.phone = :phone "
         + "ORDER BY d.paidDate DESC")
    List<Repayment> ofCustomer(@Param("phone") String phone);
}
