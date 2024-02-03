package chahboune.ma.customerfrontthymleafapp;

import chahboune.ma.customerfrontthymleafapp.entities.Customer;
import chahboune.ma.customerfrontthymleafapp.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerFrontThymleafAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerFrontThymleafAppApplication.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner(CustomerRepository customerRepository){
        return args -> {
            customerRepository.save(Customer.builder().name("marouane").email("marouane@gmail.com").build());
            customerRepository.save(Customer.builder().name("imane").email("imane@gmail.com").build());
            customerRepository.save(Customer.builder().name("Asmaa").email("asmaa@gmail.com").build());
        };
    }
}
