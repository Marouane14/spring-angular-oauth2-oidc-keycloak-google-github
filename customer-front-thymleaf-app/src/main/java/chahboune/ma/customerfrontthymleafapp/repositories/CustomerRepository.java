package chahboune.ma.customerfrontthymleafapp.repositories;

import chahboune.ma.customerfrontthymleafapp.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
