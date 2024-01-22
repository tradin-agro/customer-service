package com.example.api.web.rest;

import com.example.api.dto.AddressDto;
import com.example.api.service.AddressService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private AddressService service;

    @GetMapping
    public List<AddressDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AddressDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/cep/{cep}")
    public List<AddressDto> findByCep(@PathVariable String cep) {
        return service.getAddressByCep(cep);
    }

    @GetMapping("/search/{uf}/{localidade}/{logradouro}")
    public List<AddressDto> getAddressByUfLocLog(@PathVariable String uf, @PathVariable String localidade,
											  @PathVariable String logradouro) {
        return service.getAddressByUfLocLog(uf, localidade, logradouro);
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody @Valid AddressDto dto) {
        service.save(dto, false);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody @Valid AddressDto dto) {
        service.save(dto, true);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}
