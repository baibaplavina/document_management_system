package com.example.documentmanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsolvencyProcessRepository extends JpaRepository<InsolvencyProcess, Long> {
}
