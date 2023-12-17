package com.example.homebudgetbot.transformer;

import com.example.homebudgetbot.persistance.dto.OperationDto;
import com.example.homebudgetbot.persistance.entity.Operation;

public class OperationTransformer implements Transformer<Operation, OperationDto>{
    @Override
    public OperationDto transformTo(Operation entity) {
        return OperationDto.builder()
                .id(entity.getId())
                .sum(entity.getSum())
                .categoryOperationId(entity.getCategoryOperation().getId())
                .typeOperationId(entity.getTypeOperation().getId())
                .build();
    }

    @Override
    public Operation transformFrom(OperationDto transferObj) {
//        return Operation.builder()
//                .id(transferObj.getId())
//                .sum(transferObj.getSum())
//                .categoryOperation()
//                .typeOperationId(transferObj)
//                .build();
        return null;
    }
    ///реализовать создание нового объекта при отправке сообщения на бот

}
