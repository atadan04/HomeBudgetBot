package com.example.homebudgetbot.transformer;

public interface Transformer<E,D>{
    D transformTo(E entity);
    E transformFrom(D transferObj);
}
