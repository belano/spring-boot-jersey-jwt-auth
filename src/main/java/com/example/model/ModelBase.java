package com.example.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public abstract class ModelBase {

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
