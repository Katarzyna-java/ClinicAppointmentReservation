/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.specialist;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.car.dto.SpecialistDTO;
import pl.lodz.p.it.spjava.car.dto.SpecializationDTO;
import pl.lodz.p.it.spjava.car.ejb.endpoint.SpecializationEndpoint;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "addSpecialistPageBean")
@RequestScoped
public class AddSpecialistPageBean implements Serializable {

    @EJB
    private SpecializationEndpoint specializationEndpoint;

    @Inject
    private SpecialistControllerBean specialistControllerBean;

    private SpecialistDTO specialistDTO;

    private List<SpecializationDTO> listSpecializations;

    private SpecializationDTO specializationDTO;

    public AddSpecialistPageBean() {
    }

    public List<SpecializationDTO> getListSpecializations() {
        return listSpecializations;
    }

    public SpecializationDTO getSpecializationDTO() {
        return specializationDTO;
    }

    public void setSpecializationDTO(SpecializationDTO specializationDTO) {
        this.specializationDTO = specializationDTO;
    }

    public void setListSpecializations(List<SpecializationDTO> listSpecializations) {
        this.listSpecializations = listSpecializations;
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
            listSpecializations = specializationEndpoint.listAllSpecializations();
        } catch (AppBaseException ex) {
            Logger.getLogger(AddSpecialistPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        specialistDTO = new SpecialistDTO();
        specializationDTO = new SpecializationDTO();

    }

    public String addSpecialistAction() {
        if (specializationDTO.getSpecializationCode() == null) {
            ContextUtils.emitI18NMessage("AddSpecialistForm:specialization", "error.select.specialization.problem");
            return null;
        }
        try {
            specialistControllerBean.addSpecialist(specialistDTO, specializationDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(AddSpecialistPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        return "listSpecialists";
    }

}
