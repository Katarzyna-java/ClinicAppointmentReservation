/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.specialist;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import pl.lodz.p.it.spjava.car.dto.SpecialistDTO;
import pl.lodz.p.it.spjava.car.dto.SpecializationDTO;
import pl.lodz.p.it.spjava.car.ejb.endpoint.SpecialistEndpoint;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "specialistControllerBean")
@SessionScoped
public class SpecialistControllerBean implements Serializable {

    @EJB
    private SpecialistEndpoint specialistEndpoint;

    private int lastActionMethod = 0;

    private SpecialistDTO selectedSpecialistDTO;

    private List<SpecialistDTO> selectedSpecialistsListDTO;

    public SpecialistControllerBean() {
    }

    public SpecialistDTO getSelectedSpecialistDTO() {
        return selectedSpecialistDTO;
    }

    public void setSelectedSpecialistDTO(SpecialistDTO selectedSpecialistDTO) {
        this.selectedSpecialistDTO = selectedSpecialistDTO;
    }

    public List<SpecialistDTO> getSelectedSpecialistsListDTO() {
        return selectedSpecialistsListDTO;
    }

    public void setSelectedSpecialistsListDTO(List<SpecialistDTO> selectedSpecialistsListDTO) {
        this.selectedSpecialistsListDTO = selectedSpecialistsListDTO;
    }

    public void addSpecialist(final SpecialistDTO specialistDTO, SpecializationDTO specializationDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = specialistDTO.hashCode() + 1;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = specialistEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                specialistEndpoint.addSpecialist(specialistDTO, specializationDTO);
                endpointCallCounter--;
            } while (specialistEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("SpecialistsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public List<SpecialistDTO> listAllSpecialists() throws AppBaseException {
        int endpointCallCounter = specialistEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedSpecialistsListDTO = specialistEndpoint.listAllSpecialists();
            endpointCallCounter--;
        } while (specialistEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedSpecialistsListDTO;
    }

    public void deleteSpecialist(final SpecialistDTO specialistDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = specialistDTO.hashCode() + 2;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = specialistEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                specialistEndpoint.deleteSpecialist(specialistDTO);
                endpointCallCounter--;
            } while (specialistEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("SpecialistsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void editSpecialist(final SpecialistDTO specialistDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = specialistDTO.hashCode() + 3;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = specialistEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                specialistEndpoint.editSpecialist(specialistDTO);
                endpointCallCounter--;
            } while (specialistEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("SpecialistsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void activateSpecialist(final SpecialistDTO specialistDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = specialistDTO.hashCode() + 4;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = specialistEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                specialistEndpoint.activateSpecialist(specialistDTO);
                endpointCallCounter--;
            } while (specialistEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("SpecialistsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deactivateSpecialist(final SpecialistDTO specialistDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = specialistDTO.hashCode() + 5;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = specialistEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                specialistEndpoint.deactivateSpecialist(specialistDTO);
                endpointCallCounter--;
            } while (specialistEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("SpecialistsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    void selectSpecialistForChange(SpecialistDTO specialistDTO) throws AppBaseException {
        int endpointCallCounter = specialistEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedSpecialistDTO = specialistEndpoint.rememberSelectedSpecialistInState(specialistDTO.getLicenseNumber());
            endpointCallCounter--;
        } while (specialistEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
    }

}
