package com.clientmanagement.backend.service;

import com.clientmanagement.backend.model.dto.AddressDto;

import java.util.List;

public interface AddressService {

    void update(Long id, List<AddressDto> addressDto);

    void delete(Long id, Long addressId);

    AddressDto createAddress(Long clientId, AddressDto dto);
}
