package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.AulaRequest;
import org.example.dto.AulaResponse;
import org.example.service.AulaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aulas")
@RequiredArgsConstructor
public class AulaController {

    private final AulaService aulaService;

    @GetMapping
    public ResponseEntity<List<AulaResponse>> findAll() {
        return ResponseEntity.ok(aulaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AulaResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(aulaService.findById(id));
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<AulaResponse>> findByAlunoId(@PathVariable Integer alunoId) {
        return ResponseEntity.ok(aulaService.findByAlunoId(alunoId));
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<AulaResponse>> findByProfessorId(@PathVariable Integer professorId) {
        return ResponseEntity.ok(aulaService.findByProfessorId(professorId));
    }

    @GetMapping("/modalidade/{modalidadeId}")
    public ResponseEntity<List<AulaResponse>> findByModalidadeId(@PathVariable Integer modalidadeId) {
        return ResponseEntity.ok(aulaService.findByModalidadeId(modalidadeId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AulaResponse>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(aulaService.findByStatus(status));
    }

    @PostMapping
    public ResponseEntity<AulaResponse> create(@Valid @RequestBody AulaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AulaResponse> update(@PathVariable Integer id, @Valid @RequestBody AulaRequest request) {
        return ResponseEntity.ok(aulaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        aulaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
