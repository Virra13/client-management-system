package com.uiSpring.service;

import com.uiSpring.model.ClientModel;
import com.uiSpring.model.dto.ClientDto;

import java.util.List;
import java.util.Optional;


public interface ClientService {

List<ClientDto> getAllClient ();

Optional<ClientDto> getClientById (Long id);

Optional<ClientDto> create(ClientDto clientDto);

}
