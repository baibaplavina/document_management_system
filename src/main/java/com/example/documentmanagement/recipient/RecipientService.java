package com.example.documentmanagement.recipient;

import com.example.documentmanagement.recipient.Recipient;
import com.example.documentmanagement.recipient.RecipientRepository;
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
