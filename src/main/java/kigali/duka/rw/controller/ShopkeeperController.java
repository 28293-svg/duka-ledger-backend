package kigali.duka.rw.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

import kigali.duka.rw.domain.Shopkeeper;
import kigali.duka.rw.service.ShopkeeperService;

@RestController
@RequestMapping("/api/shopkeepers")
public class ShopkeeperController {

    @Autowired
    private ShopkeeperService service;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Shopkeeper shopkeeper) {
        return new ResponseEntity<>(service.save(shopkeeper), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> all() {
        return new ResponseEntity<>(service.all(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable UUID id) {
        Optional<Shopkeeper> found = service.one(id);
        if (found.isEmpty()) {
            return new ResponseEntity<>("The shopkeeper with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(found.get(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Shopkeeper shopkeeper) {
        String message = service.update(id, shopkeeper);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String message = service.delete(id);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // C1
    @GetMapping("/by-product-line")
    public ResponseEntity<?> byProductLine(@RequestParam String name) {
        return new ResponseEntity<>(service.byProductLine(name), HttpStatus.OK);
    }

    // D3
    @PutMapping("/{shopkeeperId}/product-lines/{productLineId}")
    public ResponseEntity<?> addProductLine(@PathVariable UUID shopkeeperId,
                                            @PathVariable UUID productLineId) {
        String message = service.addProductLine(shopkeeperId, productLineId);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.contains("already has")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // D4
    @PatchMapping("/{shopkeeperId}/shop")
    public ResponseEntity<?> moveShop(@PathVariable UUID shopkeeperId, @RequestParam int shopNumber) {
        String message = service.moveShop(shopkeeperId, shopNumber);
        if (message.contains("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.contains("is taken")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
