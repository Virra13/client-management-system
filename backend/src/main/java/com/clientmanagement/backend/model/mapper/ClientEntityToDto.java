package com.backEndSpring.model.mapper;

import com.backEndSpring.model.dto.AddressDto;
import com.backEndSpring.model.dto.ClientDto;
import com.backEndSpring.model.entity.AddressEntity;
import com.backEndSpring.model.entity.ClientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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