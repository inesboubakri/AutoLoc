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
import tn.esprit.tpautoloc.service.IEquipementService;
import tn.esprit.tpautoloc.web.dto.EquipementDTO;

@Tag(name = "Equipements", description = "Gestion des équipements")
@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
public class EquipementController {

    private static final String ID_DESC = "Identifiant de l'équipement";
    private static final String NOT_FOUND = "Équipement introuvable";
    private static final String INVALID = "Données invalides";

    private final IEquipementService equipementService;

    @Operation(summary = "Lister les équipements")
    @ApiResponse(responseCode = "200", description = "Liste des équipements")
    @GetMapping
    public ResponseEntity<List<EquipementDTO>> findAll() {
        return ResponseEntity.ok(equipementService.findAll());
    }

    @Operation(summary = "Consulter un équipement")
    @ApiResponse(responseCode = "200", description = "Équipement trouvé")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @GetMapping("/{id}")
    public ResponseEntity<EquipementDTO> findById(@Parameter(description = ID_DESC) @PathVariable Long id) {
        return ResponseEntity.ok(equipementService.findById(id));
    }

    @Operation(summary = "Créer un équipement")
    @ApiResponse(responseCode = "201", description = "Équipement créé")
    @ApiResponse(responseCode = "400", description = INVALID)
    @PostMapping
    public ResponseEntity<EquipementDTO> creer(@RequestBody EquipementDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(equipementService.creer(dto));
    }

    @Operation(summary = "Modifier un équipement")
    @ApiResponse(responseCode = "200", description = "Équipement modifié")
    @ApiResponse(responseCode = "400", description = INVALID)
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @PutMapping("/{id}")
    public ResponseEntity<EquipementDTO> update(@Parameter(description = ID_DESC) @PathVariable Long id,
                                         @RequestBody EquipementDTO dto) {
        return ResponseEntity.ok(equipementService.update(id, dto));
    }

    @Operation(summary = "Supprimer un équipement")
    @ApiResponse(responseCode = "204", description = "Équipement supprimé")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = ID_DESC) @PathVariable Long id) {
        equipementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
