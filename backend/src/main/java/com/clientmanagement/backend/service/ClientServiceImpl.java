package com.backEndSpring.service;

import com.backEndSpring.model.dto.AddressDto;
import com.backEndSpring.model.dto.ClientDto;
import com.backEndSpring.model.entity.ClientEntity;
import com.backEndSpring.repository.ClientAddressSearchProjection;
import com.backEndSpring.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final Function<ClientEntity, ClientDto> clientEntityToDtoFunction;
    private final Function<ClientDto, ClientEntity> clientDtoToEntityFunction;

    @Override
    public Stream<ClientDto> findAllClient() {
        return clientRepository.findAll().stream()
                .map(clientEntityToDtoFunction);

    }

    @Override
    public Optional<ClientDto> findByClientId(Long clientId) {
        return Optional.ofNullable(clientRepository.findById(clientId)
                .map(clientEntityToDtoFunction)
                .orElseThrow(
                        () -> new NoSuchElementException(
                                "Клиент с id=" + clientId + " не найден"
                        )));
    }

    @Override
    public ClientDto createClient(ClientDto clientDto) {

        if (clientDto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Тело запроса не должно быть пустым");
        }
        clientDto.setClientId(null);
        ClientEntity entity = clientDtoToEntityFunction.apply(clientDto);
        entity.setClientId(null);
        entity.setAdded(LocalDate.now());
        setClientForAddresses(entity);
        return clientEntityToDtoFunction.apply(clientRepository.save(entity));

    }

    private void setClientForAddresses(ClientEntity client) {
        client.getAddresses()
                .forEach(address -> {
                    address.setAddressId(null);
                    address.setClient(client);
                });
    }

    public List<ClientDto> search(String clientName, String clientType, String address) {
        clientName = clientName != null ? ("%" + clientName + "%") : null;
        clientType = clientType != null ? ("%" + clientType + "%") : null;
        address = address != null ? ("%" + address + "%") : null;

        List<ClientAddressSearchProjection> projections = clientRepository.searchByParam(clientName, clientType, address);
        Map<Long, ClientDto> clients = new LinkedHashMap<>();

        for (ClientAddressSearchProjection projection : projections) {
            ClientDto clientDto = clients.get(projection.getClientId());
            if (clientDto == null) {
                clientDto = new ClientDto();
                clientDto.setClientId(projection.getClientId());
                clientDto.setClientName(projection.getClientName());
                clientDto.setClientType(projection.getClientType());
                clientDto.setAdded(projection.getAdded());
                clientDto.setAddresses(new ArrayList<>());
                clients.put(projection.getClientId(), clientDto);
            }
            if (projection.getAddressId() != null) {
            AddressDto addressDto = new AddressDto();
            addressDto.setAddressId(projection.getAddressId());
            addressDto.setClientId(projection.getClientId());
            addressDto.setIp(projection.getIp());
            addressDto.setMac(projection.getMac());
            addressDto.setModel(projection.getModel());
            addressDto.setAddress(projection.getAddress());
            clientDto.getAddresses().add(addressDto);
        }}

        return new ArrayList<>(clients.values());
    }

    @Override
    public void update(Long clientId, ClientDto clientDto) {
        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow();
        client.setClientName(clientDto.getClientName().trim());
        client.setClientType(clientDto.getClientType());
        clientRepository.save(client);
    }

    @Transactional
    @Override
    public void delete(Long clientId) {
        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Клиент не найден"));

        clientRepository.delete(client);
    }
}
