package com.icepip.project;



public class User {

    private Integer id;

    private String name;
    public User() {
    }
    public User(String name, int id) {
        this.id=id;
        this.name=name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
