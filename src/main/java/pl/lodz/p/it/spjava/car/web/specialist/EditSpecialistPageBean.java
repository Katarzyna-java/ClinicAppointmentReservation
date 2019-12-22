/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.specialist;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.car.dto.SpecialistDTO;
import pl.lodz.p.it.spjava.car.ejb.endpoint.SpecialistEndpoint;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "editSpecialistPageBean")
@RequestScoped
public class EditSpecialistPageBean {

    @EJB
    private SpecialistEndpoint specialistEndpoint;

    @Inject
    private SpecialistControllerBean specialistControllerBean;

    private SpecialistDTO specialistDTO;

    public EditSpecialistPageBean() {
    }

    public SpecialistDTO getSpecialistDTO() {
        return specialistDTO;
    }

    public void setSpecialistDTO(SpecialistDTO specialistDTO) {
        this.specialistDTO = specialistDTO;
    }

    @PostConstruct
    public void init() {
        specialistDTO = specialistControllerBean.getSelectedSpecialistDTO();
    }

    public String saveEditSpecialistAction() {
        try {
            specialistControllerBean.editSpecialist(specialistDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(EditSpecialistPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            ContextUtils.getContext().getFlash().setKeepMessages(true);
        }
        return "listSpecialists";
    }

}
