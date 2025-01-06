package com.example.bookstoreapp.repository;

public interface SpecificationProviderManager<T> {

    StringSpecificationProvider<T> getStringSpecificationProvider(String key);

    DoubleSpecificationProvider<T> getDoubleSpecificationProvider(String key);
}
