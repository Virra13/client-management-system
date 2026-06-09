package com.clientmanagement.backend.repository;

import com.clientmanagement.backend.model.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}