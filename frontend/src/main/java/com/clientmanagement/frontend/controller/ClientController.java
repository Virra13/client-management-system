package com.clientmanagement.frontend.controller;

import com.clientmanagement.frontend.ValidationException;
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

    @GetMapping(value = "/{id}")
    public String getClientById(@PathVariable Long id, Model model) {
        log.info("🌐 Запрошена информация о пользователе {}", id);

        ClientDto client = clientService.getClientById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Клиент не найден"
                        ));

        model.addAttribute("clientsCollection", List.of(client));
        return "all_clients_page";
    }


    @GetMapping(path = "/create")
    public String getClientCreatePage() {
        return "client_create_page";
    }


    @PostMapping(value = "/")
    @ResponseBody
    public ResponseEntity<?> createClientAjax(
            @Valid @ModelAttribute ClientDto clientDto,
            BindingResult bindingResult
    ) {
        log.info("DTO из формы: {}", clientDto);

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
        clientService.deleteClientById(id);
        redirectAttributes.addFlashAttribute("success", "Клиент успешно удалён");
        return "redirect:/";
    }
}

//clients/{id}/delete

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

//    public String createClient(
//            @Valid @ModelAttribute ClientDto clientDto,
//            BindingResult bindingResult,
//            Model model) {
//
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("error", "Ошибка валидации данных");
//            model.addAttribute("formData", model);
//            return "client_create_page";
//        }
//
//        clientService.create(clientDto);
//        return "redirect:/";
//
//        try { clientService.create(clientDto);
//
//            log.info("✅ Пользователь успешно создан");
//            return "redirect:/";
//
//        } catch (ValidationException e) {
//
//            log.warn("⚠️ Ошибка валидации при создании пользователя: {}", e.getMessage());
//            log.debug("📋 Детали ошибок: {}", e.getErrorMessages());
//
//            model.addAttribute("validationErrors", e.getErrorMessages());
//            model.addAttribute("formData", model);
//
//           log.info("🔙 Возврат на страницу валидации с ошибками");
//            return "client-create";
//            //return "clients_validation_errors";
//
//        } catch (Exception e) {
//            log.error("💥 Неожиданная ошибка при создании пользователя", e);
//            throw e;
//       }
//    }