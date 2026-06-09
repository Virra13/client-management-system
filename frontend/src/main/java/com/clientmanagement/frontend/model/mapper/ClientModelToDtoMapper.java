package com.clientmanagement.frontend.model.mapper;

import com.clientmanagement.frontend.model.ClientModel;
import com.clientmanagement.frontend.model.dto.AddressDto;
import com.clientmanagement.frontend.model.dto.ClientDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class ClientModelToDtoMapper implements Function<ClientModel, ClientDto> {

    @Override
    public ClientDto apply(ClientModel clientModel) {
        return ClientDto.builder()
                .clientId(clientModel.getClientId())
                .clientName(clientModel.getClientName())
                .clientType(clientModel.getClientType())
                .added(clientModel.getAdded())
                .addresses(clientModel.getAddress() == null ?
                        List.of() :
                        List.of(AddressDto.builder()
                                .addressId(clientModel.getAddressId())
                                .ip(clientModel.getIp())
                                .mac(clientModel.getMac())
                                .model(clientModel.getModel())
                                .address(clientModel.getAddress())
                                .clientId(clientModel.getClientId())
                                .build())
                )
                .build();
    }
}
