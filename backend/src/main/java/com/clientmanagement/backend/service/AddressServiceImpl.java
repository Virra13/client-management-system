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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ClientRepository clientRepository;
    private final Function<AddressDto, AddressEntity> addressDtoAddressEntityFunction;
    private final Function<AddressEntity, AddressDto> addressEntityAddressDtoFunction;

    @Override
    public void update(Long clientId, List<AddressDto> addressDto) {

        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Клиент не найден"));
        Set<AddressEntity> existingAddresses = client.getAddresses();

        Set<Long> incomingIds = addressDto.stream()
                .map(AddressDto::getAddressId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());


        existingAddresses.removeIf(address ->
                !incomingIds.contains(address.getAddressId())
        );

        Map<Long, AddressEntity> existingById = existingAddresses.stream()
                .collect(Collectors.toMap(AddressEntity::getAddressId, Function.identity()));

        for (AddressDto dto : addressDto) {
            if (dto.getAddressId() == null) {
                AddressEntity newAddress = new AddressEntity();
                newAddress.setClient(client);
                newAddress.setIp(dto.getIp());
                newAddress.setMac(dto.getMac());
                newAddress.setModel(dto.getModel());
                newAddress.setAddress(dto.getAddress());

                existingAddresses.add(newAddress);
            } else {
                AddressEntity address = existingById.get(dto.getAddressId());

                if (address == null) {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Адрес не найден у указанного клиента"
                    );
                }
                address.setIp(dto.getIp());
                address.setMac(dto.getMac());
                address.setModel(dto.getModel());
                address.setAddress(dto.getAddress());
            }
        }
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