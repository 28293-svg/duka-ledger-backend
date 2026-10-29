package kigali.duka.rw.domain;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int shopNumber;

    private String name;

    public UUID getId() {
        return id;
    }

    public int getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(int shopNumber) {
        this.shopNumber = shopNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
