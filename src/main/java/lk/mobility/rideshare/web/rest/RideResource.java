package lk.mobility.rideshare.web.rest;

import com.codahale.metrics.annotation.Timed;
import lk.mobility.rideshare.domain.Ride;

import lk.mobility.rideshare.repository.RideRepository;
import lk.mobility.rideshare.repository.search.RideSearchRepository;
import lk.mobility.rideshare.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Ride.
 */
@RestController
@RequestMapping("/api")
public class RideResource {

    private final Logger log = LoggerFactory.getLogger(RideResource.class);

    private static final String ENTITY_NAME = "ride";

    private final RideRepository rideRepository;

    private final RideSearchRepository rideSearchRepository;

    public RideResource(RideRepository rideRepository, RideSearchRepository rideSearchRepository) {
        this.rideRepository = rideRepository;
        this.rideSearchRepository = rideSearchRepository;
    }

    /**
     * POST  /rides : Create a new ride.
     *
     * @param ride the ride to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ride, or with status 400 (Bad Request) if the ride has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rides")
    @Timed
    public ResponseEntity<Ride> createRide(@RequestBody Ride ride) throws URISyntaxException {
        log.debug("REST request to save Ride : {}", ride);
        if (ride.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ride cannot already have an ID")).body(null);
        }
        Ride result = rideRepository.save(ride);
        rideSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/rides/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rides : Updates an existing ride.
     *
     * @param ride the ride to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ride,
     * or with status 400 (Bad Request) if the ride is not valid,
     * or with status 500 (Internal Server Error) if the ride couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rides")
    @Timed
    public ResponseEntity<Ride> updateRide(@RequestBody Ride ride) throws URISyntaxException {
        log.debug("REST request to update Ride : {}", ride);
        if (ride.getId() == null) {
            return createRide(ride);
        }
        Ride result = rideRepository.save(ride);
        rideSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ride.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rides : get all the rides.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rides in body
     */
    @GetMapping("/rides")
    @Timed
    public List<Ride> getAllRides() {
        log.debug("REST request to get all Rides");
        return rideRepository.findAll();
    }

    /**
     * GET  /rides/:id : get the "id" ride.
     *
     * @param id the id of the ride to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ride, or with status 404 (Not Found)
     */
    @GetMapping("/rides/{id}")
    @Timed
    public ResponseEntity<Ride> getRide(@PathVariable Long id) {
        log.debug("REST request to get Ride : {}", id);
        Ride ride = rideRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ride));
    }

    /**
     * DELETE  /rides/:id : delete the "id" ride.
     *
     * @param id the id of the ride to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rides/{id}")
    @Timed
    public ResponseEntity<Void> deleteRide(@PathVariable Long id) {
        log.debug("REST request to delete Ride : {}", id);
        rideRepository.delete(id);
        rideSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/rides?query=:query : search for the ride corresponding
     * to the query.
     *
     * @param query the query of the ride search
     * @return the result of the search
     */
    @GetMapping("/_search/rides")
    @Timed
    public List<Ride> searchRides(@RequestParam String query) {
        log.debug("REST request to search Rides for query {}", query);
        return StreamSupport
            .stream(rideSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
