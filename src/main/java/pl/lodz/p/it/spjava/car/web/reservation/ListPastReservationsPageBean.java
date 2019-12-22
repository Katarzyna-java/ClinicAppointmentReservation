/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.reservation;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.car.ejb.endpoint.ReservationEndpoint;
import pl.lodz.p.it.spjava.car.dto.ReservationDTO;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "listPastReservationsPageBean")
@ViewScoped
public class ListPastReservationsPageBean implements Serializable {

    @EJB
    private ReservationEndpoint reservationEndpoint;

    @Inject
    private ReservationControllerBean reservationControllerBean;

    private List<ReservationDTO> listPastReservationsDTO;

    private DataModel<ReservationDTO> dataModelReservations;

    public ListPastReservationsPageBean() {
    }

    public DataModel<ReservationDTO> getDataModelReservation() {
        return dataModelReservations;
    }

    public void setDataModelReservation(DataModel<ReservationDTO> dataModelReservation) {
        this.dataModelReservations = dataModelReservation;
    }

    public ReservationControllerBean getReservationControllerBean() {
        return reservationControllerBean;
    }

    public void setReservationControllerBean(ReservationControllerBean reservationControllerBean) {
        this.reservationControllerBean = reservationControllerBean;
    }

    @PostConstruct
    public void listPastReservation() {
        Date currentDate = new Date(System.currentTimeMillis());
        try {
            listPastReservationsDTO = reservationControllerBean.listPastReservations(currentDate);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListPastReservationsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelReservations = new ListDataModel<>(listPastReservationsDTO);
    }

    public String deleteReservationAction(ReservationDTO reservationDTO) {
        try {
            reservationControllerBean.deleteReservation(reservationDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListPastReservationsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());;
        }
        listPastReservation();
        return null;
    }

    public String deleteAllPastReservations() {
        Date currentDate = new Date(System.currentTimeMillis());
        try {
            reservationControllerBean.deleteAllPastReservations(currentDate);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListPastReservationsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());;
        }
        listPastReservation();
        return null;
    }

}
