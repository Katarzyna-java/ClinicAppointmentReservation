/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.specialization;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import pl.lodz.p.it.spjava.car.dto.SpecializationDTO;
import pl.lodz.p.it.spjava.car.ejb.endpoint.SpecializationEndpoint;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "specializationControllerBean")
@SessionScoped
public class SpecializationControllerBean implements Serializable {

    @EJB
    private SpecializationEndpoint specializationEndpoint;

    private int lastActionMethod = 0;

    private SpecializationDTO selectedSpecializationDTO;

    private List<SpecializationDTO> selectedSpecializationsListDTO;

    public SpecializationControllerBean() {
    }

    public SpecializationDTO getSelectedSpecializationDTO() {
        return selectedSpecializationDTO;
    }

    public void setSelectedSpecializationDTO(SpecializationDTO selectedSpecializationDTO) {
        this.selectedSpecializationDTO = selectedSpecializationDTO;
    }

    public void addSpecialization(final SpecializationDTO specializationDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = specializationDTO.hashCode() + 1;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = specializationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                specializationEndpoint.addSpecialization(specializationDTO);
                endpointCallCounter--;
            } while (specializationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("SpecializationsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public List<SpecializationDTO> listAllSpecializations() throws AppBaseException {
        int endpointCallCounter = specializationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedSpecializationsListDTO = specializationEndpoint.listAllSpecializations();
            endpointCallCounter--;
        } while (specializationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedSpecializationsListDTO;
    }

    public void deleteSpecialization(final SpecializationDTO specializationDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = specializationDTO.hashCode() + 2;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = specializationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                specializationEndpoint.deleteSpecialization(specializationDTO);
                endpointCallCounter--;
            } while (specializationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("SpecializationsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void editSpecialization(final SpecializationDTO specializationDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = specializationDTO.hashCode() + 4;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = specializationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                specializationEndpoint.editSpecialization(specializationDTO);
                endpointCallCounter--;
            } while (specializationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("SpecializationsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    void selectSpecializationForChange(SpecializationDTO specializationDTO) throws AppBaseException {
        int endpointCallCounter = specializationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedSpecializationDTO = specializationEndpoint.rememberSelectedSpecializationInState(specializationDTO);
            endpointCallCounter--;
        } while (specializationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
    }
}
