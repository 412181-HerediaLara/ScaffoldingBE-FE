package be.parcial.config;

import be.parcial.dtos.DummyResponseDTO;
import be.parcial.entities.DummyEntity;
import be.parcial.enums.DummyEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MappersConfig.class)
class ModelMapperConfigTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @DisplayName("should map DummyEntity.type to DummyResponseDTO.dummyEnum")
    void entityTypeMapsToDtoDummyEnum() {
        DummyEntity entity = new DummyEntity();
        entity.setId(1L);
        entity.setName("Test");
        entity.setType(DummyEnum.ADMIN);

        DummyResponseDTO dto = modelMapper.map(entity, DummyResponseDTO.class);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Test");
        assertThat(dto.getDummyEnum()).isEqualTo(DummyEnum.ADMIN);
    }

    @Test
    @DisplayName("should map DummyRequestDTO to DummyEntity by field name")
    void dtoNameMapsToEntityName() {
        be.parcial.dtos.DummyRequestDTO dto = new be.parcial.dtos.DummyRequestDTO("TestName");

        DummyEntity entity = modelMapper.map(dto, DummyEntity.class);

        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("TestName");
    }

    @Test
    @DisplayName("should map DummyResponseDTO to DummyRequestDTO compatible fields")
    void responseDtoToEntityMapsName() {
        DummyResponseDTO dto = new DummyResponseDTO(1L, "TestName", DummyEnum.USER);

        DummyEntity entity = modelMapper.map(dto, DummyEntity.class);

        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("TestName");
    }
}
