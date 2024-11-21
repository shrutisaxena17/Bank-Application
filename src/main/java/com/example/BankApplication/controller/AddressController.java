package com.example.BankApplication.controller;

import com.example.BankApplication.exception.NotFoundException;
import com.example.BankApplication.model.Address;
import com.example.BankApplication.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(
            summary = "Get all addresses",
            description = "Retrieve a list of all addresses in the system",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of addresses retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Address.class),
                                    examples = @ExampleObject(name = "AddressExample", ref = "#/components/examples/AddressExample")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Addresses not found"
                    )
            }
    )
    @GetMapping
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @Operation(
            summary = "Create a new address",
            description = "Add a new address to the system by providing all required details.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Address object to be created",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Address.class),
                            examples = @ExampleObject(name = "Address request body", ref = "#/components/examples/AddressExample")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Address created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Address.class),
                                    examples = @ExampleObject(name = "Address Response Body", ref = "#/components/examples/AddressExample")

                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input, object creation failed"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Address> createNewAddress(@RequestBody Address address) {
        Address createdAddress = addressService.createAddress(address);
        return ResponseEntity.ok(createdAddress);
    }


    @Operation(
            summary = "Get address by ID",
            description = "Retrieve the details of a specific address by providing its unique ID.",
            parameters = {
                    @Parameter(
                            name = "addressId",
                            description = "Unique ID of the address to retrieve",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Address retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Address.class),
                                    examples = @ExampleObject(name = "Address Response", ref = "#/components/examples/AddressExample")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Address not found"
                    )
            }
    )
    @GetMapping("/{addressId}")
    public ResponseEntity<Address> getAddressById(@PathVariable int addressId) {
        Address address = addressService.getAddressById(addressId);
        if (address != null) {
            return ResponseEntity.ok(address);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Update an address",
            description = "Update the details of a specific address by providing its unique ID and updated data.",
            parameters = {
                    @Parameter(
                            name = "addressId",
                            description = "Unique ID of the address to update",
                            required = true,
                            example = "1"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated address details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Address.class),
                            examples = @ExampleObject(name = "Updated Address Request body", ref = "#/components/examples/AddressExample")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Address updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Address.class),
                                    examples = @ExampleObject(name = "Updated Address Response", ref = "#/components/examples/AddressExample")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Address not found"
                    )
            }
    )
    @PutMapping("/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable int addressId, @RequestBody Address address) {
        address.setId(addressId);
        Address updatedAddress = addressService.updateAddress(address);
        if (updatedAddress != null) {
            return ResponseEntity.ok(updatedAddress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Delete an address",
            description = "Delete a specific address by its unique ID.",
            parameters = {
                    @Parameter(
                            name = "addressId",
                            description = "Unique ID of the address to delete",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Address deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Address not found",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddressById(@PathVariable int addressId) {
        try {
            addressService.deleteAddress(addressId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
