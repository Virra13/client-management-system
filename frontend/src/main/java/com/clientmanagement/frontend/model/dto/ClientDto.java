package com.clientmanagement.frontend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private Long clientId;

    @JsonProperty(value = "client_name")
    @NotBlank(message = "Поле «ФИО» обязательно для заполнения")
    @Size(max = 100, message = "Имя клиента не должно превышать 100 символов")
    @Pattern(regexp = "^[а-яА-ЯёЁ\\-., ]+$", message = "Имя клиента может содержать только русские буквы, пробелы, точку, запятую и тире")
    private String clientName;

    @JsonProperty(value = "client_type")
    @NotNull(message = "Необходимо выбрать тип клиента")
    private ClientType clientType;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate added;

    @Valid
    private List<AddressDto> addresses;
}
