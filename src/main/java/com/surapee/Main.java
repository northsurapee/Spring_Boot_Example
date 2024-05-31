package com.surapee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SpringBootApplication
// Spring Boot Annotation to mark this as main of Spring Boot application
// This annotation include
// - @ComponentScan : scan for a component in where this class location and under
// - @EnableAutoConfiguration : config web server
// - @Configuration : If we have bean inside this class, this work well with it.

@RestController
// Servlet - It's a process which handles Http requests.
// Spring Web MVC - abstract away a lot of details you would have to understand yourself if writing servlets manually and allows us to create Restful services very easy.
// This indicates that the class if a controller, and that all the methods in the makes class will return a JSON response.
// This annotation include
// - @Controller - marks the class as web controller, auto-detect implementation classes/beans by scanning the classpath
// - @ResponseBody - tell spring to automatically serialize return values(s) of this classes methods into HTTP responses. (Apply "Jackson" library to transform Java Object to JSON for us)

@RequestMapping("api/v1/customers")
// Base endpoint

public class Main {

    // Inject
    private final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    record NewCustomerRequest(
            String name,
            String email,
            Integer age
    ){}

    @PostMapping
    public void addCustomer(@RequestBody NewCustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setAge(request.age());
        customerRepository.save(customer);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer id) { // grab customerId
        customerRepository.deleteById(id);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(@PathVariable("customerId") Integer id, @RequestBody NewCustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        Optional.ofNullable(request.name()).ifPresent(customer::setName);
        Optional.ofNullable(request.email()).ifPresent(customer::setEmail);
        Optional.ofNullable(request.age()).ifPresent(customer::setAge);

        customerRepository.save(customer);
    }

//    // Spring Web MVC annotation to expose this method as a rest endpoint for client to use as get requests.
//    // @RequestMapping specifies a method in the controller that should be responsible for serving the HTTP request to the given endpoint.
//    // @GetMapping is an abbreviated form of "@RequestMapping" Specifically for HTTP GET requests
//    @GetMapping("/")
//    public GreetResponse greet() {
//        GreetResponse response = new GreetResponse(
//                "Hello",
//                List.of("Java", "Golang", "Javascript"),
//                new Person("Alex", 30, 30_000.99)
//        );
//        return response;
//    }
//
//    // Record
//    record Person(
//            String name,
//            int age,
//            double saving
//    ){}
//
//    record GreetResponse(
//            String greet,
//            List<String> favProgrammingLanguages,
//            Person person
//    ) {}

    // Class that equivalent with above record.
//    class GreetResponse {
//        private final String greet;
//
//        GreetResponse(String greet) {
//            this.greet = greet;
//        }
//
//        public String getGreet() {
//            return greet;
//        }
//
//        @Override
//        public String toString() {
//            return "GreetResponse{" +
//                    "greet='" + greet + '\'' +
//                    '}';
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            GreetResponse that = (GreetResponse) o;
//            return Objects.equals(greet, that.greet);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hashCode(greet);
//        }
//    }


}
