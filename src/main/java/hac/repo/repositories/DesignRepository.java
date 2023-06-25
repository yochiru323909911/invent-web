package hac.repo.repositories;

import hac.repo.entities.Design;
import hac.repo.entities.Image;
import hac.util.UserDesignCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DesignRepository extends JpaRepository<Design, Long> {
    @Query("SELECT new hac.util.UserDesignCount(d.owner, COUNT(d)) " +
            "FROM Design d GROUP BY d.owner " +
            "ORDER BY COUNT(d) DESC")
    List<UserDesignCount> findTop3UsersWithMostDesigns();

    @Query("SELECT d FROM Design d WHERE d.owner = :owner")
    List<Design> findByUser(@Param("owner") String owner);

    @Query("SELECT d FROM Design d WHERE d.imgDesign = :imgDesign")
    List<Design> findByBackground(@Param("imgDesign") String searchTerm);



}
