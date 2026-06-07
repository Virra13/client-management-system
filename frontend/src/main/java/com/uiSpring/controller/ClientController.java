package com.uiSpring.controller;

import com.uiSpring.ValidationException;
import com.uiSpring.model.ClientModel;
import com.uiSpring.model.dto.ClientDto;
import com.uiSpring.model.dto.ClientType;
import com.uiSpring.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.util.List;
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

        List<ClientModel> clients = clientService.getAllClient()
                .stream()
                .flatMap(clientDtoToModelsFunction)
                .toList();

        model.addAttribute("clientsCollection", clients);

        long duration = System.currentTimeMillis() - startTime;
        log.info("✅ Страница пользователей подготовлена за {} мс", duration);

        return "all_clients_page";
    }

    @GetMapping(value = "/{id}")
    public String getClientById(@PathVariable Long id, Model model) {
        log.info("🌐 Запрошена информация о пользователе {}", id);

        List<ClientModel> clients = clientService.getClientById(id)
                .stream()
                .flatMap(clientDtoToModelsFunction)
                .toList();

        model.addAttribute("clientsCollection", clients);

        return "all_clients_page";
    }


    @GetMapping(path = "/create")
    public String getClientCreatePage() {

        return "client_create_page";
    }

    @PostMapping(value = "/")
//    public String createClient(@RequestParam("client_name") String clientName,
//                               @RequestParam("client_type") ClientType clientType,
//                               @RequestParam("ip") String ip,
//                               @RequestParam("mac") String mac,
//                               @RequestParam("model") String model,
//                               @RequestParam("address") String address,
//                               Model clientmodel) {
//
//        ClientModel clientModel = ClientModel.builder()
//                .clientName(clientName)
//                .clientType(clientType)
//                .ip(ip)
//                .mac(mac)
//                .model(model)
//                .address(address)
//                .build();

    public String createClient(@Valid @ModelAttribute ClientDto clientDto, Model model) {
        try { clientService.create(clientDto);

            log.info("✅ Пользователь успешно создан");
            return "redirect:/";

        } catch (ValidationException e) {

            log.warn("⚠️ Ошибка валидации при создании пользователя: {}", e.getMessage());
            log.debug("📋 Детали ошибок: {}", e.getErrorMessages());

            model.addAttribute("validationErrors", e.getErrorMessages());
            model.addAttribute("formData", model);

            log.info("🔙 Возврат на страницу валидации с ошибками");
            return "clients_validation_errors";

        } catch (Exception e) {
            log.error("💥 Неожиданная ошибка при создании пользователя", e);
            throw e;
        }
    }
}


