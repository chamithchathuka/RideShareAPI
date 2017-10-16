package lk.mobility.rideshare.repository;

import lk.mobility.rideshare.domain.RideDetails;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RideDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RideDetailsRepository extends JpaRepository<RideDetails,Long> {
    
}
