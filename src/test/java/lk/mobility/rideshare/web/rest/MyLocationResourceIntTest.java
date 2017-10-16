package lk.mobility.rideshare.web.rest;

import lk.mobility.rideshare.RideShareApp;

import lk.mobility.rideshare.domain.MyLocation;
import lk.mobility.rideshare.repository.MyLocationRepository;
import lk.mobility.rideshare.repository.search.MyLocationSearchRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MyLocationResource REST controller.
 *
 * @see MyLocationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RideShareApp.class)
public class MyLocationResourceIntTest {

    private static final String DEFAULT_REAL_LOCATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REAL_LOCATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MY_LOCATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MY_LOCATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LATI = "AAAAAAAAAA";
    private static final String UPDATED_LATI = "BBBBBBBBBB";

    private static final String DEFAULT_LONGI = "AAAAAAAAAA";
    private static final String UPDATED_LONGI = "BBBBBBBBBB";

    @Autowired
    private MyLocationRepository myLocationRepository;

    @Autowired
    private MyLocationSearchRepository myLocationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMyLocationMockMvc;

    private MyLocation myLocation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MyLocationResource myLocationResource = new MyLocationResource(myLocationRepository, myLocationSearchRepository);
        this.restMyLocationMockMvc = MockMvcBuilders.standaloneSetup(myLocationResource)
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
    public static MyLocation createEntity(EntityManager em) {
        MyLocation myLocation = new MyLocation()
            .realLocationName(DEFAULT_REAL_LOCATION_NAME)
            .myLocationName(DEFAULT_MY_LOCATION_NAME)
            .lati(DEFAULT_LATI)
            .longi(DEFAULT_LONGI);
        return myLocation;
    }

    @Before
    public void initTest() {
        myLocationSearchRepository.deleteAll();
        myLocation = createEntity(em);
    }

    @Test
    @Transactional
    public void createMyLocation() throws Exception {
        int databaseSizeBeforeCreate = myLocationRepository.findAll().size();

        // Create the MyLocation
        restMyLocationMockMvc.perform(post("/api/my-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myLocation)))
            .andExpect(status().isCreated());

        // Validate the MyLocation in the database
        List<MyLocation> myLocationList = myLocationRepository.findAll();
        assertThat(myLocationList).hasSize(databaseSizeBeforeCreate + 1);
        MyLocation testMyLocation = myLocationList.get(myLocationList.size() - 1);
        assertThat(testMyLocation.getRealLocationName()).isEqualTo(DEFAULT_REAL_LOCATION_NAME);
        assertThat(testMyLocation.getMyLocationName()).isEqualTo(DEFAULT_MY_LOCATION_NAME);
        assertThat(testMyLocation.getLati()).isEqualTo(DEFAULT_LATI);
        assertThat(testMyLocation.getLongi()).isEqualTo(DEFAULT_LONGI);

        // Validate the MyLocation in Elasticsearch
        MyLocation myLocationEs = myLocationSearchRepository.findOne(testMyLocation.getId());
        assertThat(myLocationEs).isEqualToComparingFieldByField(testMyLocation);
    }

    @Test
    @Transactional
    public void createMyLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = myLocationRepository.findAll().size();

        // Create the MyLocation with an existing ID
        myLocation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyLocationMockMvc.perform(post("/api/my-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myLocation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MyLocation> myLocationList = myLocationRepository.findAll();
        assertThat(myLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMyLocations() throws Exception {
        // Initialize the database
        myLocationRepository.saveAndFlush(myLocation);

        // Get all the myLocationList
        restMyLocationMockMvc.perform(get("/api/my-locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].realLocationName").value(hasItem(DEFAULT_REAL_LOCATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].myLocationName").value(hasItem(DEFAULT_MY_LOCATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].lati").value(hasItem(DEFAULT_LATI.toString())))
            .andExpect(jsonPath("$.[*].longi").value(hasItem(DEFAULT_LONGI.toString())));
    }

    @Test
    @Transactional
    public void getMyLocation() throws Exception {
        // Initialize the database
        myLocationRepository.saveAndFlush(myLocation);

        // Get the myLocation
        restMyLocationMockMvc.perform(get("/api/my-locations/{id}", myLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(myLocation.getId().intValue()))
            .andExpect(jsonPath("$.realLocationName").value(DEFAULT_REAL_LOCATION_NAME.toString()))
            .andExpect(jsonPath("$.myLocationName").value(DEFAULT_MY_LOCATION_NAME.toString()))
            .andExpect(jsonPath("$.lati").value(DEFAULT_LATI.toString()))
            .andExpect(jsonPath("$.longi").value(DEFAULT_LONGI.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMyLocation() throws Exception {
        // Get the myLocation
        restMyLocationMockMvc.perform(get("/api/my-locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMyLocation() throws Exception {
        // Initialize the database
        myLocationRepository.saveAndFlush(myLocation);
        myLocationSearchRepository.save(myLocation);
        int databaseSizeBeforeUpdate = myLocationRepository.findAll().size();

        // Update the myLocation
        MyLocation updatedMyLocation = myLocationRepository.findOne(myLocation.getId());
        updatedMyLocation
            .realLocationName(UPDATED_REAL_LOCATION_NAME)
            .myLocationName(UPDATED_MY_LOCATION_NAME)
            .lati(UPDATED_LATI)
            .longi(UPDATED_LONGI);

        restMyLocationMockMvc.perform(put("/api/my-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMyLocation)))
            .andExpect(status().isOk());

        // Validate the MyLocation in the database
        List<MyLocation> myLocationList = myLocationRepository.findAll();
        assertThat(myLocationList).hasSize(databaseSizeBeforeUpdate);
        MyLocation testMyLocation = myLocationList.get(myLocationList.size() - 1);
        assertThat(testMyLocation.getRealLocationName()).isEqualTo(UPDATED_REAL_LOCATION_NAME);
        assertThat(testMyLocation.getMyLocationName()).isEqualTo(UPDATED_MY_LOCATION_NAME);
        assertThat(testMyLocation.getLati()).isEqualTo(UPDATED_LATI);
        assertThat(testMyLocation.getLongi()).isEqualTo(UPDATED_LONGI);

        // Validate the MyLocation in Elasticsearch
        MyLocation myLocationEs = myLocationSearchRepository.findOne(testMyLocation.getId());
        assertThat(myLocationEs).isEqualToComparingFieldByField(testMyLocation);
    }

    @Test
    @Transactional
    public void updateNonExistingMyLocation() throws Exception {
        int databaseSizeBeforeUpdate = myLocationRepository.findAll().size();

        // Create the MyLocation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMyLocationMockMvc.perform(put("/api/my-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myLocation)))
            .andExpect(status().isCreated());

        // Validate the MyLocation in the database
        List<MyLocation> myLocationList = myLocationRepository.findAll();
        assertThat(myLocationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMyLocation() throws Exception {
        // Initialize the database
        myLocationRepository.saveAndFlush(myLocation);
        myLocationSearchRepository.save(myLocation);
        int databaseSizeBeforeDelete = myLocationRepository.findAll().size();

        // Get the myLocation
        restMyLocationMockMvc.perform(delete("/api/my-locations/{id}", myLocation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean myLocationExistsInEs = myLocationSearchRepository.exists(myLocation.getId());
        assertThat(myLocationExistsInEs).isFalse();

        // Validate the database is empty
        List<MyLocation> myLocationList = myLocationRepository.findAll();
        assertThat(myLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMyLocation() throws Exception {
        // Initialize the database
        myLocationRepository.saveAndFlush(myLocation);
        myLocationSearchRepository.save(myLocation);

        // Search the myLocation
        restMyLocationMockMvc.perform(get("/api/_search/my-locations?query=id:" + myLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].realLocationName").value(hasItem(DEFAULT_REAL_LOCATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].myLocationName").value(hasItem(DEFAULT_MY_LOCATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].lati").value(hasItem(DEFAULT_LATI.toString())))
            .andExpect(jsonPath("$.[*].longi").value(hasItem(DEFAULT_LONGI.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyLocation.class);
        MyLocation myLocation1 = new MyLocation();
        myLocation1.setId(1L);
        MyLocation myLocation2 = new MyLocation();
        myLocation2.setId(myLocation1.getId());
        assertThat(myLocation1).isEqualTo(myLocation2);
        myLocation2.setId(2L);
        assertThat(myLocation1).isNotEqualTo(myLocation2);
        myLocation1.setId(null);
        assertThat(myLocation1).isNotEqualTo(myLocation2);
    }
}
