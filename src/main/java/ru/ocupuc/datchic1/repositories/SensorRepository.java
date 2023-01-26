package ru.ocupuc.datchic1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ocupuc.datchic1.models.Sensor;

import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, String> {
    Optional<Sensor>findByName(String name);
}
