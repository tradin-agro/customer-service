package com.example.api.web.rest;

import com.example.api.dto.AddressDto;
import com.example.api.dto.CustomerDto;
import com.example.api.dto.PagingCustomerDto;
import com.example.api.dto.UserDto;
import com.example.api.security.DataTokenJWT;
import com.example.api.service.CustomerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JacksonTester<CustomerDto> jsonCustomerDto;

    @Autowired
    private JacksonTester<AddressDto> jsonAddressDto;

    @Autowired
    private JacksonTester<UserDto> jsonUserDto;

    @Autowired
    private JacksonTester<PagingCustomerDto> jsonPagingCustomerDto;

    private final ObjectMapper mapper = new ObjectMapper();

    private final TypeReference<List<CustomerDto>> customerDtoTypeList = new TypeReference<>() {
    };

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
    @DisplayName("Insert Customer")
    @Order(1)
    void saveInsert() throws Exception {

        String customerJson = "{\"name\":\"Capitão América\",\"email\":\"capitao@avengers.com.br\",\"gender\":\"M\"}";
        CustomerDto customerDto = mapper.readValue(customerJson, CustomerDto.class);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .header("authorization", headerTokenAuth)
                        .content(jsonCustomerDto.write(customerDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());

        MockHttpServletResponse responseAfterInsert = mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/6")
                        .header("authorization", headerTokenAuth)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, responseAfterInsert.getStatus());

        CustomerDto customerDtoActual = mapper.readValue(responseAfterInsert.getContentAsString(), CustomerDto.class);

        Assertions.assertEquals(6, customerDtoActual.getId());
        Assertions.assertEquals(customerDto.getName(), customerDtoActual.getName());
        Assertions.assertEquals(customerDto.getEmail(), customerDtoActual.getEmail());
        Assertions.assertEquals(customerDto.getGender(), customerDtoActual.getGender());
    }

    @Test
    @DisplayName("Error insert Customer with Gender diff M|F validation")
    @Order(2)
    void saveInsert400Gender() throws Exception {

        String customerJson = "{\"name\":\"Capitão América\",\"email\":\"capitao@avengers.com.br\",\"gender\":\"R\"}";
        CustomerDto customerDto = mapper.readValue(customerJson, CustomerDto.class);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .header("authorization", headerTokenAuth)
                        .content(jsonCustomerDto.write(customerDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(400, response.getStatus());

        ApiErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ApiErrorResponse.class);

        Assertions.assertEquals("gender: Gener is M or F", errorResponse.getMessage());
        Assertions.assertEquals(1, errorResponse.getValidations().size());
    }

    @Test
    @DisplayName("Error insert Customer with Name min length validation")
    @Order(3)
    void saveInsert400NameMinLength() throws Exception {

        String customerJson = "{\"name\":\"C\",\"email\":\"capitao@avengers.com.br\",\"gender\":\"M\"}";
        CustomerDto customerDto = mapper.readValue(customerJson, CustomerDto.class);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .header("authorization", headerTokenAuth)
                        .content(jsonCustomerDto.write(customerDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(400, response.getStatus());

        ApiErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ApiErrorResponse.class);

        Assertions.assertEquals("name: Name must have at minimum two letters", errorResponse.getMessage());
        Assertions.assertEquals(1, errorResponse.getValidations().size());
    }

    @Test
    @DisplayName("Error insert Customer with invalid Email")
    @Order(4)
    void saveInsert400InvalidEmail() throws Exception {

        String customerJson = "{\"name\":\"Capitão América\",\"email\":\"capitao@\",\"gender\":\"M\"}";
        CustomerDto customerDto = mapper.readValue(customerJson, CustomerDto.class);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .header("authorization", headerTokenAuth)
                        .content(jsonCustomerDto.write(customerDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(400, response.getStatus());

        ApiErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ApiErrorResponse.class);

        Assertions.assertEquals("email: Email is not valid", errorResponse.getMessage());
        Assertions.assertEquals(1, errorResponse.getValidations().size());
    }

    @Test
    @DisplayName("Error insert Customer empty values")
    @Order(5)
    void saveInsert400EmptyValues() throws Exception {

        String customerJson = "{\"name\":\"\",\"email\":\"\",\"gender\":\"\"}";
        CustomerDto customerDto = mapper.readValue(customerJson, CustomerDto.class);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .header("authorization", headerTokenAuth)
                        .content(jsonCustomerDto.write(customerDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(400, response.getStatus());

        ApiErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ApiErrorResponse.class);

        Assertions.assertEquals(7, errorResponse.getValidations().size());
        List<Map<String, String>> nameValidations = errorResponse.getValidations().stream()
                .filter(validation -> validation.containsKey("name")).collect(Collectors.toList());
        List<Map<String, String>> emailValidations = errorResponse.getValidations().stream()
                .filter(validation -> validation.containsKey("email")).collect(Collectors.toList());
        List<Map<String, String>> genderValidations = errorResponse.getValidations().stream()
                .filter(validation -> validation.containsKey("gender")).collect(Collectors.toList());

        Assertions.assertEquals(2, nameValidations.size());
        Assertions.assertEquals(2, emailValidations.size());
        Assertions.assertEquals(3, genderValidations.size());
    }

    @Test
    @DisplayName("Update Customer")
    @Order(6)
    void saveUpdate() throws Exception {

        String customerJson = "{\"id\":5,\"name\":\"Capitã África\",\"email\":\"africa@avengers.com.br\",\"gender\":\"F\"}";
        CustomerDto customerDto = mapper.readValue(customerJson, CustomerDto.class);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/customers")
                        .header("authorization", headerTokenAuth)
                        .content(jsonCustomerDto.write(customerDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());

        MockHttpServletResponse responseAfterInsert = mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/5")
                        .header("authorization", headerTokenAuth)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, responseAfterInsert.getStatus());

        CustomerDto customerDtoActual = mapper.readValue(responseAfterInsert.getContentAsString(), CustomerDto.class);

        Assertions.assertEquals(5, customerDtoActual.getId());
        Assertions.assertEquals(customerDto.getName(), customerDtoActual.getName());
        Assertions.assertEquals(customerDto.getEmail(), customerDtoActual.getEmail());
        Assertions.assertEquals(customerDto.getGender(), customerDtoActual.getGender());
    }

    @Test
    @DisplayName("Delete Customer")
    @Order(7)
    void delete() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/customers/{id}", 5)
                        .header("authorization", headerTokenAuth)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());

    }

    @Test
    @DisplayName("Find Customer by Id")
    @Order(8)
    void findById() throws Exception {

        String customerJson = "{\"id\":2,\"name\":\"Thor\",\"email\":\"thor@vingadores.com\",\"gender\":\"M\"}";

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/{id}", 2)
                        .header("authorization", headerTokenAuth)
        ).andReturn().getResponse();

        Assertions.assertEquals(customerJson, response.getContentAsString());
    }

    @Test
    @DisplayName("Search All Customers")
    @Order(9)
    void searchAll() throws Exception {

        String customerJson = "[{\"id\":6,\"name\":\"Capitão América\",\"email\":\"capitao@avengers.com.br\",\"gender\":\"M\"},{\"id\":1,\"name\":\"Homem Aranha\",\"email\":\"aranha@vingadores.com\",\"gender\":\"M\"},{\"id\":4,\"name\":\"Namor\",\"email\":\"namor@vingadores.com\",\"gender\":\"M\"},{\"id\":2,\"name\":\"Thor\",\"email\":\"thor@vingadores.com\",\"gender\":\"M\"},{\"id\":3,\"name\":\"Viuva Negra\",\"email\":\"viuva@vingadores.com\",\"gender\":\"F\"}]";

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/customers")
                        .header("authorization", headerTokenAuth)
        ).andReturn().getResponse();

        Assertions.assertEquals(customerJson, response.getContentAsString());
    }

    @Test
    @DisplayName("Search Customer by Name, Email and Gender")
    @Order(10)
    void searchByNameEmailGender() throws Exception {

        String customerJson = "{\"name\":\"or\",\"email\":\"or@\",\"gender\":\"M\"}";
        CustomerDto customerDto = mapper.readValue(customerJson, CustomerDto.class);

        String custumersJson = "[{\"id\":2,\"name\":\"Thor\",\"email\":\"thor@vingadores.com\",\"gender\":\"M\"},{\"id\":4,\"name\":\"Namor\",\"email\":\"namor@vingadores.com\",\"gender\":\"M\"}]";
        List<CustomerDto> listCustomerDtoExpec = mapper.readValue(custumersJson, customerDtoTypeList);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/customers/search")
                        .header("authorization", headerTokenAuth)
                        .content(jsonCustomerDto.write(customerDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());

        List<CustomerDto> listCustomerDtoActual = mapper.readValue(response.getContentAsString(), customerDtoTypeList);

        Assertions.assertEquals(listCustomerDtoExpec.size(), listCustomerDtoActual.size());
        Assertions.assertEquals(listCustomerDtoExpec.get(0).getId(), listCustomerDtoActual.get(0).getId());
        Assertions.assertEquals(listCustomerDtoExpec.get(1).getId(), listCustomerDtoActual.get(1).getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2})
    @DisplayName("Search Customer Paging")
    @Order(11)
    void searchPaging(int pageNumber) throws Exception {

        String pagingJson = "{\"filterCustomers\":{\"name\":\"\",\"email\":\"\",\"gender\":\"\",\"uf\":\"\",\"localidade\":\"\"},\"customers\":[],\"pageNumber\":0,\"sortBy\":\"name\",\"pageSize\":2,\"numberOfPages\":0,\"totalRecords\":0}";
        PagingCustomerDto pagingCustomerDto = mapper.readValue(pagingJson, PagingCustomerDto.class);
        pagingCustomerDto.setPageNumber(pageNumber);

        String[] pages = new String[3];
        pages[0] = "[{\"id\":6,\"name\":\"Capitão América\",\"email\":\"capitao@avengers.com.br\",\"gender\":\"M\"},{\"id\":1,\"name\":\"Homem Aranha\",\"email\":\"aranha@vingadores.com\",\"gender\":\"M\"}]";
        pages[1] = "[{\"id\":4,\"name\":\"Namor\",\"email\":\"namor@vingadores.com\",\"gender\":\"M\"},{\"id\":2,\"name\":\"Thor\",\"email\":\"thor@vingadores.com\",\"gender\":\"M\"}]";
        pages[2] = "[{\"id\":3,\"name\":\"Viuva Negra\",\"email\":\"viuva@vingadores.com\",\"gender\":\"F\"}]";

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/customers/search/paging")
                        .header("authorization", headerTokenAuth)
                        .content(jsonPagingCustomerDto.write(pagingCustomerDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
        PagingCustomerDto pagingCustomerDtoActual = mapper.readValue(response.getContentAsString(), PagingCustomerDto.class);
        String customersActual = mapper.writeValueAsString(pagingCustomerDtoActual.getCustomers());
        Assertions.assertEquals(pages[pageNumber], customersActual);
    }

    @Test
    @DisplayName("Insert Customer Address")
    @Order(12)
    void insertAddress() throws Exception {

        String customerJson = "{\"cep\":\"78300-110\",\"customer\":{\"id\":1}}";
        AddressDto addressDto = mapper.readValue(customerJson, AddressDto.class);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/addresses")
                        .header("authorization", headerTokenAuth)
                        .content(jsonAddressDto.write(addressDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());

        MockHttpServletResponse responseAfterInsert = mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses/{id}", 1)
                        .header("authorization", headerTokenAuth)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, responseAfterInsert.getStatus());
        AddressDto addressDtoActual = mapper.readValue(responseAfterInsert.getContentAsString(), AddressDto.class);
        Assertions.assertEquals(1, addressDtoActual.getId());
        Assertions.assertEquals("78300-110", addressDtoActual.getCep());
        Assertions.assertEquals("MT", addressDtoActual.getUf());
        Assertions.assertEquals("Tangará da Serra", addressDtoActual.getLocalidade());
        Assertions.assertEquals("Centro", addressDtoActual.getBairro());
        Assertions.assertEquals("Rua José Alves de Souza", addressDtoActual.getLogradouro());
    }

    @Test
    @DisplayName("Update Customer Address")
    @Order(13)
    void updateAddress() throws Exception {

        String customerJson = "{\"id\":\"1\",\"cep\":\"78300-112\",\"customer\":{\"id\":1}}";
        AddressDto addressDto = mapper.readValue(customerJson, AddressDto.class);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/addresses")
                        .header("authorization", headerTokenAuth)
                        .content(jsonAddressDto.write(addressDto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());

        MockHttpServletResponse responseAfterInsert = mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses/{id}", 1)
                        .header("authorization", headerTokenAuth)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, responseAfterInsert.getStatus());
        AddressDto addressDtoActual = mapper.readValue(responseAfterInsert.getContentAsString(), AddressDto.class);
        Assertions.assertEquals(1, addressDtoActual.getId());
        Assertions.assertEquals("78300-112", addressDtoActual.getCep());
        Assertions.assertEquals("MT", addressDtoActual.getUf());
        Assertions.assertEquals("Tangará da Serra", addressDtoActual.getLocalidade());
        Assertions.assertEquals("Centro", addressDtoActual.getBairro());
        Assertions.assertEquals("Rua Francisco Ferreira Ramos", addressDtoActual.getLogradouro());
    }

    @Test
    @DisplayName("Search Customer by Address Uf and Localidade")
    @Order(14)
    void searchCustomerByUfLocalidade() throws Exception {

       String customersJson = "[{\"id\":1,\"name\":\"Homem Aranha\",\"email\":\"aranha@vingadores.com\",\"gender\":\"M\",\"address\":[{\"id\":1,\"cep\":\"78300-112\",\"logradouro\":\"Rua Francisco Ferreira Ramos\",\"complemento\":\"\",\"bairro\":\"Centro\",\"localidade\":\"Tangará da Serra\",\"uf\":\"MT\",\"ibge\":5107958,\"gia\":0,\"ddd\":65,\"siafi\":9185}]}]";

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/search/{uf}/{localidade}"
                        , "MT","Tangará da Serra")
                        .header("authorization", headerTokenAuth)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals(customersJson, response.getContentAsString());

    }

    @Test
    @DisplayName("Delete Address")
    @Order(15)
    void deleteAddres() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/addresses/{id}", 1)
                        .header("authorization", headerTokenAuth)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());

    }

    @Test
    @DisplayName("Search Address by CEP")
    @Order(16)
    void searchAddresByCep() throws Exception {

        String cep = "91040-000";

        String addressJson = "[{\"cep\":\"91040-000\",\"logradouro\":\"Rua Domingos Rubbo\",\"complemento\":\"\",\"bairro\":\"Cristo Redentor\",\"localidade\":\"Porto Alegre\",\"uf\":\"RS\",\"ibge\":4314902,\"gia\":0,\"ddd\":51,\"siafi\":8801}]";

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses/cep/{cep}", cep)
                        .header("authorization", headerTokenAuth)
        ).andReturn().getResponse();

        Assertions.assertEquals(addressJson, response.getContentAsString());
    }

}