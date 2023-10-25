package com.example.documentmanagement.insolvencyProcess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InsolvencyProcessRepository extends JpaRepository<InsolvencyProcess, Long>  {

}
