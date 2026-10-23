package tn.esprit.autoloc.autolocapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.service.IVehiculeService;

import java.util.List;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(name = "Véhicules", description = "Gestion du parc de véhicules")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @Operation(summary = "Créer un véhicule",
               description = "Crée un nouveau véhicule. Retourne 400 si le tarif journalier est négatif ou nul.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Véhicule créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides (valeur numérique négative ou champ obligatoire manquant)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicule create(@RequestBody Vehicule vehicule) {
        return vehiculeService.create(vehicule);
    }

    @Operation(summary = "Lister tous les véhicules")
    @ApiResponse(responseCode = "200", description = "Liste des véhicules retournée")
    @GetMapping
    public List<Vehicule> findAll() {
        return vehiculeService.findAll();
    }

    @Operation(summary = "Consulter un véhicule par son identifiant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Véhicule trouvé"),
        @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    @GetMapping("/{id}")
    public Vehicule findById(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        return vehiculeService.findById(id);
    }

    @Operation(summary = "Modifier un véhicule")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Véhicule mis à jour"),
        @ApiResponse(responseCode = "404", description = "Véhicule introuvable"),
        @ApiResponse(responseCode = "400", description = "Données invalides (valeur numérique négative)")
    })
    @PutMapping("/{id}")
    public Vehicule update(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long id,
            @RequestBody Vehicule vehicule) {
        return vehiculeService.update(id, vehicule);
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
