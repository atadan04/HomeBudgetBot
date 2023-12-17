package com.example.homebudgetbot.persistance.repository;

import com.example.homebudgetbot.persistance.entity.TypeOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TypeOperationRepository extends JpaRepository<TypeOperation, Integer> {
}