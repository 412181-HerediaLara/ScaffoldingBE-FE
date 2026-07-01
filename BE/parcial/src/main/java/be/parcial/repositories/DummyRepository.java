package be.parcial.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import be.parcial.entities.DummyEntity;

@Repository
public interface DummyRepository extends JpaRepository<DummyEntity, Long> {

}
