package com.example.documentmanagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Recipient {
   private String recipientName;
   private String recipientAddress;
   private String recipientEmail;
   private String recipientEAddress;
}
