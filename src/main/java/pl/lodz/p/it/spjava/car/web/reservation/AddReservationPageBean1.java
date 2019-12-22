/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.reservation;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.car.dto.SpecializationDTO;
import pl.lodz.p.it.spjava.car.ejb.endpoint.SpecializationEndpoint;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "addReservationPageBean1")
@ViewScoped
public class AddReservationPageBean1 implements Serializable {

    @EJB
    private SpecializationEndpoint specializationEndpoint;

    @Inject
    private ReservationControllerBean reservationControllerBean;

    private SpecializationDTO specializationDTO;

    private List<SpecializationDTO> listSpecializations;

    public AddReservationPageBean1() {
    }

    public List<SpecializationDTO> getListSpecializations() {
        return listSpecializations;
    }

    public void setListSpecializations(List<SpecializationDTO> listSpecializations) {
        this.listSpecializations = listSpecializations;
    }

    public SpecializationDTO getSpecializationDTO() {
        return specializationDTO;
    }

    public void setSpecializationDTO(SpecializationDTO specializationDTO) {
        this.specializationDTO = specializationDTO;
    }

    @PostConstruct
    public void init() {
        try {
            listSpecializations = specializationEndpoint.listAllSpecializations();
        } catch (AppBaseException ex) {
            Logger.getLogger(AddReservationPageBean1.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        specializationDTO = new SpecializationDTO();
    }

    public String selectSpecializationAction() {

        Iterator<SpecializationDTO> iterator = listSpecializations.iterator();
        while (iterator.hasNext()) {
            SpecializationDTO richSpecializationDTO = iterator.next();
            if (richSpecializationDTO.getSpecializationCode().equals(specializationDTO.getSpecializationCode())) {
                specializationDTO = richSpecializationDTO;
            }
        }

        if (specializationDTO.getSpecializationCode() != null) {

            try {
                reservationControllerBean.selectSpecialization(specializationDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(AddReservationPageBean1.class.getName()).log(Level.SEVERE, null, ex);

                String messageLocation;
                switch (ex.getMessage()) {
                    case "error.no.result.specialization.problem":
                    case "error.specialization.already.edited.problem":
                        messageLocation = "AddReservationForm1:specialization";
                        break;
                    default:
                        messageLocation = null;
                        break;
                }
                ContextUtils.emitI18NMessage(messageLocation, ex.getMessage());
                init();
                return null;
            }
            return "addReservation2";
        }
        ContextUtils.emitI18NMessage("AddReservationForm1:specialization", "error.select.specialization.problem");
        init();
        return null;
    }

}
