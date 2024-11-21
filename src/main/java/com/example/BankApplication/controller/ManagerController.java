package com.example.BankApplication.controller;

import com.example.BankApplication.model.Manager;
import com.example.BankApplication.service.ManagerService;
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
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @Operation(
            summary = "Get a list of Managers",
            description = "Endpoint to get a list of all Managers",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Managers successfully retrieved",
                            content = @ Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Manager.class),
                                    examples = @ExampleObject(
                                            name = "Manager List Example",
                                            summary = "Sample manager data",
                                            value = """
                    [{
                       "id": 1,
                       "firstName": "Anurag",
                       "lastName": "Johri",
                       "age": 33,
                       "emailAddress": "anurag.johri@gmail.com",
                       "address": {
                           "id": 5,
                           "street": "Sector-41",
                           "city": "Noida",
                           "state": "Delhi NCR",
                           "pincode": "201101",
                           "country": "India",
                           "customerUniqueId": null,
                           "managerId": 1
                       },
                       "phone": {
                           "id": 5,
                           "number": "9903121322",
                           "type": "Mobile",
                           "customerUniqueId": null,
                           "managerId": 1
                       }
                    }]
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No managers found"
                    )
            }
    )
    @GetMapping
    public List<Manager> getAllManagers() {
        return managerService.getAllManagers();
    }

    @Operation(
            summary = "Create a new Manager",
            description = "Endpoint to create a new Manager",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Manager Request Example",
                                    summary = "Sample manager request data",
                                    value = """
                {
                   "firstName": "Anurag",
                   "lastName": "Johri",
                   "age": 33,
                   "emailAddress": "anurag.johri@gmail.com",
                   "address": {
                       "id": 5,
                       "street": "Sector-41",
                       "city": "Noida",
                       "state": "Delhi NCR",
                       "pincode": "201101",
                       "country": "India"
                   },
                   "phone": {
                       "id": 5,
                       "number": "9903121322",
                       "type": "Mobile"
                   }
                }
                """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Manager created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Manager.class),
                                    examples = @ExampleObject(
                                            name = "Manager Created Successfully Example",
                                            summary = "Sample manager data after creation",
                                            value = """
                     {
                       "id": 1,
                       "firstName": "Anurag",
                       "lastName": "Johri",
                       "age": 33,
                       "emailAddress": "anurag.johri@gmail.com",
                       "address": {
                           "id": 5,
                           "street": "Sector-41",
                           "city": "Noida",
                           "state": "Delhi NCR",
                           "pincode": "201101",
                           "country": "India",
                           "customerUniqueId": null,
                           "managerId": 1
                       },
                       "phone": {
                           "id": 5,
                           "number": "9903121322",
                           "type": "Mobile",
                           "customerUniqueId": null,
                           "managerId": 1
                       }
                    }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Void> createManager(@RequestBody Manager manager) {
        managerService.createManager(manager);
        return ResponseEntity.status(201).build();
    }

    @Operation(
            summary = "Get Manager by ID",
            description = "Endpoint to get manager data using its ID",
            parameters = {
                    @Parameter(
                            name = "managerId",
                            description = "ID of the manager to get",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Manager information retrieved",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Manager.class),
                                    examples = @ExampleObject(
                                            name = "Manager Response Body Example",
                                            summary = "Sample manager data",
                                            value = """
                    {
                       "id": 1,
                       "firstName": "Anurag",
                       "lastName": "Johri",
                       "age": 33,
                       "emailAddress": "anurag.johri@gmail.com",
                       "address": {
                           "id": 5,
                           "street": "Sector-41",
                           "city": "Noida",
                           "state": "Delhi NCR",
                           "pincode": "201101",
                           "country": "India",
                           "customerUniqueId": null,
                           "managerId": 1
                       },
                       "phone": {
                           "id": 5,
                           "number": "9903121322",
                           "type": "Mobile",
                           "customerUniqueId": null,
                           "managerId": 1
                       }
                    }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Manager not found"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Manager> getManagerById(@PathVariable int id) {
        Manager manager = managerService.getManagerById(id);
        if (manager != null) {
            return ResponseEntity.ok(manager);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Update Manager",
            description = "Endpoint to update manager data using its ID",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Manager Update Example",
                                    summary = "Sample manager data to update",
                                    value = """
                {
                   "id": 1,
                   "firstName": "Anurag",
                   "lastName": "Johri",
                   "age": 34,
                   "emailAddress": "anurag.johri_updated@gmail.com",
                   "address": {
                       "id": 5,
                       "street": "Sector-41",
                       "city": "Noida",
                       "state": "Delhi NCR",
                       "pincode": "201102",
                       "country": "India"
                   },
                   "phone": {
                       "id": 5,
                       "number": "9903121322",
                       "type": "Mobile"
                   }
                }
                """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Manager updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Manager.class),
                                    examples = @ExampleObject(
                                            name = "Manager Updated Successfully Example",
                                            summary = "Sample manager data after update",
                                            value = """
                     {
                       "id": 1,
                       "firstName": "Anurag",
                       "lastName": "Johri",
                       "age": 34,
                       "emailAddress": "anurag.johri_updated@gmail.com",
                       "address": {
                           "id": 5,
                           "street": "Sector-41",
                           "city": "Noida",
                           "state": "Delhi NCR",
                           "pincode": "201102",
                           "country": "India",
                           "customerUniqueId": null,
                           "managerId": 1
                       },
                       "phone": {
                           "id": 5,
                           "number": "9903121322",
                           "type": "Mobile",
                           "customerUniqueId": null,
                           "managerId": 1
                       }
                    }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Manager not found"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Manager> updateManager(@PathVariable int id, @RequestBody Manager manager) {
        manager.setId(id);
        try {
            Manager updatedManager = managerService.updateManager(manager);
            return ResponseEntity.ok(updatedManager);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Delete Manager by ID",
            description = "Endpoint to delete a manager using its ID",
            parameters = {
                    @Parameter(
                            name = "managerId",
                            description = "ID of the manager to delete",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Manager deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Manager not found"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable int id) {
        try {
            managerService.deleteManager(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
