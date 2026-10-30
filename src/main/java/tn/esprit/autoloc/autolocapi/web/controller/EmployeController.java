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
import tn.esprit.autoloc.autolocapi.service.IEmployeService;
import tn.esprit.autoloc.autolocapi.web.dto.EmployeDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.EmployeMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
@Tag(name = "Employés", description = "Gestion des employés")
public class EmployeController {

    private final IEmployeService employeService;
    private final EmployeMapper employeMapper;

    @Operation(summary = "Créer un employé")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Employé créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeDTO create(@Valid @RequestBody EmployeDTO dto) {
        return employeMapper.toDTO(employeService.create(employeMapper.toEntity(dto)));
    }

    @Operation(summary = "Lister tous les employés")
    @ApiResponse(responseCode = "200", description = "Liste des employés retournée")
    @GetMapping
    public List<EmployeDTO> findAll() {
        return employeService.findAll().stream()
                .map(employeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter un employé par son identifiant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employé trouvé"),
        @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    @GetMapping("/{id}")
    public EmployeDTO findById(
            @Parameter(description = "Identifiant de l'employé") @PathVariable Long id) {
        return employeMapper.toDTO(employeService.findById(id));
    }

    @Operation(summary = "Modifier un employé")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employé mis à jour"),
        @ApiResponse(responseCode = "404", description = "Employé introuvable"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/{id}")
    public EmployeDTO update(
            @Parameter(description = "Identifiant de l'employé") @PathVariable Long id,
            @Valid @RequestBody EmployeDTO dto) {
        return employeMapper.toDTO(employeService.update(id, employeMapper.toEntity(dto)));
    }

    @Operation(summary = "Supprimer un employé")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Employé supprimé"),
        @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de l'employé") @PathVariable Long id) {
        employeService.deleteById(id);
    }
}
