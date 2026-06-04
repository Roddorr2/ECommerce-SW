package com.ecommerce.app.shared.infrastructure.persistence.repository;

import com.ecommerce.app.shared.infrastructure.persistence.entity.CargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoJpaRepository extends JpaRepository<CargoEntity, Integer> {
}
