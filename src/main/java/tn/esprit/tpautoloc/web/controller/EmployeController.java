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
import tn.esprit.tpautoloc.service.IEmployeService;
import tn.esprit.tpautoloc.web.dto.EmployeDTO;

@Tag(name = "Employes", description = "Gestion des employés")
@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
public class EmployeController {

    private static final String ID_DESC = "Identifiant de l'employé";
    private static final String NOT_FOUND = "Employé introuvable";
    private static final String INVALID = "Données invalides";

    private final IEmployeService employeService;

    @Operation(summary = "Lister les employés")
    @ApiResponse(responseCode = "200", description = "Liste des employés")
    @GetMapping
    public ResponseEntity<List<EmployeDTO>> findAll() {
        return ResponseEntity.ok(employeService.findAll());
    }

    @Operation(summary = "Consulter un employé")
    @ApiResponse(responseCode = "200", description = "Employé trouvé")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @GetMapping("/{id}")
    public ResponseEntity<EmployeDTO> findById(@Parameter(description = ID_DESC) @PathVariable Long id) {
        return ResponseEntity.ok(employeService.findById(id));
    }

    @Operation(summary = "Créer un employé")
    @ApiResponse(responseCode = "201", description = "Employé créé")
    @ApiResponse(responseCode = "400", description = INVALID)
    @ApiResponse(responseCode = "404", description = "Agence introuvable")
    @PostMapping
    public ResponseEntity<EmployeDTO> creer(@RequestBody EmployeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeService.creer(dto));
    }

    @Operation(summary = "Modifier un employé")
    @ApiResponse(responseCode = "200", description = "Employé modifié")
    @ApiResponse(responseCode = "400", description = INVALID)
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @PutMapping("/{id}")
    public ResponseEntity<EmployeDTO> update(@Parameter(description = ID_DESC) @PathVariable Long id,
                                         @RequestBody EmployeDTO dto) {
        return ResponseEntity.ok(employeService.update(id, dto));
    }

    @Operation(summary = "Supprimer un employé")
    @ApiResponse(responseCode = "204", description = "Employé supprimé")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = ID_DESC) @PathVariable Long id) {
        employeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ===================== Partie 1 : affectation / désaffectation =====================

    @Operation(summary = "Affecter un employé à une agence")
    @ApiResponse(responseCode = "204", description = "Employé affecté")
    @ApiResponse(responseCode = "404", description = "Employé ou agence introuvable")
    @PutMapping("/{employeId}/affecter-agence/{agenceId}")
    public ResponseEntity<Void> affecterEmployeAAgence(@Parameter(description = ID_DESC) @PathVariable Long employeId,
            @Parameter(description = "Identifiant de l'agence") @PathVariable Long agenceId) {
        employeService.affecterEmployeAAgence(employeId, agenceId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Désaffecter un employé de son agence")
    @ApiResponse(responseCode = "204", description = "Employé désaffecté")
    @ApiResponse(responseCode = "400", description = "L'employé n'est rattaché à aucune agence")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @PutMapping("/{employeId}/desaffecter-agence")
    public ResponseEntity<Void> desaffecterEmployeDeAgence(@Parameter(description = ID_DESC) @PathVariable Long employeId) {
        employeService.desaffecterEmployeDeAgence(employeId);
        return ResponseEntity.noContent().build();
    }
}
