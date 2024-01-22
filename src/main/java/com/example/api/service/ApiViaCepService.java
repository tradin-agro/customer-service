package com.example.api.service;

import com.example.api.domain.Address;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ApiViaCepService {

    @Value("${api.viacep.url}")
    private String urlApiExterna;

    protected List<Address> getAddressByCep(String cep) {
        String cepNumbers = cep.replace("-", "").trim();
        return getAddressFromViaCep("/" + cepNumbers + "/json/");
    }

    protected List<Address> getAddressByUfLocLog(String uf, String localidade, String logradouro) {
        return getAddressFromViaCep("/" + uf + "/" + localidade + "/" + logradouro + "/json/");
    }
    protected List<Address> getAddressFromViaCep(String queryUrl) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                urlApiExterna + queryUrl,
                HttpMethod.GET, null, String.class);

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Address>> pTypeList = new TypeReference<>() {
        };
        TypeReference<Address> pTypeItem = new TypeReference<Address>() {
        };

        List<Address> listAddress = new ArrayList<>();

        if (Objects.requireNonNull(response.getBody()).startsWith("[")) {
            try {
                listAddress = mapper.readValue(response.getBody(), pTypeList);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            Address address = null;
            try {
                address = mapper.readValue(response.getBody(), pTypeItem);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            listAddress.add(address);
        }

        return listAddress;
    }

}
