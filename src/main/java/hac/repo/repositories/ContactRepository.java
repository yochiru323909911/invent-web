package hac.repo.repositories;

import hac.repo.entities.Contact;
import hac.repo.entities.Design;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("SELECT c FROM Contact c WHERE c.owner = :owner")
    List<Contact> findByOwner(@Param("owner") String owner);
}
