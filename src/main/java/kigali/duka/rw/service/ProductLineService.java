package kigali.duka.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.duka.rw.domain.ProductLine;
import kigali.duka.rw.repository.ProductLineRepository;

@Service
public class ProductLineService {

    @Autowired
    private ProductLineRepository productLines;

    public String save(ProductLine productLine) {
        productLines.save(productLine);
        return "Product line saved successfully";
    }

    public List<ProductLine> all() {
        return productLines.findAll();
    }

    public Optional<ProductLine> one(UUID id) {
        return productLines.findById(id);
    }

    public String update(UUID id, ProductLine incoming) {
        Optional<ProductLine> found = productLines.findById(id);
        if (found.isEmpty()) {
            return "The product line with that id does not exist";
        }
        ProductLine existing = found.get();
        existing.setName(incoming.getName());
        productLines.save(existing);
        return "Product line updated successfully";
    }

    public String delete(UUID id) {
        if (!productLines.existsById(id)) {
            return "The product line with that id does not exist";
        }
        productLines.deleteById(id);
        return "Product line deleted successfully";
    }

    // C3
    public List<ProductLine> unused() {
        return productLines.unused();
    }
}
