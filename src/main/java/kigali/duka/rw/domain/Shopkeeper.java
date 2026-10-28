package kigali.duka.rw.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "shopkeeper")
public class Shopkeeper {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;

    private String lastName;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToMany
    @JoinTable(name = "shopkeeper_product_line",
        joinColumns = @JoinColumn(name = "shopkeeper_id"),
        inverseJoinColumns = @JoinColumn(name = "product_line_id"))
    private List<ProductLine> productLines = new ArrayList<>();

    // only so the JSON body can send ids
    @Transient
    private UUID shopId;

    @Transient
    private List<UUID> productLineIds = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<ProductLine> getProductLines() {
        return productLines;
    }

    public void setProductLines(List<ProductLine> productLines) {
        this.productLines = productLines;
    }

    public UUID getShopId() {
        return shopId;
    }

    public void setShopId(UUID shopId) {
        this.shopId = shopId;
    }

    public List<UUID> getProductLineIds() {
        return productLineIds;
    }

    public void setProductLineIds(List<UUID> productLineIds) {
        this.productLineIds = productLineIds;
    }

}
