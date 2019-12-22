/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.specialization;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.car.dto.SpecializationDTO;

import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "addSpecializationPageBean")
@RequestScoped
public class AddSpecializationPageBean implements Serializable {

    @Inject
    private SpecializationControllerBean specializationControllerBean;

    private SpecializationDTO specializationDTO;

    public AddSpecializationPageBean() {
    }

    public SpecializationDTO getSpecializationDTO() {
        return specializationDTO;
    }

    public void setSpecializationDTO(SpecializationDTO specializationDTO) {
        this.specializationDTO = specializationDTO;
    }

    @PostConstruct
    public void init() {
        specializationDTO = new SpecializationDTO();


    }

    public String addSpecializationAction() {
        try {
            specializationControllerBean.addSpecialization(specializationDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(AddSpecializationPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        return "listSpecializations";
    }

}
