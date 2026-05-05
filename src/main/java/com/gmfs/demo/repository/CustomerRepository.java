package com.gmfs.demo.repository;

import com.gmfs.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByIdNumber(String idNumber);

    boolean existsByIdNumber(String idNumber);
}
