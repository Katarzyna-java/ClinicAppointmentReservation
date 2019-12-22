/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.reservation;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.car.dto.ReservationDTO;
import pl.lodz.p.it.spjava.car.ejb.endpoint.ReservationEndpoint;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "editReservationPageBean")
@RequestScoped
public class EditReservationPageBean {

    @EJB
    private ReservationEndpoint reservationEndpoint;

    @Inject
    private ReservationControllerBean reservationControllerBean;

    private ReservationDTO reservationDTO;

    public ReservationDTO getReservationDTO() {
        return reservationDTO;
    }

    public void setReservationDTO(ReservationDTO reservationDTO) {
        this.reservationDTO = reservationDTO;
    }

    @PostConstruct
    public void init() {
        reservationDTO = reservationControllerBean.getSelectedReservationDTO();
    }

    public String saveEditedReservationAction() {
        try {
            reservationControllerBean.editReservation(reservationDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(EditReservationPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        return "listMyCurrentReservations";
    }

}
