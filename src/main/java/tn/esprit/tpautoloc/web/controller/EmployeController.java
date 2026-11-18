package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.service.IEmployeService;
import tn.esprit.tpautoloc.web.request.EmployeRequest;

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
    public List<Employe> findAll() {
        return employeService.findAll();
    }

    @Operation(summary = "Consulter un employé")
    @ApiResponse(responseCode = "200", description = "Employé trouvé")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @GetMapping("/{id}")
    public Employe findById(@Parameter(description = ID_DESC) @PathVariable Long id) {
        return employeService.findById(id);
    }

    @Operation(summary = "Créer un employé")
    @ApiResponse(responseCode = "201", description = "Employé créé")
    @ApiResponse(responseCode = "400", description = INVALID)
    @ApiResponse(responseCode = "404", description = "Agence introuvable")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employe create(@RequestBody EmployeRequest employe) {
        return employeService.create(employe.toEntity());
    }

    @Operation(summary = "Modifier un employé")
    @ApiResponse(responseCode = "200", description = "Employé modifié")
    @ApiResponse(responseCode = "400", description = INVALID)
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @PutMapping("/{id}")
    public Employe update(@Parameter(description = ID_DESC) @PathVariable Long id,
                          @RequestBody EmployeRequest employe) {
        return employeService.update(id, employe.toEntity());
    }

    @Operation(summary = "Supprimer un employé")
    @ApiResponse(responseCode = "204", description = "Employé supprimé")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = ID_DESC) @PathVariable Long id) {
        employeService.deleteById(id);
    }

    // ===================== Atelier 7 — Partie 1.4 : affectation / désaffectation =====================

    @Operation(summary = "Affecter un employé à une agence")
    @ApiResponse(responseCode = "204", description = "Employé affecté")
    @ApiResponse(responseCode = "404", description = "Employé ou agence introuvable")
    @PutMapping("/{employeId}/affecter-agence/{agenceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void affecterEmployeAAgence(@Parameter(description = ID_DESC) @PathVariable Long employeId,
            @Parameter(description = "Identifiant de l'agence") @PathVariable Long agenceId) {
        employeService.affecterEmployeAAgence(employeId, agenceId);
    }

    @Operation(summary = "Désaffecter un employé de son agence")
    @ApiResponse(responseCode = "204", description = "Employé désaffecté")
    @ApiResponse(responseCode = "400", description = "L'employé n'est rattaché à aucune agence")
    @ApiResponse(responseCode = "404", description = NOT_FOUND)
    @PutMapping("/{employeId}/desaffecter-agence")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desaffecterEmployeDeAgence(@Parameter(description = ID_DESC) @PathVariable Long employeId) {
        employeService.desaffecterEmployeDeAgence(employeId);
    }
}