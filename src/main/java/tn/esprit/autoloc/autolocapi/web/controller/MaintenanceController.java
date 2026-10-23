package tn.esprit.autoloc.autolocapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.domain.Maintenance;
import tn.esprit.autoloc.autolocapi.service.IMaintenanceService;

import java.util.List;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
@Tag(name = "Maintenances", description = "Gestion des maintenances de véhicules")
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    @Operation(summary = "Créer une maintenance",
               description = "Corps attendu : { \"dateDebut\": \"2026-11-01\", \"dateFin\": \"2026-11-05\", " +
                             "\"description\": \"...\", \"vehicule\": {\"idVehicule\": 1} }. " +
                             "Retourne 400 si dateFin est antérieure à dateDebut.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Maintenance créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides (date de fin antérieure ou véhicule manquant)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Maintenance create(@RequestBody Maintenance maintenance) {
        return maintenanceService.create(maintenance);
    }

    @Operation(summary = "Lister toutes les maintenances")
    @ApiResponse(responseCode = "200", description = "Liste des maintenances retournée")
    @GetMapping
    public List<Maintenance> findAll() {
        return maintenanceService.findAll();
    }

    @Operation(summary = "Consulter une maintenance par son identifiant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Maintenance trouvée"),
        @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    @GetMapping("/{id}")
    public Maintenance findById(
            @Parameter(description = "Identifiant de la maintenance") @PathVariable Long id) {
        return maintenanceService.findById(id);
    }

    @Operation(summary = "Modifier une maintenance")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Maintenance mise à jour"),
        @ApiResponse(responseCode = "404", description = "Maintenance introuvable"),
        @ApiResponse(responseCode = "400", description = "Données invalides (date de fin antérieure)")
    })
    @PutMapping("/{id}")
    public Maintenance update(
            @Parameter(description = "Identifiant de la maintenance") @PathVariable Long id,
            @RequestBody Maintenance maintenance) {
        return maintenanceService.update(id, maintenance);
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
