package com.services.banktransactionservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum SourceType {
    COMPANY("COMPANY"),
    PERSON("PERSON");

    private final String description;

    SourceType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getId() {
        return name();
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static SourceType fromStr(String value) {
        return Stream.of(values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid source type"));
    }
}
