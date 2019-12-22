/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.ejb.endpoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.car.dto.SpecializationDTO;
import pl.lodz.p.it.spjava.car.ejb.facade.AccountFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.SpecializationFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.PatientFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.ReceptionFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.SpecialistFacade;
import pl.lodz.p.it.spjava.car.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.car.exception.SpecializationException;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.model.Specialization;
import pl.lodz.p.it.spjava.car.model.Reception;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class SpecializationEndpoint extends AbstractEndpoint implements SessionSynchronization {

    @EJB
    private ReceptionFacade receptionFacade;

    @EJB
    private PatientFacade patientFacade;

    @EJB
    private AccountFacade accountFacade;

    @EJB
    private SpecializationFacade specializationFacade;

    @EJB
    private SpecialistFacade specialistFacade;

    @Resource
    private SessionContext sessionContext;

    private Specialization specializationState;

    private List<Specialization> savedSpecializationStateList;

    public Specialization getSpecializationState() {
        return specializationState;
    }

    public void setSpecializationState(Specialization specializationState) {
        this.specializationState = specializationState;
    }

    private Reception loadCurrentReception() throws AppBaseException {
        String receptionLogin = sessionContext.getCallerPrincipal().getName();
        Reception receptionAccount = receptionFacade.findByLogin(receptionLogin);
        if (receptionAccount != null) {
            return receptionAccount;
        } else {
            throw AppBaseException.createExceptionNotAuthorizedAction();
        }
    }

    public Specialization selectSpecializationWithIterator(String specializationCode, List<Specialization> specializations) {
        Iterator<Specialization> iterator = specializations.iterator();
        while (iterator.hasNext()) {
            Specialization specialization = iterator.next();
            if (specialization.getSpecializationCode().equals(specializationCode)) {
                return specialization;
            }
        }
        return null;
    }

    @RolesAllowed({"Reception"})
    public void addSpecialization(SpecializationDTO specializationDTO) throws AppBaseException {
        Specialization specialization = new Specialization();
        specialization.setSpecializationCode(specializationDTO.getSpecializationCode());
        specialization.setSpecializationCreator(loadCurrentReception());
        specialization.setSpecializationName(specializationDTO.getSpecializationName());

        specializationFacade.create(specialization);
    }

    @RolesAllowed({"Reception", "Patient"})
    public List<SpecializationDTO> listAllSpecializations() throws AppBaseException {
        List<Specialization> listSpecializations = specializationFacade.findAll();
        savedSpecializationStateList = listSpecializations;
        List<SpecializationDTO> listAllSpecializations = new ArrayList<>();
        for (Specialization specialization : listSpecializations) {
            SpecializationDTO specializationDTO = new SpecializationDTO(
                    specialization.getSpecializationName(),
                    specialization.getSpecializationCode(),
                    specialistFacade.findBySpecializationCode(specialization.getSpecializationCode()).isEmpty()
            );
            listAllSpecializations.add(specializationDTO);
        }
        return listAllSpecializations;
    }

    @RolesAllowed({"Reception"})
    public void deleteSpecialization(SpecializationDTO specializationDTO) throws AppBaseException {
        Specialization specialization = selectSpecializationWithIterator(specializationDTO.getSpecializationCode(), savedSpecializationStateList);
        specializationFacade.findBySpecializationCode(specializationDTO.getSpecializationCode());
        specializationFacade.remove(specialization);
    }

    @RolesAllowed({"Patient"})
    public SpecializationDTO selectSpecialization(SpecializationDTO specializationDTO) throws AppBaseException {
        Specialization specialization = specializationFacade.findBySpecializationCode(specializationDTO.getSpecializationCode());
        if (!specialization.getSpecializationName().equals(specializationDTO.getSpecializationName())) {
            throw SpecializationException.createExceptionSpecializationEdited(specialization);
        }
        return new SpecializationDTO(
                specialization.getSpecializationCode(),
                specialization.getSpecializationName()
        );
    }

    @RolesAllowed({"Reception"})
    public void editSpecialization(SpecializationDTO specializationDTO) throws AppBaseException {
        if (specializationState.getSpecializationCode().equals(specializationDTO.getSpecializationCode())) {
            specializationState.setSpecializationName(specializationDTO.getSpecializationName());
            specializationState.setModifiedBy(loadCurrentReception());
            specializationFacade.edit(specializationState);
        } else {
            specializationFacade.findBySpecializationCode(specializationDTO.getSpecializationCode());
            throw SpecializationException.createExceptionWrongState(specializationState);
        }
    }

    public SpecializationDTO rememberSelectedSpecializationInState(SpecializationDTO specializationDTO) throws AppBaseException {
        specializationState = specializationFacade.findBySpecializationCode(specializationDTO.getSpecializationCode());
        return new SpecializationDTO(specializationState.getSpecializationCode(), specializationState.getSpecializationName());

    }

}
