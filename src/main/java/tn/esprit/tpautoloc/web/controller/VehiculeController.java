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
import tn.esprit.tpautoloc.service.IVehiculeService;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

@Tag(name = "Vehicules", description = "Gestion des véhicules")
@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private static final String ID_DESC = "Identifiant du véhicule";
    private static final String NOT_FOUND = "Véhicule introuvable";
    private static final String INVALID = "Données invalides";

    private final IVehiculeService vehiculeService;

    @Operation(summary = "Lister les véhicules")
    @ApiResponse(responseCode = "200", description = "Liste des véhicules")
    @GetMapping
    public ResponseEntity<List<VehiculeDTO>> findAll() {
        return ResponseEntity.ok(vehiculeService.findAll());
    }

    @Operation(summary = "Consulter un véhicule")
    @ApiResponse(responseCode = "200", description = "Véhicule trouvé")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> findById(@Parameter(description = ID_DESC) @PathVariable Long id) {
        return ResponseEntity.ok(vehiculeService.findById(id));
    }

    @Operation(summary = "Créer un véhicule")
    @ApiResponse(responseCode = "201", description = "Véhicule créé")
    @ApiResponse(responseCode = "400", description = INVALID)
    @ApiResponse(responseCode = "404", description = "Agence introuvable")
    @PostMapping
    public ResponseEntity<VehiculeDTO> creer(@RequestBody VehiculeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehiculeService.creer(dto));
    }

    @Operation(summary = "Modifier un véhicule")
    @ApiResponse(responseCode = "200", description = "Véhicule modifié")
    @ApiResponse(responseCode = "400", description = INVALID)
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @PutMapping("/{id}")
    public ResponseEntity<VehiculeDTO> update(@Parameter(description = ID_DESC) @PathVariable Long id,
                                         @RequestBody VehiculeDTO dto) {
        return ResponseEntity.ok(vehiculeService.update(id, dto));
    }

    @Operation(summary = "Supprimer un véhicule")
    @ApiResponse(responseCode = "204", description = "Véhicule supprimé")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = ID_DESC) @PathVariable Long id) {
        vehiculeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ===================== Partie 1 : affectation / désaffectation =====================

    @Operation(summary = "Affecter un véhicule à une agence")
    @ApiResponse(responseCode = "204", description = "Véhicule affecté")
    @ApiResponse(responseCode = "404", description = "Véhicule ou agence introuvable")
    @PutMapping("/{vehiculeId}/affecter-agence/{agenceId}")
    public ResponseEntity<Void> affecterVehiculeAAgence(@Parameter(description = ID_DESC) @PathVariable Long vehiculeId,
            @Parameter(description = "Identifiant de l'agence") @PathVariable Long agenceId) {
        vehiculeService.affecterVehiculeAAgence(vehiculeId, agenceId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Désaffecter un véhicule de son agence")
    @ApiResponse(responseCode = "204", description = "Véhicule désaffecté")
    @ApiResponse(responseCode = "400", description = "Le véhicule n'est rattaché à aucune agence")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @PutMapping("/{vehiculeId}/desaffecter-agence")
    public ResponseEntity<Void> desaffecterVehiculeDeAgence(@Parameter(description = ID_DESC) @PathVariable Long vehiculeId) {
        vehiculeService.desaffecterVehiculeDeAgence(vehiculeId);
        return ResponseEntity.noContent().build();
    }
}
