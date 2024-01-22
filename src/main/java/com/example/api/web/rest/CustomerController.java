package com.example.api.web.rest;

import java.util.List;

import com.example.api.dto.CustomerDto;
import com.example.api.dto.PagingCustomerDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.api.service.CustomerService;

import javax.validation.Valid;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping
    public List<CustomerDto> findAll() {
        return service.findAll();
    }

    @PostMapping("/search")
    public List<CustomerDto> findAllByNameEmailGender(@RequestBody @Valid CustomerDto dto) {
        return service.findAllByNameEmailGender(dto);
    }

    @GetMapping("/search/{uf}/{localidade}")
    public List<CustomerDto> findDistinctByUfLocalidade(@PathVariable String uf, @PathVariable String localidade) {
        return service.findDistinctByUfLocalidade(uf, localidade);
    }

    @PostMapping("/search/paging")
    public PagingCustomerDto findAllByNameEmailGenderUfLocalidadePaging(@RequestBody @Valid PagingCustomerDto dto) {
        return service.findAllByNameEmailGenderUfLocalidadePaging(dto);
    }

    @GetMapping("/{id}")
    public CustomerDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody @Valid CustomerDto dto) {
        service.save(dto, false);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody @Valid CustomerDto dto) {
        service.save(dto, true);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}
