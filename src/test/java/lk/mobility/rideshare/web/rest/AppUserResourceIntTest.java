package lk.mobility.rideshare.web.rest;

import lk.mobility.rideshare.RideShareApp;

import lk.mobility.rideshare.domain.AppUser;
import lk.mobility.rideshare.repository.AppUserRepository;
import lk.mobility.rideshare.repository.search.AppUserSearchRepository;
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

import lk.mobility.rideshare.domain.enumeration.Gender;
/**
 * Test class for the AppUserResource REST controller.
 *
 * @see AppUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RideShareApp.class)
public class AppUserResourceIntTest {

    private static final Long DEFAULT_IUSER_ID = 1L;
    private static final Long UPDATED_IUSER_ID = 2L;

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FB_ID = "AAAAAAAAAA";
    private static final String UPDATED_FB_ID = "BBBBBBBBBB";

    private static final String DEFAULT_GOOGLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_GOOGLE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LINKED_IN_ID = "AAAAAAAAAA";
    private static final String UPDATED_LINKED_IN_ID = "BBBBBBBBBB";

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserSearchRepository appUserSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppUserMockMvc;

    private AppUser appUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AppUserResource appUserResource = new AppUserResource(appUserRepository, appUserSearchRepository);
        this.restAppUserMockMvc = MockMvcBuilders.standaloneSetup(appUserResource)
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
    public static AppUser createEntity(EntityManager em) {
        AppUser appUser = new AppUser()
            .iuserId(DEFAULT_IUSER_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .gender(DEFAULT_GENDER)
            .address(DEFAULT_ADDRESS)
            .email(DEFAULT_EMAIL)
            .fbID(DEFAULT_FB_ID)
            .googleID(DEFAULT_GOOGLE_ID)
            .linkedInID(DEFAULT_LINKED_IN_ID);
        return appUser;
    }

    @Before
    public void initTest() {
        appUserSearchRepository.deleteAll();
        appUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppUser() throws Exception {
        int databaseSizeBeforeCreate = appUserRepository.findAll().size();

        // Create the AppUser
        restAppUserMockMvc.perform(post("/api/app-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isCreated());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeCreate + 1);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getIuserId()).isEqualTo(DEFAULT_IUSER_ID);
        assertThat(testAppUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAppUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAppUser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testAppUser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testAppUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAppUser.getFbID()).isEqualTo(DEFAULT_FB_ID);
        assertThat(testAppUser.getGoogleID()).isEqualTo(DEFAULT_GOOGLE_ID);
        assertThat(testAppUser.getLinkedInID()).isEqualTo(DEFAULT_LINKED_IN_ID);

        // Validate the AppUser in Elasticsearch
        AppUser appUserEs = appUserSearchRepository.findOne(testAppUser.getId());
        assertThat(appUserEs).isEqualToComparingFieldByField(testAppUser);
    }

    @Test
    @Transactional
    public void createAppUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appUserRepository.findAll().size();

        // Create the AppUser with an existing ID
        appUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppUserMockMvc.perform(post("/api/app-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAppUsers() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList
        restAppUserMockMvc.perform(get("/api/app-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].iuserId").value(hasItem(DEFAULT_IUSER_ID.intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].fbID").value(hasItem(DEFAULT_FB_ID.toString())))
            .andExpect(jsonPath("$.[*].googleID").value(hasItem(DEFAULT_GOOGLE_ID.toString())))
            .andExpect(jsonPath("$.[*].linkedInID").value(hasItem(DEFAULT_LINKED_IN_ID.toString())));
    }

    @Test
    @Transactional
    public void getAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get the appUser
        restAppUserMockMvc.perform(get("/api/app-users/{id}", appUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appUser.getId().intValue()))
            .andExpect(jsonPath("$.iuserId").value(DEFAULT_IUSER_ID.intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.fbID").value(DEFAULT_FB_ID.toString()))
            .andExpect(jsonPath("$.googleID").value(DEFAULT_GOOGLE_ID.toString()))
            .andExpect(jsonPath("$.linkedInID").value(DEFAULT_LINKED_IN_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAppUser() throws Exception {
        // Get the appUser
        restAppUserMockMvc.perform(get("/api/app-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);
        appUserSearchRepository.save(appUser);
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser
        AppUser updatedAppUser = appUserRepository.findOne(appUser.getId());
        updatedAppUser
            .iuserId(UPDATED_IUSER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .address(UPDATED_ADDRESS)
            .email(UPDATED_EMAIL)
            .fbID(UPDATED_FB_ID)
            .googleID(UPDATED_GOOGLE_ID)
            .linkedInID(UPDATED_LINKED_IN_ID);

        restAppUserMockMvc.perform(put("/api/app-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAppUser)))
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getIuserId()).isEqualTo(UPDATED_IUSER_ID);
        assertThat(testAppUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAppUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAppUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAppUser.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAppUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAppUser.getFbID()).isEqualTo(UPDATED_FB_ID);
        assertThat(testAppUser.getGoogleID()).isEqualTo(UPDATED_GOOGLE_ID);
        assertThat(testAppUser.getLinkedInID()).isEqualTo(UPDATED_LINKED_IN_ID);

        // Validate the AppUser in Elasticsearch
        AppUser appUserEs = appUserSearchRepository.findOne(testAppUser.getId());
        assertThat(appUserEs).isEqualToComparingFieldByField(testAppUser);
    }

    @Test
    @Transactional
    public void updateNonExistingAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Create the AppUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAppUserMockMvc.perform(put("/api/app-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isCreated());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);
        appUserSearchRepository.save(appUser);
        int databaseSizeBeforeDelete = appUserRepository.findAll().size();

        // Get the appUser
        restAppUserMockMvc.perform(delete("/api/app-users/{id}", appUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean appUserExistsInEs = appUserSearchRepository.exists(appUser.getId());
        assertThat(appUserExistsInEs).isFalse();

        // Validate the database is empty
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);
        appUserSearchRepository.save(appUser);

        // Search the appUser
        restAppUserMockMvc.perform(get("/api/_search/app-users?query=id:" + appUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].iuserId").value(hasItem(DEFAULT_IUSER_ID.intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].fbID").value(hasItem(DEFAULT_FB_ID.toString())))
            .andExpect(jsonPath("$.[*].googleID").value(hasItem(DEFAULT_GOOGLE_ID.toString())))
            .andExpect(jsonPath("$.[*].linkedInID").value(hasItem(DEFAULT_LINKED_IN_ID.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUser.class);
        AppUser appUser1 = new AppUser();
        appUser1.setId(1L);
        AppUser appUser2 = new AppUser();
        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);
        appUser2.setId(2L);
        assertThat(appUser1).isNotEqualTo(appUser2);
        appUser1.setId(null);
        assertThat(appUser1).isNotEqualTo(appUser2);
    }
}
