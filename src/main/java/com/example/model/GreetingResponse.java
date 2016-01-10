package com.example.model;

import java.io.Serializable;

public class GreetingResponse extends ModelBase implements Serializable {

    private static final long serialVersionUID = 6103699194152428303L;
    private String greeting;

    public GreetingResponse(String greeting) {
        super();
        this.greeting = greeting;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

}
