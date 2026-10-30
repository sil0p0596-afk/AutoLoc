package tn.esprit.autoloc.autolocapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.service.IPaiementService;
import tn.esprit.autoloc.autolocapi.web.dto.PaiementDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.PaiementMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
@Tag(name = "Paiements", description = "Consultation des paiements (lecture seule — créés via la cascade Contrat)")
public class PaiementController {

    private final IPaiementService paiementService;
    private final PaiementMapper paiementMapper;

    @Operation(summary = "Lister tous les paiements")
    @ApiResponse(responseCode = "200", description = "Liste des paiements retournée")
    @GetMapping
    public List<PaiementDTO> findAll() {
        return paiementService.findAll().stream()
                .map(paiementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter un paiement par son identifiant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paiement trouvé"),
        @ApiResponse(responseCode = "404", description = "Paiement introuvable")
    })
    @GetMapping("/{id}")
    public PaiementDTO findById(
            @Parameter(description = "Identifiant du paiement") @PathVariable Long id) {
        return paiementMapper.toDTO(paiementService.findById(id));
    }
}
