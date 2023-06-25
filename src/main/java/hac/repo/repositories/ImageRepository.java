package hac.repo.repositories;

import hac.repo.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT i FROM Image i WHERE i.category = :category")
    List<Image> findByCategory(@Param("category") String category);

    @Query("SELECT i FROM Image i WHERE i.path = :path")
    List<Image> findByImagePath(@Param("path") String path);
}
