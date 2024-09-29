package com.redoca2k.accounts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.redoca2k.accounts.entity.Customer;
import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Optional<Customer> findByMobileNumber(String mobileNumber);
}
