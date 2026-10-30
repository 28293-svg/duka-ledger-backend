package kigali.duka.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.duka.rw.domain.CreditEntry;
import kigali.duka.rw.domain.CreditStatus;
import kigali.duka.rw.domain.Shopkeeper;
import kigali.duka.rw.repository.CreditEntryRepository;
import kigali.duka.rw.repository.CustomerRepository;
import kigali.duka.rw.repository.RepaymentRepository;
import kigali.duka.rw.repository.ShopkeeperRepository;

@Service
public class CreditEntryService {

    @Autowired
    private CreditEntryRepository entries;

    @Autowired
    private ShopkeeperRepository shopkeepers;

    @Autowired
    private CustomerRepository customers;

    @Autowired
    private RepaymentRepository repayments;

    // D1
    public String save(CreditEntry entry) {
        if (entry.getEntryDate() != null && entry.getEntryDate().isBefore(LocalDate.now())) {
            return "Credit entry date cannot be in the past";
        }

        Shopkeeper shopkeeper = null;
        if (entry.getShopkeeperId() != null) {
            shopkeeper = shopkeepers.findById(entry.getShopkeeperId()).orElse(null);
        }
        if (shopkeeper != null
                && entries.existsByShopkeeperAndEntryDateAndStatusNot(
                        shopkeeper, entry.getEntryDate(), CreditStatus.WRITTEN_OFF)) {
            return "Shopkeeper already recorded a credit entry on that date";
        }

        entry.setShopkeeper(shopkeeper);
        if (entry.getCustomerId() != null) {
            entry.setCustomer(customers.findById(entry.getCustomerId()).orElse(null));
        }

        // A brand-new entry always starts as RECORDED, whatever the body carried.
        entry.setStatus(CreditStatus.RECORDED);
        entries.save(entry);
        return "Credit entry saved successfully";
    }

    public List<CreditEntry> all() {
        return entries.findAll();
    }

    public Optional<CreditEntry> one(UUID id) {
        return entries.findById(id);
    }

    public String update(UUID id, CreditEntry incoming) {
        CreditEntry existing = entries.findById(id).orElse(null);
        if (existing == null) {
            return "The credit entry with that id does not exist";
        }
        // Status is left alone here; only changeStatus() is allowed to move it.
        existing.setEntryDate(incoming.getEntryDate());
        existing.setItems(incoming.getItems());
        existing.setAmount(incoming.getAmount());
        if (incoming.getCustomerId() != null) {
            existing.setCustomer(customers.findById(incoming.getCustomerId()).orElse(null));
        }
        if (incoming.getShopkeeperId() != null) {
            existing.setShopkeeper(shopkeepers.findById(incoming.getShopkeeperId()).orElse(null));
        }
        entries.save(existing);
        return "Credit entry updated successfully";
    }

    public String delete(UUID id) {
        CreditEntry existing = entries.findById(id).orElse(null);
        if (existing == null) {
            return "The credit entry with that id does not exist";
        }
        // E3 - cannot delete an entry that has repayments
        if (repayments.existsByCreditEntry(existing)) {
            return "That credit entry has repayments, delete them first";
        }
        entries.deleteById(id);
        return "Credit entry deleted successfully";
    }

    // B2
    public List<CreditEntry> byStatus(CreditStatus status) {
        return entries.findByStatusOrderByEntryDateAsc(status);
    }

    // B3
    public List<CreditEntry> between(LocalDate from, LocalDate to) {
        return entries.findByEntryDateBetweenOrderByEntryDateAsc(from, to);
    }

    // D2
    public String changeStatus(UUID id, CreditStatus target) {
        CreditEntry existing = entries.findById(id).orElse(null);
        if (existing == null) {
            return "The credit entry with that id does not exist";
        }

        CreditStatus current = existing.getStatus();
        boolean allowed = switch (current) {
            case RECORDED -> target == CreditStatus.OVERDUE || target == CreditStatus.WRITTEN_OFF;
            case OVERDUE  -> target == CreditStatus.REPAID  || target == CreditStatus.WRITTEN_OFF;
            case REPAID, WRITTEN_OFF -> false;
        };
        if (!allowed) {
            return "Cannot change status from " + current + " to " + target;
        }

        existing.setStatus(target);
        entries.save(existing);
        return "Credit entry status updated successfully";
    }

    // F1
    public List<Object[]> statsByStatus() {
        return entries.countPerStatus();
    }

    // F2
    @Transactional
    public String cancelDay(UUID shopkeeperId, LocalDate day) {
        int cancelled = entries.cancelDay(
                CreditStatus.WRITTEN_OFF, CreditStatus.REPAID, shopkeeperId, day);
        return cancelled + " credit entries cancelled";
    }

    // bonus - paging
    public Page<CreditEntry> page(Pageable pageable) {
        return entries.findAll(pageable);
    }
}
