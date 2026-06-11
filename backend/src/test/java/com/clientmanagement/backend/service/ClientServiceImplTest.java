package com.clientmanagement.backend.service;

import com.clientmanagement.backend.model.dto.ClientDto;
import com.clientmanagement.backend.model.entity.ClientEntity;
import com.clientmanagement.backend.repository.ClientRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private Function<ClientEntity, ClientDto> clientEntityToDtoFunction;

    @Mock
    private Function<ClientDto, ClientEntity> clientDtoToEntityFunction;

    private ClientServiceImpl clientService;

    private AddressService addressService;

    @BeforeEach
    void setUp() {
        clientService = new ClientServiceImpl(
                clientRepository,
                addressService,
                clientEntityToDtoFunction,
                clientDtoToEntityFunction
        );
    }

    //тест метода findAllClient() при наличии объектов
    @Test
    void shouldReturnAllClients() {

        ClientEntity entity1 = new ClientEntity();
        ClientEntity entity2 = new ClientEntity();

        ClientDto dto1 = new ClientDto();
        ClientDto dto2 = new ClientDto();

        when(clientRepository.findAll())
                .thenReturn(List.of(entity1, entity2));
        when(clientEntityToDtoFunction.apply(entity1))
                .thenReturn(dto1);
        when(clientEntityToDtoFunction.apply(entity2))
                .thenReturn(dto2);

        List<ClientDto> result = clientService.findAllClient().toList();

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        verify(clientRepository).findAll();
        verify(clientEntityToDtoFunction).apply(entity1);
        verify(clientEntityToDtoFunction).apply(entity2);
    }

    //тест метода findAllClient() при отстутствии объектов
    @Test
    void shouldReturnEmpty() {

        when(clientRepository.findAll())
                .thenReturn(List.of());

        List<ClientDto> result = clientService.findAllClient().toList();

        assertTrue(result.isEmpty());
        verify(clientRepository).findAll();
        verifyNoInteractions(clientEntityToDtoFunction);
    }

    //тест метода findByClientId(clientId) когда клиент найден
    @Test
    void shouldReturnClientById() {
        ClientEntity entity1 = new ClientEntity();
        Long clientId = 1L;
        ClientDto dto1 = new ClientDto();

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(entity1));
        when(clientEntityToDtoFunction.apply(entity1))
                .thenReturn(dto1);

        Optional<ClientDto> result = clientService.findByClientId(clientId);

        assertTrue(result.isPresent());
        assertEquals(dto1, result.get());

        verify(clientRepository).findById(clientId);
        verify(clientEntityToDtoFunction).apply(entity1);
    }

    //тест метода findByClientId(clientId) когда клиент не найден
    @Test
    void shouldThrowExceptionWhenClientDoesNotExist() {
        Long clientId = 1L;

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.empty());

        NoSuchElementException exception =
                assertThrows(
                        NoSuchElementException.class,
                        () -> clientService.findByClientId(clientId)
                );

        assertEquals(
                "Клиент с id=" + clientId + " не найден",
                exception.getMessage()
        );

        verify(clientRepository).findById(clientId);
        verifyNoInteractions(clientEntityToDtoFunction);
    }
}
