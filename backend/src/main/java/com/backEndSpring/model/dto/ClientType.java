package com.backEndSpring.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Set;

public enum ClientType {
    PERSON("Физическое лицо",
            Set.of("person", "physical person", "individual", "private person",
                    "физ лицо", "физлицо", "физическое лицо",
                    "частник", "частное лицо", "гражданин")),

    BUSINESS("Юридическое лицо",
            Set.of("business", "legal entity", "company", "corporation",
                    "юр лицо", "юрлицо", "юридическое лицо",
                    "компания", "организация", "предприятие"));

    @Getter
    private final String displayName;

    private final Set<String> aliases;

    ClientType(String displayName, Set<String> aliases) {
        this.displayName = displayName;
        this.aliases = aliases;
    }

    @JsonCreator
    public static ClientType fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Client type is null");
        }

        String normalized = value.trim().toLowerCase().replace("ё", "е");

        for (ClientType type : ClientType.values()) {
            if (type.name().toLowerCase().equals(normalized)) {
                return type;
            }

            if (type.aliases.contains(normalized)) {
                return type;
            }
        }
            throw new IllegalArgumentException("Unknown client type: " + value);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
