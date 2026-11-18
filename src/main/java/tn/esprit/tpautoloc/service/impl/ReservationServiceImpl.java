package tn.esprit.tpautoloc.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IClientRepository;
import tn.esprit.tpautoloc.repository.IReservationRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;
import tn.esprit.tpautoloc.service.IReservationService;
import tn.esprit.tpautoloc.service.factory.TarificationStrategyFactory;
import tn.esprit.tpautoloc.service.observer.ReservationSubject;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;
import tn.esprit.tpautoloc.web.mapper.ReservationMapper;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final ReservationMapper reservationMapper;
    private final TarificationStrategyFactory tarificationStrategyFactory;
    private final ReservationSubject reservationSubject;

    @Override
    public ReservationResponseDTO creer(ReservationRequestDTO dto) {
        validerDates(dto);
        Reservation reservation = reservationMapper.toEntity(dto);
        reservation.setClient(resoudreClient(dto.getClientId()));
        reservation.setVehicule(resoudreVehicule(dto.getVehiculeId()));
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        Reservation saved = reservationRepository.save(reservation);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO findById(Long id) {
        return toResponse(getReservation(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAll() {
        return reservationRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ReservationResponseDTO update(Long id, ReservationRequestDTO dto) {
        validerDates(dto);
        Reservation existant = getReservation(id);
        existant.setDateDebut(dto.getDateDebut());
        existant.setDateFin(dto.getDateFin());
        existant.setClient(resoudreClient(dto.getClientId()));
        existant.setVehicule(resoudreVehicule(dto.getVehiculeId()));
        // le statut et le contrat ne font pas partie de ReservationRequestDTO : ils sont conservés
        return toResponse(reservationRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }

    // ===================== Atelier 6 : pattern Observer =====================

    /**
     * Crée une réservation et notifie tous les observateurs
     */
    @Override
    public Reservation creerReservation(Client client) {
        Reservation reservation = Reservation.builder()
                .client(client)
                .dateDebut(LocalDate.now(ZoneId.systemDefault()))
                .statut(StatutReservation.CONFIRMEE)
                .build();

        // Déclenche le process métier via le pattern Observer
        reservationSubject.notifierConfirmation(reservation);

        return reservation;
    }

    /**
     * Annule une réservation existante et notifie tous les observateurs
     */
    @Override
    public Reservation annulerReservation(Reservation reservation) {
        reservation.setStatut(StatutReservation.ANNULEE);
        log.info("Reservation #{} annulée pour le client {}",
                reservation.getIdReservation(), reservation.getClient().getEmail());

        reservationSubject.notifierAnnulation(reservation);

        return reservation;
    }

    // ========================================================================

    /**
     * Convertit l'entité en DTO de réponse ; tarifEstime (ignoré par le mapper)
     * est calculé avec la stratégie de tarification adaptée (Atelier 6).
     */
    private ReservationResponseDTO toResponse(Reservation reservation) {
        ReservationResponseDTO dto = reservationMapper.toDto(reservation);
        Vehicule vehicule = reservation.getVehicule();
        if (vehicule != null && vehicule.getTarifJournalier() != null
                && reservation.getDateDebut() != null && reservation.getDateFin() != null) {
            dto.setTarifEstime(tarificationStrategyFactory.resoudre(reservation)
                    .calculerTarif(vehicule, reservation));
        }
        return dto;
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    private void validerDates(ReservationRequestDTO dto) {
        if (dto.getDateDebut() == null || dto.getDateFin() == null) {
            throw new IllegalArgumentException("Les dates de début et de fin sont obligatoires");
        }
        if (dto.getDateFin().isBefore(dto.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }
    }

    private Client resoudreClient(Long clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("Le client de la réservation est obligatoire");
        }
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", clientId));
    }

    private Vehicule resoudreVehicule(Long vehiculeId) {
        if (vehiculeId == null) {
            throw new IllegalArgumentException("Le véhicule de la réservation est obligatoire");
        }
        return vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", vehiculeId));
    }
}
