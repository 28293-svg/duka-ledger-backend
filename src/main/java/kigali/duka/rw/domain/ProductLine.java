package kigali.duka.rw.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "product_line")
public class ProductLine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "productLines")
    @JsonIgnore
    private List<Shopkeeper> shopkeepers = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Shopkeeper> getShopkeepers() {
        return shopkeepers;
    }

    public void setShopkeepers(List<Shopkeeper> shopkeepers) {
        this.shopkeepers = shopkeepers;
    }

}
