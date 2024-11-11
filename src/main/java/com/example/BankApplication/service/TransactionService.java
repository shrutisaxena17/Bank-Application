package com.example.BankApplication.service;

import com.example.BankApplication.dao.TransactionDAO;
import com.example.BankApplication.exception.NotFoundException;
import com.example.BankApplication.model.Customer;
import com.example.BankApplication.model.Transaction;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionDAO transactionDAO;

    @Autowired
    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @Autowired
    CustomerService customerService;

    @Autowired
    OtpService otpService;

    public void recordDeposit(String accountNumber, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setSenderAccount(null);
        transaction.setReceiverAccount(accountNumber);
        transaction.setAmount(amount);
        transaction.setTransactionType("DEPOSIT");
        transaction.setTransactionDate(LocalDateTime.now());

        transactionDAO.create(transaction);
    }

    public void recordWithdrawal(String accountNumber, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setSenderAccount(accountNumber);
        transaction.setReceiverAccount(null);
        transaction.setAmount(amount);
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setTransactionDate(LocalDateTime.now());

        transactionDAO.create(transaction);
    }

    public void sendToAnotherAccount(String senderAccountNumber, String receiverAccountNumber, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setSenderAccount(senderAccountNumber);
        transaction.setReceiverAccount(receiverAccountNumber);
        transaction.setAmount(amount);
        transaction.setTransactionType("TRANSFER");
        transaction.setTransactionDate(LocalDateTime.now());

        transactionDAO.create(transaction);
    }

    public List<Transaction> getTransactionHistory(String accountNumber) {
        return transactionDAO.findTransactionHistoryByAccountNumber(accountNumber);
    }

    public List<Transaction> getAllTransactions() {
        return transactionDAO.findAll();
    }

    public Transaction getTransactionById(int id) {
        try {
            Transaction transaction = transactionDAO.findById(id);
            if (transaction == null) {
                throw new NotFoundException("Transaction does not exist with ID: " + id);
            }
            return transaction;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Transaction does not exist with ID: " + id);
        }
    }

    public Transaction updateTransaction(Transaction transaction) {
        int updatedRows = transactionDAO.update(transaction);
        if (updatedRows > 0) {
            return transaction;
        } else {
            throw new NotFoundException("Failed to update transaction with ID: " + transaction.getTransactionId());
        }
    }

    public void deleteTransaction(int id) {
        try {
            int deletedRows = transactionDAO.delete(id);
            if (deletedRows == 0) {
                throw new NotFoundException("Failed to delete transaction with ID: " + id);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Transaction does not exist with ID: " + id);
        }
    }

    @Scheduled(cron="0 * * * * *")
    public void sendHourlyTransactionReceipts(){
        System.out.println("We are in the scheduler method");
        LocalDateTime hourAgo = LocalDateTime.now().minusHours(1);
        List<Transaction> transactionList = transactionDAO.findAll();
        //filter transactions -> that occurred in the last hour
        List<Transaction> transactionsInLastHour = transactionList.stream()
                .filter(transaction -> transaction.getTransactionDate().isAfter(hourAgo))
                .collect(Collectors.toList());
        //group transactions by customer
        Map<String,List<Transaction>> transactionsByCustomer = transactionsInLastHour.stream()
                .collect(Collectors.groupingBy(transaction ->
                        transaction.getReceiverAccount()!=null? transaction.getReceiverAccount() : transaction.getSenderAccount()));
        //send transaction receipts to customer
        for (Map.Entry<String, List<Transaction>> entry : transactionsByCustomer.entrySet()) {
            String accountId = entry.getKey();
            List<Transaction> transactions = entry.getValue();
            // Retrieve customer details based on accountId
            Customer customer = customerService.findCustomerByAccountId(accountId);
            if (customer != null && customer.getEmailAddress() != null) {
                //sendTransactionReceipt(customer.getEmailAddress(), transactions);
                sendTransactionReceiptWithPdf(customer.getEmailAddress(),transactions);
            }
        }
    }

    /*
    public void sendTransactionReceipt(String email, List<Transaction> transactions) {
        StringBuilder receipt = new StringBuilder("Dear customer,\n\nHere is your transaction receipt for the last hour:\n\n");

        for (Transaction transaction : transactions) {
            receipt.append("Transaction ID: ").append(transaction.getTransactionId()).append("\n")
                    .append("Type: ").append(transaction.getTransactionType()).append("\n")
                    .append("Amount: ").append(transaction.getAmount()).append("\n")
                    .append("Date: ").append(transaction.getTransactionDate()).append("\n\n");
        }

        receipt.append("Thank you for using our service.\n\nBest regards,\nBank Application Team");

        otpService.sendEmail(email, "Transaction Receipt", receipt.toString());
    }
     */

    public void sendTransactionReceiptWithPdf(String email, List<Transaction> transactions) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Add transaction details to PDF
        document.add(new Paragraph("Dear customer,"));
        document.add(new Paragraph("\nHere is your transaction receipt for the last hour:"));

        for (Transaction transaction : transactions) {
            document.add(new Paragraph("Transaction ID: " + transaction.getTransactionId()));
            document.add(new Paragraph("Type: " + transaction.getTransactionType()));
            document.add(new Paragraph("Amount: " + transaction.getAmount()));
            document.add(new Paragraph("Date: " + transaction.getTransactionDate()));
            document.add(new Paragraph("\n"));
        }

        document.add(new Paragraph("\nThank you for using our service."));
        document.add(new Paragraph("\nBest regards,"));
        document.add(new Paragraph("Bank Application Team"));

        // Close the document
        document.close();

        // Step 2: Send the email with the generated PDF as an attachment
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        otpService.sendEmailWithAttachment(email, "Transaction Receipt", "Here is your transaction receipt PDF.", pdfBytes, "transaction_receipt.pdf");
    }

}
