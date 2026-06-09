package com.clientmanagement.backend.service;

import com.clientmanagement.backend.model.dto.AddressDto;
import com.clientmanagement.backend.model.entity.AddressEntity;
import com.clientmanagement.backend.model.entity.ClientEntity;
import com.clientmanagement.backend.repository.AddressRepository;
import com.clientmanagement.backend.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ClientRepository clientRepository;
    private final Function<AddressDto, AddressEntity> addressDtoAddressEntityFunction;
    private final Function<AddressEntity, AddressDto> addressEntityAddressDtoFunction;

    @Override
    public void update(Long clientId, Long addressId, AddressDto dto) {

        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Тело запроса не должно быть пустым");
        }

        AddressEntity address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Адрес не найден"));

        if (!address.getClient().getClientId().equals(clientId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Адрес не найден у указанного клиента");
        }

        address.setIp(dto.getIp());
        address.setMac(dto.getMac());
        address.setModel(dto.getModel());
        address.setAddress(dto.getAddress());
        addressRepository.save(address);
    }

    @Override
    public void delete(Long clientId, Long addressId) {
        AddressEntity address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Адрес не найден"));

        if (!address.getClient().getClientId().equals(clientId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Адрес не найден у указанного клиента");
        }

       addressRepository.delete(address);
    }

    @Override
    public AddressDto createAddress(Long clientId, AddressDto dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Тело запроса не должно быть пустым");
        }

        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Клиент не найден"));

        dto.setAddressId(null);
        AddressEntity entity = addressDtoAddressEntityFunction.apply(dto);
        entity.setClient(client);
        AddressEntity saved = addressRepository.save(entity);
        return addressEntityAddressDtoFunction.apply(saved);
    }
}