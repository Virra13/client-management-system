package com.clientmanagement.backend.model.entity;

import com.clientmanagement.backend.model.dto.ClientType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "client", schema = "public")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "client_name", length = 100, nullable = false)
    private String clientName;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", length = 20, nullable = false)
    private ClientType clientType;

    @Column(name = "added", nullable = false, updatable = false)
    private LocalDate added;

    /**
     * Устанавливает дату создания клиента перед первичным сохранением сущности.
     */
    @PrePersist
    protected void onCreate() {
            this.added = LocalDate.now();
    }

    /**
     * Список адресов, связанных с клиентом.
     */
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("addressId ASC")
    private Set<AddressEntity> addresses = new LinkedHashSet<>();
}




