package lk.mobility.rideshare.web.rest;

import com.codahale.metrics.annotation.Timed;
import lk.mobility.rideshare.domain.AppUser;

import lk.mobility.rideshare.repository.AppUserRepository;
import lk.mobility.rideshare.repository.search.AppUserSearchRepository;
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
 * REST controller for managing AppUser.
 */
@RestController
@RequestMapping("/api")
public class AppUserResource {

    private final Logger log = LoggerFactory.getLogger(AppUserResource.class);

    private static final String ENTITY_NAME = "appUser";

    private final AppUserRepository appUserRepository;

    private final AppUserSearchRepository appUserSearchRepository;

    public AppUserResource(AppUserRepository appUserRepository, AppUserSearchRepository appUserSearchRepository) {
        this.appUserRepository = appUserRepository;
        this.appUserSearchRepository = appUserSearchRepository;
    }

    /**
     * POST  /app-users : Create a new appUser.
     *
     * @param appUser the appUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appUser, or with status 400 (Bad Request) if the appUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/app-users")
    @Timed
    public ResponseEntity<AppUser> createAppUser(@RequestBody AppUser appUser) throws URISyntaxException {
        log.debug("REST request to save AppUser : {}", appUser);
        if (appUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new appUser cannot already have an ID")).body(null);
        }
        AppUser result = appUserRepository.save(appUser);
        appUserSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/app-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /app-users : Updates an existing appUser.
     *
     * @param appUser the appUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appUser,
     * or with status 400 (Bad Request) if the appUser is not valid,
     * or with status 500 (Internal Server Error) if the appUser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/app-users")
    @Timed
    public ResponseEntity<AppUser> updateAppUser(@RequestBody AppUser appUser) throws URISyntaxException {
        log.debug("REST request to update AppUser : {}", appUser);
        if (appUser.getId() == null) {
            return createAppUser(appUser);
        }
        AppUser result = appUserRepository.save(appUser);
        appUserSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /app-users : get all the appUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of appUsers in body
     */
    @GetMapping("/app-users")
    @Timed
    public List<AppUser> getAllAppUsers() {
        log.debug("REST request to get all AppUsers");
        return appUserRepository.findAll();
    }

    /**
     * GET  /app-users/:id : get the "id" appUser.
     *
     * @param id the id of the appUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appUser, or with status 404 (Not Found)
     */
    @GetMapping("/app-users/{id}")
    @Timed
    public ResponseEntity<AppUser> getAppUser(@PathVariable Long id) {
        log.debug("REST request to get AppUser : {}", id);
        AppUser appUser = appUserRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(appUser));
    }

    /**
     * DELETE  /app-users/:id : delete the "id" appUser.
     *
     * @param id the id of the appUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/app-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteAppUser(@PathVariable Long id) {
        log.debug("REST request to delete AppUser : {}", id);
        appUserRepository.delete(id);
        appUserSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/app-users?query=:query : search for the appUser corresponding
     * to the query.
     *
     * @param query the query of the appUser search
     * @return the result of the search
     */
    @GetMapping("/_search/app-users")
    @Timed
    public List<AppUser> searchAppUsers(@RequestParam String query) {
        log.debug("REST request to search AppUsers for query {}", query);
        return StreamSupport
            .stream(appUserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
