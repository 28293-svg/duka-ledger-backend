package kigali.duka.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.duka.rw.domain.Customer;
import kigali.duka.rw.repository.CustomerRepository;
import kigali.duka.rw.repository.ShopkeeperRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customers;

    @Autowired
    private ShopkeeperRepository shopkeepers;

    public String save(Customer customer) {
        customers.save(customer);
        return "Customer saved successfully";
    }

    public List<Customer> all() {
        return customers.findAll();
    }

    public Optional<Customer> one(UUID id) {
        return customers.findById(id);
    }

    public String update(UUID id, Customer incoming) {
        Optional<Customer> found = customers.findById(id);
        if (found.isEmpty()) {
            return "The customer with that id does not exist";
        }
        Customer existing = found.get();
        existing.setFirstName(incoming.getFirstName());
        existing.setLastName(incoming.getLastName());
        existing.setPhone(incoming.getPhone());
        existing.setVillage(incoming.getVillage());
        customers.save(existing);
        return "Customer updated successfully";
    }

    public String delete(UUID id) {
        if (!customers.existsById(id)) {
            return "The customer with that id does not exist";
        }
        customers.deleteById(id);
        return "Customer deleted successfully";
    }

    // B1
    public List<Customer> byLastName(String lastName) {
        return customers.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    // C4
    // Optional.empty() means the shopkeeper doesn't exist; an empty list means they just have no customers.
    public Optional<List<Customer>> ofShopkeeper(UUID shopkeeperId) {
        if (!shopkeepers.existsById(shopkeeperId)) {
            return Optional.empty();
        }
        return Optional.of(customers.ofShopkeeper(shopkeeperId));
    }
}
