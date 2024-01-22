package com.example.api.service;

import com.example.api.domain.Address;
import com.example.api.dto.AddressDto;
import com.example.api.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private Validator validator;

    @Autowired
    private ApiViaCepService apiViaCepService;

    public List<AddressDto> findAll() {
        return listEntityToDto(repository.findAll());
    }

    public AddressDto findById(Long id) {
        return entityToDto(repository.findById(id).orElseThrow(() -> new NoSuchElementException("Customer not found")));
    }

    public List<AddressDto> getAddressByCep(String cep){
        return listEntityToDto(apiViaCepService.getAddressByCep(cep));
    }

    public List<AddressDto> getAddressByUfLocLog(String uf, String localidade, String logradouro){
        return listEntityToDto(apiViaCepService.getAddressByUfLocLog(uf, localidade, logradouro));
    }

    public void save(AddressDto dto, boolean isUpdate) {
        executeValidations(dto);

        Address address = apiViaCepService.getAddressByCep(dto.getCep()).get(0);

        if (isUpdate) {
            repository.findById(dto.getId())
                    .orElseThrow(() -> new NoSuchElementException("Address not found"));
            address.setId(dto.getId());
        }

        address.setCustomer(
                customerService.dtoToEntity(
                customerService.findById(dto.getCustomer().getId())));

        repository.save(address);
    }

    public void delete(long id) {
        Address address = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Address not found"));
        repository.delete(address);
    }

    private void executeValidations(AddressDto dto) {
        Set<ConstraintViolation<Address>> violations = validator.validate(dtoToEntity(dto));
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
    }

    private List<Address> listDtoToListEntity(List<AddressDto> dto) {
        return dto.stream().map(d -> new Address(d.getId(), d.getCep(), d.getLogradouro(), d.getComplemento(),
                d.getBairro(), d.getLocalidade(), d.getUf(), d.getIbge(), d.getGia(), d.getDdd(),
                d.getSiafi(), d.getErro())).collect(Collectors.toList());
    }

    public List<AddressDto> listEntityToDto(List<Address> addresses) {
        return addresses.stream().map(a -> new AddressDto(a.getId(), a.getCep(), a.getLogradouro(),
                a.getComplemento(), a.getBairro(), a.getLocalidade(), a.getUf(), a.getIbge(), a.getGia(),
                a.getDdd(), a.getSiafi(), a.getErro())).collect(Collectors.toList());
    }

    private Address dtoToEntity(AddressDto dto) {
        return new Address(dto.getId(), dto.getCep(), dto.getLogradouro(), dto.getComplemento(), dto.getBairro(),
                dto.getLocalidade(), dto.getUf(), dto.getIbge(), dto.getGia(), dto.getDdd(), dto.getSiafi(), dto.getErro());
    }

    private AddressDto entityToDto(Address address) {
        return new AddressDto(address.getId(), address.getCep(), address.getLogradouro(), address.getComplemento(), address.getBairro(),
                address.getLocalidade(), address.getUf(), address.getIbge(), address.getGia(), address.getDdd(), address.getSiafi(), address.getErro());
    }

}
