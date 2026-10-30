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
import tn.esprit.autoloc.autolocapi.service.IEquipementService;
import tn.esprit.autoloc.autolocapi.web.dto.EquipementDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.EquipementMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
@Tag(name = "Équipements", description = "Gestion des équipements de véhicules")
public class EquipementController {

    private final IEquipementService equipementService;
    private final EquipementMapper equipementMapper;

    @Operation(summary = "Créer un équipement")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Équipement créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipementDTO create(@Valid @RequestBody EquipementDTO dto) {
        return equipementMapper.toDTO(equipementService.create(equipementMapper.toEntity(dto)));
    }

    @Operation(summary = "Lister tous les équipements")
    @ApiResponse(responseCode = "200", description = "Liste des équipements retournée")
    @GetMapping
    public List<EquipementDTO> findAll() {
        return equipementService.findAll().stream()
                .map(equipementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter un équipement par son identifiant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Équipement trouvé"),
        @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    @GetMapping("/{id}")
    public EquipementDTO findById(
            @Parameter(description = "Identifiant de l'équipement") @PathVariable Long id) {
        return equipementMapper.toDTO(equipementService.findById(id));
    }

    @Operation(summary = "Modifier un équipement")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Équipement mis à jour"),
        @ApiResponse(responseCode = "404", description = "Équipement introuvable"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/{id}")
    public EquipementDTO update(
            @Parameter(description = "Identifiant de l'équipement") @PathVariable Long id,
            @Valid @RequestBody EquipementDTO dto) {
        return equipementMapper.toDTO(equipementService.update(id, equipementMapper.toEntity(dto)));
    }

    @Operation(summary = "Supprimer un équipement")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Équipement supprimé"),
        @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de l'équipement") @PathVariable Long id) {
        equipementService.deleteById(id);
    }
}
