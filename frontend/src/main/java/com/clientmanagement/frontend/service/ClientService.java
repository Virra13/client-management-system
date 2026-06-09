package com.clientmanagement.frontend.service;

import com.clientmanagement.frontend.model.dto.ClientDto;

import java.util.List;
import java.util.Optional;


public interface ClientService {

List<ClientDto> getAllClient ();

Optional<ClientDto> getClientById (Long id);

Optional<ClientDto> create(ClientDto clientDto);

void deleteClientById(Long clientId);
}
