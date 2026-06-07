package com.backEndSpring.service;

import com.backEndSpring.model.dto.AddressDto;
import com.backEndSpring.model.dto.ClientDto;

public interface AddressService {

    void update(Long id, Long addressId, AddressDto dto);

    void delete(Long id, Long addressId);

    AddressDto createAddress(Long clientId, AddressDto dto);
}
