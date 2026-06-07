package com.backEndSpring.repository;

import com.backEndSpring.model.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}