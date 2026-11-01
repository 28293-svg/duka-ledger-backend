package kigali.duka.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.duka.rw.domain.CreditEntry;
import kigali.duka.rw.domain.CreditStatus;
import kigali.duka.rw.domain.Repayment;
import kigali.duka.rw.repository.CreditEntryRepository;
import kigali.duka.rw.repository.RepaymentRepository;

@Service
public class RepaymentService {

    @Autowired
    private RepaymentRepository repayments;

    @Autowired
    private CreditEntryRepository entries;

    // E1
    public String save(UUID creditEntryId, Repayment repayment) {
        CreditEntry entry = entries.findById(creditEntryId).orElse(null);
        if (entry == null) {
            return "The credit entry with that id does not exist";
        }
        if (entry.getStatus() != CreditStatus.REPAID) {
            return "Repayments are only for REPAID credit entries";
        }
        repayment.setCreditEntry(entry);
        repayment.setPaidDate(LocalDate.now());
        repayments.save(repayment);
        return "Repayment saved successfully";
    }

    public List<Repayment> all() {
        return repayments.findAll();
    }

    // E2
    public List<Repayment> ofCustomer(String phone) {
        return repayments.ofCustomer(phone);
    }

    // E3
    public String delete(UUID id) {
        if (!repayments.existsById(id)) {
            return "The repayment with that id does not exist";
        }
        repayments.deleteById(id);
        return "Repayment deleted successfully";
    }
}
