package com.clientmanagement.frontend.controller;

import com.clientmanagement.frontend.model.ClientModel;
import com.clientmanagement.frontend.model.dto.ClientDto;
import com.clientmanagement.frontend.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final Function<ClientDto, Stream<ClientModel>> clientDtoToModelsFunction;
    private final Function<ClientModel, ClientDto> clientModelToDtoFunction;

    @GetMapping(value = "/")
    public String getAllClientPage(Model model) {
        log.info("🌐 Запрошена страница списка пользователей (маршрут: / )");
        long startTime = System.currentTimeMillis();

        List<ClientDto> clients = clientService.getAllClient();
        model.addAttribute("clientsCollection", clients);

        long duration = System.currentTimeMillis() - startTime;
        log.info("✅ Страница пользователей подготовлена за {} мс", duration);

        return "all_clients_page";
    }

    @GetMapping(value = "/clients/{id}")
    public String getClientById(@PathVariable Long id, Model model) {
        log.info("🌐 Запрошена информация о пользователе {}", id);
        long startTime = System.currentTimeMillis();

        ClientDto client = clientService.getClientById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Клиент не найден"
                        ));

        model.addAttribute("clientsCollection", List.of(client));

        long duration = System.currentTimeMillis() - startTime;
        log.info("✅ Страница подготовлена за {} мс", duration);

        return "all_clients_page";
    }


    @GetMapping(path = "/create")
    public String getClientCreatePage() {
        log.info("🌐 Запрошена страница создания пользователя");
        return "client_create_page";
    }

    @GetMapping(path = "/clients/{id}/edit")
    public String getClientUpdatePage(@PathVariable Long id, Model model) {
        log.info("🌐 Запрошено редактирование пользоваетля {}", id);

        ClientDto client = clientService.getClientById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Клиент не найден"
                        ));

        model.addAttribute("clientDto", client);

        return "client_update_page";
    }


    @PostMapping("/clients/{id}/edit")
    @ResponseBody
    public ResponseEntity<?> updateClient(
            @PathVariable Long id,
            @Valid @ModelAttribute ClientDto clientDto,
            BindingResult bindingResult
    ) {
        log.info("DTO из формы: {}", clientDto);
        log.info("🌐 Проверка валидности введенных данных");

        if (clientDto.getAddresses() == null) {
            clientDto.setAddresses(new ArrayList<>());
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();

            bindingResult.getFieldErrors()
                    .forEach(error -> fieldErrors.put(
                            error.getField(),
                            error.getDefaultMessage()
                    ));

            return ResponseEntity
                    .badRequest()
                    .body(fieldErrors);
        }
        log.info("✅ Данные успешно прошли валидацию");
        clientService.updateClient(id, clientDto);
        log.info("✅ Пользователь {} отредактирован", id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/")
    @ResponseBody
    public ResponseEntity<?> createClientAjax(
            @Valid @ModelAttribute ClientDto clientDto,
            BindingResult bindingResult
    ) {
        log.info("DTO из формы: {}", clientDto);
        log.info("🌐 Проверка валидности введенных данных");

        if (clientDto.getAddresses() == null) {
            clientDto.setAddresses(new ArrayList<>());
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();

            bindingResult.getFieldErrors()
                    .forEach(error -> fieldErrors.put(
                            error.getField(),
                            error.getDefaultMessage()
                    ));

            return ResponseEntity
                    .badRequest()
                    .body(fieldErrors);
        }
        log.info("✅ Данные успешно прошли валидацию");
        try {
            clientService.create(clientDto);
            log.info("Клиент успешно создан");
            return ResponseEntity.ok("Клиент создан");
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Ошибка при создании клиента");
        }
    }

    @PostMapping("/clients/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("🌐 Удаление клиента с id {}", id);
        clientService.deleteClientById(id);
        redirectAttributes.addFlashAttribute("success", "Клиент успешно удалён");
        return "redirect:/";
    }

    @PostMapping("/clients/search")
    public String findByRequest (        @RequestParam(required = false) String clientType,
                                         @RequestParam(required = false) String clientName,
                                         @RequestParam(required = false) String address,
                                         Model model) {
        log.info("🌐 Поиск клиента по данным: тип клиента {}, имя клиента {}, адрес клиента {}", clientType, clientName, address);
        List<ClientDto> clients = clientService.search(clientType, clientName, address);
        model.addAttribute("clientsCollection", clients);

        return "all_clients_page";
    }
}