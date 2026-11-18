package tn.esprit.tpautoloc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.RoleEmploye;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.repository.IEmployeRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;
import tn.esprit.tpautoloc.service.IEmployeService;
import tn.esprit.tpautoloc.service.IVehiculeService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Atelier 7 — Partie 1.5 : contrôles et tests de l'affectation / désaffectation
 * d'un véhicule et d'un employé à une agence.
 */
@Slf4j
@SpringBootTest
class AffectationTest {

    private static final Long ID_INEXISTANT = 999_999L;

    private final IVehiculeService vehiculeService;
    private final IEmployeService employeService;
    private final IAgenceRepository agenceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IEmployeRepository employeRepository;

    @Autowired
    AffectationTest(IVehiculeService vehiculeService,
                    IEmployeService employeService,
                    IAgenceRepository agenceRepository,
                    IVehiculeRepository vehiculeRepository,
                    IEmployeRepository employeRepository) {
        this.vehiculeService = vehiculeService;
        this.employeService = employeService;
        this.agenceRepository = agenceRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.employeRepository = employeRepository;
    }

    private Agence agenceA;
    private Agence agenceB;
    private Vehicule vehicule;
    private Employe employe;

    @BeforeEach
    void setUp() {
        agenceA = agenceRepository.save(nouvelleAgence("Agence Test A", "Tunis"));
        agenceB = agenceRepository.save(nouvelleAgence("Agence Test B", "Sousse"));

        Vehicule v = new Vehicule();
        v.setImmatriculation("111 TUN 2222");
        v.setMarque("Kia");
        v.setModele("Picanto");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setTarifJournalier(new BigDecimal("70.00"));
        v.setStatut(StatutVehicule.DISPONIBLE);
        vehicule = vehiculeRepository.save(v); // sans agence au départ

        Employe e = new Employe();
        e.setNom("Mansour");
        e.setPrenom("Rim");
        e.setRole(RoleEmploye.AGENT);
        employe = employeRepository.save(e); // sans agence au départ
    }

    @AfterEach
    void tearDown() {
        vehiculeRepository.deleteById(vehicule.getIdVehicule());
        employeRepository.deleteById(employe.getIdEmploye());
        agenceRepository.deleteById(agenceA.getIdAgence());
        agenceRepository.deleteById(agenceB.getIdAgence());
    }

    // ============================== VÉHICULE ==============================

    @Test
    void affecterVehicule_agenceExistante() {
        vehiculeService.affecterVehiculeAAgence(vehicule.getIdVehicule(), agenceA.getIdAgence());

        assertEquals(agenceA.getIdAgence(), agenceDuVehiculeEnBase());
        log.info("Véhicule {} affecté à l'agence {}", vehicule.getIdVehicule(), agenceA.getIdAgence());
    }

    @Test
    void affecterVehicule_vehiculeInexistant() {
        Long agenceId = agenceA.getIdAgence();
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.affecterVehiculeAAgence(ID_INEXISTANT, agenceId));
        log.info("Erreur attendue : {}", ex.getMessage());
    }

    @Test
    void affecterVehicule_agenceInexistante_baseInchangee() {
        vehiculeService.affecterVehiculeAAgence(vehicule.getIdVehicule(), agenceA.getIdAgence());
        Long vehiculeId = vehicule.getIdVehicule();

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.affecterVehiculeAAgence(vehiculeId, ID_INEXISTANT));
        log.info("Erreur attendue : {}", ex.getMessage());

        // l'erreur ne laisse pas la base dans un état incohérent : toujours dans l'agence A
        assertEquals(agenceA.getIdAgence(), agenceDuVehiculeEnBase());
    }

    @Test
    void transfererVehicule_dUneAgenceAUneAutre() {
        vehiculeService.affecterVehiculeAAgence(vehicule.getIdVehicule(), agenceA.getIdAgence());
        vehiculeService.affecterVehiculeAAgence(vehicule.getIdVehicule(), agenceB.getIdAgence());

        assertEquals(agenceB.getIdAgence(), agenceDuVehiculeEnBase());
        log.info("Véhicule transféré de l'agence A vers l'agence B");
    }

    @Test
    void desaffecterVehicule_cleEtrangereANull() {
        vehiculeService.affecterVehiculeAAgence(vehicule.getIdVehicule(), agenceA.getIdAgence());

        vehiculeService.desaffecterVehiculeDeAgence(vehicule.getIdVehicule());

        // la clé étrangère id_agence est remise à NULL en base
        assertNull(agenceDuVehiculeEnBase());
        log.info("Véhicule désaffecté : id_agence = NULL");
    }

    @Test
    void desaffecterVehicule_nonRattache() {
        Long vehiculeId = vehicule.getIdVehicule();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> vehiculeService.desaffecterVehiculeDeAgence(vehiculeId));
        log.info("Erreur attendue : {}", ex.getMessage());
    }

    @Test
    void desaffecterVehicule_vehiculeInexistant() {
        assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.desaffecterVehiculeDeAgence(ID_INEXISTANT));
    }

    // ============================== EMPLOYÉ ==============================

    @Test
    void affecterEmploye_agenceExistante() {
        employeService.affecterEmployeAAgence(employe.getIdEmploye(), agenceA.getIdAgence());

        assertEquals(agenceA.getIdAgence(), agenceDeLEmployeEnBase());
        log.info("Employé {} affecté à l'agence {}", employe.getIdEmploye(), agenceA.getIdAgence());
    }

    @Test
    void affecterEmploye_employeInexistant() {
        Long agenceId = agenceA.getIdAgence();
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> employeService.affecterEmployeAAgence(ID_INEXISTANT, agenceId));
        log.info("Erreur attendue : {}", ex.getMessage());
    }

    @Test
    void affecterEmploye_agenceInexistante_baseInchangee() {
        employeService.affecterEmployeAAgence(employe.getIdEmploye(), agenceA.getIdAgence());
        Long employeId = employe.getIdEmploye();

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> employeService.affecterEmployeAAgence(employeId, ID_INEXISTANT));
        log.info("Erreur attendue : {}", ex.getMessage());

        assertEquals(agenceA.getIdAgence(), agenceDeLEmployeEnBase());
    }

    @Test
    void transfererEmploye_dUneAgenceAUneAutre() {
        employeService.affecterEmployeAAgence(employe.getIdEmploye(), agenceA.getIdAgence());
        employeService.affecterEmployeAAgence(employe.getIdEmploye(), agenceB.getIdAgence());

        assertEquals(agenceB.getIdAgence(), agenceDeLEmployeEnBase());
        log.info("Employé transféré de l'agence A vers l'agence B");
    }

    @Test
    void desaffecterEmploye_cleEtrangereANull() {
        employeService.affecterEmployeAAgence(employe.getIdEmploye(), agenceA.getIdAgence());

        employeService.desaffecterEmployeDeAgence(employe.getIdEmploye());

        assertNull(agenceDeLEmployeEnBase());
        log.info("Employé désaffecté : id_agence = NULL");
    }

    @Test
    void desaffecterEmploye_nonRattache() {
        Long employeId = employe.getIdEmploye();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> employeService.desaffecterEmployeDeAgence(employeId));
        log.info("Erreur attendue : {}", ex.getMessage());
    }

    // ============================== utilitaires ==============================

    /** Relit le véhicule en base et retourne l'id de son agence (clé étrangère). */
    private Long agenceDuVehiculeEnBase() {
        Agence agence = vehiculeRepository.findById(vehicule.getIdVehicule()).orElseThrow().getAgence();
        return agence == null ? null : agence.getIdAgence();
    }

    /** Relit l'employé en base et retourne l'id de son agence (clé étrangère). */
    private Long agenceDeLEmployeEnBase() {
        Agence agence = employeRepository.findById(employe.getIdEmploye()).orElseThrow().getAgence();
        return agence == null ? null : agence.getIdAgence();
    }

    private static Agence nouvelleAgence(String nom, String ville) {
        Agence agence = new Agence();
        agence.setNom(nom);
        agence.setVille(ville);
        return agence;
    }
}
