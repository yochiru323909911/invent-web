package hac.repo.repositories;

import hac.repo.entities.Design;
import hac.util.DesignCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DesignRepository extends JpaRepository<Design, Long> {
    @Query("SELECT new hac.util.DesignCount(d.owner, COUNT(d)) " +
            "FROM Design d GROUP BY d.owner " +
            "ORDER BY COUNT(d) DESC")
    List<DesignCount> findTop3UsersWithMostDesigns();

    //find the top 3 favorite designs, by giving the images that are most used in designs
    @Query("SELECT new hac.util.DesignCount(d.imgDesign.path, COUNT(d)) " +
            "FROM Design d GROUP BY d.imgDesign.path " +
            "ORDER BY COUNT(d) DESC")
    List<DesignCount> findTop3FavoriteDesigns();

    @Query("SELECT d FROM Design d WHERE d.owner = :owner")
    List<Design> findByUser(@Param("owner") String owner);

    @Query("SELECT d FROM Design d WHERE d.imgDesign.category = :imgDesign")
    List<Design> findByBackground(@Param("imgDesign") String searchTerm);

    @Query("SELECT d FROM Design d WHERE d.creationDate = :creationDate")
    List<Design> findByDate(@Param("creationDate") LocalDate date);
}
