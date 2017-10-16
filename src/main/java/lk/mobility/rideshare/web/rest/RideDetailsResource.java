package lk.mobility.rideshare.web.rest;

import com.codahale.metrics.annotation.Timed;
import lk.mobility.rideshare.domain.RideDetails;

import lk.mobility.rideshare.repository.RideDetailsRepository;
import lk.mobility.rideshare.repository.search.RideDetailsSearchRepository;
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
 * REST controller for managing RideDetails.
 */
@RestController
@RequestMapping("/api")
public class RideDetailsResource {

    private final Logger log = LoggerFactory.getLogger(RideDetailsResource.class);

    private static final String ENTITY_NAME = "rideDetails";

    private final RideDetailsRepository rideDetailsRepository;

    private final RideDetailsSearchRepository rideDetailsSearchRepository;

    public RideDetailsResource(RideDetailsRepository rideDetailsRepository, RideDetailsSearchRepository rideDetailsSearchRepository) {
        this.rideDetailsRepository = rideDetailsRepository;
        this.rideDetailsSearchRepository = rideDetailsSearchRepository;
    }

    /**
     * POST  /ride-details : Create a new rideDetails.
     *
     * @param rideDetails the rideDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rideDetails, or with status 400 (Bad Request) if the rideDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ride-details")
    @Timed
    public ResponseEntity<RideDetails> createRideDetails(@RequestBody RideDetails rideDetails) throws URISyntaxException {
        log.debug("REST request to save RideDetails : {}", rideDetails);
        if (rideDetails.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new rideDetails cannot already have an ID")).body(null);
        }
        RideDetails result = rideDetailsRepository.save(rideDetails);
        rideDetailsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/ride-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ride-details : Updates an existing rideDetails.
     *
     * @param rideDetails the rideDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rideDetails,
     * or with status 400 (Bad Request) if the rideDetails is not valid,
     * or with status 500 (Internal Server Error) if the rideDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ride-details")
    @Timed
    public ResponseEntity<RideDetails> updateRideDetails(@RequestBody RideDetails rideDetails) throws URISyntaxException {
        log.debug("REST request to update RideDetails : {}", rideDetails);
        if (rideDetails.getId() == null) {
            return createRideDetails(rideDetails);
        }
        RideDetails result = rideDetailsRepository.save(rideDetails);
        rideDetailsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rideDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ride-details : get all the rideDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rideDetails in body
     */
    @GetMapping("/ride-details")
    @Timed
    public List<RideDetails> getAllRideDetails() {
        log.debug("REST request to get all RideDetails");
        return rideDetailsRepository.findAll();
    }

    /**
     * GET  /ride-details/:id : get the "id" rideDetails.
     *
     * @param id the id of the rideDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rideDetails, or with status 404 (Not Found)
     */
    @GetMapping("/ride-details/{id}")
    @Timed
    public ResponseEntity<RideDetails> getRideDetails(@PathVariable Long id) {
        log.debug("REST request to get RideDetails : {}", id);
        RideDetails rideDetails = rideDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rideDetails));
    }

    /**
     * DELETE  /ride-details/:id : delete the "id" rideDetails.
     *
     * @param id the id of the rideDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ride-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteRideDetails(@PathVariable Long id) {
        log.debug("REST request to delete RideDetails : {}", id);
        rideDetailsRepository.delete(id);
        rideDetailsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ride-details?query=:query : search for the rideDetails corresponding
     * to the query.
     *
     * @param query the query of the rideDetails search
     * @return the result of the search
     */
    @GetMapping("/_search/ride-details")
    @Timed
    public List<RideDetails> searchRideDetails(@RequestParam String query) {
        log.debug("REST request to search RideDetails for query {}", query);
        return StreamSupport
            .stream(rideDetailsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
