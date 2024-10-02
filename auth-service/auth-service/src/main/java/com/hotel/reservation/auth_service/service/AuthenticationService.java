package com.hotel.reservation.auth_service.service;

import com.google.gson.Gson;
import com.hotel.reservation.auth_service.entity.Customer;
import com.hotel.reservation.auth_service.model.NotificationContext;
import com.hotel.reservation.auth_service.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private CustomerService customerService;

    public String authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        return jwtUtil.generateToken(email);
    }

    public Customer findByEmail(String email) {
        return customerService.getCustomerDetails(email).block();
    }

    public void sendRegistrationNotification(Customer customer) {
        NotificationContext nc = new NotificationContext();

        String confirmationMessageTemplate = "Thanks %s for choosing us as your comfort partner.\n" +
                "You have successfully registered!!" +
                "-Stay Well Hotels\n";

        String confirmationMessage = String.format(confirmationMessageTemplate,
                customer.getFirstName() + " " + customer.getLastName()
        );

        nc.setBody(confirmationMessage);
        nc.setType("email");
        nc.setSeverity("Low");
        nc.setCreatedAt(new Date());
        Map<String, String> context = new HashMap<>();
        context.put("to", customer.getEmail());
        context.put("sub", "Successful registration");
        nc.setContext(context);

        Gson gson = new Gson();
        kafkaService.sendMessage("notification-topic", gson.toJson(nc));
    }

    public Customer save(Customer customer) {
        customer =  customerService.saveCustomer(customer).block();
        sendRegistrationNotification (customer);
        return customer;
    }
}