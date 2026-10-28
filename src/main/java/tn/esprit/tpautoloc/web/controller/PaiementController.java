package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.domain.Paiement;
import tn.esprit.tpautoloc.service.IPaiementService;

@Tag(name = "Paiements", description = "Gestion des paiements")
@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private static final String ID_DESC = "Identifiant du paiement";
    private static final String NOT_FOUND = "Paiement introuvable";
    private static final String INVALID = "Données invalides";

    private final IPaiementService paiementService;

    @Operation(summary = "Lister les paiements")
    @ApiResponse(responseCode = "200", description = "Liste des paiements")
    @GetMapping
    public List<Paiement> findAll() {
        return paiementService.findAll();
    }

    @Operation(summary = "Consulter un paiement")
    @ApiResponse(responseCode = "200", description = "Paiement trouvé")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @GetMapping("/{id}")
    public Paiement findById(@Parameter(description = ID_DESC) @PathVariable Long id) {
        return paiementService.findById(id);
    }

    @Operation(summary = "Créer un paiement rattaché à un contrat")
    @ApiResponse(responseCode = "201", description = "Paiement créé")
    @ApiResponse(responseCode = "400", description = INVALID)
    @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Paiement create(@Parameter(description = "Identifiant du contrat") @RequestParam Long contratId,
                           @RequestBody Paiement paiement) {
        return paiementService.create(contratId, paiement);
    }

    @Operation(summary = "Modifier un paiement")
    @ApiResponse(responseCode = "200", description = "Paiement modifié")
    @ApiResponse(responseCode = "400", description = INVALID)
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @PutMapping("/{id}")
    public Paiement update(@Parameter(description = ID_DESC) @PathVariable Long id,
                           @RequestBody Paiement paiement) {
        return paiementService.update(id, paiement);
    }

    @Operation(summary = "Supprimer un paiement")
    @ApiResponse(responseCode = "204", description = "Paiement supprimé")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = ID_DESC) @PathVariable Long id) {
        paiementService.deleteById(id);
    }
}