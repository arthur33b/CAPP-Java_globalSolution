package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.AlunoRequest;
import org.example.dto.AlunoResponse;
import org.example.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @GetMapping
    public ResponseEntity<List<AlunoResponse>> findAll() {
        return ResponseEntity.ok(alunoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(alunoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> create(@Valid @RequestBody AlunoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponse> update(@PathVariable Integer id, @Valid @RequestBody AlunoRequest request) {
        return ResponseEntity.ok(alunoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
