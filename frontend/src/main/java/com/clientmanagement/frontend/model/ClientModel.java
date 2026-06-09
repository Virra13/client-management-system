package com.clientmanagement.frontend.model;

import com.clientmanagement.frontend.model.dto.ClientType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientModel {
    private Long clientId;
    private String clientName;
    private ClientType clientType;
    private LocalDate added;
    private Long addressId;
    private String  ip;
    private String mac;
    private String model;
    private String address;
}

