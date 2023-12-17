package com.example.homebudgetbot.persistance.repository;

import com.example.homebudgetbot.persistance.entity.CategoryOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryOperation, UUID> {
    @Query("SELECT C FROM CategoryOperation C WHERE C.category=:str")
    Optional<CategoryOperation> findCategoryOperationByCategoryContaining(@Param("str") String str);
}
