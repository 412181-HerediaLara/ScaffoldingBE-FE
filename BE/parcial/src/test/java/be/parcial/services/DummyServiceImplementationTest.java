package be.parcial.services;

import be.parcial.dtos.DummyRequestDTO;
import be.parcial.dtos.DummyResponseDTO;
import be.parcial.entities.DummyEntity;
import be.parcial.enums.DummyEnum;
import be.parcial.exceptions.ResourceNotFoundException;
import be.parcial.models.Dummy;
import be.parcial.repositories.DummyRepository;
import be.parcial.services.implementations.DummyServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DummyServiceImplementationTest {

    @Mock
    private DummyRepository dummyRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock(name = "mergerMapper")
    private ModelMapper mergerMapper;

    private DummyServiceImplementation dummyService;

    private DummyEntity dummyEntity;
    private Dummy dummyModel;
    private DummyResponseDTO dummyResponseDTO;
    private DummyRequestDTO dummyRequestDTO;

    @BeforeEach
    void setUp() {
        dummyService = new DummyServiceImplementation(dummyRepository, modelMapper, mergerMapper);

        dummyEntity = new DummyEntity();
        dummyEntity.setId(1L);
        dummyEntity.setName("Test Dummy");
        dummyEntity.setType(DummyEnum.USER);

        dummyModel = new Dummy(1L, "Test Dummy", DummyEnum.USER);

        dummyResponseDTO = new DummyResponseDTO(1L, "Test Dummy", DummyEnum.USER);
        dummyRequestDTO = new DummyRequestDTO("Test Dummy");
    }

    @Nested
    @DisplayName("getAllDummies")
    class GetAllDummies {

        @Test
        @DisplayName("should return list of dummies")
        void getAllDummies_withData_returnsList() {
            when(dummyRepository.findAll()).thenReturn(Arrays.asList(dummyEntity));
            when(modelMapper.map(any(DummyEntity.class), eq(Dummy.class))).thenReturn(dummyModel);
            when(modelMapper.map(any(Dummy.class), eq(DummyResponseDTO.class))).thenReturn(dummyResponseDTO);

            List<DummyResponseDTO> result = dummyService.getAllDummies();

            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Test Dummy");
            verify(dummyRepository).findAll();
        }

        @Test
        @DisplayName("should return empty list when no dummies")
        void getAllDummies_empty_returnsEmptyList() {
            when(dummyRepository.findAll()).thenReturn(Collections.emptyList());

            List<DummyResponseDTO> result = dummyService.getAllDummies();

            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getDummyById")
    class GetDummyById {

        @Test
        @DisplayName("should return dummy when found")
        void getDummyById_existingId_returnsDummy() {
            when(dummyRepository.findById(1L)).thenReturn(Optional.of(dummyEntity));
            when(modelMapper.map(any(DummyEntity.class), eq(Dummy.class))).thenReturn(dummyModel);
            when(modelMapper.map(any(Dummy.class), eq(DummyResponseDTO.class))).thenReturn(dummyResponseDTO);

            DummyResponseDTO result = dummyService.getDummyById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Test Dummy");
        }

        @Test
        @DisplayName("should throw exception when not found")
        void getDummyById_nonExistingId_throwsException() {
            when(dummyRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> dummyService.getDummyById(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Dummy not found with id: 99");
        }
    }

    @Nested
    @DisplayName("createDummy")
    class CreateDummy {

        @Test
        @DisplayName("should create dummy successfully")
        void createDummy_validRequest_returnsCreated() {
            when(modelMapper.map(any(DummyRequestDTO.class), eq(Dummy.class))).thenReturn(dummyModel);
            when(modelMapper.map(any(Dummy.class), eq(DummyEntity.class))).thenReturn(dummyEntity);
            when(dummyRepository.save(any(DummyEntity.class))).thenReturn(dummyEntity);
            when(modelMapper.map(any(DummyEntity.class), eq(Dummy.class))).thenReturn(dummyModel);
            when(modelMapper.map(any(Dummy.class), eq(DummyResponseDTO.class))).thenReturn(dummyResponseDTO);

            DummyResponseDTO result = dummyService.createDummy(dummyRequestDTO);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Test Dummy");
            verify(dummyRepository).save(any(DummyEntity.class));
        }

        @Test
        @DisplayName("should set default type when null")
        void createDummy_nullType_setsDefault() {
            Dummy modelWithoutType = new Dummy(null, "No Type", null);
            DummyEntity entityWithoutType = new DummyEntity();
            entityWithoutType.setName("No Type");
            entityWithoutType.setType(null);

            when(modelMapper.map(any(DummyRequestDTO.class), eq(Dummy.class))).thenReturn(modelWithoutType);
            when(modelMapper.map(any(Dummy.class), eq(DummyEntity.class))).thenReturn(entityWithoutType);
            when(dummyRepository.save(any(DummyEntity.class))).thenReturn(entityWithoutType);
            when(modelMapper.map(any(DummyEntity.class), eq(Dummy.class))).thenReturn(modelWithoutType);
            when(modelMapper.map(any(Dummy.class), eq(DummyResponseDTO.class))).thenReturn(dummyResponseDTO);

            DummyResponseDTO result = dummyService.createDummy(dummyRequestDTO);

            assertThat(result).isNotNull();
            verify(dummyRepository).save(any(DummyEntity.class));
        }
    }

    @Nested
    @DisplayName("updateDummy")
    class UpdateDummy {

        @Test
        @DisplayName("should update dummy successfully")
        void updateDummy_existingId_returnsUpdated() {
            doNothing().when(mergerMapper).map(any(DummyRequestDTO.class), any(DummyEntity.class));
            when(dummyRepository.findById(1L)).thenReturn(Optional.of(dummyEntity));
            when(dummyRepository.save(any(DummyEntity.class))).thenReturn(dummyEntity);
            when(modelMapper.map(any(DummyEntity.class), eq(Dummy.class))).thenReturn(dummyModel);
            when(modelMapper.map(any(Dummy.class), eq(DummyResponseDTO.class))).thenReturn(dummyResponseDTO);

            DummyResponseDTO result = dummyService.updateDummy(1L, dummyRequestDTO);

            assertThat(result).isNotNull();
            verify(mergerMapper).map(any(DummyRequestDTO.class), any(DummyEntity.class));
            verify(dummyRepository).save(dummyEntity);
        }

        @Test
        @DisplayName("should throw exception when dummy not found")
        void updateDummy_nonExistingId_throwsException() {
            when(dummyRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> dummyService.updateDummy(99L, dummyRequestDTO))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Dummy not found with id: 99");
        }
    }

    @Nested
    @DisplayName("deleteDummy")
    class DeleteDummy {

        @Test
        @DisplayName("should delete dummy successfully")
        void deleteDummy_existingId_deletes() {
            when(dummyRepository.existsById(1L)).thenReturn(true);

            dummyService.deleteDummy(1L);

            verify(dummyRepository).deleteById(1L);
        }

        @Test
        @DisplayName("should throw exception when dummy not found")
        void deleteDummy_nonExistingId_throwsException() {
            when(dummyRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> dummyService.deleteDummy(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Dummy not found with id: 99");

            verify(dummyRepository, never()).deleteById(anyLong());
        }
    }
}
