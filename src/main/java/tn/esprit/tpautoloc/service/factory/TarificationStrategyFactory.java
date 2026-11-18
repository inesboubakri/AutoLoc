package tn.esprit.tpautoloc.service.factory;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.service.strategy.ITarificationStrategy;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Selectionne dynamiquement la TarificationStrategy adaptee a une reservation
 * (duree du sejour). Toutes les implementations Spring de TarificationStrategy
 * sont injectees automatiquement (Map<code, strategy>).
 */
@Component
public class TarificationStrategyFactory {

    private static final long SEUIL_LONGUE_DUREE = 7;

    private final Map<String, ITarificationStrategy> strategies;

    public TarificationStrategyFactory(List<ITarificationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(ITarificationStrategy::getCode, Function.identity()));
    }

    public ITarificationStrategy resoudre(Reservation reservation) {
        long jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
        if (jours >= SEUIL_LONGUE_DUREE) {
            return strategies.get("LONGUE_DUREE");
        }
        if (estWeekend(reservation)) {
            return strategies.get("WEEKEND");
        }
        return strategies.get("STANDARD");
    }

    private boolean estWeekend(Reservation reservation) {
        DayOfWeek debut = reservation.getDateDebut().getDayOfWeek();
        DayOfWeek fin = reservation.getDateFin().getDayOfWeek();
        long jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
        return (DayOfWeek.FRIDAY.equals(debut) || DayOfWeek.SATURDAY.equals(debut))
                && (DayOfWeek.SUNDAY.equals(fin) || DayOfWeek.MONDAY.equals(fin))
                && jours <= 3;
    }
}
