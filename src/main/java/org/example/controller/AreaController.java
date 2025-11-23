package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.AreaRequest;
import org.example.dto.AreaResponse;
import org.example.service.AreaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
@RequiredArgsConstructor
@Tag(name = "Áreas", description = "Gerenciamento de áreas de conhecimento")
public class AreaController {

    private final AreaService areaService;

    @GetMapping
    @Operation(summary = "Listar todas as áreas", description = "Retorna lista paginada de todas as áreas")
    public ResponseEntity<Page<AreaResponse>> findAll(
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(areaService.findAll(pageable));
    }

    @GetMapping("/all")
    @Operation(summary = "Listar todas as áreas sem paginação")
    public ResponseEntity<List<AreaResponse>> findAllNoPagination() {
        return ResponseEntity.ok(areaService.findAllNoPagination());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(areaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AreaResponse> create(@Valid @RequestBody AreaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(areaService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaResponse> update(@PathVariable Integer id, @Valid @RequestBody AreaRequest request) {
        return ResponseEntity.ok(areaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        areaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
