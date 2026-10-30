package kigali.duka.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.duka.rw.domain.Shop;
import kigali.duka.rw.repository.ShopRepository;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shops;

    public String save(Shop shop) {
        shops.save(shop);
        return "Shop saved successfully";
    }

    public List<Shop> all() {
        return shops.findAll();
    }

    public Optional<Shop> one(UUID id) {
        return shops.findById(id);
    }

    public String update(UUID id, Shop incoming) {
        Optional<Shop> found = shops.findById(id);
        if (found.isEmpty()) {
            return "The shop with that id does not exist";
        }
        Shop existing = found.get();
        existing.setShopNumber(incoming.getShopNumber());
        existing.setName(incoming.getName());
        shops.save(existing);
        return "Shop updated successfully";
    }

    public String delete(UUID id) {
        if (!shops.existsById(id)) {
            return "The shop with that id does not exist";
        }
        shops.deleteById(id);
        return "Shop deleted successfully";
    }

    // C2
    public List<Shop> free() {
        return shops.free();
    }
}
