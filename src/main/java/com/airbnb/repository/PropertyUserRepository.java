package com.airbnb.repository;

import com.airbnb.entity.PropertyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyUserRepository extends JpaRepository<PropertyUser, Long> {
}