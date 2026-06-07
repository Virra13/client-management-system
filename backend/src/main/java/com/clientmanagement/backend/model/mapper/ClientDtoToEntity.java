package com.backEndSpring.model.mapper;

import com.backEndSpring.model.dto.AddressDto;
import com.backEndSpring.model.dto.ClientDto;
import com.backEndSpring.model.entity.AddressEntity;
import com.backEndSpring.model.entity.ClientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientDtoToEntity implements Function<ClientDto, ClientEntity> {

    private final Function<AddressDto, AddressEntity> addressDtoFunction;

    @Override
    public ClientEntity apply(ClientDto dto) {
        return ClientEntity.builder()
                .clientId(dto.getClientId())
                .clientName(dto.getClientName())
                .clientType(dto.getClientType())
                .added(dto.getAdded())
                .addresses(
                        Optional.ofNullable(dto.getAddresses())
                                .orElse(new ArrayList<>())
                                .stream()
                                .map(addressDtoFunction)
                                .collect(Collectors.toSet())
                )
                .build();
    }
}