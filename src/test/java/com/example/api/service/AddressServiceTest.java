package com.example.api.service;

import com.example.api.domain.Address;
import com.example.api.domain.Customer;
import com.example.api.dto.AddressDto;
import com.example.api.repository.AddressRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private Validator validator;

    @Mock
    ApiViaCepService apiViaCepService;

    @Mock
    CustomerService customerService;

    @Captor
    private ArgumentCaptor<Address> addressArgumentCaptor;

    private final ObjectMapper mapper = new ObjectMapper();

    private final TypeReference<List<Address>> addressTypeList = new TypeReference<>() {
    };

    @Test
    void findAll() throws JsonProcessingException {

        String addDto1 = "{\"id\":\"1\",\"cep\":\"78300-112\",\"logradouro\":\"Rua Francisco Ferreira Ramos\",\"complemento\":\"\",\"bairro\":\"Centro\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":\"5107958\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9185\"}";
        AddressDto addressDto1 = mapper.readValue(addDto1, AddressDto.class);

        String addDto2 = "{\"id\":\"2\",\"cep\":\"78300-110\",\"logradouro\":\"Rua José Alves de Souza\",\"complemento\":\"\",\"bairro\":\"Centro\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":\"5107958\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9185\"}";
        AddressDto addressDto2 = mapper.readValue(addDto2, AddressDto.class);

        String addDto3 = "{\"id\":\"3\",\"cep\":\"78043-695\",\"logradouro\":\"Avenida Miguel Sutil\",\"complemento\":\"lado par\",\"bairro\":\"Jardim Santa Marta\",\"localidade\":\"Cuiabá\",\"uf\":\"MT\",\"ibge\":\"5103403\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9067\"}";
        AddressDto addressDto3 = mapper.readValue(addDto3, AddressDto.class);

        List<AddressDto> listAddressDto = new ArrayList<>();
        listAddressDto.add(addressDto1);
        listAddressDto.add(addressDto2);
        listAddressDto.add(addressDto3);

        String add1 = "{\"id\":\"1\",\"cep\":\"78300-112\",\"logradouro\":\"Rua Francisco Ferreira Ramos\",\"complemento\":\"\",\"bairro\":\"Centro\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":\"5107958\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9185\"}";
        Address address1 = mapper.readValue(add1, Address.class);

        String add2 = "{\"id\":\"2\",\"cep\":\"78300-110\",\"logradouro\":\"Rua José Alves de Souza\",\"complemento\":\"\",\"bairro\":\"Centro\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":\"5107958\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9185\"}";
        Address address2 = mapper.readValue(add2, Address.class);

        String add3 = "{\"id\":\"3\",\"cep\":\"78043-695\",\"logradouro\":\"Avenida Miguel Sutil\",\"complemento\":\"lado par\",\"bairro\":\"Jardim Santa Marta\",\"localidade\":\"Cuiabá\",\"uf\":\"MT\",\"ibge\":\"5103403\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9067\"}";
        Address address3 = mapper.readValue(add3, Address.class);

        List<Address> listAddress = new ArrayList<>();
        listAddress.add(address1);
        listAddress.add(address2);
        listAddress.add(address3);

        BDDMockito.given(addressRepository.findAll()).willReturn(listAddress);

        List<AddressDto> listAddressDtoActual = addressService.findAll();

        BDDMockito.verify(addressRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(listAddressDto.size(), listAddressDtoActual.size());
        Assertions.assertEquals(listAddressDto.get(0).getCep(), listAddressDtoActual.get(0).getCep());
        Assertions.assertEquals(listAddressDto.get(1).getCep(), listAddressDtoActual.get(1).getCep());
        Assertions.assertEquals(listAddressDto.get(2).getCep(), listAddressDtoActual.get(2).getCep());
    }

    @Test
    void findById() throws JsonProcessingException {

        String addDto1 = "{\"id\":\"11\",\"cep\":\"78300-112\",\"logradouro\":\"Rua Francisco Ferreira Ramos\",\"complemento\":\"\",\"bairro\":\"Centro\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":\"5107958\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9185\"}";
        AddressDto addressDtoExpected = mapper.readValue(addDto1, AddressDto.class);

        String add1 = "{\"id\":\"11\",\"cep\":\"78300-112\",\"logradouro\":\"Rua Francisco Ferreira Ramos\",\"complemento\":\"\",\"bairro\":\"Centro\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":\"5107958\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9185\"}";
        Address address1 = mapper.readValue(add1, Address.class);
        Optional<Address> optAddress1 = Optional.of(address1);

        BDDMockito.given(addressRepository.findById(11L)).willReturn(optAddress1);

        AddressDto addressDtoActual = addressService.findById(11L);

        BDDMockito.verify(addressRepository, Mockito.times(1)).findById(11L);
        Assertions.assertEquals(addressDtoActual.getId(), addressDtoExpected.getId());
        Assertions.assertEquals(addressDtoActual.getCep(), addressDtoExpected.getCep());
    }

    @Test
    @DisplayName("Insert Address")
    void saveInsert() throws JsonProcessingException {

        String addDto1 = "{\"cep\":\"78300-112\",\"customer\":{\"id\":1}}";
        AddressDto addressDtoExpected = mapper.readValue(addDto1, AddressDto.class);

        String add1 = "{\"cep\":\"78300-112\",\"logradouro\":\"Rua Francisco Ferreira Ramos\",\"complemento\":\"\",\"bairro\":\"Centro\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":\"5107958\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9185\"}";
        Address address1 = mapper.readValue(add1, Address.class);
        Customer customer = new Customer(1L, "Homem Aranha", "aranha@marvel.com.br", "M");
        address1.setCustomer(customer);
        List<Address> listAddress = new ArrayList<>();
        listAddress.add(address1);

        BDDMockito.given(apiViaCepService.getAddressByCep("78300-112")).willReturn(listAddress);
        BDDMockito.given(customerService.findById(1L)).willReturn(addressDtoExpected.getCustomer());
        BDDMockito.given(customerService.dtoToEntity(addressDtoExpected.getCustomer())).willReturn(customer);

        addressService.save(addressDtoExpected, false);

        BDDMockito.then(addressRepository).should().save(addressArgumentCaptor.capture());
        Address address = addressArgumentCaptor.getValue();

        BDDMockito.verify(addressRepository, Mockito.times(1)).save(address1);
        Assertions.assertEquals(addressDtoExpected.getCep(), address.getCep());
    }

    @Test
    @DisplayName("Update Address")
    void saveUpdate() throws JsonProcessingException {

        String addDto1 = "{\"id\":1,\"cep\":\"78300-112\",\"customer\":{\"id\":1}}";
        AddressDto addressDtoExpected = mapper.readValue(addDto1, AddressDto.class);

        String add1 = "{\"cep\":\"78300-112\",\"logradouro\":\"Rua Francisco Ferreira Ramos\",\"complemento\":\"\",\"bairro\":\"Centro\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":\"5107958\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9185\"}";
        Address address1 = mapper.readValue(add1, Address.class);
        Customer customer = new Customer(1L, "Homem Aranha", "aranha@marvel.com.br", "M");
        address1.setCustomer(customer);
        List<Address> listAddress = new ArrayList<>();
        listAddress.add(address1);
        Optional<Address> optAddress1 = Optional.of(address1);

        BDDMockito.given(addressRepository.findById(1L)).willReturn(optAddress1);
        BDDMockito.given(apiViaCepService.getAddressByCep("78300-112")).willReturn(listAddress);
        BDDMockito.given(customerService.findById(1L)).willReturn(addressDtoExpected.getCustomer());
        BDDMockito.given(customerService.dtoToEntity(addressDtoExpected.getCustomer())).willReturn(customer);

        addressService.save(addressDtoExpected, true);

        BDDMockito.then(addressRepository).should().save(addressArgumentCaptor.capture());
        Address address = addressArgumentCaptor.getValue();

        BDDMockito.verify(addressRepository, Mockito.times(1)).save(address1);
        Assertions.assertEquals(addressDtoExpected.getCep(), address.getCep());
        Assertions.assertEquals(addressDtoExpected.getId(), address.getId());
    }

    @Test
    void delete() throws JsonProcessingException {

        String add1 = "{\"id\":1,\"cep\":\"78300-112\",\"logradouro\":\"Rua Francisco Ferreira Ramos\",\"complemento\":\"\",\"bairro\":\"Centro\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":\"5107958\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9185\"}";
        Address addressExpected = mapper.readValue(add1, Address.class);
        Customer customer = new Customer(1L, "Homem Aranha", "aranha@marvel.com.br", "M");
        addressExpected.setCustomer(customer);
        Optional<Address> optAddressExpc = Optional.of(addressExpected);

        BDDMockito.given(addressRepository.findById(1L)).willReturn(optAddressExpc);

        addressService.delete(1L);

        BDDMockito.then(addressRepository).should().delete(addressArgumentCaptor.capture());
        Address address = addressArgumentCaptor.getValue();

        BDDMockito.verify(addressRepository, Mockito.times(1)).delete(addressExpected);
        Assertions.assertEquals(addressExpected.getCep(), address.getCep());
        Assertions.assertEquals(addressExpected.getId(), address.getId());
    }

    @Test
    void getAddressByCep() throws JsonProcessingException {

        String add1 = "{\"cep\":\"78300-112\",\"logradouro\":\"Rua Francisco Ferreira Ramos\",\"complemento\":\"\",\"bairro\":\"Centro\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":\"5107958\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9185\"}";
        Address addressExpected = mapper.readValue(add1, Address.class);
        List<Address> listAddress = new ArrayList<>();
        listAddress.add(addressExpected);

        BDDMockito.given(apiViaCepService.getAddressByCep("78300-112")).willReturn(listAddress);

        List<AddressDto> addressDto = addressService.getAddressByCep("78300-112");

        Assertions.assertEquals(addressExpected.getCep(), addressDto.get(0).getCep());
        Assertions.assertEquals(addressExpected.getLogradouro(), addressDto.get(0).getLogradouro());
        Assertions.assertEquals(addressExpected.getLocalidade(), addressDto.get(0).getLocalidade());
        Assertions.assertEquals(addressExpected.getBairro(), addressDto.get(0).getBairro());
        Assertions.assertEquals(addressExpected.getUf(), addressDto.get(0).getUf());
    }

    @Test
    void getAddressByUfLocLog() throws JsonProcessingException {

        String addresses = "[{\"cep\":\"78300-136\",\"logradouro\":\"Rua João Elias Ramos\",\"complemento\":\"\",\"bairro\":\"Jardim Europa\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":\"5107958\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9185\"},{\"cep\":\"78300-228\",\"logradouro\":\"Rua João Elias Ramos\",\"complemento\":\"\",\"bairro\":\"Jardim Floriza\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":\"5107958\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9185\"},{\"cep\":\"78302-148\",\"logradouro\":\"Rua João Elias Ramos\",\"complemento\":\"\",\"bairro\":\"Jardim Horizonte\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":\"5107958\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9185\"},{\"cep\":\"78301-071\",\"logradouro\":\"Rua João Elias Ramos\",\"complemento\":\"\",\"bairro\":\"Jardim Goiás\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":\"5107958\",\"gia\":\"\",\"ddd\":\"65\",\"siafi\":\"9185\"}]";
        List<Address> listAddress = mapper.readValue(addresses, addressTypeList);

        BDDMockito.given(apiViaCepService.getAddressByUfLocLog("MT", "Tangará da Serra",
                        "Rua João Elias Ramos"))
                .willReturn(listAddress);

        List<AddressDto> listAddressesActual = addressService.getAddressByUfLocLog("MT", "Tangará da Serra", "Rua João Elias Ramos");

        Assertions.assertEquals(listAddress.size(), listAddressesActual.size());
    }

}