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
import tn.esprit.autoloc.autolocapi.service.IAgenceService;
import tn.esprit.autoloc.autolocapi.web.dto.AgenceDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.AgenceMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agences")
@RequiredArgsConstructor
@Tag(name = "Agences", description = "Gestion des agences de location")
public class AgenceController {

    private final IAgenceService agenceService;
    private final AgenceMapper agenceMapper;

    @Operation(summary = "Créer une agence")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Agence créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgenceDTO create(@Valid @RequestBody AgenceDTO dto) {
        return agenceMapper.toDTO(agenceService.create(agenceMapper.toEntity(dto)));
    }

    @Operation(summary = "Lister toutes les agences")
    @ApiResponse(responseCode = "200", description = "Liste des agences retournée")
    @GetMapping
    public List<AgenceDTO> findAll() {
        return agenceService.findAll().stream()
                .map(agenceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter une agence par son identifiant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Agence trouvée"),
        @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    @GetMapping("/{id}")
    public AgenceDTO findById(
            @Parameter(description = "Identifiant de l'agence") @PathVariable Long id) {
        return agenceMapper.toDTO(agenceService.findById(id));
    }

    @Operation(summary = "Modifier une agence")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Agence mise à jour"),
        @ApiResponse(responseCode = "404", description = "Agence introuvable"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/{id}")
    public AgenceDTO update(
            @Parameter(description = "Identifiant de l'agence") @PathVariable Long id,
            @Valid @RequestBody AgenceDTO dto) {
        return agenceMapper.toDTO(agenceService.update(id, agenceMapper.toEntity(dto)));
    }

    @Operation(summary = "Supprimer une agence")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Agence supprimée"),
        @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de l'agence") @PathVariable Long id) {
        agenceService.deleteById(id);
    }
}
