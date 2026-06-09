package com.clientmanagement.backend.controller;

import com.clientmanagement.backend.model.dto.AddressDto;
import com.clientmanagement.backend.model.dto.ClientDto;
import com.clientmanagement.backend.service.AddressService;
import com.clientmanagement.backend.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;
    private final AddressService addressService;

    //Получить всех клиентов (с их адресами)
    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClient() {
        log.info("Получен запрос всех клиентов!");
        return ResponseEntity.ok(clientService.findAllClient().toList());

    }

    //Получить клиента и его адреса по ID
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        log.info("Получен запрос клиента id: {}", id);
        return clientService.findByClientId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Создать клиента вместе с его адресами
    @PostMapping
    public ResponseEntity<ClientDto> createClient(
            @RequestBody @Valid ClientDto clientDto
    ) {
        log.info("📋 Создание клиента");
        log.info("CLIENT DTO = {}", clientDto);
        ClientDto created = clientService.createClient(clientDto);
         return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    //Создать новый адрес для клиента
    @PostMapping("/{id}/addresses")
    public ResponseEntity<AddressDto> createAddress(
            @PathVariable Long id,
            @RequestBody @Valid AddressDto addressDto
    ) {
        log.info("📋 Создание адреса для клиента id: {}", id);
        log.info("CLIENT DTO = {}", addressDto);

        AddressDto created = addressService.createAddress(id, addressDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    //Обновить данные клиента
    @PutMapping("/{id}")
    public void updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientDto dto
    ) {
        log.info("Получен запрос на изменение данных клиента id: {}", id);
        clientService.update(id, dto);
    }

    //Обновить данные конкретного адреса
    @PutMapping("/{id}/addresses/{addressId}")
    public void updateAddress(
            @PathVariable Long id,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressDto dto
    ) {
        log.info("Получен запрос на изменение адреса клиента id: {}", id);
        addressService.update(id, addressId, dto);
    }

    //Удалить клиента и все его адреса
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.info("Получен запрос на удаление клиента id: {}", id);
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //Удалить конкретный адрес клиента
    @DeleteMapping("/{id}/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id, @PathVariable Long addressId) {
        log.info("Получен запрос на удаление адреса клиента id: {}", id);
        addressService.delete(id, addressId);
        return ResponseEntity.noContent().build();
    }

    //Поиск по clientName и/или type и/или address (частичное совпадение)
    @GetMapping("/search")
    public ResponseEntity<List<ClientDto>> search(
            @RequestParam(value = "client_name", required = false) String clientName,
            @RequestParam(value = "client_type", required = false) String clientType,
            @RequestParam(value = "address", required = false) String address

    ) {
        return ResponseEntity.ok(clientService.search(clientName, clientType, address));
    }

}