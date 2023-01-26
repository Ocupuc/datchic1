package ru.ocupuc.datchic1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ocupuc.datchic1.models.Measurement;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement,Integer> {

}
