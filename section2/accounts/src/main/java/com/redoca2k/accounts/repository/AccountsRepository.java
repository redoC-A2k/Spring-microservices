package com.redoca2k.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.redoca2k.accounts.entity.Accounts;

import jakarta.transaction.Transactional;

import java.util.Optional;
@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long>{
    Optional<Accounts> findByCustomerId(Long customerId);
    
    // since this query method is written by us hence we do not  
    @Transactional 
    @Modifying
    void deleteByCustomerId(Long customerId);
}
