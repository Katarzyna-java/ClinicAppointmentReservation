/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.reservation;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import pl.lodz.p.it.spjava.car.dto.ReservationDTO;
import pl.lodz.p.it.spjava.car.dto.SpecialistDTO;
import pl.lodz.p.it.spjava.car.dto.SpecializationDTO;
import pl.lodz.p.it.spjava.car.ejb.endpoint.ReservationEndpoint;
import pl.lodz.p.it.spjava.car.ejb.endpoint.SpecialistEndpoint;
import pl.lodz.p.it.spjava.car.ejb.endpoint.SpecializationEndpoint;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "reservationControllerBean")
@SessionScoped
public class ReservationControllerBean implements Serializable {

    @EJB
    private ReservationEndpoint reservationEndpoint;

    @EJB
    private SpecializationEndpoint specializationEndpoint;

    @EJB
    private SpecialistEndpoint specialistEndpoint;

    private int lastActionMethod = 0;

    private ReservationDTO selectedReservationDTO;

    private ReservationDTO selectedReservationForCalendarDisplayDTO;

    private SpecializationDTO selectedSpecializationDTO;

    private SpecialistDTO selectedSpecialistDTO;

    private List<ReservationDTO> selectedReservationsListDTO;

    public ReservationControllerBean() {
    }

    public ReservationDTO getSelectedReservationForCalendarDisplayDTO() {
        return selectedReservationForCalendarDisplayDTO;
    }

    public void setSelectedReservationForCalendarDisplayDTO(ReservationDTO selectedReservationForCalendarDisplayDTO) {
        this.selectedReservationForCalendarDisplayDTO = selectedReservationForCalendarDisplayDTO;
    }

    public ReservationDTO getSelectedReservationDTO() {
        return selectedReservationDTO;
    }

    public void setSelectedReservationDTO(ReservationDTO selectedReservationDTO) {
        this.selectedReservationDTO = selectedReservationDTO;
    }

    public SpecializationDTO getSelectedSpecializationDTO() {
        return selectedSpecializationDTO;
    }

    public void setSelectedSpecializationDTO(SpecializationDTO selectedSpecializationDTO) {
        this.selectedSpecializationDTO = selectedSpecializationDTO;
    }

    public SpecialistDTO getSelectedSpecialistDTO() {
        return selectedSpecialistDTO;
    }

    public void setSelectedSpecialistDTO(SpecialistDTO selectedSpecialistDTO) {
        this.selectedSpecialistDTO = selectedSpecialistDTO;
    }

    public void addReservation(final ReservationDTO reservationDTO, final SpecialistDTO specialistDTO, final SpecializationDTO specializationDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = reservationDTO.hashCode() + 1;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = reservationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                selectedSpecialistDTO = specialistEndpoint.selectSpecialistForReservation(specialistDTO, specializationDTO);
                reservationEndpoint.addReservation(reservationDTO, specialistDTO, specializationDTO);
                endpointCallCounter--;
            } while (reservationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("ReservationsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    
    public List<ReservationDTO> listCurrentReservations(Date currentDate) throws AppBaseException {
        int endpointCallCounter = reservationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedReservationsListDTO = reservationEndpoint.listCurrentReservations(currentDate);
            endpointCallCounter--;
        } while (reservationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedReservationsListDTO;
    }

    public List<ReservationDTO> listPastReservations(Date currentDate) throws AppBaseException {
        int endpointCallCounter = reservationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedReservationsListDTO = reservationEndpoint.listPastReservations(currentDate);
            endpointCallCounter--;
        } while (reservationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedReservationsListDTO;
    }

    public List<ReservationDTO> listMyCurrentReservations(Date currentDate) throws AppBaseException {
        int endpointCallCounter = reservationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedReservationsListDTO = reservationEndpoint.listMyCurrentReservations(currentDate);
            endpointCallCounter--;
        } while (reservationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedReservationsListDTO;
    }

    public List<ReservationDTO> listMyPastReservations(Date currentDate) throws AppBaseException {
        int endpointCallCounter = reservationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedReservationsListDTO = reservationEndpoint.listMyPastReservations(currentDate);
            endpointCallCounter--;
        } while (reservationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedReservationsListDTO;
    }

    public List<ReservationDTO> listSpecialistReservations(SpecialistDTO specialistDTO) throws AppBaseException {
        int endpointCallCounter = reservationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedReservationsListDTO = reservationEndpoint.listSpecialistReservations(specialistDTO.getLicenseNumber());
            endpointCallCounter--;
        } while (reservationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedReservationsListDTO;
    }

    public void deleteMyReservation(final ReservationDTO reservationDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = reservationDTO.hashCode() + 2;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = reservationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                reservationEndpoint.deleteMyReservation(reservationDTO);
                endpointCallCounter--;
            } while (reservationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("ReservationsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deleteAllPastReservations(final Date currentDate) throws AppBaseException {
        final int UNIQ_METHOD_ID = currentDate.hashCode() + 3;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = reservationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                reservationEndpoint.deleteAllPastReservations(currentDate);
                endpointCallCounter--;
            } while (reservationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("ReservationsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deleteAllSpecialistReservations(final SpecialistDTO specialistDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = specialistDTO.hashCode() + 4;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = reservationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                reservationEndpoint.deleteAllSpecialistReservations(specialistDTO.getLicenseNumber());
                endpointCallCounter--;
            } while (reservationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("ReservationsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deleteReservation(final ReservationDTO reservationDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = reservationDTO.hashCode() + 5;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = reservationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                reservationEndpoint.deleteReservation(reservationDTO);
                endpointCallCounter--;
            } while (reservationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("ReservationsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void editReservation(final ReservationDTO reservationDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = reservationDTO.hashCode() + 6;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = reservationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                reservationEndpoint.editReservation(reservationDTO);
                endpointCallCounter--;
            } while (reservationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("ReservationsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    void selectSpecialization(SpecializationDTO specializationDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = specializationDTO.hashCode() + 7;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = reservationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                selectedSpecializationDTO = specializationEndpoint.selectSpecialization(specializationDTO);
                endpointCallCounter--;
            } while (reservationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    void selectSpecialist(SpecialistDTO specialistDTO, SpecializationDTO specializationDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = specialistDTO.hashCode() + 8;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = reservationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                selectedSpecializationDTO = specializationEndpoint.rememberSelectedSpecializationInState(specializationDTO);
                selectedReservationForCalendarDisplayDTO = specialistEndpoint.selectSpecialist(specialistDTO, specializationDTO);
                selectedSpecialistDTO = specialistDTO;
                endpointCallCounter--;
            } while (reservationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    void selectReservationForChange(ReservationDTO reservationDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = reservationDTO.hashCode() + 9;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = reservationEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                selectedReservationDTO = reservationEndpoint.rememberSelectedReservationInState(reservationDTO);
                endpointCallCounter--;
            } while (reservationEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }
}
