package com.hotel.reservation.customer_service.service;

import com.google.gson.Gson;
import com.hotel.reservation.customer_service.entity.Customer;
import com.hotel.reservation.customer_service.model.NotificationContext;
import com.hotel.reservation.customer_service.repository.CustomerRepository;
import com.hotel.reservation.customer_service.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private CustomerRepository customerRepository;

    public String authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        return jwtUtil.generateToken(email);
    }


    public Customer save(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer =  customerRepository.save(customer);
        sendRegistrationNotification(customer);
        return customer;
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public void sendRegistrationNotification(Customer customer) {
        NotificationContext nc = new NotificationContext();

        String confirmationMessageTemplate = "Thanks %s for choosing us as your comfort partner.\n" +
                "You have successfully registered!!" +
                "-Great Comfort Hotels\n";

        String confirmationMessage = String.format(confirmationMessageTemplate,
                customer.getFirstName() +" "+ customer.getLastName()
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
        kafkaService.sendMessage("notification-topic",gson.toJson(nc));
    }
}