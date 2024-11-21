package com.example.BankApplication.config;

import com.example.BankApplication.model.Address;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig {

    @Value("${app.name}")
    private String appName;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/bank_management");
        dataSource.setUsername("root");
        dataSource.setPassword("shruti");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("shrutdlf@gmail.com");
        mailSender.setPassword("kisjrbygwcosalmc");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Address address = new Address();
        address.setStreet("B-64 DLF Colony");
        address.setCity("Ghaziabad");
        address.setState("UP");
        address.setPincode("201005");
        address.setCountry("India");
        address.setCustomerUniqueId("SHRSAXBAJPC");
        address.setManagerId(null);


        Example addressExample = new Example().value(address);


        return new OpenAPI()
                .components(new Components().addExamples("AddressExample", addressExample))
                .info(new Info().title("Bank API")
                        .version("1.0")
                        .description("API for managing bank addresses"));
    }

    public void print(){
        System.out.println("Application Name:"+appName);
    }

}