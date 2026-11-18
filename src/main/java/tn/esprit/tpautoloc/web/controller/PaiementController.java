package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.service.IPaiementService;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;

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
    public ResponseEntity<List<PaiementDTO>> findAll() {
        return ResponseEntity.ok(paiementService.findAll());
    }

    @Operation(summary = "Consulter un paiement")
    @ApiResponse(responseCode = "200", description = "Paiement trouvé")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @GetMapping("/{id}")
    public ResponseEntity<PaiementDTO> findById(@Parameter(description = ID_DESC) @PathVariable Long id) {
        return ResponseEntity.ok(paiementService.findById(id));
    }

    @Operation(summary = "Créer un paiement rattaché à un contrat")
    @ApiResponse(responseCode = "201", description = "Paiement créé")
    @ApiResponse(responseCode = "400", description = INVALID)
    @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    @PostMapping
    public ResponseEntity<PaiementDTO> creer(@RequestBody PaiementDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paiementService.creer(dto));
    }

    @Operation(summary = "Modifier un paiement")
    @ApiResponse(responseCode = "200", description = "Paiement modifié")
    @ApiResponse(responseCode = "400", description = INVALID)
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @PutMapping("/{id}")
    public ResponseEntity<PaiementDTO> update(@Parameter(description = ID_DESC) @PathVariable Long id,
                                         @RequestBody PaiementDTO dto) {
        return ResponseEntity.ok(paiementService.update(id, dto));
    }

    @Operation(summary = "Supprimer un paiement")
    @ApiResponse(responseCode = "204", description = "Paiement supprimé")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = ID_DESC) @PathVariable Long id) {
        paiementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
