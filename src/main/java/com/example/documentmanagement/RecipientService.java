package com.example.documentmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipientService {
    @Autowired
    public RecipientRepository recipientRepository;

    List<Recipient> findAll() {
        return recipientRepository.findAll();

    }
}
