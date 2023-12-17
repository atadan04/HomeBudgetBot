package com.example.homebudgetbot.persistance.service;

import com.example.homebudgetbot.persistance.entity.TypeOperation;
import com.example.homebudgetbot.persistance.repository.TypeOperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class TypeOperationService {
    private final TypeOperationRepository typeOperationRepository;
    public TypeOperation save(TypeOperation typeOperation){
        return typeOperationRepository.save(typeOperation);
    }
    public void deleteById(int id){
        typeOperationRepository.deleteById(id);
    }

    public List<TypeOperation> findAll(){
        return typeOperationRepository.findAll();
    }
    public Optional<TypeOperation> findById(int id){
        return typeOperationRepository.findById(id);

    }
}
