package com.example.api.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.example.api.domain.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long>, PagingAndSortingRepository<Customer, Long> {

    List<Customer> findAllByOrderByNameAsc();

    List<Customer> findAllByNameContaining(String name);

    List<Customer> findAllByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContaining(String name, String email, String gender);

    List<Customer> findAllByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContaining(String name, String email, String gender, Pageable pageable);

    int countAllByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContaining(String name, String email, String gender);

    int countDistinctByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContainingAndAddressUfIgnoreCaseContainingAndAddressLocalidadeIgnoreCaseContaining(String name, String email, String gender, String uf, String localidade);

    List<Customer> findDistinctByAddressUfIgnoreCaseContainingAndAddressLocalidadeIgnoreCaseContaining(String uf, String localidade);

    List<Customer> findDistinctByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContainingAndAddressUfIgnoreCaseContainingAndAddressLocalidadeIgnoreCaseContaining(String name, String email, String gender, String uf, String localidade, Pageable pageable);

}
