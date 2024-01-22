package com.example.api.service;

import java.util.*;
import java.util.stream.Collectors;

import com.example.api.dto.CustomerDto;
import com.example.api.dto.PagingCustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.api.domain.Customer;
import com.example.api.repository.CustomerRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private Validator validator;

    public List<CustomerDto> findAll() {
        return listEntityToDto(repository.findAllByOrderByNameAsc());
    }

    public List<CustomerDto> findAllByNameEmailGender(CustomerDto dto) {
        return listEntityToDto(
                repository.findAllByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContaining(
                        dto.getName(), dto.getEmail(), dto.getGender()));
    }

    public List<CustomerDto> findDistinctByUfLocalidade(String uf, String localidade) {
        return listEntityToDto(repository.findDistinctByAddressUfIgnoreCaseContainingAndAddressLocalidadeIgnoreCaseContaining(
                uf, localidade));
    }

    public PagingCustomerDto findAllByNameEmailGenderUfLocalidadePaging(PagingCustomerDto dto) {
        if(!dto.getFilterCustomers().getLocalidade().isEmpty() || !dto.getFilterCustomers().getUf().isEmpty()) {
            dto.setCustomers(queryWithAddress(dto));
        } else {
            dto.setCustomers(query(dto));
        }
        return dto;
    }

    private List<CustomerDto> query(PagingCustomerDto dto) {
        int totalRecords = repository.countAllByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContaining(
                dto.getFilterCustomers().getName(), dto.getFilterCustomers().getEmail(),
                dto.getFilterCustomers().getGender());
        dto.setTotalRecords(totalRecords);

        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize(), Sort.by(dto.getSortBy()).ascending());

        return listEntityToDto(
                repository.findAllByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContaining(
                        dto.getFilterCustomers().getName(), dto.getFilterCustomers().getEmail(),
                        dto.getFilterCustomers().getGender(),
                        pageable));
    }

    private List<CustomerDto> queryWithAddress(PagingCustomerDto dto) {
        int totalRecords = repository.countDistinctByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContainingAndAddressUfIgnoreCaseContainingAndAddressLocalidadeIgnoreCaseContaining(
                dto.getFilterCustomers().getName(), dto.getFilterCustomers().getEmail(),
                dto.getFilterCustomers().getGender(), dto.getFilterCustomers().getUf(),
                dto.getFilterCustomers().getLocalidade());
        dto.setTotalRecords(totalRecords);

        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize(), Sort.by(dto.getSortBy()).ascending());

        return listEntityToDto(
                repository.findDistinctByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContainingAndAddressUfIgnoreCaseContainingAndAddressLocalidadeIgnoreCaseContaining(
                        dto.getFilterCustomers().getName(), dto.getFilterCustomers().getEmail(),
                        dto.getFilterCustomers().getGender(), dto.getFilterCustomers().getUf(),
                        dto.getFilterCustomers().getLocalidade(),
                        pageable));
    }

    public CustomerDto findById(Long id) {
        return entityToDto(repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer not found")));
    }

    public void save(CustomerDto dto, boolean isUpdate) {
        executeValidations(dto);
        Customer customer = dtoToEntity(dto);
        if (isUpdate) {
            repository.findById(dto.getId())
                    .orElseThrow(() -> new NoSuchElementException("Customer not found"));
            customer.setId(dto.getId());
        }
        repository.save(customer);
    }

    public void delete(long id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));
        repository.delete(customer);
    }

    private void executeValidations(CustomerDto dto) {
        Set<ConstraintViolation<Customer>> violations = validator.validate(dtoToEntity(dto));
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
    }

    private List<Customer> listDtoToListEntity(List<CustomerDto> dto) {
        return dto.stream().map(d -> new Customer(d.getId(), d.getName(), d.getEmail(), d.getGender()))
                .collect(Collectors.toList());
    }

    private List<CustomerDto> listEntityToDto(List<Customer> custumers) {
        return custumers.stream().map(c -> new CustomerDto(c.getId(), c.getName(), c.getEmail(), c.getGender(),
                addressService.listEntityToDto(c.getAddress())))
                .collect(Collectors.toList());
    }

    public Customer dtoToEntity(CustomerDto dto) {
        return new Customer(dto.getId(), dto.getName(), dto.getEmail(), dto.getGender());
    }

    public CustomerDto entityToDto(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getName(), customer.getEmail(), customer.getGender(), addressService.listEntityToDto(customer.getAddress()));
    }

}
