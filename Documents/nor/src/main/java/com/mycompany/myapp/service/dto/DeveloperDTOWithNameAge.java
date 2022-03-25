package com.mycompany.myapp.service.dto;

import java.io.Serializable;

public class DeveloperDTOWithNameAge implements Serializable {

    private Long id;

    private String name;

    private Integer age;

    public DeveloperDTOWithNameAge() {
    }

    public DeveloperDTOWithNameAge(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

}
