package lk.mobility.rideshare.web.rest;

import com.codahale.metrics.annotation.Timed;
import lk.mobility.rideshare.domain.MyLocation;

import lk.mobility.rideshare.repository.MyLocationRepository;
import lk.mobility.rideshare.repository.search.MyLocationSearchRepository;
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
 * REST controller for managing MyLocation.
 */
@RestController
@RequestMapping("/api")
public class MyLocationResource {

    private final Logger log = LoggerFactory.getLogger(MyLocationResource.class);

    private static final String ENTITY_NAME = "myLocation";

    private final MyLocationRepository myLocationRepository;

    private final MyLocationSearchRepository myLocationSearchRepository;

    public MyLocationResource(MyLocationRepository myLocationRepository, MyLocationSearchRepository myLocationSearchRepository) {
        this.myLocationRepository = myLocationRepository;
        this.myLocationSearchRepository = myLocationSearchRepository;
    }

    /**
     * POST  /my-locations : Create a new myLocation.
     *
     * @param myLocation the myLocation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new myLocation, or with status 400 (Bad Request) if the myLocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/my-locations")
    @Timed
    public ResponseEntity<MyLocation> createMyLocation(@RequestBody MyLocation myLocation) throws URISyntaxException {
        log.debug("REST request to save MyLocation : {}", myLocation);
        if (myLocation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new myLocation cannot already have an ID")).body(null);
        }
        MyLocation result = myLocationRepository.save(myLocation);
        myLocationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/my-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /my-locations : Updates an existing myLocation.
     *
     * @param myLocation the myLocation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated myLocation,
     * or with status 400 (Bad Request) if the myLocation is not valid,
     * or with status 500 (Internal Server Error) if the myLocation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/my-locations")
    @Timed
    public ResponseEntity<MyLocation> updateMyLocation(@RequestBody MyLocation myLocation) throws URISyntaxException {
        log.debug("REST request to update MyLocation : {}", myLocation);
        if (myLocation.getId() == null) {
            return createMyLocation(myLocation);
        }
        MyLocation result = myLocationRepository.save(myLocation);
        myLocationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, myLocation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /my-locations : get all the myLocations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of myLocations in body
     */
    @GetMapping("/my-locations")
    @Timed
    public List<MyLocation> getAllMyLocations() {
        log.debug("REST request to get all MyLocations");
        return myLocationRepository.findAll();
    }

    /**
     * GET  /my-locations/:id : get the "id" myLocation.
     *
     * @param id the id of the myLocation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the myLocation, or with status 404 (Not Found)
     */
    @GetMapping("/my-locations/{id}")
    @Timed
    public ResponseEntity<MyLocation> getMyLocation(@PathVariable Long id) {
        log.debug("REST request to get MyLocation : {}", id);
        MyLocation myLocation = myLocationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(myLocation));
    }

    /**
     * DELETE  /my-locations/:id : delete the "id" myLocation.
     *
     * @param id the id of the myLocation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/my-locations/{id}")
    @Timed
    public ResponseEntity<Void> deleteMyLocation(@PathVariable Long id) {
        log.debug("REST request to delete MyLocation : {}", id);
        myLocationRepository.delete(id);
        myLocationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/my-locations?query=:query : search for the myLocation corresponding
     * to the query.
     *
     * @param query the query of the myLocation search
     * @return the result of the search
     */
    @GetMapping("/_search/my-locations")
    @Timed
    public List<MyLocation> searchMyLocations(@RequestParam String query) {
        log.debug("REST request to search MyLocations for query {}", query);
        return StreamSupport
            .stream(myLocationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
