package kigali.duka.rw.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.duka.rw.domain.CreditStatus;
import kigali.duka.rw.repository.CreditEntryRepository;
import kigali.duka.rw.repository.CustomerRepository;
import kigali.duka.rw.repository.RepaymentRepository;
import kigali.duka.rw.repository.ShopRepository;
import kigali.duka.rw.repository.ShopkeeperRepository;

@Service
public class DashboardService {

    @Autowired
    private CustomerRepository customers;

    @Autowired
    private ShopkeeperRepository shopkeepers;

    @Autowired
    private ShopRepository shops;

    @Autowired
    private CreditEntryRepository entries;

    @Autowired
    private RepaymentRepository repayments;

    // bonus - dashboard
    public Map<String, Long> summary() {
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("customers", customers.count());
        counts.put("shopkeepers", shopkeepers.count());
        counts.put("freeShops", (long) shops.free().size());
        counts.put("entriesToday", entries.countByEntryDate(LocalDate.now()));
        counts.put("recorded", entries.countByStatus(CreditStatus.RECORDED));
        counts.put("repayments", repayments.count());
        return counts;
    }
}
