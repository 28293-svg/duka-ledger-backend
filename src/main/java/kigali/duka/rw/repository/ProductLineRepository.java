package kigali.duka.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kigali.duka.rw.domain.ProductLine;

public interface ProductLineRepository extends JpaRepository<ProductLine, UUID> {

    // C3 - nobody holds it
    // Product lines with no shopkeeper attached (Airtime in the seed data).
    @Query("SELECT p FROM ProductLine p WHERE p.shopkeepers IS EMPTY")
    List<ProductLine> unused();
}
