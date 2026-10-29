package kigali.duka.rw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.duka.rw.domain.CreditEntry;
import kigali.duka.rw.domain.CreditStatus;
import kigali.duka.rw.domain.Shopkeeper;

public interface CreditEntryRepository extends JpaRepository<CreditEntry, UUID> {

    // B2
    List<CreditEntry> findByStatusOrderByEntryDateAsc(CreditStatus status);

    // B3
    List<CreditEntry> findByEntryDateBetweenOrderByEntryDateAsc(LocalDate start, LocalDate end);

    // D1
    boolean existsByShopkeeperAndEntryDateAndStatusNot(
            Shopkeeper shopkeeper, LocalDate entryDate, CreditStatus status);

    // dashboard
    long countByEntryDate(LocalDate entryDate);

    long countByStatus(CreditStatus status);

    // F1
    // Grouped count used by the stats endpoint.
    @Query("SELECT e.status, COUNT(e) FROM CreditEntry e GROUP BY e.status")
    List<Object[]> countPerStatus();

    // F2
    // Bulk flip of one shopkeeper's day. REPAID rows are skipped.
    @Modifying(clearAutomatically = true)
    @Query("UPDATE CreditEntry e SET e.status = :to "
         + "WHERE e.shopkeeper.id = :sid AND e.entryDate = :day AND e.status <> :skip")
    int cancelDay(@Param("to") CreditStatus to,
                  @Param("skip") CreditStatus skip,
                  @Param("sid") UUID sid,
                  @Param("day") LocalDate day);
}
