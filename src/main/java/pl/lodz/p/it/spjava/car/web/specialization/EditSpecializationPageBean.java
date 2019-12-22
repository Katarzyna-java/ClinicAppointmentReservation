/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.specialization;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.car.dto.SpecializationDTO;
import pl.lodz.p.it.spjava.car.ejb.endpoint.SpecializationEndpoint;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "editSpecializationPageBean")
@RequestScoped
public class EditSpecializationPageBean {

    @EJB
    private SpecializationEndpoint specializationEndpoint;

    @Inject
    private SpecializationControllerBean specializationControllerBean;

    private SpecializationDTO specializationDTO;

    private String specializationName;

    public EditSpecializationPageBean() {
    }

    public SpecializationDTO getSpecializationDTO() {
        return specializationDTO;
    }

    public void setSpecializationDTO(SpecializationDTO specializationDTO) {
        this.specializationDTO = specializationDTO;
    }

    public String getSpecializationName() {
        return specializationName;
    }

    public void setSpecializationName(String specializationName) {
        this.specializationName = specializationName;
    }

    @PostConstruct
    public void init() {
        specializationDTO = specializationControllerBean.getSelectedSpecializationDTO();
    }

    public String saveEditSpecializationAction() {
        try {
            specializationControllerBean.editSpecialization(specializationDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(EditSpecializationPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        return "listSpecializations";
    }

}
