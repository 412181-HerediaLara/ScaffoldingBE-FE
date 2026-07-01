package be.parcial.controllers;

import be.parcial.ParcialApplication;
import be.parcial.dtos.DummyRequestDTO;
import be.parcial.dtos.DummyResponseDTO;
import be.parcial.services.DummyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(classes = ParcialApplication.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DummyControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private DummyService dummyService;

    private JsonMapper jsonMapper;

    @BeforeEach
    void setUp() {
        jsonMapper = JsonMapper.builder().build();
        DummyController controller = new DummyController(dummyService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("should create and retrieve dummy")
    @WithMockUser(roles = "USER")
    void createAndRetrieveDummy_fullFlow() throws Exception {
        DummyRequestDTO request = new DummyRequestDTO("Integration Dummy");

        MockHttpServletResponse createResponse = mockMvc.perform(post("/api/v1/dummies")
                        .contentType("application/json")
                        .content(jsonMapper.writeValueAsString(request)))
                .andReturn().getResponse();

        assertThat(createResponse.getStatus()).isEqualTo(201);

        MockHttpServletResponse getAllResponse = mockMvc.perform(get("/api/v1/dummies")
                        .accept("application/json"))
                .andReturn().getResponse();

        assertThat(getAllResponse.getStatus()).isEqualTo(200);
        assertThat(getAllResponse.getContentAsString()).contains("Integration Dummy");
    }

    @Test
    @DisplayName("should update dummy")
    @WithMockUser(roles = "USER")
    void updateDummy_existingRecord() throws Exception {
        MockHttpServletResponse createResponse = mockMvc.perform(post("/api/v1/dummies")
                        .contentType("application/json")
                        .content(jsonMapper.writeValueAsString(new DummyRequestDTO("Original Name"))))
                .andReturn().getResponse();

        assertThat(createResponse.getStatus()).isEqualTo(201);
        DummyResponseDTO created = jsonMapper.readValue(
                createResponse.getContentAsString(), DummyResponseDTO.class);

        DummyRequestDTO updateRequest = new DummyRequestDTO("Updated Name");
        MockHttpServletResponse updateResponse = mockMvc.perform(put("/api/v1/dummies/" + created.getId())
                        .contentType("application/json")
                        .content(jsonMapper.writeValueAsString(updateRequest)))
                .andReturn().getResponse();

        assertThat(updateResponse.getStatus()).isEqualTo(200);
        assertThat(updateResponse.getContentAsString()).contains("Updated Name");
    }

    @Test
    @DisplayName("should delete dummy")
    @WithMockUser(roles = "USER")
    void deleteDummy_existingRecord() throws Exception {
        MockHttpServletResponse createResponse = mockMvc.perform(post("/api/v1/dummies")
                        .contentType("application/json")
                        .content(jsonMapper.writeValueAsString(new DummyRequestDTO("To Delete"))))
                .andReturn().getResponse();

        assertThat(createResponse.getStatus()).isEqualTo(201);
        DummyResponseDTO created = jsonMapper.readValue(
                createResponse.getContentAsString(), DummyResponseDTO.class);

        MockHttpServletResponse deleteResponse = mockMvc.perform(
                        delete("/api/v1/dummies/" + created.getId()))
                .andReturn().getResponse();

        assertThat(deleteResponse.getStatus()).isEqualTo(204);
    }

    @Test
    @DisplayName("should return 200 when getting all dummies")
    @WithMockUser(roles = "USER")
    void getAllDummies_returns200() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/dummies")
                        .accept("application/json"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
    }
}
