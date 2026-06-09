package com.clientmanagement.backend.service;

import com.clientmanagement.backend.model.dto.ClientDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ClientService {
    Stream<ClientDto> findAllClient();

    Optional<ClientDto> findByClientId(Long id);

    ClientDto createClient(ClientDto clientDto);

    List<ClientDto> search(String clientName, String clientType, String address);

    void update(Long id, ClientDto clientDto);

    void delete(Long id);
}



