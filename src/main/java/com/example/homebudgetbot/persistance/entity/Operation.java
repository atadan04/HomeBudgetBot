package com.example.homebudgetbot.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private double sum;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private CategoryOperation categoryOperation;
    @OneToOne(cascade =CascadeType.ALL)
    @JoinColumn(name = "type_operation_id")
    private TypeOperation typeOperation;
}
