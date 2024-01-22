package com.example.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, message = "Name must have at minimum two letters")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column(nullable = false)
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email is not valid", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @Column(nullable = false)
    @Size(min = 1, max = 1, message = "Gender can has one letter")
    @Pattern(message = "Gener is M or F", regexp = "(M|F)")
    @NotEmpty(message = "Gender cannot be empty")
    private String gender;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Address> address = new ArrayList<>();

    public Customer() {
    }

    public Customer(Long id, String name, String email, String gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        address.forEach(a -> a.setCustomer(this));
        this.address = address;
    }
}
