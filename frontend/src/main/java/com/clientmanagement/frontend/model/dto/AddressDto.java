package com.clientmanagement.frontend.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    @JsonProperty("address_id")
    private Long addressId;
    @NotBlank(message = "Поле «IP-адрес» обязательно для заполнения")
    @Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])$", message = "Введите корректный IP-адрес в формате 192.168.0.1")
    private String ip;

    @NotBlank(message = "Поле «MAC-адрес» обязательно для заполнения")
    @Pattern(regexp = "^([0-9A-Fa-f]{2}-){5}[0-9A-Fa-f]{2}$", message = "Введите корректный MAC-адрес в формате AA-BB-CC-DD-EE-FF")
    private String mac;

    @NotBlank(message = "Поле «Модель устройства» обязательно для заполнения")
    @Size(max = 100, message = "Название модели не должно превышать 100 символов")
    private String model;

    @NotBlank(message = "Поле «Адрес» обязательно для заполнения")
    @Size(max = 200, message = "Адрес не должен превышать 200 символов")
    private String address;

    private Long clientId;

}
