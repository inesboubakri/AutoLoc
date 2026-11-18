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
import tn.esprit.tpautoloc.service.IMaintenanceService;
import tn.esprit.tpautoloc.web.dto.MaintenanceDTO;

@Tag(name = "Maintenances", description = "Gestion des maintenances")
@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private static final String ID_DESC = "Identifiant de la maintenance";
    private static final String NOT_FOUND = "Maintenance introuvable";
    private static final String INVALID = "Données invalides";

    private final IMaintenanceService maintenanceService;

    @Operation(summary = "Lister les maintenances")
    @ApiResponse(responseCode = "200", description = "Liste des maintenances")
    @GetMapping
    public ResponseEntity<List<MaintenanceDTO>> findAll() {
        return ResponseEntity.ok(maintenanceService.findAll());
    }

    @Operation(summary = "Consulter une maintenance")
    @ApiResponse(responseCode = "200", description = "Maintenance trouvée")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> findById(@Parameter(description = ID_DESC) @PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.findById(id));
    }

    @Operation(summary = "Créer une maintenance")
    @ApiResponse(responseCode = "201", description = "Maintenance créée")
    @ApiResponse(responseCode = "400", description = INVALID)
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    @PostMapping
    public ResponseEntity<MaintenanceDTO> creer(@RequestBody MaintenanceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(maintenanceService.creer(dto));
    }

    @Operation(summary = "Modifier une maintenance")
    @ApiResponse(responseCode = "200", description = "Maintenance modifiée")
    @ApiResponse(responseCode = "400", description = INVALID)
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> update(@Parameter(description = ID_DESC) @PathVariable Long id,
                                         @RequestBody MaintenanceDTO dto) {
        return ResponseEntity.ok(maintenanceService.update(id, dto));
    }

    @Operation(summary = "Supprimer une maintenance")
    @ApiResponse(responseCode = "204", description = "Maintenance supprimée")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = ID_DESC) @PathVariable Long id) {
        maintenanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
