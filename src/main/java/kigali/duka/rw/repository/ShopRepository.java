package kigali.duka.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kigali.duka.rw.domain.Shop;

public interface ShopRepository extends JpaRepository<Shop, UUID> {

    // C2 - no shopkeeper in it
    // Shops with no shopkeeper (shop 4 in the seed data).
    @Query("SELECT l FROM Shop l "
         + "WHERE NOT EXISTS (SELECT p FROM Shopkeeper p WHERE p.shop = l) "
         + "ORDER BY l.shopNumber")
    List<Shop> free();

    // D4
    Shop findByShopNumber(int shopNumber);
}
