package com.uiSpring.model.mapper;

import com.uiSpring.model.ClientModel;
import com.uiSpring.model.dto.AddressDto;
import com.uiSpring.model.dto.ClientDto;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class ClientDtoToModelMapper implements Function<ClientDto, Stream<ClientModel>> {

    @Override
    public Stream<ClientModel> apply(ClientDto clientDto) {
        List<ClientModel> clients = new LinkedList<>();
        List<AddressDto> addresses = clientDto.getAddresses();

        if (addresses != null && !addresses.isEmpty()) {
            addresses.forEach(address -> clients.add(ClientModel.builder()
                    .clientId(clientDto.getClientId())
                    .clientName(clientDto.getClientName())
                    .clientType(clientDto.getClientType())
                    .added(clientDto.getAdded())
                    .ip(address.getIp())
                    .mac(address.getMac())
                    .model(address.getModel())
                    .address(address.getAddress())
                    .build()));
        } else {
            clients.add(ClientModel.builder()
                    .clientId(clientDto.getClientId())
                    .clientName(clientDto.getClientName())
                    .clientType(clientDto.getClientType())
                    .added(clientDto.getAdded())
                    .build());
        }
        return clients.stream();
    }
}
