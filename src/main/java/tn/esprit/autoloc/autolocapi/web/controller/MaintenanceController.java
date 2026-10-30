package tn.esprit.autoloc.autolocapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.service.IMaintenanceService;
import tn.esprit.autoloc.autolocapi.web.dto.MaintenanceDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.MaintenanceMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
@Tag(name = "Maintenances", description = "Gestion des maintenances de véhicules")
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;
    private final MaintenanceMapper maintenanceMapper;

    @Operation(summary = "Créer une maintenance")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Maintenance créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaintenanceDTO create(@Valid @RequestBody MaintenanceDTO dto) {
        return maintenanceMapper.toDTO(maintenanceService.create(maintenanceMapper.toEntity(dto)));
    }

    @Operation(summary = "Lister toutes les maintenances")
    @ApiResponse(responseCode = "200", description = "Liste des maintenances retournée")
    @GetMapping
    public List<MaintenanceDTO> findAll() {
        return maintenanceService.findAll().stream()
                .map(maintenanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter une maintenance par son identifiant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Maintenance trouvée"),
        @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    @GetMapping("/{id}")
    public MaintenanceDTO findById(
            @Parameter(description = "Identifiant de la maintenance") @PathVariable Long id) {
        return maintenanceMapper.toDTO(maintenanceService.findById(id));
    }

    @Operation(summary = "Modifier une maintenance")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Maintenance mise à jour"),
        @ApiResponse(responseCode = "404", description = "Maintenance introuvable"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/{id}")
    public MaintenanceDTO update(
            @Parameter(description = "Identifiant de la maintenance") @PathVariable Long id,
            @Valid @RequestBody MaintenanceDTO dto) {
        return maintenanceMapper.toDTO(maintenanceService.update(id, maintenanceMapper.toEntity(dto)));
    }

    @Operation(summary = "Supprimer une maintenance")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Maintenance supprimée"),
        @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de la maintenance") @PathVariable Long id) {
        maintenanceService.deleteById(id);
    }
}
