package com.backEndSpring.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address", schema = "public")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private Long  addressId;

    @Column(name = "ip", length = 25, nullable = false)
    private String ip;

    @Column(name = "mac", length = 20, nullable = false)
    private String mac;

    @Column(name = "model", length = 100, nullable = false)
    private String model;

    @Column(name = "address", length = 200, nullable = false)
    private String address;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;
}
