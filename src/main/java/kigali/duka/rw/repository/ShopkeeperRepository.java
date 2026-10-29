package kigali.duka.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.duka.rw.domain.Shop;
import kigali.duka.rw.domain.Shopkeeper;

public interface ShopkeeperRepository extends JpaRepository<Shopkeeper, UUID> {

    // C1
    @Query("SELECT DISTINCT s FROM Shopkeeper s JOIN s.productLines p "
         + "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Shopkeeper> byProductLine(@Param("name") String name);

    // D4
    boolean existsByShop(Shop shop);
}
