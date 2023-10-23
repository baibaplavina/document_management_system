package com.example.documentmanagement.insolvencyProcess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InsolvencyProcessRepository extends JpaRepository<InsolvencyProcess, Long>  {

   List<InsolvencyProcess> findInsolvencyProcessesByCaseClosingDateBefore(LocalDate now);
   List<InsolvencyProcess> findByCaseClosingDateIsNull();
}
