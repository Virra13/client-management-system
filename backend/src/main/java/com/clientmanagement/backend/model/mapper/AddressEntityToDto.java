package com.clientmanagement.backend.model.mapper;

import com.clientmanagement.backend.model.dto.AddressDto;
import com.clientmanagement.backend.model.entity.AddressEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AddressEntityToDto implements Function<AddressEntity, AddressDto> {

    @Override
    public AddressDto apply(AddressEntity addressEntity) {
        return AddressDto.builder()
                .addressId(addressEntity.getAddressId())
                .ip(addressEntity.getIp())
                .mac(addressEntity.getMac())
                .model(addressEntity.getModel())
                .address(addressEntity.getAddress())
                .clientId(addressEntity.getClient().getClientId())
                .build();
    }
}