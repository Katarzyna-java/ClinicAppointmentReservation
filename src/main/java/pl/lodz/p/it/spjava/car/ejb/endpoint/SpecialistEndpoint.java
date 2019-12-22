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
import pl.lodz.p.it.spjava.car.dto.ReservationDTO;
import pl.lodz.p.it.spjava.car.dto.SpecialistDTO;
import pl.lodz.p.it.spjava.car.dto.SpecializationDTO;
import pl.lodz.p.it.spjava.car.ejb.facade.AccountFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.SpecialistFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.PatientFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.ReceptionFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.ReservationFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.SpecializationFacade;
import pl.lodz.p.it.spjava.car.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.car.exception.SpecialistException;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.exception.SpecializationException;
import pl.lodz.p.it.spjava.car.model.Specialist;
import pl.lodz.p.it.spjava.car.model.Reception;
import pl.lodz.p.it.spjava.car.model.Specialization;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class SpecialistEndpoint extends AbstractEndpoint implements SessionSynchronization {

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

    @EJB
    private ReservationFacade reservationFacade;

    @Resource
    private SessionContext sessionContext;

    private Specialist specialistState;
    private Specialization specializationState;

    private List<Specialist> savedSpecialistStateList;

    public Specialist getSpecialistState() {
        return specialistState;
    }

    public void setSpecialistState(Specialist specialistState) {
        this.specialistState = specialistState;
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

    public Specialist selectSpecialistWithIterator(String licenseNumber, List<Specialist> specialists) {
        Iterator<Specialist> iterator = specialists.iterator();
        while (iterator.hasNext()) {
            Specialist specialist = iterator.next();
            if (specialist.getLicenseNumber().equals(licenseNumber)) {
                return specialist;
            }
        }
        return null;
    }

    @RolesAllowed({"Reception"})
    public void addSpecialist(SpecialistDTO specialistDTO, SpecializationDTO specializationDTO) throws AppBaseException {
        specializationState = specializationFacade.findBySpecializationCode(specializationDTO.getSpecializationCode());
        Specialist specialist = new Specialist();
        specialist.setSpecialization(specializationState);
        specialist.setSpecialistName(specialistDTO.getSpecialistName());
        specialist.setSpecialistSurname(specialistDTO.getSpecialistSurname());
        specialist.setLicenseNumber(specialistDTO.getLicenseNumber());

        specialist.setActive(true);
        specialist.setSpecialistCreator(loadCurrentReception());

        specialistFacade.create(specialist);
    }

    @RolesAllowed({"Reception", "Patient"})
    public List<SpecialistDTO> listAllSpecialists() throws AppBaseException {
        List<Specialist> listSpecialists = specialistFacade.findAll();
        savedSpecialistStateList = listSpecialists;
        List<SpecialistDTO> listAllSpecialists = new ArrayList<>();
        for (Specialist specialist : listSpecialists) {
            SpecialistDTO specialistDTO = new SpecialistDTO(
                    specialist.getSpecialization(),
                    specialist.getSpecialistName(),
                    specialist.getSpecialistSurname(),
                    specialist.getLicenseNumber(),
                    specialist.isActive(),
                    reservationFacade.findByReservedSpecialistLicenseNumber(specialist.getLicenseNumber()).isEmpty()
            );
            listAllSpecialists.add(specialistDTO);
        }
        return listAllSpecialists;
    }

    @RolesAllowed({"Patient"})
    public List<SpecialistDTO> listSpecificSpecialists(SpecializationDTO specializationDTO) throws AppBaseException {
        List<Specialist> listSpecialists = specialistFacade.findSpecificSpecialists(specializationDTO.getSpecializationCode());
        List<SpecialistDTO> listSpecificSpecialists = new ArrayList<>();
        for (Specialist specialist : listSpecialists) {
            SpecialistDTO specialistDTO = new SpecialistDTO(
                    specialist.getSpecialization(),
                    specialist.getSpecialistName(),
                    specialist.getSpecialistSurname(),
                    specialist.getLicenseNumber(),
                    specialist.isActive()
            );
            listSpecificSpecialists.add(specialistDTO);
        }
        return listSpecificSpecialists;
    }

    @RolesAllowed({"Reception", "Patient"})
    public List<SpecialistDTO> listSpecialistsFromChosenSpecialization() throws AppBaseException {
        List<Specialist> listSpecialists = specialistFacade.findAll();
        List<SpecialistDTO> listSpecialistsFromChosenSpecialization = new ArrayList<>();
        for (Specialist specialist : listSpecialists) {
            SpecialistDTO specialistDTO = new SpecialistDTO(
                    specialist.getSpecialization(),
                    specialist.getSpecialistName(),
                    specialist.getSpecialistSurname(),
                    specialist.getLicenseNumber(),
                    specialist.isActive()
            );
            listSpecialistsFromChosenSpecialization.add(specialistDTO);
        }
        return listSpecialistsFromChosenSpecialization;
    }

    @RolesAllowed({"Reception"})
    public void deleteSpecialist(SpecialistDTO specialistDTO) throws AppBaseException {
        Specialist specialist = selectSpecialistWithIterator(specialistDTO.getLicenseNumber(), savedSpecialistStateList);
        specialistFacade.findByLicenseNumber(specialistDTO.getLicenseNumber());
        specialistFacade.remove(specialist);
    }

    @RolesAllowed({"Reception"})
    public void activateSpecialist(SpecialistDTO specialistDTO) throws AppBaseException {
        Specialist specialist = selectSpecialistWithIterator(specialistDTO.getLicenseNumber(), savedSpecialistStateList);
        specialistFacade.findByLicenseNumber(specialist.getLicenseNumber());
        if (!specialist.isActive()) {
            specialist.setActive(true);
            specialist.setModifiedBy(loadCurrentReception());
            specialistFacade.edit(specialist);
        } else {
            throw SpecialistException.createExceptionSpecialistAlreadyActvivated(specialist);
        }
    }

    @RolesAllowed({"Reception"})
    public void deactivateSpecialist(SpecialistDTO specialistDTO) throws AppBaseException {
        Specialist specialist = selectSpecialistWithIterator(specialistDTO.getLicenseNumber(), savedSpecialistStateList);
        specialistFacade.findByLicenseNumber(specialistDTO.getLicenseNumber()); 
        if (specialist.isActive()) {
            specialist.setActive(false);
            specialist.setModifiedBy(loadCurrentReception());
            specialistFacade.edit(specialist);
        } else {
            throw SpecialistException.createExceptionSpecialistAlreadyDeactvivated(specialist);
        }
    }

    @RolesAllowed({"Reception"})
    public void editSpecialist(SpecialistDTO specialistDTO) throws AppBaseException {
        if (specialistState.getLicenseNumber().equals(specialistDTO.getLicenseNumber())) {
            specialistState.setSpecialization(specialistDTO.getSpecialization());
            specialistState.setSpecialistName(specialistDTO.getSpecialistName());
            specialistState.setSpecialistSurname(specialistDTO.getSpecialistSurname());
            specialistState.setModifiedBy(loadCurrentReception());
            specialistFacade.edit(specialistState);
        } else {
            specialistFacade.findByLicenseNumber(specialistDTO.getLicenseNumber());
            throw SpecialistException.createExceptionWrongState(specialistState);
        }

    }

    public SpecialistDTO rememberSelectedSpecialistInState(String licenseNumber) throws AppBaseException {
        specialistState = specialistFacade.findByLicenseNumber(licenseNumber);
        return new SpecialistDTO(
                specialistState.getSpecialization(),
                specialistState.getSpecialistName(),
                specialistState.getSpecialistSurname(),
                specialistState.getLicenseNumber());
    }

    @RolesAllowed({"Patient"})
    public ReservationDTO selectSpecialist(SpecialistDTO specialistDTO, SpecializationDTO specializationDTO) throws AppBaseException {
        Specialist specialist = specialistFacade.findByLicenseNumber(specialistDTO.getLicenseNumber());
        Specialization specialization = specializationFacade.findBySpecializationCode(specializationDTO.getSpecializationCode());
        if (!specialization.getSpecializationName().equals(specialistDTO.getSpecialization().getSpecializationName())) {
            throw SpecializationException.createExceptionSpecializationEdited(specialization);
        }
        if (specialist.isActive()) {
            if (specialist.getSpecialistName().equals(specialistDTO.getSpecialistName()) && specialist.getSpecialistSurname().equals(specialistDTO.getSpecialistSurname())) {
                return new ReservationDTO(specialist);
            } else {
                throw SpecialistException.createExceptionEditedSpecialist(specialist);
            }
        } else {
            throw SpecialistException.createExceptionInactiveSpecialist(specialist);
        }
    }

    @RolesAllowed({"Patient"})
    public SpecialistDTO selectSpecialistForReservation(SpecialistDTO specialistDTO, SpecializationDTO specializationDTO) throws AppBaseException {
        Specialization specialization = specializationFacade.findBySpecializationCode(specializationDTO.getSpecializationCode());
        Specialist specialist = specialistFacade.findByLicenseNumber(specialistDTO.getLicenseNumber());
        if (specialist.isActive()) {
            return new SpecialistDTO(
                    specialization,
                    specialist.getSpecialistName(),
                    specialist.getSpecialistSurname(),
                    specialist.getLicenseNumber());
        } else {
            throw SpecialistException.createExceptionInactiveSpecialist(specialist);
        }
    }

}
