package kigali.duka.rw.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "credit_entry")
public class CreditEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDate entryDate;

    @Enumerated(EnumType.STRING)
    private CreditStatus status;

    private String items;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "shopkeeper_id")
    private Shopkeeper shopkeeper;

    // Write-only helpers, same idea as Shopkeeper.shopId.
    @Transient
    private UUID customerId;

    @Transient
    private UUID shopkeeperId;

    public UUID getId() {
        return id;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public CreditStatus getStatus() {
        return status;
    }

    public void setStatus(CreditStatus status) {
        this.status = status;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Shopkeeper getShopkeeper() {
        return shopkeeper;
    }

    public void setShopkeeper(Shopkeeper shopkeeper) {
        this.shopkeeper = shopkeeper;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getShopkeeperId() {
        return shopkeeperId;
    }

    public void setShopkeeperId(UUID shopkeeperId) {
        this.shopkeeperId = shopkeeperId;
    }
}
