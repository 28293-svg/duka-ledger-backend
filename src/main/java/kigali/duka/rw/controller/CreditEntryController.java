package kigali.duka.rw.controller;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.duka.rw.domain.CreditEntry;
import kigali.duka.rw.domain.CreditStatus;
import kigali.duka.rw.service.CreditEntryService;

@RestController
@RequestMapping("/api/credit-entries")
public class CreditEntryController {

    @Autowired
    private CreditEntryService service;

    // D1
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody CreditEntry entry) {
        String message = service.save(entry);
        HttpStatus code = HttpStatus.CREATED;
        if (message.startsWith("Credit entry date cannot be in the past")) {
            code = HttpStatus.BAD_REQUEST;
        } else if (message.startsWith("Shopkeeper already recorded")) {
            code = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(message, code);
    }

    @GetMapping("/all")
    public ResponseEntity<?> all() {
        return new ResponseEntity<>(service.all(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable UUID id) {
        Optional<CreditEntry> found = service.one(id);
        if (found.isPresent()) {
            return new ResponseEntity<>(found.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The credit entry with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody CreditEntry entry) {
        String message = service.update(id, entry);
        HttpStatus code = message.startsWith("The credit entry with that id does not exist")
                ? HttpStatus.NOT_FOUND
                : HttpStatus.OK;
        return new ResponseEntity<>(message, code);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String message = service.delete(id);
        HttpStatus code = HttpStatus.OK;
        if (message.startsWith("The credit entry with that id does not exist")) {
            code = HttpStatus.NOT_FOUND;
        } else if (message.startsWith("That credit entry has repayments")) {
            code = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(message, code);
    }

    // B2
    @GetMapping("/by-status")
    public ResponseEntity<?> byStatus(@RequestParam CreditStatus status) {
        return new ResponseEntity<>(service.byStatus(status), HttpStatus.OK);
    }

    // B3
    @GetMapping("/between")
    public ResponseEntity<?> between(@RequestParam String start, @RequestParam String end) {
        LocalDate from = LocalDate.parse(start);
        LocalDate to = LocalDate.parse(end);
        return new ResponseEntity<>(service.between(from, to), HttpStatus.OK);
    }

    // D2
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable UUID id, @RequestParam CreditStatus status) {
        String message = service.changeStatus(id, status);
        HttpStatus code = HttpStatus.OK;
        if (message.startsWith("The credit entry with that id does not exist")) {
            code = HttpStatus.NOT_FOUND;
        } else if (message.startsWith("Cannot change status")) {
            code = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(message, code);
    }

    // F1
    @GetMapping("/stats/by-status")
    public ResponseEntity<?> statsByStatus() {
        return new ResponseEntity<>(service.statsByStatus(), HttpStatus.OK);
    }

    // F2
    @PatchMapping("/cancel-day")
    public ResponseEntity<?> cancelDay(@RequestParam UUID shopkeeperId, @RequestParam String date) {
        String message = service.cancelDay(shopkeeperId, LocalDate.parse(date));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // bonus - paging
    @GetMapping("/page")
    public ResponseEntity<?> page(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(defaultValue = "entryDate,desc") String sort) {
        String[] parts = sort.split(",");
        Sort.Direction direction = parts.length > 1
                ? Sort.Direction.fromString(parts[1])
                : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, parts[0]));
        Page<CreditEntry> result = service.page(pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
