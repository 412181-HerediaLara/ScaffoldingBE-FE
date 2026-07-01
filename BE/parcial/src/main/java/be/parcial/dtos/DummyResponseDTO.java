package be.parcial.dtos;

import be.parcial.enums.DummyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DummyResponseDTO {
    private Long id;
    private String name;
    private DummyEnum dummyEnum;
}
