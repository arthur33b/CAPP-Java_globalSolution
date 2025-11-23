package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.ModalidadeRequest;
import org.example.dto.ModalidadeResponse;
import org.example.service.ModalidadeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modalidades")
@RequiredArgsConstructor
public class ModalidadeController {

    private final ModalidadeService modalidadeService;

    @GetMapping
    public ResponseEntity<List<ModalidadeResponse>> findAll() {
        return ResponseEntity.ok(modalidadeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModalidadeResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(modalidadeService.findById(id));
    }

    @GetMapping("/area/{areaId}")
    public ResponseEntity<List<ModalidadeResponse>> findByAreaId(@PathVariable Integer areaId) {
        return ResponseEntity.ok(modalidadeService.findByAreaId(areaId));
    }

    @PostMapping
    public ResponseEntity<ModalidadeResponse> create(@Valid @RequestBody ModalidadeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(modalidadeService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModalidadeResponse> update(@PathVariable Integer id, @Valid @RequestBody ModalidadeRequest request) {
        return ResponseEntity.ok(modalidadeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        modalidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
