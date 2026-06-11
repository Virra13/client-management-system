package com.clientmanagement.frontend.service;

import com.clientmanagement.frontend.util.ValidationErrorResponse;
import com.clientmanagement.frontend.util.ValidationException;
import com.clientmanagement.frontend.model.dto.ClientDto;
import com.clientmanagement.frontend.rest_client.ExternalApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientApiClient implements ClientService {

    private final RestClient restClient;

    @Override
    public List<ClientDto> getAllClient() {

        List<ClientDto> clients = restClient.get()
                .uri("/api/clients")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleErrorResponse)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleErrorResponse)
                .body(new ParameterizedTypeReference<List<ClientDto>>() {
                });

//        Stream<ClientDto> resultStream = clients != null ? clients.stream() : Stream.empty();

        long count = clients != null ? clients.size() : 0;
        log.info("✅ Получено {} клиентов", count);

        return clients == null ? List.of() : clients;
    }

    @Override
    public Optional<ClientDto> getClientById(Long id) {

        Optional<ClientDto> client = Optional.ofNullable(restClient.get()
                .uri("/api/clients/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleErrorResponse)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleErrorResponse)
                .body(ClientDto.class));

        log.info("✅ Получен клиент id {}", id);
        return client;
    }


    @Override
    public Optional<ClientDto> create(ClientDto clientDto) {

        try {
            ClientDto created = restClient.post()
                    .uri("/api/clients")
                    .body(clientDto)
                    .retrieve()
                    // Кастомный обработчик для клиентских ошибок
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        int statusCode = res.getStatusCode().value();
                        log.warn("⚠️ Клиентская ошибка при создании: HTTP {}", statusCode);

                        if (statusCode == 400) {
                            // Специальная обработка валидации: читаем тело и парсим ошибку
                            try {
                                String body = new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8);
                                log.debug("📄 Тело ошибки валидации: {}", body);

                                ValidationErrorResponse validationError = new ObjectMapper()
                                        .readValue(body, ValidationErrorResponse.class);

                                // Выбрасываем предметное исключение с деталями валидации
                                throw new ValidationException(
                                        validationError.getMessage(),
                                        validationError.getAllErrorMessages()
                                );
                            } catch (IOException | JacksonException e) {
                                log.error("❌ Не удалось распарсить ошибку валидации", e);
                                throw new ValidationException(
                                        "Ошибка валидации",
                                        List.of("Не удалось прочитать детали ошибки")
                                );
                            }
                        }

                        // Для 409 (Conflict) и 422 (Unprocessable) — не выбрасываем исключение,
                        // позволяем методу завершиться и вернуть Optional.empty()
                        if (statusCode == 409 || statusCode == 422) {
                            log.info("ℹ️ Статус {}: пользователь не создан (возможно, уже существует)", statusCode);
                            return; // Прерываем обработку, не выбрасывая исключение
                        }

                        // Все остальные 4xx — стандартная обработка
                        handleErrorResponse(req, res);
                    })
                    // Обработчик серверных ошибок (500-599)
                    .onStatus(HttpStatusCode::is5xxServerError, this::handleErrorResponse)
                    .body(ClientDto.class);

            if (created != null) {
                log.info("✅ Пользователь успешно создан: id={}",
                        created.getClientId());
                return Optional.of(created);
            } else {
                log.info("⚪ Пользователь не создан (ответ пустой)");
                return Optional.empty();
            }

        } catch (ValidationException | ExternalApiException e) {
            // Предметные исключения пробрасываем дальше — они уже залогированы
            throw e;
        } catch (Exception e) {
            // Неожиданные ошибки логируем и оборачиваем
            log.error("💥 Неожиданная ошибка при создании пользователя", e);
            throw new ExternalApiException(
                    "Внутренняя ошибка клиента",
                    null,
                    null,
                    e
            );
        }
    }

    @Override
    public void deleteClientById(Long clientId) {
        restClient.delete()
                .uri("/api/clients/{id}", clientId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleErrorResponse)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleErrorResponse)
                .toBodilessEntity();

        log.info("✅ Пользователь id={} успешно удален!", clientId);
    }

    @Override
    public List<ClientDto> search(String clientType, String clientName, String address) {

        List<ClientDto> clients = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/clients/search");
                    if (clientName != null && !clientName.isBlank()) {
                        uriBuilder.queryParam("client_name", clientName);
                    }
                    if (clientType != null && !clientType.isBlank()) {
                        uriBuilder.queryParam("client_type", clientType);
                    }
                    if (address != null && !address.isBlank()) {
                        uriBuilder.queryParam("address", address);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleErrorResponse)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleErrorResponse)
                .body(new ParameterizedTypeReference<List<ClientDto>>() {
                });
        return clients == null ? List.of() : clients;
    }

    @Override
    public void updateClient(Long id, ClientDto clientDto) {

         restClient.put()
                .uri("/api/clients/{id}", id)
                .body(clientDto)
                .retrieve()
                .toBodilessEntity();
    }

        private void handleErrorResponse (HttpRequest request, ClientHttpResponse response){
            HttpStatusCode code = null;

            try {
                code = response.getStatusCode();
                // Читаем тело ответа для включения в исключение (отладка/логирование)
                String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);

                if (code.value() == 404) {
                    log.error("❌ Ресурс не найден");
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Ресурс не найден");
                }

                String errorType = code.is4xxClientError() ? "🔴 Client error" : "🔥 Server error";
                log.error("{}: {} | URL: {} | Тело: {}",
                        errorType, code, request.getURI(), body);

                // Выбрасываем предметное исключение с полной информацией об ошибке
                throw new ExternalApiException(
                        errorType + ": " + code,
                        code,
                        body
                );

            } catch (IOException e) {
                // Если не удалось прочитать тело ответа (например, оно уже прочитано)
                log.error("❌ Не удалось прочитать тело ошибки для статуса {}", code, e);

                throw new ExternalApiException(
                        "Failed to read error response",
                        code,
                        null,
                        e // Сохраняем исходное исключение как причину
                );
            }
        }


    }
