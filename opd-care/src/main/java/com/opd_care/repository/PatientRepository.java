package com.opd_care.repository;

import com.opd_care.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    boolean existsByPhone(String phone);
    List<Patient> findByNameContainingIgnoreCase(String name);
    List<Patient> findByPhone(String phone);
}
