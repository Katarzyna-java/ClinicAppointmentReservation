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
import pl.lodz.p.it.spjava.car.dto.SpecialistDTO;
import pl.lodz.p.it.spjava.car.dto.SpecializationDTO;
import pl.lodz.p.it.spjava.car.ejb.endpoint.SpecialistEndpoint;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "addReservationPageBean2")
@ViewScoped
public class AddReservationPageBean2 implements Serializable {

    @EJB
    private SpecialistEndpoint specialistEndpoint;

    @Inject
    private ReservationControllerBean reservationControllerBean;

    private SpecializationDTO specializationDTO;
    private SpecialistDTO specialistDTO;

    private List<SpecialistDTO> listSpecialists;

    public AddReservationPageBean2() {
    }

    public SpecializationDTO getSpecializationDTO() {
        return specializationDTO;
    }

    public void setSpecializationDTO(SpecializationDTO specializationDTO) {
        this.specializationDTO = specializationDTO;
    }

    public List<SpecialistDTO> getListSpecialists() {
        return listSpecialists;
    }

    public void setListSpecialists(List<SpecialistDTO> listSpecialists) {
        this.listSpecialists = listSpecialists;
    }

    public SpecialistDTO getSpecialistDTO() {
        return specialistDTO;
    }

    public void setSpecialistDTO(SpecialistDTO specialistDTO) {
        this.specialistDTO = specialistDTO;
    }

    @PostConstruct
    public void init() {
        try {
            specializationDTO = reservationControllerBean.getSelectedSpecializationDTO();
            listSpecialists = specialistEndpoint.listSpecificSpecialists(specializationDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(AddReservationPageBean2.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        specialistDTO = new SpecialistDTO();
    }

    public String selectSpecialistAction() {
        if (specialistDTO.getLicenseNumber() != null) {

            Iterator<SpecialistDTO> iterator = listSpecialists.iterator();
            while (iterator.hasNext()) {
                SpecialistDTO fullSpecialistDTO = iterator.next();
                if (fullSpecialistDTO.getLicenseNumber().equals(specialistDTO.getLicenseNumber())) {
                    specialistDTO = fullSpecialistDTO;
                }
            }
            try {
                reservationControllerBean.selectSpecialist(specialistDTO, specializationDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(AddReservationPageBean2.class.getName()).log(Level.SEVERE, null, ex);
                String messageLocation;
                switch (ex.getMessage()) {
                    case "error.specialist.is.inactive.problem":
                    case "error.specialist.was.edited.problem":
                    case "error.no.result.specialist.problem":
                        messageLocation = "AddReservationForm2:specialist";
                        break;
                    case "error.no.result.specialization.problem":
                    case "error.specialization.already.edited.problem":
                        messageLocation = "AddReservationForm2:specialization";
                        break;
                    default:
                        messageLocation = null;
                        break;
                }
                ContextUtils.emitI18NMessage(messageLocation, ex.getMessage());
                init();
                return null;
            }
            return "addReservation3";
        }
        init();
        ContextUtils.emitI18NMessage("AddReservationForm2:specialist", "specialist.cannot.be.empty");
        return null;
    }

}
