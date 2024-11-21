package com.example.BankApplication.controller;

import com.example.BankApplication.model.Phone;
import com.example.BankApplication.model.Transaction;
import com.example.BankApplication.service.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phone")
public class PhoneController {

    private final PhoneService phoneService;

    @Autowired
    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @Operation(
            summary = "Get a list of Phone Numbers",
            description = "Endpoint to get a list of Phone Numbers",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Phones successfully retrieved",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Phone.class),
                                    examples = @ExampleObject(
                                            name = "Phone Example",
                                            summary = "Sample phone data",
                                            value = """
                    {
                       "id": 1,
                       "number": "9560891243",
                       "type": "Mobile",
                        "customerUniqueId": "SHRSAXBAJPC",
                        "managerId": null
                    } {
                       "id": 1,
                       "number": "9560891243",
                       "type": "Mobile",
                        "customerUniqueId": "SHRSAXBAJPC",
                        "managerId": null
                    }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Phone not found"
                    )
            }
    )
    @GetMapping
    public List<Phone> getAllPhones() {
        return phoneService.getAllPhones();
    }


    @Operation(
            summary = "Post a Phone",
            description = "Endpoint to post a phone",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Phone Request Example",
                                    summary = "Sample phone request data",
                                    value = """
                {
                       "number": "9560891243",
                       "type": "Mobile",
                        "customerUniqueId": "SHRSAXBAJPC",
                        "managerId": null
                }
                """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Phone posted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Phone.class),
                                    examples = @ExampleObject(
                                            name = "Phone Added Successfully Example",
                                            summary = "Sample transaction data",
                                            value = """
                     {
                       "id": 1,
                       "number": "9560891243",
                       "type": "Mobile",
                        "customerUniqueId": "SHRSAXBAJPC",
                        "managerId": null
                    }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Phone could not be added"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Phone> createPhone(@RequestBody Phone phone) {
        Phone createdPhone = phoneService.createPhone(phone);
        return ResponseEntity.status(201).body(createdPhone);
    }


    @Operation(
            summary = "Get Phone data by using phone id",
            description = "Endpoint to get phone data using its ID",
            parameters = {
                    @Parameter(
                            name = "transactionId",
                            description = "ID of the phone information to get",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Phone Information",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Transaction.class),
                                    examples = @ExampleObject(
                                            name = "Phone Response  Body Example",
                                            summary = "Sample transaction data",
                                            value = """
                    {   "id": 1,
                       "number": "9560891243",
                       "type": "Mobile",
                        "customerUniqueId": "SHRSAXBAJPC",
                        "managerId": null
                        }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Phone id not found to be retrieved"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Phone> getPhoneById(@PathVariable int id) {
        Phone phone = phoneService.getPhoneById(id);
        if (phone != null) {
            return ResponseEntity.ok(phone);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Update a Phone",
            description = "Endpoint to update a phone using its ID",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Phone Request Example",
                                    summary = "Sample phone request data",
                                    value = """
                {
                    "transactionId": 1,
                    "senderAccount": "0927203934",
                    "receiverAccount": "0170976263",
                    "amount": 500,
                    "transactionType": "transfer",
                    "transactionDate": "2024-11-18T11:08:45.015Z"
                }
                """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transaction updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Transaction.class),
                                    examples = @ExampleObject(
                                            name = "Transaction Response Body Example",
                                            summary = "Sample transaction data",
                                            value = """
{Transaction updated successfully}
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Transaction id not found to be updated"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Phone> updatePhone(@PathVariable int id, @RequestBody Phone phone) {
        phone.setId(id);
        try {
            Phone updatedPhone = phoneService.updatePhone(phone);
            return ResponseEntity.ok(updatedPhone);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Delete a Phone",
            description = "Endpoint to delete a phone using its ID",
            parameters = {
                    @Parameter(
                            name = "phoneId",
                            description = "ID of the phone to delete",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Phone deleted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Transaction.class),
                                    examples = @ExampleObject(
                                            name = "Phone Deleted Successfully",
                                            summary = "Sample transaction data",
                                            value = """
                    { "phoneId": "1", "message": "Phone deleted successfully" }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Phone id not found to be deleted"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhone(@PathVariable int id) {
        try {
            phoneService.deletePhone(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
