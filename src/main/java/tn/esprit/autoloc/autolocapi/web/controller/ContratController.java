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
import tn.esprit.autoloc.autolocapi.service.IContratService;
import tn.esprit.autoloc.autolocapi.web.dto.ContratDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.ContratMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion des contrats de location")
public class ContratController {

    private final IContratService contratService;
    private final ContratMapper contratMapper;

    @Operation(summary = "Créer un contrat",
               description = "Les paiements inclus dans le corps sont persistés en cascade.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Contrat créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContratDTO create(@Valid @RequestBody ContratDTO dto) {
        return contratMapper.toDTO(contratService.create(contratMapper.toEntity(dto)));
    }

    @Operation(summary = "Lister tous les contrats")
    @ApiResponse(responseCode = "200", description = "Liste des contrats retournée")
    @GetMapping
    public List<ContratDTO> findAll() {
        return contratService.findAll().stream()
                .map(contratMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter un contrat par son identifiant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contrat trouvé"),
        @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @GetMapping("/{id}")
    public ContratDTO findById(
            @Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        return contratMapper.toDTO(contratService.findById(id));
    }

    @Operation(summary = "Modifier un contrat")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contrat mis à jour"),
        @ApiResponse(responseCode = "404", description = "Contrat introuvable"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/{id}")
    public ContratDTO update(
            @Parameter(description = "Identifiant du contrat") @PathVariable Long id,
            @Valid @RequestBody ContratDTO dto) {
        return contratMapper.toDTO(contratService.update(id, contratMapper.toEntity(dto)));
    }

    @Operation(summary = "Supprimer un contrat",
               description = "Supprime le contrat et ses paiements (cascade ALL + orphanRemoval).")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Contrat supprimé"),
        @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        contratService.deleteById(id);
    }
}
