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
import tn.esprit.autoloc.autolocapi.service.IVehiculeService;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.VehiculeMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(name = "Véhicules", description = "Gestion du parc de véhicules")
public class VehiculeController {

    private final IVehiculeService vehiculeService;
    private final VehiculeMapper vehiculeMapper;

    @Operation(summary = "Créer un véhicule")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Véhicule créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculeDTO create(@Valid @RequestBody VehiculeDTO dto) {
        return vehiculeMapper.toDTO(vehiculeService.create(vehiculeMapper.toEntity(dto)));
    }

    @Operation(summary = "Lister tous les véhicules")
    @ApiResponse(responseCode = "200", description = "Liste des véhicules retournée")
    @GetMapping
    public List<VehiculeDTO> findAll() {
        return vehiculeService.findAll().stream()
                .map(vehiculeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter un véhicule par son identifiant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Véhicule trouvé"),
        @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    @GetMapping("/{id}")
    public VehiculeDTO findById(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        return vehiculeMapper.toDTO(vehiculeService.findById(id));
    }

    @Operation(summary = "Modifier un véhicule")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Véhicule mis à jour"),
        @ApiResponse(responseCode = "404", description = "Véhicule introuvable"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/{id}")
    public VehiculeDTO update(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long id,
            @Valid @RequestBody VehiculeDTO dto) {
        return vehiculeMapper.toDTO(vehiculeService.update(id, vehiculeMapper.toEntity(dto)));
    }

    @Operation(summary = "Supprimer un véhicule")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Véhicule supprimé"),
        @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        vehiculeService.deleteById(id);
    }
}
