package com.nothing.contacts;

import java.util.UUID;

public class ContactDetails {
    private UUID id;
    private String name, number;

    public ContactDetails(){
        this(UUID.randomUUID());
    }

    public ContactDetails(UUID id){
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public UUID getId() {
        return id;
    }
}
