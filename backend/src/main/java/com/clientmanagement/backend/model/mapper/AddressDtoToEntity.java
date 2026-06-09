package com.clientmanagement.backend.model.mapper;

import com.clientmanagement.backend.model.dto.AddressDto;
import com.clientmanagement.backend.model.entity.AddressEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AddressDtoToEntity implements Function<AddressDto, AddressEntity> {
    @Override
    public AddressEntity apply(AddressDto dto) {
        return AddressEntity.builder()
                .addressId(dto.getAddressId())
                .ip(dto.getIp())
                .mac(dto.getMac())
                .model(dto.getModel())
                .address(dto.getAddress())
                .build();
    }
}
