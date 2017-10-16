package lk.mobility.rideshare.web.rest;

import lk.mobility.rideshare.RideShareApp;

import lk.mobility.rideshare.domain.Ride;
import lk.mobility.rideshare.repository.RideRepository;
import lk.mobility.rideshare.repository.search.RideSearchRepository;
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

import lk.mobility.rideshare.domain.enumeration.RideType;
import lk.mobility.rideshare.domain.enumeration.Privacy;
/**
 * Test class for the RideResource REST controller.
 *
 * @see RideResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RideShareApp.class)
public class RideResourceIntTest {

    private static final String DEFAULT_START_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_START_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_END_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_END_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_START_LOCAION_LAT = "AAAAAAAAAA";
    private static final String UPDATED_START_LOCAION_LAT = "BBBBBBBBBB";

    private static final String DEFAULT_START_LOCATION_LONG = "AAAAAAAAAA";
    private static final String UPDATED_START_LOCATION_LONG = "BBBBBBBBBB";

    private static final String DEFAULT_END_LOCAION_LAT = "AAAAAAAAAA";
    private static final String UPDATED_END_LOCAION_LAT = "BBBBBBBBBB";

    private static final String DEFAULT_END_LOCATION_LONG = "AAAAAAAAAA";
    private static final String UPDATED_END_LOCATION_LONG = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATED_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_SEAT_CAPASITY = 1;
    private static final Integer UPDATED_SEAT_CAPASITY = 2;

    private static final RideType DEFAULT_RIDE_TYPE = RideType.REQUEST;
    private static final RideType UPDATED_RIDE_TYPE = RideType.SHARE;

    private static final Privacy DEFAULT_PRIVACY = Privacy.FRIENDONLY;
    private static final Privacy UPDATED_PRIVACY = Privacy.PUBLIC;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RideSearchRepository rideSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRideMockMvc;

    private Ride ride;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RideResource rideResource = new RideResource(rideRepository, rideSearchRepository);
        this.restRideMockMvc = MockMvcBuilders.standaloneSetup(rideResource)
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
    public static Ride createEntity(EntityManager em) {
        Ride ride = new Ride()
            .startLocation(DEFAULT_START_LOCATION)
            .endLocation(DEFAULT_END_LOCATION)
            .startLocaionLat(DEFAULT_START_LOCAION_LAT)
            .startLocationLong(DEFAULT_START_LOCATION_LONG)
            .endLocaionLat(DEFAULT_END_LOCAION_LAT)
            .endLocationLong(DEFAULT_END_LOCATION_LONG)
            .startDateTime(DEFAULT_START_DATE_TIME)
            .createdDateTime(DEFAULT_CREATED_DATE_TIME)
            .seatCapasity(DEFAULT_SEAT_CAPASITY)
            .rideType(DEFAULT_RIDE_TYPE)
            .privacy(DEFAULT_PRIVACY);
        return ride;
    }

    @Before
    public void initTest() {
        rideSearchRepository.deleteAll();
        ride = createEntity(em);
    }

    @Test
    @Transactional
    public void createRide() throws Exception {
        int databaseSizeBeforeCreate = rideRepository.findAll().size();

        // Create the Ride
        restRideMockMvc.perform(post("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ride)))
            .andExpect(status().isCreated());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeCreate + 1);
        Ride testRide = rideList.get(rideList.size() - 1);
        assertThat(testRide.getStartLocation()).isEqualTo(DEFAULT_START_LOCATION);
        assertThat(testRide.getEndLocation()).isEqualTo(DEFAULT_END_LOCATION);
        assertThat(testRide.getStartLocaionLat()).isEqualTo(DEFAULT_START_LOCAION_LAT);
        assertThat(testRide.getStartLocationLong()).isEqualTo(DEFAULT_START_LOCATION_LONG);
        assertThat(testRide.getEndLocaionLat()).isEqualTo(DEFAULT_END_LOCAION_LAT);
        assertThat(testRide.getEndLocationLong()).isEqualTo(DEFAULT_END_LOCATION_LONG);
        assertThat(testRide.getStartDateTime()).isEqualTo(DEFAULT_START_DATE_TIME);
        assertThat(testRide.getCreatedDateTime()).isEqualTo(DEFAULT_CREATED_DATE_TIME);
        assertThat(testRide.getSeatCapasity()).isEqualTo(DEFAULT_SEAT_CAPASITY);
        assertThat(testRide.getRideType()).isEqualTo(DEFAULT_RIDE_TYPE);
        assertThat(testRide.getPrivacy()).isEqualTo(DEFAULT_PRIVACY);

        // Validate the Ride in Elasticsearch
        Ride rideEs = rideSearchRepository.findOne(testRide.getId());
        assertThat(rideEs).isEqualToComparingFieldByField(testRide);
    }

    @Test
    @Transactional
    public void createRideWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rideRepository.findAll().size();

        // Create the Ride with an existing ID
        ride.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRideMockMvc.perform(post("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ride)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRides() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList
        restRideMockMvc.perform(get("/api/rides?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ride.getId().intValue())))
            .andExpect(jsonPath("$.[*].startLocation").value(hasItem(DEFAULT_START_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].endLocation").value(hasItem(DEFAULT_END_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].startLocaionLat").value(hasItem(DEFAULT_START_LOCAION_LAT.toString())))
            .andExpect(jsonPath("$.[*].startLocationLong").value(hasItem(DEFAULT_START_LOCATION_LONG.toString())))
            .andExpect(jsonPath("$.[*].endLocaionLat").value(hasItem(DEFAULT_END_LOCAION_LAT.toString())))
            .andExpect(jsonPath("$.[*].endLocationLong").value(hasItem(DEFAULT_END_LOCATION_LONG.toString())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(sameInstant(DEFAULT_START_DATE_TIME))))
            .andExpect(jsonPath("$.[*].createdDateTime").value(hasItem(sameInstant(DEFAULT_CREATED_DATE_TIME))))
            .andExpect(jsonPath("$.[*].seatCapasity").value(hasItem(DEFAULT_SEAT_CAPASITY)))
            .andExpect(jsonPath("$.[*].rideType").value(hasItem(DEFAULT_RIDE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].privacy").value(hasItem(DEFAULT_PRIVACY.toString())));
    }

    @Test
    @Transactional
    public void getRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get the ride
        restRideMockMvc.perform(get("/api/rides/{id}", ride.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ride.getId().intValue()))
            .andExpect(jsonPath("$.startLocation").value(DEFAULT_START_LOCATION.toString()))
            .andExpect(jsonPath("$.endLocation").value(DEFAULT_END_LOCATION.toString()))
            .andExpect(jsonPath("$.startLocaionLat").value(DEFAULT_START_LOCAION_LAT.toString()))
            .andExpect(jsonPath("$.startLocationLong").value(DEFAULT_START_LOCATION_LONG.toString()))
            .andExpect(jsonPath("$.endLocaionLat").value(DEFAULT_END_LOCAION_LAT.toString()))
            .andExpect(jsonPath("$.endLocationLong").value(DEFAULT_END_LOCATION_LONG.toString()))
            .andExpect(jsonPath("$.startDateTime").value(sameInstant(DEFAULT_START_DATE_TIME)))
            .andExpect(jsonPath("$.createdDateTime").value(sameInstant(DEFAULT_CREATED_DATE_TIME)))
            .andExpect(jsonPath("$.seatCapasity").value(DEFAULT_SEAT_CAPASITY))
            .andExpect(jsonPath("$.rideType").value(DEFAULT_RIDE_TYPE.toString()))
            .andExpect(jsonPath("$.privacy").value(DEFAULT_PRIVACY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRide() throws Exception {
        // Get the ride
        restRideMockMvc.perform(get("/api/rides/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);
        rideSearchRepository.save(ride);
        int databaseSizeBeforeUpdate = rideRepository.findAll().size();

        // Update the ride
        Ride updatedRide = rideRepository.findOne(ride.getId());
        updatedRide
            .startLocation(UPDATED_START_LOCATION)
            .endLocation(UPDATED_END_LOCATION)
            .startLocaionLat(UPDATED_START_LOCAION_LAT)
            .startLocationLong(UPDATED_START_LOCATION_LONG)
            .endLocaionLat(UPDATED_END_LOCAION_LAT)
            .endLocationLong(UPDATED_END_LOCATION_LONG)
            .startDateTime(UPDATED_START_DATE_TIME)
            .createdDateTime(UPDATED_CREATED_DATE_TIME)
            .seatCapasity(UPDATED_SEAT_CAPASITY)
            .rideType(UPDATED_RIDE_TYPE)
            .privacy(UPDATED_PRIVACY);

        restRideMockMvc.perform(put("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRide)))
            .andExpect(status().isOk());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);
        Ride testRide = rideList.get(rideList.size() - 1);
        assertThat(testRide.getStartLocation()).isEqualTo(UPDATED_START_LOCATION);
        assertThat(testRide.getEndLocation()).isEqualTo(UPDATED_END_LOCATION);
        assertThat(testRide.getStartLocaionLat()).isEqualTo(UPDATED_START_LOCAION_LAT);
        assertThat(testRide.getStartLocationLong()).isEqualTo(UPDATED_START_LOCATION_LONG);
        assertThat(testRide.getEndLocaionLat()).isEqualTo(UPDATED_END_LOCAION_LAT);
        assertThat(testRide.getEndLocationLong()).isEqualTo(UPDATED_END_LOCATION_LONG);
        assertThat(testRide.getStartDateTime()).isEqualTo(UPDATED_START_DATE_TIME);
        assertThat(testRide.getCreatedDateTime()).isEqualTo(UPDATED_CREATED_DATE_TIME);
        assertThat(testRide.getSeatCapasity()).isEqualTo(UPDATED_SEAT_CAPASITY);
        assertThat(testRide.getRideType()).isEqualTo(UPDATED_RIDE_TYPE);
        assertThat(testRide.getPrivacy()).isEqualTo(UPDATED_PRIVACY);

        // Validate the Ride in Elasticsearch
        Ride rideEs = rideSearchRepository.findOne(testRide.getId());
        assertThat(rideEs).isEqualToComparingFieldByField(testRide);
    }

    @Test
    @Transactional
    public void updateNonExistingRide() throws Exception {
        int databaseSizeBeforeUpdate = rideRepository.findAll().size();

        // Create the Ride

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRideMockMvc.perform(put("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ride)))
            .andExpect(status().isCreated());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);
        rideSearchRepository.save(ride);
        int databaseSizeBeforeDelete = rideRepository.findAll().size();

        // Get the ride
        restRideMockMvc.perform(delete("/api/rides/{id}", ride.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean rideExistsInEs = rideSearchRepository.exists(ride.getId());
        assertThat(rideExistsInEs).isFalse();

        // Validate the database is empty
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);
        rideSearchRepository.save(ride);

        // Search the ride
        restRideMockMvc.perform(get("/api/_search/rides?query=id:" + ride.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ride.getId().intValue())))
            .andExpect(jsonPath("$.[*].startLocation").value(hasItem(DEFAULT_START_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].endLocation").value(hasItem(DEFAULT_END_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].startLocaionLat").value(hasItem(DEFAULT_START_LOCAION_LAT.toString())))
            .andExpect(jsonPath("$.[*].startLocationLong").value(hasItem(DEFAULT_START_LOCATION_LONG.toString())))
            .andExpect(jsonPath("$.[*].endLocaionLat").value(hasItem(DEFAULT_END_LOCAION_LAT.toString())))
            .andExpect(jsonPath("$.[*].endLocationLong").value(hasItem(DEFAULT_END_LOCATION_LONG.toString())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(sameInstant(DEFAULT_START_DATE_TIME))))
            .andExpect(jsonPath("$.[*].createdDateTime").value(hasItem(sameInstant(DEFAULT_CREATED_DATE_TIME))))
            .andExpect(jsonPath("$.[*].seatCapasity").value(hasItem(DEFAULT_SEAT_CAPASITY)))
            .andExpect(jsonPath("$.[*].rideType").value(hasItem(DEFAULT_RIDE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].privacy").value(hasItem(DEFAULT_PRIVACY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ride.class);
        Ride ride1 = new Ride();
        ride1.setId(1L);
        Ride ride2 = new Ride();
        ride2.setId(ride1.getId());
        assertThat(ride1).isEqualTo(ride2);
        ride2.setId(2L);
        assertThat(ride1).isNotEqualTo(ride2);
        ride1.setId(null);
        assertThat(ride1).isNotEqualTo(ride2);
    }
}
