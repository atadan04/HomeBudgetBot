package com.example.homebudgetbot.persistance.service;

import com.example.homebudgetbot.persistance.entity.CategoryOperation;
import com.example.homebudgetbot.persistance.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private CategoryRepository categoryRepository;
    public CategoryOperation save(CategoryOperation categoryOperation){
        return categoryRepository.save(categoryOperation);
    }
    public void deleteById(UUID id){
        categoryRepository.deleteById(id);
    }

    public List<CategoryOperation> findAll(){
        return categoryRepository.findAll();
    }
    public Optional<CategoryOperation> findById(UUID id){
        return categoryRepository.findById(id);

    }
    public Optional<CategoryOperation> findCategoryOperationByCategoryContaining(String str){
        return categoryRepository.findCategoryOperationByCategoryContaining(str);
    }


}
