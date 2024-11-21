package com.example.BankApplication.controller;

import com.example.BankApplication.model.Transaction;
import com.example.BankApplication.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Get Transaction History by accountId",
            description = "Endpoint to get transaction history of an account using its ID",
            parameters = {
                    @Parameter(
                            name = "transactionId",
                            description = "ID of the transaction history to get",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transaction history",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Transaction.class),
                                    examples = @ExampleObject(
                                            name = "Transaction History Body Example",
                                            summary = "Sample transaction data",
                                            value = """
                    {  "transactionId": 1,
                        "senderAccount": "0927203934",
                        "receiverAccount": "0170976263",
                        "amount": 1000.50,
                        "transactionType": "transfer",
                        "transactionDate": "2024-11-18T10:38:13"}
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Transaction id not found to be deleted"
                    )
            }
    )
    @GetMapping("/{accountId}/history")
   //PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String accountId) {
        List<Transaction> transactions = transactionService.getTransactionHistory(accountId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(
            summary = "Get a list of Transactions",
            description = "Endpoint to get a list of transactions",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transaction successfully retrieved",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Transaction.class),
                                    examples = @ExampleObject(
                                            name = "Transaction Example",
                                            summary = "Sample transaction data",
                                            value = """
                    {
                        "transactionId": 1,
                        "senderAccount": "0927203934",
                        "receiverAccount": "0170976263",
                        "amount": 1000.50,
                        "transactionType": "transfer",
                        "transactionDate": "2024-11-18T10:38:13"
                    }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Transaction not found"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @Operation(
            summary = "Get a transaction by ID",
            description = "Endpoint to retrieve a transaction using its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transaction successfully retrieved",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Transaction.class),
                                    examples = @ExampleObject(
                                            name = "Transaction Example",
                                            summary = "Sample transaction data",
                                            value = """
                    {
                        "transactionId": 1,
                        "senderAccount": "0927203934",
                        "receiverAccount": "0170976263",
                        "amount": 1000.50,
                        "transactionType": "transfer",
                        "transactionDate": "2024-11-18T10:38:13"
                    }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Transaction not found"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable int id) {
        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Update a Transaction",
            description = "Endpoint to update a transaction using its ID",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Transaction Request Example",
                                    summary = "Sample transaction request data",
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
    public ResponseEntity<Transaction> updateTransaction(@PathVariable int id, @RequestBody Transaction transaction) {
        transaction.setTransactionId(id);
        try {
            Transaction updatedTransaction = transactionService.updateTransaction(transaction);
            return ResponseEntity.ok(updatedTransaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Delete a Transaction",
            description = "Endpoint to delete a transaction using its ID",
            parameters = {
                    @Parameter(
                            name = "transactionId",
                            description = "ID of the transaction to delete",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transaction deleted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Transaction.class),
                                    examples = @ExampleObject(
                                            name = "Transaction Request Body Example",
                                            summary = "Sample transaction data",
                                            value = """
                    { "transactionId": "1", "message": "Transaction deleted successfully" }
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Transaction id not found to be deleted"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
