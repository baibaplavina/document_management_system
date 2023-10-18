package com.example.documentmanagement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InsolvencyProcessRepository extends JpaRepository<InsolvencyProcess, Long>  {
   List<InsolvencyProcess> findInsolvencyProcessByCaseClosingDate(LocalDate date);


}
