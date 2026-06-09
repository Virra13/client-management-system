package com.clientmanagement.frontend.model.dto;

public enum ClientType {
    PERSON ("Физическое лицо"),
    BUSINESS ("Юридическое лицо");

    private String displayName;

    ClientType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
