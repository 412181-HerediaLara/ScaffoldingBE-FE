package be.parcial.controllers;

import be.parcial.dtos.DummyRequestDTO;
import be.parcial.dtos.DummyResponseDTO;
import be.parcial.services.DummyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dummies")
@RequiredArgsConstructor
@Tag(name = "Dummy", description = "CRUD operations for Dummy entity")
public class DummyController {

    private final DummyService dummyService;

    @GetMapping
    @Operation(summary = "Get all dummies", description = "Retrieve all dummy records")
    public ResponseEntity<List<DummyResponseDTO>> getAllDummies() {
        List<DummyResponseDTO> dummies = dummyService.getAllDummies();
        return ResponseEntity.ok(dummies);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get dummy by ID", description = "Retrieve a single dummy by its ID")
    public ResponseEntity<DummyResponseDTO> getDummyById(
            @Parameter(description = "Dummy ID") @PathVariable Long id) {
        DummyResponseDTO dummy = dummyService.getDummyById(id);
        return ResponseEntity.ok(dummy);
    }

    @PostMapping
    @Operation(summary = "Create a new dummy", description = "Create a new dummy record")
    public ResponseEntity<DummyResponseDTO> createDummy(
            @Valid @RequestBody DummyRequestDTO request) {
        DummyResponseDTO created = dummyService.createDummy(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a dummy", description = "Update an existing dummy record")
    public ResponseEntity<DummyResponseDTO> updateDummy(
            @Parameter(description = "Dummy ID") @PathVariable Long id,
            @Valid @RequestBody DummyRequestDTO request) {
        DummyResponseDTO updated = dummyService.updateDummy(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a dummy", description = "Delete a dummy record by its ID")
    public ResponseEntity<Void> deleteDummy(
            @Parameter(description = "Dummy ID") @PathVariable Long id) {
        dummyService.deleteDummy(id);
        return ResponseEntity.noContent().build();
    }
}
