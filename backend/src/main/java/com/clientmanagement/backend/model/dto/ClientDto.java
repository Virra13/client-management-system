package com.clientmanagement.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long clientId;

    @JsonProperty(value = "client_name")
    @NotBlank
    @Size(max = 100, message = "Имя клиента не должно превышать 100 символов")
    @Pattern(regexp = "^[а-яА-ЯёЁ\\-., ]+$", message = "Имя клиента может содержать только русские буквы, пробелы, точку, запятую и тире")
    private String clientName;

    @JsonProperty(value = "client_type")
    @NotNull(message = "Необходимо выбрать тип клиента")
    private ClientType clientType;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate added;

    private List<AddressDto> addresses;
}