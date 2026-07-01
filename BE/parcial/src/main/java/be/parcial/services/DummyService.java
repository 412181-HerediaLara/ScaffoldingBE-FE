package be.parcial.services;

import be.parcial.dtos.DummyRequestDTO;
import be.parcial.dtos.DummyResponseDTO;

import java.util.List;

public interface DummyService {
    List<DummyResponseDTO> getAllDummies();
    DummyResponseDTO getDummyById(Long id);
    DummyResponseDTO createDummy(DummyRequestDTO dto);
    DummyResponseDTO updateDummy(Long id, DummyRequestDTO dto);
    void deleteDummy(Long id);
}
