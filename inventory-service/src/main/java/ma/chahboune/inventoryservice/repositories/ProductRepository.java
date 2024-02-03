package ma.chahboune.inventoryservice.repositories;

import ma.chahboune.inventoryservice.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,String> {
}
