package com.clientmanagement.backend.repository;

import com.clientmanagement.backend.model.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Query(value = """
          select
              c.client_id as clientId,
              c.client_name as clientName,
              c.client_type as clientType,
              c.added as added,
              a.address_id as addressId,
              a.ip as ip,
              a.mac as mac,
              a.model as model,
              a.address as address
          from client c
          left join address a on c.client_id = a.client_id                 
          where
              (:client_name is null OR c.client_name ilike :client_name) AND
              (:client_type is null OR c.client_type ilike :client_type) AND
              (:address is null OR a.address ilike :address)
          order by c.client_id, a.address_id
            """, nativeQuery = true)

    List<ClientAddressSearchProjection> searchByParam(
            @Param("client_name") String clientName,
            @Param("client_type") String clientType,
            @Param("address") String address);
}


