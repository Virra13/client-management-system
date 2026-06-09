package com.clientmanagement.backend.model.mapper;

import com.clientmanagement.backend.model.dto.AddressDto;
import com.clientmanagement.backend.model.dto.ClientDto;
import com.clientmanagement.backend.model.entity.AddressEntity;
import com.clientmanagement.backend.model.entity.ClientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ClientEntityToDto implements Function<ClientEntity, ClientDto> {

    private final Function<AddressEntity, AddressDto> addressEntityAddressDtoFunction;

    @Override
    public ClientDto apply(ClientEntity clientEntity) {
        return ClientDto.builder()
                .clientId(clientEntity.getClientId())
                .clientName(clientEntity.getClientName())
                .clientType(clientEntity.getClientType())
                .added(clientEntity.getAdded())
                .addresses(
                        Optional.ofNullable(clientEntity.getAddresses())
                                .orElse(new HashSet<>())
                                .stream()
                                .map(addressEntityAddressDtoFunction)
                                .toList()
                )
                .build();
    }
}