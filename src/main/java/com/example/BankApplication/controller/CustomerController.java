package com.example.BankApplication.controller;
import com.example.BankApplication.model.Customer;
import com.example.BankApplication.response.CustomerUniqueIdResponse;
import com.example.BankApplication.security.JwtService;
import com.example.BankApplication.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    JwtService jwtService;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) {
        String registrationId = customerService.registerCustomer(customer);
        return ResponseEntity.ok("OTP has been sent to the email provided. Use registration ID " + registrationId + " to verify.");
    }


    @PostMapping("/verifyOtp")
    public ResponseEntity<CustomerUniqueIdResponse> verifyOtp(@RequestParam String registrationId, @RequestParam String otp) {
        String uniqueId = customerService.verifyOtpAndCreateCustomer(registrationId, otp);
        if (uniqueId != null) {
            String jwt = jwtService.generateJwt(uniqueId, "ROLE_CUSTOMER");
            CustomerUniqueIdResponse response = new CustomerUniqueIdResponse("Customer registered successfully. Customer ID: ", uniqueId, jwt);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }



    @GetMapping("/{uniqueId}")
    public ResponseEntity<Customer> getCustomerByUniqueId(@PathVariable String uniqueId) {
        Customer customer = customerService.getCustomerByUniqueId(uniqueId);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{uniqueId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String uniqueId, @RequestBody Customer customer) {
        customer.setCustomerUniqueId(uniqueId);
        Customer updatedCustomer = customerService.updateCustomer(customer);
        if (updatedCustomer != null) {
            return ResponseEntity.ok(updatedCustomer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{uniqueId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String uniqueId) {
        customerService.deleteCustomer(uniqueId);
        return ResponseEntity.noContent().build();
    }
}
