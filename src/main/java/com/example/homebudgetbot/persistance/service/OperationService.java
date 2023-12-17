package com.example.homebudgetbot.persistance.service;

import com.example.homebudgetbot.persistance.entity.Operation;
import com.example.homebudgetbot.persistance.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OperationService {
    private final OperationRepository operationRepository;
    public Operation save(Operation operation){
        return operationRepository.save(operation);
    }
    public List<Operation> findAll(){
     return  operationRepository.findAll();
    }
    public Optional<Operation> findById(UUID id){
        return operationRepository.findById(id);
    }
    public void deleteById(UUID id){
        operationRepository.deleteById(id);
    }

}

