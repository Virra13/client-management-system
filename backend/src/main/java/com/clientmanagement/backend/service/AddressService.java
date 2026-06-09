package com.clientmanagement.backend.service;

import com.clientmanagement.backend.model.dto.AddressDto;

public interface AddressService {

    void update(Long id, Long addressId, AddressDto dto);

    void delete(Long id, Long addressId);

    AddressDto createAddress(Long clientId, AddressDto dto);
}
