package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class AddressDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    private String cep;

    private String logradouro;

    private String complemento;

    private String bairro;

    private String localidade;

    private String uf;

    private int ibge;

    private int gia;

    private int ddd;

    private int siafi;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String erro;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CustomerDto customer;

    public AddressDto() {
    }

    public AddressDto(Long id, String cep, String logradouro, String complemento, String bairro, String localidade, String uf, int ibge, int gia, int ddd, int siafi, String erro) {
        this.id = id;
        this.cep = cep;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
        this.ibge = ibge;
        this.gia = gia;
        this.ddd = ddd;
        this.siafi = siafi;
        this.erro = erro;
    }

    public Long getId() {
        return id;
    }

    public String getCep() {
        return cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public String getUf() {
        return uf;
    }

    public int getIbge() {
        return ibge;
    }

    public int getGia() {
        return gia;
    }

    public int getDdd() {
        return ddd;
    }

    public int getSiafi() {
        return siafi;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public String getErro() {
        return erro;
    }
}
