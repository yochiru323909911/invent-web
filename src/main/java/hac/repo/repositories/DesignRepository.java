package hac.repo.repositories;

import hac.repo.entities.Design;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignRepository extends JpaRepository<Design, Long> {
}
