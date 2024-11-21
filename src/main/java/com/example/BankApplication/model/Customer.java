package com.example.BankApplication.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class Customer {

    @Schema(description = "Unique identifier for the customer", example = "1")
    private int customerId;

    @Schema(description = "First name of the customer", example = "Shruti")
    private String firstName;

    @Schema(description = "Last name of the customer", example = "Saxena")
    private String lastName;

    @Schema(description = "Age of the customer", example = "22")
    private Integer age;

    @Schema(description = "Gender of the customer", example = "Female")
    private String gender;

    @Schema(description = "Email address of the customer", example = "shruti.saxena@google.com")
    private String emailAddress;

    @Schema(description = "PAN number of the customer", example = "BAJPC4350M")
    private String panNumber;

    @Schema(description = "Aadhar card number of the customer", example = "662223509284")
    private String aadharCardNumber;

    @Schema(description = "Unique identifier for the customer in the system", example = "SHRSAXBAJPC")
    private String customerUniqueId;

    @Schema(description = "Address details of the customer")
    private Address address;

    @Schema(description = "Phone details of the customer")
    private Phone phone;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getAadharCardNumber() {
        return aadharCardNumber;
    }

    public void setAadharCardNumber(String aadharCardNumber) {
        this.aadharCardNumber = aadharCardNumber;
    }

    public String getCustomerUniqueId() {
        return customerUniqueId;
    }

    public void setCustomerUniqueId(String customerUniqueId) {
        this.customerUniqueId = customerUniqueId;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", panNumber='" + panNumber + '\'' +
                ", aadharCardNumber='" + aadharCardNumber + '\'' +
                ", customerUniqueId='" + customerUniqueId + '\'' +
                '}';
    }
}
