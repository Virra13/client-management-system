package com.uiSpring.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Size(max = 100, message = "Имя клиента не должно превышать 100 символов")
    @Pattern(regexp = "^[а-яА-ЯёЁ\\-., ]+$", message = "Имя клиента может содержать только русские буквы, пробелы, точку, запятую и тире")
    private String clientName;

    @JsonProperty(value = "client_type")
    @NotNull(message = "Необходимо выбрать тип клиента")
    private ClientType clientType;

    private LocalDate added;

    private List<AddressDto> addresses;
}
