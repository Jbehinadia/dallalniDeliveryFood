package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TypePlat;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.TypePlatRepository;
import com.mycompany.myapp.service.dto.TypePlatDTO;
import com.mycompany.myapp.service.mapper.TypePlatMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TypePlatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TypePlatResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-plats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypePlatRepository typePlatRepository;

    @Autowired
    private TypePlatMapper typePlatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TypePlat typePlat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePlat createEntity(EntityManager em) {
        TypePlat typePlat = new TypePlat().type(DEFAULT_TYPE).imagePath(DEFAULT_IMAGE_PATH);
        return typePlat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePlat createUpdatedEntity(EntityManager em) {
        TypePlat typePlat = new TypePlat().type(UPDATED_TYPE).imagePath(UPDATED_IMAGE_PATH);
        return typePlat;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TypePlat.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        typePlat = createEntity(em);
    }

    @Test
    void createTypePlat() throws Exception {
        int databaseSizeBeforeCreate = typePlatRepository.findAll().collectList().block().size();
        // Create the TypePlat
        TypePlatDTO typePlatDTO = typePlatMapper.toDto(typePlat);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlatDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll().collectList().block();
        assertThat(typePlatList).hasSize(databaseSizeBeforeCreate + 1);
        TypePlat testTypePlat = typePlatList.get(typePlatList.size() - 1);
        assertThat(testTypePlat.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTypePlat.getImagePath()).isEqualTo(DEFAULT_IMAGE_PATH);
    }

    @Test
    void createTypePlatWithExistingId() throws Exception {
        // Create the TypePlat with an existing ID
        typePlat.setId(1L);
        TypePlatDTO typePlatDTO = typePlatMapper.toDto(typePlat);

        int databaseSizeBeforeCreate = typePlatRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll().collectList().block();
        assertThat(typePlatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllTypePlats() {
        // Initialize the database
        typePlatRepository.save(typePlat).block();

        // Get all the typePlatList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(typePlat.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].imagePath")
            .value(hasItem(DEFAULT_IMAGE_PATH.toString()));
    }

    @Test
    void getTypePlat() {
        // Initialize the database
        typePlatRepository.save(typePlat).block();

        // Get the typePlat
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, typePlat.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(typePlat.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.imagePath")
            .value(is(DEFAULT_IMAGE_PATH.toString()));
    }

    @Test
    void getNonExistingTypePlat() {
        // Get the typePlat
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTypePlat() throws Exception {
        // Initialize the database
        typePlatRepository.save(typePlat).block();

        int databaseSizeBeforeUpdate = typePlatRepository.findAll().collectList().block().size();

        // Update the typePlat
        TypePlat updatedTypePlat = typePlatRepository.findById(typePlat.getId()).block();
        updatedTypePlat.type(UPDATED_TYPE).imagePath(UPDATED_IMAGE_PATH);
        TypePlatDTO typePlatDTO = typePlatMapper.toDto(updatedTypePlat);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, typePlatDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlatDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll().collectList().block();
        assertThat(typePlatList).hasSize(databaseSizeBeforeUpdate);
        TypePlat testTypePlat = typePlatList.get(typePlatList.size() - 1);
        assertThat(testTypePlat.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTypePlat.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
    }

    @Test
    void putNonExistingTypePlat() throws Exception {
        int databaseSizeBeforeUpdate = typePlatRepository.findAll().collectList().block().size();
        typePlat.setId(count.incrementAndGet());

        // Create the TypePlat
        TypePlatDTO typePlatDTO = typePlatMapper.toDto(typePlat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, typePlatDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll().collectList().block();
        assertThat(typePlatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTypePlat() throws Exception {
        int databaseSizeBeforeUpdate = typePlatRepository.findAll().collectList().block().size();
        typePlat.setId(count.incrementAndGet());

        // Create the TypePlat
        TypePlatDTO typePlatDTO = typePlatMapper.toDto(typePlat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll().collectList().block();
        assertThat(typePlatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTypePlat() throws Exception {
        int databaseSizeBeforeUpdate = typePlatRepository.findAll().collectList().block().size();
        typePlat.setId(count.incrementAndGet());

        // Create the TypePlat
        TypePlatDTO typePlatDTO = typePlatMapper.toDto(typePlat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlatDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll().collectList().block();
        assertThat(typePlatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTypePlatWithPatch() throws Exception {
        // Initialize the database
        typePlatRepository.save(typePlat).block();

        int databaseSizeBeforeUpdate = typePlatRepository.findAll().collectList().block().size();

        // Update the typePlat using partial update
        TypePlat partialUpdatedTypePlat = new TypePlat();
        partialUpdatedTypePlat.setId(typePlat.getId());

        partialUpdatedTypePlat.type(UPDATED_TYPE).imagePath(UPDATED_IMAGE_PATH);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTypePlat.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTypePlat))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll().collectList().block();
        assertThat(typePlatList).hasSize(databaseSizeBeforeUpdate);
        TypePlat testTypePlat = typePlatList.get(typePlatList.size() - 1);
        assertThat(testTypePlat.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTypePlat.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
    }

    @Test
    void fullUpdateTypePlatWithPatch() throws Exception {
        // Initialize the database
        typePlatRepository.save(typePlat).block();

        int databaseSizeBeforeUpdate = typePlatRepository.findAll().collectList().block().size();

        // Update the typePlat using partial update
        TypePlat partialUpdatedTypePlat = new TypePlat();
        partialUpdatedTypePlat.setId(typePlat.getId());

        partialUpdatedTypePlat.type(UPDATED_TYPE).imagePath(UPDATED_IMAGE_PATH);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTypePlat.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTypePlat))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll().collectList().block();
        assertThat(typePlatList).hasSize(databaseSizeBeforeUpdate);
        TypePlat testTypePlat = typePlatList.get(typePlatList.size() - 1);
        assertThat(testTypePlat.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTypePlat.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
    }

    @Test
    void patchNonExistingTypePlat() throws Exception {
        int databaseSizeBeforeUpdate = typePlatRepository.findAll().collectList().block().size();
        typePlat.setId(count.incrementAndGet());

        // Create the TypePlat
        TypePlatDTO typePlatDTO = typePlatMapper.toDto(typePlat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, typePlatDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll().collectList().block();
        assertThat(typePlatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTypePlat() throws Exception {
        int databaseSizeBeforeUpdate = typePlatRepository.findAll().collectList().block().size();
        typePlat.setId(count.incrementAndGet());

        // Create the TypePlat
        TypePlatDTO typePlatDTO = typePlatMapper.toDto(typePlat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlatDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll().collectList().block();
        assertThat(typePlatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTypePlat() throws Exception {
        int databaseSizeBeforeUpdate = typePlatRepository.findAll().collectList().block().size();
        typePlat.setId(count.incrementAndGet());

        // Create the TypePlat
        TypePlatDTO typePlatDTO = typePlatMapper.toDto(typePlat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typePlatDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll().collectList().block();
        assertThat(typePlatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTypePlat() {
        // Initialize the database
        typePlatRepository.save(typePlat).block();

        int databaseSizeBeforeDelete = typePlatRepository.findAll().collectList().block().size();

        // Delete the typePlat
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, typePlat.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TypePlat> typePlatList = typePlatRepository.findAll().collectList().block();
        assertThat(typePlatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
