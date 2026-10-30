package kigali.duka.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.duka.rw.domain.ProductLine;
import kigali.duka.rw.domain.Shop;
import kigali.duka.rw.domain.Shopkeeper;
import kigali.duka.rw.repository.ProductLineRepository;
import kigali.duka.rw.repository.ShopRepository;
import kigali.duka.rw.repository.ShopkeeperRepository;

@Service
public class ShopkeeperService {

    @Autowired
    private ShopkeeperRepository shopkeepers;

    @Autowired
    private ShopRepository shops;

    @Autowired
    private ProductLineRepository productLines;

    public String save(Shopkeeper shopkeeper) {
        resolveBodyReferences(shopkeeper);
        shopkeepers.save(shopkeeper);
        return "Shopkeeper saved successfully";
    }

    public List<Shopkeeper> all() {
        return shopkeepers.findAll();
    }

    public Optional<Shopkeeper> one(UUID id) {
        return shopkeepers.findById(id);
    }

    public String update(UUID id, Shopkeeper incoming) {
        Optional<Shopkeeper> found = shopkeepers.findById(id);
        if (found.isEmpty()) {
            return "The shopkeeper with that id does not exist";
        }
        Shopkeeper existing = found.get();
        existing.setFirstName(incoming.getFirstName());
        existing.setLastName(incoming.getLastName());
        existing.setPhone(incoming.getPhone());

        resolveBodyReferences(incoming);
        if (incoming.getShopId() != null) {
            existing.setShop(incoming.getShop());
        }
        if (incoming.getProductLineIds() != null && !incoming.getProductLineIds().isEmpty()) {
            existing.setProductLines(incoming.getProductLines());
        }
        shopkeepers.save(existing);
        return "Shopkeeper updated successfully";
    }

    public String delete(UUID id) {
        if (!shopkeepers.existsById(id)) {
            return "The shopkeeper with that id does not exist";
        }
        shopkeepers.deleteById(id);
        return "Shopkeeper deleted successfully";
    }

    // Turns shopId / productLineIds from the JSON body into real entities.
    private void resolveBodyReferences(Shopkeeper incoming) {
        if (incoming.getShopId() != null) {
            incoming.setShop(shops.findById(incoming.getShopId()).orElse(null));
        }
        if (incoming.getProductLineIds() != null && !incoming.getProductLineIds().isEmpty()) {
            incoming.setProductLines(productLines.findAllById(incoming.getProductLineIds()));
        }
    }

    // C1
    public List<Shopkeeper> byProductLine(String name) {
        return shopkeepers.byProductLine(name);
    }

    // D3
    public String addProductLine(UUID shopkeeperId, UUID productLineId) {
        Optional<Shopkeeper> shopkeeper = shopkeepers.findById(shopkeeperId);
        if (shopkeeper.isEmpty()) {
            return "The shopkeeper with that id does not exist";
        }
        Optional<ProductLine> line = productLines.findById(productLineId);
        if (line.isEmpty()) {
            return "That product line does not exist";
        }

        Shopkeeper keeper = shopkeeper.get();
        boolean alreadyHas = keeper.getProductLines().stream()
                .anyMatch(pl -> pl.getId().equals(productLineId));
        if (alreadyHas) {
            return "The shopkeeper already has that product line";
        }
        keeper.getProductLines().add(line.get());
        shopkeepers.save(keeper);
        return "Product line added to the shopkeeper";
    }

    // D4
    public String moveShop(UUID shopkeeperId, int shopNumber) {
        Optional<Shopkeeper> shopkeeper = shopkeepers.findById(shopkeeperId);
        if (shopkeeper.isEmpty()) {
            return "The shopkeeper with that id does not exist";
        }
        Shop target = shops.findByShopNumber(shopNumber);
        if (target == null) {
            return "The shop with that shopNumber does not exist";
        }

        Shopkeeper keeper = shopkeeper.get();
        boolean sameShop = keeper.getShop() != null && keeper.getShop().getId().equals(target.getId());
        if (!sameShop && shopkeepers.existsByShop(target)) {
            return "That shop is taken by another shopkeeper";
        }
        keeper.setShop(target);
        shopkeepers.save(keeper);
        return "Shop changed successfully";
    }
}
