package com.example.api.service;

import com.example.api.domain.Customer;
import com.example.api.dto.CustomerDto;
import com.example.api.dto.PagingCustomerDto;
import com.example.api.repository.CustomerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    AddressService addressService;

    @Mock
    Validator validator;

    private final ObjectMapper mapper = new ObjectMapper();

    private final TypeReference<List<Customer>> customerTypeList = new TypeReference<>() {
    };

    private final TypeReference<List<CustomerDto>> customerDtoTypeList = new TypeReference<>() {
    };

    @Test
    void findAll() throws JsonProcessingException {

        String customers = "[{\"id\":5,\"name\":\"Gamora\",\"email\":\"gamora@vingadores.com\",\"gender\":\"F\"},{\"id\":1,\"name\":\"Homem Aranha\",\"email\":\"aranha@vingadores.com\",\"gender\":\"M\"},{\"id\":4,\"name\":\"Namor\",\"email\":\"namor@vingadores.com\",\"gender\":\"M\"},{\"id\":2,\"name\":\"Thor\",\"email\":\"thor@vingadores.com\",\"gender\":\"M\"},{\"id\":3,\"name\":\"Viuva Negra\",\"email\":\"viuva@vingadores.com\",\"gender\":\"F\"}]";
        List<Customer> listCustomer = mapper.readValue(customers, customerTypeList);

        BDDMockito.given(customerRepository.findAllByOrderByNameAsc()).willReturn(listCustomer);

        List<CustomerDto> listCustomerDto = customerService.findAll();

        String customersExpc = mapper.writeValueAsString(listCustomer);
        String customersActu = mapper.writeValueAsString(listCustomerDto);

        BDDMockito.verify(customerRepository, Mockito.times(1)).findAllByOrderByNameAsc();
        Assertions.assertEquals(listCustomer.size(), listCustomerDto.size());
        Assertions.assertEquals(customersExpc, customersActu);

    }

    @Test
    @DisplayName("Busca por nome")
    void findAllByName() throws JsonProcessingException {

        String dtoSearch = "{\"name\":\"Thor\",\"email\":\"\",\"gender\":\"\"}";
        CustomerDto customerDto = mapper.readValue(dtoSearch, CustomerDto.class);

        String custormer = "[{\"id\":2,\"name\":\"Thor\",\"email\":\"thor@vingadores.com\",\"gender\":\"M\"}]";
        List<Customer> listCustomer  = mapper.readValue(custormer, customerTypeList);

        BDDMockito.given(customerRepository.findAllByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContaining(
                customerDto.getName(),customerDto.getEmail(), customerDto.getGender())).willReturn(listCustomer);

        List<CustomerDto> listCustomerDtoActual = customerService.findAllByNameEmailGender(customerDto);

        String customersExpc = mapper.writeValueAsString(listCustomer);
        String customersActu = mapper.writeValueAsString(listCustomerDtoActual);

        BDDMockito.verify(customerRepository, Mockito.times(1)).findAllByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContaining(
                customerDto.getName(),customerDto.getEmail(), customerDto.getGender()
        );
        Assertions.assertEquals(listCustomer.size(), listCustomerDtoActual.size());
        Assertions.assertEquals(customersExpc, customersActu);
    }

    @Test
    @DisplayName("Busca por email")
    void findAllByEmail() throws JsonProcessingException {

        String dtoSearch = "{\"name\":\"\",\"email\":\"thor@vingadores.com\",\"gender\":\"\"}";
        CustomerDto customerDto = mapper.readValue(dtoSearch, CustomerDto.class);

        String custormer = "[{\"id\":2,\"name\":\"Thor\",\"email\":\"thor@vingadores.com\",\"gender\":\"M\"}]";
        List<Customer> listCustomer  = mapper.readValue(custormer, customerTypeList);

        BDDMockito.given(customerRepository.findAllByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContaining(
                customerDto.getName(),customerDto.getEmail(), customerDto.getGender())).willReturn(listCustomer);

        List<CustomerDto> listCustomerDtoActual = customerService.findAllByNameEmailGender(customerDto);

        String customersExpc = mapper.writeValueAsString(listCustomer);
        String customersActu = mapper.writeValueAsString(listCustomerDtoActual);

        BDDMockito.verify(customerRepository, Mockito.times(1)).findAllByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContaining(
                customerDto.getName(),customerDto.getEmail(), customerDto.getGender());
        Assertions.assertEquals(listCustomer.size(), listCustomerDtoActual.size());
        Assertions.assertEquals(customersExpc, customersActu);
    }

    @Test
    @DisplayName("Busca por Genero")
    void findAllByGender() throws JsonProcessingException {

        String dtoSearch = "{\"name\":\"\",\"email\":\"\",\"gender\":\"M\"}";
        CustomerDto customerDto = mapper.readValue(dtoSearch, CustomerDto.class);

        String custormer = "[{\"id\":2,\"name\":\"Thor\",\"email\":\"thor@vingadores.com\",\"gender\":\"M\"}]";
        List<Customer> listCustomer  = mapper.readValue(custormer, customerTypeList);

        BDDMockito.given(customerRepository.findAllByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContaining(
                customerDto.getName(),customerDto.getEmail(), customerDto.getGender())).willReturn(listCustomer);

        List<CustomerDto> listCustomerDtoActual = customerService.findAllByNameEmailGender(customerDto);

        String customersExpc = mapper.writeValueAsString(listCustomer);
        String customersActu = mapper.writeValueAsString(listCustomerDtoActual);

        BDDMockito.verify(customerRepository, Mockito.times(1)).findAllByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContaining(
                customerDto.getName(),customerDto.getEmail(), customerDto.getGender());
        Assertions.assertEquals(listCustomer.size(), listCustomerDtoActual.size());
        Assertions.assertEquals(customersExpc, customersActu);
    }

    @Test
    void findDistinctByUfLocalidade() throws JsonProcessingException {

        String customers = "[{\"id\":1,\"name\":\"Homem Aranha\",\"email\":\"aranha@vingadores.com\",\"gender\":\"M\",\"address\":[{\"id\":4,\"cep\":\"91420-270\",\"logradouro\":\"Rua São Domingos\",\"complemento\":\"\",\"bairro\":\"Bom Jesus\",\"localidade\":\"Porto Alegre\",\"uf\":\"RS\",\"ibge\":4314902,\"gia\":0,\"ddd\":51,\"siafi\":8801,\"erro\":null}]}]";
        List<Customer> listCustomer = mapper.readValue(customers, customerTypeList);

        BDDMockito.given(customerRepository.findDistinctByAddressUfIgnoreCaseContainingAndAddressLocalidadeIgnoreCaseContaining(
                "RS","Porto Alegre")).willReturn(listCustomer);

        List<CustomerDto> listCustomerDto = customerService.findDistinctByUfLocalidade("RS", "Porto Alegre");

        String customersExpc = mapper.writeValueAsString(listCustomer);
        String customersActu = mapper.writeValueAsString(listCustomerDto);
        BDDMockito.verify(customerRepository, Mockito.times(1)).findDistinctByAddressUfIgnoreCaseContainingAndAddressLocalidadeIgnoreCaseContaining(
                "RS","Porto Alegre");
        Assertions.assertEquals(listCustomer.size(), listCustomerDto.size());
        Assertions.assertEquals(customersExpc, customersActu);
    }

    @Test
    void findAllByNameEmailGenderUfLocalidadePaging() throws JsonProcessingException {

        String PagingCustomerJson = "{\"filterCustomers\":{\"name\":\"\",\"email\":\"\",\"gender\":\"\",\"uf\":\"\",\"localidade\":\"\"},\"customers\":[],\"pageNumber\":0,\"sortBy\":\"name\",\"pageSize\":2,\"numberOfPages\":0,\"totalRecords\":0}";
        PagingCustomerDto pagingCustomerDto = mapper.readValue(PagingCustomerJson, PagingCustomerDto.class);

        String customersJson = "[{\"id\":5,\"name\":\"Gamora\",\"email\":\"gamora@vingadores.com\",\"gender\":\"F\"},{\"id\":1,\"name\":\"Homem Aranha\",\"email\":\"aranha@vingadores.com\",\"gender\":\"M\"}]";
        List<Customer> listCustomer = mapper.readValue(customersJson, customerTypeList);

        BDDMockito.given(customerRepository.countAllByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContaining(
                pagingCustomerDto.getFilterCustomers().getName(),
                pagingCustomerDto.getFilterCustomers().getName(),
                pagingCustomerDto.getFilterCustomers().getGender())).willReturn(5);

        Pageable pageable = PageRequest.of(pagingCustomerDto.getPageNumber(), pagingCustomerDto.getPageSize(),
                Sort.by(pagingCustomerDto.getSortBy()).ascending());

        BDDMockito.given(customerRepository.findAllByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndGenderIgnoreCaseContaining(
                pagingCustomerDto.getFilterCustomers().getName(),
                pagingCustomerDto.getFilterCustomers().getName(),
                pagingCustomerDto.getFilterCustomers().getGender(),
                pageable)).willReturn(listCustomer);

        PagingCustomerDto pagingCustomerDtoActural = customerService.findAllByNameEmailGenderUfLocalidadePaging(pagingCustomerDto);

        Assertions.assertEquals(5, pagingCustomerDtoActural.getTotalRecords());
        Assertions.assertEquals(3, pagingCustomerDtoActural.getNumberOfPages());
        Assertions.assertEquals(listCustomer.size(), pagingCustomerDtoActural.getCustomers().size());

    }

    @Test
    void findById() throws JsonProcessingException {
        String customerJson = "{\"id\":2,\"name\":\"Thor\",\"email\":\"thor@vingadores.com\",\"gender\":\"M\"}";

        Customer customer = mapper.readValue(customerJson, Customer.class);
        Optional<Customer> opCustomer = Optional.of(customer);

        BDDMockito.given(customerRepository.findById(2L)).willReturn(opCustomer);

        CustomerDto customerDto = customerService.findById(2L);

        BDDMockito.verify(customerRepository,Mockito.times(1)).findById(2L);
        Assertions.assertEquals(customer.getId(), customerDto.getId());
        Assertions.assertEquals(customer.getName(), customerDto.getName());
        Assertions.assertEquals(customer.getEmail(), customerDto.getEmail());
        Assertions.assertEquals(customer.getGender(), customerDto.getGender());
    }

    @Test
    @DisplayName("Insert Customer")
    void saveInsert() throws JsonProcessingException {
        String customerJson = "{\"name\":\"Capitão América\",\"email\":\"capitao@avengers.com.br\",\"gender\":\"M\"}";
        CustomerDto customerDto = mapper.readValue(customerJson, CustomerDto.class);
        customerService.save(customerDto, false);
        BDDMockito.verify(customerRepository,Mockito.times(1)).save(BDDMockito.any());
    }

    @Test
    @DisplayName("Update Customer")
    void saveUpdate() throws JsonProcessingException {
        String customerJson = "{\"id\":1,\"name\":\"Capitão Europa\",\"email\":\"europa@avengers.com.br\",\"gender\":\"F\"}";
        CustomerDto customerDto = mapper.readValue(customerJson, CustomerDto.class);

        Customer customer = mapper.readValue(customerJson, Customer.class);
        Optional<Customer> opCustomer = Optional.of(customer);

        BDDMockito.given(customerRepository.findById(1L)).willReturn(opCustomer);

        customerService.save(customerDto, true);

        BDDMockito.verify(customerRepository,Mockito.times(1)).findById(1L);
        BDDMockito.verify(customerRepository,Mockito.times(1)).save(BDDMockito.any());
    }

    @Test

    void delete() throws JsonProcessingException {
        String customerJson = "{\"id\":1,\"name\":\"Capitão Europa\",\"email\":\"europa@avengers.com.br\",\"gender\":\"F\"}";

        Customer customer = mapper.readValue(customerJson, Customer.class);
        Optional<Customer> opCustomer = Optional.of(customer);

        BDDMockito.given(customerRepository.findById(1L)).willReturn(opCustomer);

        customerService.delete(1L);

        BDDMockito.verify(customerRepository,Mockito.times(1)).delete(customer);
    }

}