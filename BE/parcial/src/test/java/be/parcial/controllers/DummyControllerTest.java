package be.parcial.controllers;

import be.parcial.dtos.DummyRequestDTO;
import be.parcial.dtos.DummyResponseDTO;
import be.parcial.enums.DummyEnum;
import be.parcial.security.JwtService;
import be.parcial.services.DummyService;
import be.parcial.security.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
class DummyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DummyService dummyService;

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private DummyController dummyController;

    private JsonMapper jsonMapper;

    private DummyResponseDTO dummyResponseDTO;
    private DummyRequestDTO dummyRequestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dummyController).build();
        jsonMapper = JsonMapper.builder().build();

        dummyResponseDTO = new DummyResponseDTO(1L, "Test Dummy", DummyEnum.USER);
        dummyRequestDTO = new DummyRequestDTO("Test Dummy");
    }

    @Test
    @DisplayName("should return all dummies")
    void getAllDummies_returns200WithList() throws Exception {
        when(dummyService.getAllDummies()).thenReturn(List.of(dummyResponseDTO));

        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/dummies")
                        .with(SecurityMockMvcRequestPostProcessors.user("test@email.com").roles("USER"))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("Test Dummy");
        verify(dummyService).getAllDummies();
    }

    @Test
    @DisplayName("should return dummy by id")
    void getDummyById_validId_returns200() throws Exception {
        when(dummyService.getDummyById(1L)).thenReturn(dummyResponseDTO);

        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/dummies/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("test@email.com").roles("USER"))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("Test Dummy");
        verify(dummyService).getDummyById(1L);
    }

    @Test
    @DisplayName("should create dummy")
    void createDummy_validRequest_returns201() throws Exception {
        when(dummyService.createDummy(any(DummyRequestDTO.class))).thenReturn(dummyResponseDTO);

        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/dummies")
                        .with(SecurityMockMvcRequestPostProcessors.user("test@email.com").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(dummyRequestDTO)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);
        verify(dummyService).createDummy(any(DummyRequestDTO.class));
    }

    @Test
    @DisplayName("should update dummy")
    void updateDummy_validRequest_returns200() throws Exception {
        DummyResponseDTO updatedDTO = new DummyResponseDTO(1L, "Updated Dummy", DummyEnum.USER);
        when(dummyService.updateDummy(eq(1L), any(DummyRequestDTO.class))).thenReturn(updatedDTO);

        MockHttpServletResponse response = mockMvc.perform(put("/api/v1/dummies/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("test@email.com").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(dummyRequestDTO)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        verify(dummyService).updateDummy(eq(1L), any(DummyRequestDTO.class));
    }

    @Test
    @DisplayName("should delete dummy")
    void deleteDummy_validId_returns204() throws Exception {
        doNothing().when(dummyService).deleteDummy(1L);

        MockHttpServletResponse response = mockMvc.perform(delete("/api/v1/dummies/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("test@email.com").roles("USER")))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(204);
        verify(dummyService).deleteDummy(1L);
    }
}
