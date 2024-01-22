package com.example.api.web.rest;

import com.example.api.domain.Customer;
import com.example.api.dto.CustomerDto;
import com.example.api.dto.PagingCustomerDto;
import com.example.api.dto.UserDto;
import com.example.api.repository.CustomerRepository;
import com.example.api.security.DataTokenJWT;
import com.example.api.service.CustomerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Autowired
    private JacksonTester<CustomerDto> jsonCustomerDto;

    @Autowired
    private JacksonTester<PagingCustomerDto> jsonPagingCustomerDto;

    @Autowired
    private JacksonTester<UserDto> jsonUserDto;

    private final ObjectMapper mapper = new ObjectMapper();

    private final TypeReference<List<CustomerDto>> customerDtoTypeList = new TypeReference<>() {
    };

    private final TypeReference<List<Customer>> customerTypeList = new TypeReference<>() {
    };

    @Captor
    private ArgumentCaptor<CustomerDto> customerDtoArgumentCaptor;

    @Captor
    private  ArgumentCaptor<Boolean> isUpdateArgumentCaptor;

    private String headerTokenAuth;

    @BeforeEach
    void load() throws Exception {
        String loginJson = "{\"login\":\"giovanni\",\"password\":\"123456\"}";
        UserDto userDto = mapper.readValue(loginJson, UserDto.class);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .content(jsonUserDto.write(userDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        DataTokenJWT dataTokenJWT = mapper.readValue(response.getContentAsString(), DataTokenJWT.class);

        this.headerTokenAuth = "Bearer "+dataTokenJWT.getToken();

    }

    @Test
    void findAll() throws Exception {

        String custumersJson = "[{\"id\":5,\"name\":\"Gamora\",\"email\":\"gamora@vingadores.com\",\"gender\":\"F\"},{\"id\":1,\"name\":\"Homem Aranha\",\"email\":\"aranha@vingadores.com\",\"gender\":\"M\"},{\"id\":4,\"name\":\"Namor\",\"email\":\"namor@vingadores.com\",\"gender\":\"M\"},{\"id\":2,\"name\":\"Thor\",\"email\":\"thor@vingadores.com\",\"gender\":\"M\"},{\"id\":3,\"name\":\"Viuva Negra\",\"email\":\"viuva@vingadores.com\",\"gender\":\"F\"}]";

        List<CustomerDto> listCustomerDto = mapper.readValue(custumersJson, customerDtoTypeList);

        BDDMockito.given(customerService.findAll()).willReturn(listCustomerDto);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/customers")
                        .header("authorization", headerTokenAuth)
        ).andReturn().getResponse();

        List<CustomerDto> listCustomerDtoActual = mapper.readValue(response.getContentAsString(), customerDtoTypeList);

        BDDMockito.verify(customerService, BDDMockito.times(1)).findAll();
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertNotNull(listCustomerDtoActual);
        Assertions.assertEquals(5, listCustomerDtoActual.size());
    }

    @Test
    void findAllByNameEmailGender() throws Exception {

        String customerSearchJson = "{\"name\":\"or\",\"email\":\"or\",\"gender\":\"F\"}";
        CustomerDto customerDto = mapper.readValue(customerSearchJson, CustomerDto.class);

        String customer = "[{\"id\":5,\"name\":\"Gamora\",\"email\":\"gamora@vingadores.com\",\"gender\":\"F\"}]";
        List<CustomerDto> listCustomerDto = mapper.readValue(customer, customerDtoTypeList);

        BDDMockito.given(customerService.findAllByNameEmailGender(BDDMockito.any())).willReturn(listCustomerDto);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/customers/search")
                        .header("authorization", headerTokenAuth)
                        .content(jsonCustomerDto.write(customerDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        List<CustomerDto> customerDtoActual = mapper.readValue(response.getContentAsString(), customerDtoTypeList);

        String customerExpec = mapper.writeValueAsString(listCustomerDto);
        String customerActual = mapper.writeValueAsString(customerDtoActual);

        Assertions.assertEquals(200, response.getStatus());
        BDDMockito.verify(customerService, BDDMockito.times(1))
                .findAllByNameEmailGender(BDDMockito.any());
        Assertions.assertEquals(customerExpec, customerActual);
    }

    @Test
    void findDistinctByUfLocalidade() throws Exception {

        String customerJson = "[{\"id\":1,\"name\":\"Homem Aranha\",\"email\":\"aranha@vingadores.com\",\"gender\":\"M\",\"address\":[{\"id\":4,\"cep\":\"91420-270\",\"logradouro\":\"Rua São Domingos\",\"complemento\":\"\",\"bairro\":\"Bom Jesus\",\"localidade\":\"Porto Alegre\",\"uf\":\"RS\",\"ibge\":4314902,\"gia\":0,\"ddd\":51,\"siafi\":8801,\"erro\":null}]}]";
        List<CustomerDto> listCustomerDto = mapper.readValue(customerJson, customerDtoTypeList);

        BDDMockito.given(customerService
                .findDistinctByUfLocalidade("RS", "Porto Alegre")).willReturn(listCustomerDto);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/search/{uf}/{localidade}", "RS", "Porto Alegre")
                        .header("authorization", headerTokenAuth)
        ).andReturn().getResponse();

        List<CustomerDto> listCustomerDtoActual = mapper.readValue(response.getContentAsString(), customerDtoTypeList);

        Assertions.assertEquals(200, response.getStatus());
        BDDMockito.verify(customerService, BDDMockito.times(1))
                .findDistinctByUfLocalidade("RS", "Porto Alegre");
        Assertions.assertEquals("Homem Aranha", listCustomerDtoActual.get(0).getName());
        Assertions.assertEquals("RS",listCustomerDtoActual.get(0).getAddress().get(0).getUf());
        Assertions.assertEquals("Porto Alegre",listCustomerDtoActual.get(0).getAddress().get(0).getLocalidade());
    }

    @Test
    void findAllByNameEmailGenderUfLocalidadePaging() throws Exception {

        String customerJson = "{\"filterCustomers\":{\"name\":\"\",\"email\":\"\",\"gender\":\"\",\"uf\":\"\",\"localidade\":\"\"},\"customers\":[],\"pageNumber\":0,\"sortBy\":\"name\",\"pageSize\":2,\"numberOfPages\":0,\"totalRecords\":0}";
        PagingCustomerDto pagingCustomerDto = mapper.readValue(customerJson, PagingCustomerDto.class);

        String customersJson = "[{\"id\":5,\"name\":\"Gamora\",\"email\":\"gamora@vingadores.com\",\"gender\":\"F\"},{\"id\":1,\"name\":\"Homem Aranha\",\"email\":\"aranha@vingadores.com\",\"gender\":\"M\"}]";
        List<Customer> listCustomer = mapper.readValue(customersJson, customerTypeList);

        String pagingReturn = "{\"filterCustomers\":{\"name\":\"\",\"email\":\"\",\"gender\":\"\",\"uf\":\"\",\"localidade\":\"\"},\"customers\":[{\"id\":5,\"name\":\"Gamora\",\"email\":\"gamora@vingadores.com\",\"gender\":\"F\"},{\"id\":1,\"name\":\"Homem Aranha\",\"email\":\"aranha@vingadores.com\",\"gender\":\"M\"}],\"pageNumber\":0,\"sortBy\":\"name\",\"pageSize\":2,\"numberOfPages\":3,\"totalRecords\":5}";
        PagingCustomerDto pagingReturnDto = mapper.readValue(pagingReturn, PagingCustomerDto.class);

        BDDMockito.given(customerService.findAllByNameEmailGenderUfLocalidadePaging(BDDMockito.any())).willReturn(pagingReturnDto);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/customers/search/paging")
                        .header("authorization", headerTokenAuth)
                        .content(jsonPagingCustomerDto.write(pagingCustomerDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
        PagingCustomerDto pagingCustomerDtoActual = mapper.readValue(response.getContentAsString(), PagingCustomerDto.class);
        Assertions.assertEquals(5, pagingCustomerDtoActual.getTotalRecords());
        Assertions.assertEquals(3, pagingCustomerDtoActual.getNumberOfPages());
        Assertions.assertEquals(listCustomer.size(), pagingCustomerDtoActual.getCustomers().size());
    }

    @Test
    void findById() throws Exception {

        String customerJson = "{\"id\":2,\"name\":\"Thor\",\"email\":\"thor@vingadores.com\",\"gender\":\"M\"}";
        CustomerDto customerDto = mapper.readValue(customerJson, CustomerDto.class);

        BDDMockito.given(customerService.findById(2L)).willReturn(customerDto);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/{id}", 2)
                        .header("authorization", headerTokenAuth)
        ).andReturn().getResponse();

        CustomerDto customerDtoActual = mapper.readValue(response.getContentAsString(), CustomerDto.class);

        String customerExpected = mapper.writeValueAsString(customerDto);
        String customerActual = mapper.writeValueAsString(customerDtoActual);

        BDDMockito.verify(customerService, BDDMockito.times(1)).findById(2L);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals(customerExpected, customerActual);
    }

    @Test
    @DisplayName("Insert Customer")
    void saveInsert() throws Exception {

        String customerJson = "{\"name\":\"Capitão América\",\"email\":\"capitao@avengers.com.br\",\"gender\":\"M\"}";
        CustomerDto customerDto = mapper.readValue(customerJson, CustomerDto.class);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .header("authorization", headerTokenAuth)
                        .content(jsonCustomerDto.write(customerDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        BDDMockito.then(customerService).should().save(
                customerDtoArgumentCaptor.capture(), isUpdateArgumentCaptor.capture());
        CustomerDto customerDtoActual = customerDtoArgumentCaptor.getValue();
        boolean isUpdate = isUpdateArgumentCaptor.getValue();

        Assertions.assertEquals(200, response.getStatus());
        BDDMockito.verify(customerService, BDDMockito.times(1)).save(
                BDDMockito.any(), BDDMockito.anyBoolean());
        Assertions.assertEquals(customerDto.getName(), customerDtoActual.getName());
        Assertions.assertEquals(customerDto.getEmail(), customerDtoActual.getEmail());
        Assertions.assertEquals(customerDto.getGender(), customerDtoActual.getGender());
        Assertions.assertFalse(isUpdate);
    }

    @Test
    @DisplayName("Update Customer")
    void saveUpdate() throws Exception {
        String customerJson = "{\"id\":6,\"name\":\"Capitão Europa\",\"email\":\"europa@avengers.com.br\",\"gender\":\"F\"}";
        CustomerDto customerDto = mapper.readValue(customerJson, CustomerDto.class);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/customers")
                        .header("authorization", headerTokenAuth)
                        .content(jsonCustomerDto.write(customerDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        BDDMockito.then(customerService).should().save(
                customerDtoArgumentCaptor.capture(), isUpdateArgumentCaptor.capture());
        CustomerDto customerDtoActual = customerDtoArgumentCaptor.getValue();
        boolean isUpdate = isUpdateArgumentCaptor.getValue();

        Assertions.assertEquals(200, response.getStatus());
        BDDMockito.verify(customerService, BDDMockito.times(1)).save(
                BDDMockito.any(), BDDMockito.anyBoolean());
        Assertions.assertEquals(customerDto.getId(), customerDtoActual.getId());
        Assertions.assertEquals(customerDto.getName(), customerDtoActual.getName());
        Assertions.assertEquals(customerDto.getEmail(), customerDtoActual.getEmail());
        Assertions.assertEquals(customerDto.getGender(), customerDtoActual.getGender());
        Assertions.assertTrue(isUpdate);
    }

    @Test
    void delete() throws Exception {
        String customerJson = "{\"id\":6,\"name\":\"Capitão Europa\",\"email\":\"europa@avengers.com.br\",\"gender\":\"F\"}";
        Customer customer = mapper.readValue(customerJson, Customer.class);
        Optional<Customer> opCustomer = Optional.of(customer);

        BDDMockito.given(customerRepository.findById(6L)).willReturn(opCustomer);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/customers/{id}", 6)
                        .header("authorization", headerTokenAuth)
        ).andReturn().getResponse();

        BDDMockito.verify(customerService, BDDMockito.times(1)).delete(6L);
        Assertions.assertEquals(200, response.getStatus());
    }
}