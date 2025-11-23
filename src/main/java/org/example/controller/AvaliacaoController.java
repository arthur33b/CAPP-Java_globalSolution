package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.AvaliacaoRequest;
import org.example.dto.AvaliacaoResponse;
import org.example.service.AvaliacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @GetMapping
    public ResponseEntity<List<AvaliacaoResponse>> findAll() {
        return ResponseEntity.ok(avaliacaoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(avaliacaoService.findById(id));
    }

    @GetMapping("/aula/{aulaId}")
    public ResponseEntity<List<AvaliacaoResponse>> findByAulaId(@PathVariable Integer aulaId) {
        return ResponseEntity.ok(avaliacaoService.findByAulaId(aulaId));
    }

    @PostMapping
    public ResponseEntity<AvaliacaoResponse> create(@Valid @RequestBody AvaliacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoResponse> update(@PathVariable Integer id, @Valid @RequestBody AvaliacaoRequest request) {
        return ResponseEntity.ok(avaliacaoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        avaliacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
