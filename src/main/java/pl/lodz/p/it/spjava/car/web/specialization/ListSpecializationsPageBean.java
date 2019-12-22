/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.specialization;

import java.io.Serializable;
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
import pl.lodz.p.it.spjava.car.ejb.endpoint.SpecializationEndpoint;
import pl.lodz.p.it.spjava.car.dto.SpecializationDTO;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "listSpecializationsPageBean")
@ViewScoped
public class ListSpecializationsPageBean implements Serializable {

    @EJB
    private SpecializationEndpoint specializationEndpoint;

    @Inject
    private SpecializationControllerBean specializationControllerBean;

    private List<SpecializationDTO> listSpecializationsDTO;

    private DataModel<SpecializationDTO> dataModelSpecializations;

    public ListSpecializationsPageBean() {
    }

    public DataModel<SpecializationDTO> getDataModelSpecialization() {
        return dataModelSpecializations;
    }

    public void setDataModelSpecialization(DataModel<SpecializationDTO> dataModelSpecialization) {
        this.dataModelSpecializations = dataModelSpecialization;
    }

    public SpecializationControllerBean getSpecializationControllerBean() {
        return specializationControllerBean;
    }

    public void setSpecializationControllerBean(SpecializationControllerBean specializationControllerBean) {
        this.specializationControllerBean = specializationControllerBean;
    }

    @PostConstruct
    public void listAllSpecializations() {
        try {
            listSpecializationsDTO = specializationControllerBean.listAllSpecializations();
        } catch (AppBaseException ex) {
            Logger.getLogger(ListSpecializationsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelSpecializations = new ListDataModel<>(listSpecializationsDTO);
    }

    public String deleteSelectedSpecializationAction(SpecializationDTO specializationDTO) {
        try {
            specializationControllerBean.deleteSpecialization(specializationDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListSpecializationsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        listAllSpecializations();
        return null;
    }

    public String editSpecializationAction(SpecializationDTO specializationDTO) {
        try {
            specializationControllerBean.selectSpecializationForChange(specializationDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListSpecializationsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        return "editSpecialization";
    }
}
