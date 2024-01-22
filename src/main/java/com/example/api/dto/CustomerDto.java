package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class CustomerDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    private String name;

    private String email;

    private String gender;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String uf;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String localidade;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AddressDto> address = new ArrayList<>();

    public CustomerDto() {
    }

    public CustomerDto(Long id, String name, String email, String gender, List<AddressDto> address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getUf() {
        return uf;
    }

    public String getLocalidade() {
        return localidade;
    }

    public List<AddressDto> getAddress() {
        return address;
    }
}
