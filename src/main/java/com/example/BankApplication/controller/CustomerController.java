package com.example.BankApplication.controller;
import com.example.BankApplication.model.Customer;
import com.example.BankApplication.response.CustomerUniqueIdResponse;
import com.example.BankApplication.security.JwtService;
import com.example.BankApplication.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers in the system.")
    @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Customer.class)))
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @Operation(summary = "Create a new customer", description = "Create a new customer with the provided details.")
    @ApiResponse(responseCode = "201", description = "Customer created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Customer.class)))
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



    @Operation(summary = "Get customer by ID", description = "Retrieve a specific customer by their ID.")
    @ApiResponse(responseCode = "200", description = "Customer retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Customer.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @GetMapping("/{uniqueId}")
    public ResponseEntity<Customer> getCustomerByUniqueId(@PathVariable String uniqueId) {
        Customer customer = customerService.getCustomerByUniqueId(uniqueId);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Update customer details", description = "Update the details of an existing customer.")
    @ApiResponse(responseCode = "200", description = "Customer updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Customer.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found")
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


    @Operation(summary = "Delete a customer", description = "Delete a customer by their ID.")
    @ApiResponse(responseCode = "204", description = "Customer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @DeleteMapping("/{uniqueId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String uniqueId) {
        customerService.deleteCustomer(uniqueId);
        return ResponseEntity.noContent().build();
    }
}
