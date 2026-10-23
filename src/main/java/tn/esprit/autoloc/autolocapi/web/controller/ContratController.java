package tn.esprit.autoloc.autolocapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.domain.Contrat;
import tn.esprit.autoloc.autolocapi.service.IContratService;

import java.util.List;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion des contrats de location")
public class ContratController {

    private final IContratService contratService;

    @Operation(summary = "Créer un contrat",
               description = "Crée un contrat. Les paiements inclus dans le corps sont persistés en cascade. " +
                             "Retourne 400 si l'identifiant est fourni ou si le montant est invalide.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Contrat créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides (identifiant présent ou montant négatif)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contrat create(@RequestBody Contrat contrat) {
        return contratService.create(contrat);
    }

    @Operation(summary = "Lister tous les contrats")
    @ApiResponse(responseCode = "200", description = "Liste des contrats retournée")
    @GetMapping
    public List<Contrat> findAll() {
        return contratService.findAll();
    }

    @Operation(summary = "Consulter un contrat par son identifiant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contrat trouvé"),
        @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @GetMapping("/{id}")
    public Contrat findById(
            @Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        return contratService.findById(id);
    }

    @Operation(summary = "Modifier un contrat",
               description = "Met à jour les attributs du contrat (sans toucher aux paiements).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contrat mis à jour"),
        @ApiResponse(responseCode = "404", description = "Contrat introuvable"),
        @ApiResponse(responseCode = "400", description = "Données invalides (montant négatif)")
    })
    @PutMapping("/{id}")
    public Contrat update(
            @Parameter(description = "Identifiant du contrat") @PathVariable Long id,
            @RequestBody Contrat contrat) {
        return contratService.update(id, contrat);
    }

    @Operation(summary = "Supprimer un contrat",
               description = "Supprime le contrat et ses paiements associés (cascade ALL + orphanRemoval).")
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
