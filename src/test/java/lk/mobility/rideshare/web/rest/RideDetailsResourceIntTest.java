package lk.mobility.rideshare.web.rest;

import lk.mobility.rideshare.RideShareApp;

import lk.mobility.rideshare.domain.RideDetails;
import lk.mobility.rideshare.repository.RideDetailsRepository;
import lk.mobility.rideshare.repository.search.RideDetailsSearchRepository;
import lk.mobility.rideshare.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static lk.mobility.rideshare.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lk.mobility.rideshare.domain.enumeration.Status;
/**
 * Test class for the RideDetailsResource REST controller.
 *
 * @see RideDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RideShareApp.class)
public class RideDetailsResourceIntTest {

    private static final ZonedDateTime DEFAULT_PICKEDUP_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PICKEDUP_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DROPPED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DROPPED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.AVAILABLE;
    private static final Status UPDATED_STATUS = Status.RESERVED;

    @Autowired
    private RideDetailsRepository rideDetailsRepository;

    @Autowired
    private RideDetailsSearchRepository rideDetailsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRideDetailsMockMvc;

    private RideDetails rideDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RideDetailsResource rideDetailsResource = new RideDetailsResource(rideDetailsRepository, rideDetailsSearchRepository);
        this.restRideDetailsMockMvc = MockMvcBuilders.standaloneSetup(rideDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RideDetails createEntity(EntityManager em) {
        RideDetails rideDetails = new RideDetails()
            .pickedupOn(DEFAULT_PICKEDUP_ON)
            .droppedOn(DEFAULT_DROPPED_ON)
            .comment(DEFAULT_COMMENT)
            .status(DEFAULT_STATUS);
        return rideDetails;
    }

    @Before
    public void initTest() {
        rideDetailsSearchRepository.deleteAll();
        rideDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createRideDetails() throws Exception {
        int databaseSizeBeforeCreate = rideDetailsRepository.findAll().size();

        // Create the RideDetails
        restRideDetailsMockMvc.perform(post("/api/ride-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDetails)))
            .andExpect(status().isCreated());

        // Validate the RideDetails in the database
        List<RideDetails> rideDetailsList = rideDetailsRepository.findAll();
        assertThat(rideDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        RideDetails testRideDetails = rideDetailsList.get(rideDetailsList.size() - 1);
        assertThat(testRideDetails.getPickedupOn()).isEqualTo(DEFAULT_PICKEDUP_ON);
        assertThat(testRideDetails.getDroppedOn()).isEqualTo(DEFAULT_DROPPED_ON);
        assertThat(testRideDetails.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testRideDetails.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the RideDetails in Elasticsearch
        RideDetails rideDetailsEs = rideDetailsSearchRepository.findOne(testRideDetails.getId());
        assertThat(rideDetailsEs).isEqualToComparingFieldByField(testRideDetails);
    }

    @Test
    @Transactional
    public void createRideDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rideDetailsRepository.findAll().size();

        // Create the RideDetails with an existing ID
        rideDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRideDetailsMockMvc.perform(post("/api/ride-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDetails)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RideDetails> rideDetailsList = rideDetailsRepository.findAll();
        assertThat(rideDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRideDetails() throws Exception {
        // Initialize the database
        rideDetailsRepository.saveAndFlush(rideDetails);

        // Get all the rideDetailsList
        restRideDetailsMockMvc.perform(get("/api/ride-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rideDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].pickedupOn").value(hasItem(sameInstant(DEFAULT_PICKEDUP_ON))))
            .andExpect(jsonPath("$.[*].droppedOn").value(hasItem(sameInstant(DEFAULT_DROPPED_ON))))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getRideDetails() throws Exception {
        // Initialize the database
        rideDetailsRepository.saveAndFlush(rideDetails);

        // Get the rideDetails
        restRideDetailsMockMvc.perform(get("/api/ride-details/{id}", rideDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rideDetails.getId().intValue()))
            .andExpect(jsonPath("$.pickedupOn").value(sameInstant(DEFAULT_PICKEDUP_ON)))
            .andExpect(jsonPath("$.droppedOn").value(sameInstant(DEFAULT_DROPPED_ON)))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRideDetails() throws Exception {
        // Get the rideDetails
        restRideDetailsMockMvc.perform(get("/api/ride-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRideDetails() throws Exception {
        // Initialize the database
        rideDetailsRepository.saveAndFlush(rideDetails);
        rideDetailsSearchRepository.save(rideDetails);
        int databaseSizeBeforeUpdate = rideDetailsRepository.findAll().size();

        // Update the rideDetails
        RideDetails updatedRideDetails = rideDetailsRepository.findOne(rideDetails.getId());
        updatedRideDetails
            .pickedupOn(UPDATED_PICKEDUP_ON)
            .droppedOn(UPDATED_DROPPED_ON)
            .comment(UPDATED_COMMENT)
            .status(UPDATED_STATUS);

        restRideDetailsMockMvc.perform(put("/api/ride-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRideDetails)))
            .andExpect(status().isOk());

        // Validate the RideDetails in the database
        List<RideDetails> rideDetailsList = rideDetailsRepository.findAll();
        assertThat(rideDetailsList).hasSize(databaseSizeBeforeUpdate);
        RideDetails testRideDetails = rideDetailsList.get(rideDetailsList.size() - 1);
        assertThat(testRideDetails.getPickedupOn()).isEqualTo(UPDATED_PICKEDUP_ON);
        assertThat(testRideDetails.getDroppedOn()).isEqualTo(UPDATED_DROPPED_ON);
        assertThat(testRideDetails.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testRideDetails.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the RideDetails in Elasticsearch
        RideDetails rideDetailsEs = rideDetailsSearchRepository.findOne(testRideDetails.getId());
        assertThat(rideDetailsEs).isEqualToComparingFieldByField(testRideDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingRideDetails() throws Exception {
        int databaseSizeBeforeUpdate = rideDetailsRepository.findAll().size();

        // Create the RideDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRideDetailsMockMvc.perform(put("/api/ride-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDetails)))
            .andExpect(status().isCreated());

        // Validate the RideDetails in the database
        List<RideDetails> rideDetailsList = rideDetailsRepository.findAll();
        assertThat(rideDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRideDetails() throws Exception {
        // Initialize the database
        rideDetailsRepository.saveAndFlush(rideDetails);
        rideDetailsSearchRepository.save(rideDetails);
        int databaseSizeBeforeDelete = rideDetailsRepository.findAll().size();

        // Get the rideDetails
        restRideDetailsMockMvc.perform(delete("/api/ride-details/{id}", rideDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean rideDetailsExistsInEs = rideDetailsSearchRepository.exists(rideDetails.getId());
        assertThat(rideDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<RideDetails> rideDetailsList = rideDetailsRepository.findAll();
        assertThat(rideDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRideDetails() throws Exception {
        // Initialize the database
        rideDetailsRepository.saveAndFlush(rideDetails);
        rideDetailsSearchRepository.save(rideDetails);

        // Search the rideDetails
        restRideDetailsMockMvc.perform(get("/api/_search/ride-details?query=id:" + rideDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rideDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].pickedupOn").value(hasItem(sameInstant(DEFAULT_PICKEDUP_ON))))
            .andExpect(jsonPath("$.[*].droppedOn").value(hasItem(sameInstant(DEFAULT_DROPPED_ON))))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RideDetails.class);
        RideDetails rideDetails1 = new RideDetails();
        rideDetails1.setId(1L);
        RideDetails rideDetails2 = new RideDetails();
        rideDetails2.setId(rideDetails1.getId());
        assertThat(rideDetails1).isEqualTo(rideDetails2);
        rideDetails2.setId(2L);
        assertThat(rideDetails1).isNotEqualTo(rideDetails2);
        rideDetails1.setId(null);
        assertThat(rideDetails1).isNotEqualTo(rideDetails2);
    }
}
