package lk.mobility.rideshare.repository;

import lk.mobility.rideshare.domain.Ride;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Ride entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RideRepository extends JpaRepository<Ride,Long> {
    
}
