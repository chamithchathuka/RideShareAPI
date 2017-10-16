package lk.mobility.rideshare.repository;

import lk.mobility.rideshare.domain.MyLocation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MyLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MyLocationRepository extends JpaRepository<MyLocation,Long> {
    
}
