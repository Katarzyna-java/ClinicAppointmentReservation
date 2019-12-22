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
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.car.ejb.endpoint.SpecialistEndpoint; 
import pl.lodz.p.it.spjava.car.dto.SpecialistDTO;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "listSpecialistsPageBean")
@ViewScoped
public class ListSpecialistPageBean implements Serializable {

    @EJB
    private SpecialistEndpoint specialistEndpoint;

    @Inject
    private SpecialistControllerBean specialistControllerBean;

    private List<SpecialistDTO> listSpecialistsDTO;

    private DataModel<SpecialistDTO> dataModelSpecialists;

    public ListSpecialistPageBean() {
    }

    public DataModel<SpecialistDTO> getDataModelSpecialist() {
        return dataModelSpecialists;
    }

    public void setDataModelSpecialist(DataModel<SpecialistDTO> dataModelSpecialist) {
        this.dataModelSpecialists = dataModelSpecialist;
    }

    public SpecialistControllerBean getSpecialistControllerBean() {
        return specialistControllerBean;
    }

    public void setSpecialistControllerBean(SpecialistControllerBean specialistControllerBean) {
        this.specialistControllerBean = specialistControllerBean;
    }

    @PostConstruct
    public void listAllSpecialist() {
        try {
            listSpecialistsDTO = specialistControllerBean.listAllSpecialists();
        } catch (AppBaseException ex) {
            Logger.getLogger(ListSpecialistPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelSpecialists = new ListDataModel<>(listSpecialistsDTO);
    }

    public String deleteSelectedSpecialistAction(SpecialistDTO specialistDTO) {
        try {
            specialistControllerBean.deleteSpecialist(specialistDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListSpecialistPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        listAllSpecialist();
        return null;
    }

    public String activateSpecialistAction(SpecialistDTO specialistDTO) {
        try {
            specialistControllerBean.activateSpecialist(specialistDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListSpecialistPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        listAllSpecialist();
        return null;
    }

    public String deactivateSpecialistAction(SpecialistDTO specialistDTO) {
        try {
            specialistControllerBean.deactivateSpecialist(specialistDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListSpecialistPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        listAllSpecialist();
        return null;
    }

    public String editSpecialistAction(SpecialistDTO specialistDTO) {
        try {
            specialistControllerBean.selectSpecialistForChange(specialistDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListSpecialistPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        return "editSpecialist";
    }

    public String listSpecialistReservations(SpecialistDTO specialistDTO) {
        try {
            specialistControllerBean.setSelectedSpecialistsListDTO(null); 
            specialistControllerBean.selectSpecialistForChange(specialistDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListSpecialistPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            listAllSpecialist();
            return null;
        }
        return "listSpecialistReservations";
    }
}
