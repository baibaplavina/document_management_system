package com.example.documentmanagement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Recipient {
   @Id
   @GeneratedValue()
   private Long id;
   private String recipientName;
   private String recipientAddress;
   private String recipientEmail;
   private String recipientEAddress;
}
