package be.parcial.services.implementations;

import be.parcial.dtos.DummyRequestDTO;
import be.parcial.dtos.DummyResponseDTO;
import be.parcial.entities.DummyEntity;
import be.parcial.enums.DummyEnum;
import be.parcial.exceptions.Messages;
import be.parcial.exceptions.ResourceNotFoundException;
import be.parcial.models.Dummy;
import be.parcial.repositories.DummyRepository;
import be.parcial.services.DummyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DummyServiceImplementation implements DummyService {

    private final DummyRepository dummyRepository;
    private final ModelMapper modelMapper;
    private final @Qualifier("mergerMapper") ModelMapper mergerMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DummyResponseDTO> getAllDummies() {
        List<DummyEntity> entities = dummyRepository.findAll();
        return entities.stream()
                .map(e -> modelMapper.map(e, Dummy.class))
                .map(m -> modelMapper.map(m, DummyResponseDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DummyResponseDTO getDummyById(Long id) {
        DummyEntity entity = dummyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(Messages.DUMMY_NOT_FOUND, id)));
        Dummy model = modelMapper.map(entity, Dummy.class);
        return modelMapper.map(model, DummyResponseDTO.class);
    }

    @Override
    @Transactional
    public DummyResponseDTO createDummy(DummyRequestDTO dto) {
        Dummy model = modelMapper.map(dto, Dummy.class);
        DummyEntity entity = modelMapper.map(model, DummyEntity.class);
        if (entity.getType() == null) {
            entity.setType(DummyEnum.USER);
        }
        DummyEntity saved = dummyRepository.save(entity);
        Dummy savedModel = modelMapper.map(saved, Dummy.class);
        return modelMapper.map(savedModel, DummyResponseDTO.class);
    }

    @Override
    @Transactional
    public DummyResponseDTO updateDummy(Long id, DummyRequestDTO dto) {
        DummyEntity entity = dummyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(Messages.DUMMY_NOT_FOUND, id)));
        mergerMapper.map(dto, entity);
        DummyEntity updated = dummyRepository.save(entity);
        Dummy updatedModel = modelMapper.map(updated, Dummy.class);
        return modelMapper.map(updatedModel, DummyResponseDTO.class);
    }

    @Override
    @Transactional
    public void deleteDummy(Long id) {
        if (!dummyRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format(Messages.DUMMY_NOT_FOUND, id));
        }
        dummyRepository.deleteById(id);
    }
}
