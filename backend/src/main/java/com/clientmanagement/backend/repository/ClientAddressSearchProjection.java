package com.clientmanagement.backend.repository;

import com.clientmanagement.backend.model.dto.ClientType;

import java.time.LocalDate;

public interface ClientAddressSearchProjection {
    Long getClientId();
    String getClientName();
    ClientType getClientType();
    LocalDate getAdded();
    Long getAddressId();
    String getIp();
    String getMac();
    String getModel();
    String getAddress();
}
