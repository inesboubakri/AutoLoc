package tn.esprit.tpautoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Contrat;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IReservationRepository;
import tn.esprit.tpautoloc.service.IClientService;
import tn.esprit.tpautoloc.service.IContratService;
import tn.esprit.tpautoloc.service.IReservationService;
import tn.esprit.tpautoloc.service.IVehiculeService;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IClientService clientService;
    private final IVehiculeService vehiculeService;
    private final IContratService contratService;

    @Override
    public Reservation create(Reservation reservation) {
        if (reservation.getIdReservation() != null) {
            throw new IllegalArgumentException("Une nouvelle réservation ne doit pas avoir d'identifiant");
        }
        validerDates(reservation);
        reservation.setClient(resoudreClient(reservation));
        reservation.setVehicule(resoudreVehicule(reservation));
        reservation.setContrat(resoudreContrat(reservation));
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation update(Long id, Reservation reservation) {
        validerDates(reservation);
        Reservation existant = findById(id);
        existant.setDateDebut(reservation.getDateDebut());
        existant.setDateFin(reservation.getDateFin());
        existant.setStatut(reservation.getStatut());
        existant.setClient(resoudreClient(reservation));
        existant.setVehicule(resoudreVehicule(reservation));
        existant.setContrat(resoudreContrat(reservation));
        return reservationRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }

    private void validerDates(Reservation r) {
        if (r.getDateDebut() == null || r.getDateFin() == null) {
            throw new IllegalArgumentException("Les dates de début et de fin sont obligatoires");
        }
        if (r.getDateFin().isBefore(r.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }
    }

    private Client resoudreClient(Reservation r) {
        if (r.getClient() == null || r.getClient().getIdClient() == null) {
            throw new IllegalArgumentException("Le client de la réservation est obligatoire");
        }
        return clientService.findById(r.getClient().getIdClient());
    }

    private Vehicule resoudreVehicule(Reservation r) {
        if (r.getVehicule() == null || r.getVehicule().getIdVehicule() == null) {
            throw new IllegalArgumentException("Le véhicule de la réservation est obligatoire");
        }
        return vehiculeService.findById(r.getVehicule().getIdVehicule());
    }

    private Contrat resoudreContrat(Reservation r) {
        if (r.getContrat() == null || r.getContrat().getIdContrat() == null) {
            return null; // le contrat est optionnel
        }
        return contratService.findById(r.getContrat().getIdContrat());
    }
}