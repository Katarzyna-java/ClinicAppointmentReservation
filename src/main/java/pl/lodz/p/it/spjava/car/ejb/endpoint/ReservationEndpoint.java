/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.ejb.endpoint;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import pl.lodz.p.it.spjava.car.ejb.facade.PatientFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.ReceptionFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.ReservationFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.SpecialistFacade;
import pl.lodz.p.it.spjava.car.ejb.facade.SpecializationFacade;
import pl.lodz.p.it.spjava.car.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.exception.ReservationException;
import pl.lodz.p.it.spjava.car.exception.SpecialistException;
import pl.lodz.p.it.spjava.car.exception.SpecializationException;
import pl.lodz.p.it.spjava.car.model.Patient;
import pl.lodz.p.it.spjava.car.model.Reservation;
import pl.lodz.p.it.spjava.car.model.Specialist;
import pl.lodz.p.it.spjava.car.model.Specialization;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class ReservationEndpoint extends AbstractEndpoint implements SessionSynchronization {

    @EJB
    private ReservationFacade reservationFacade;

    @EJB
    private ReceptionFacade receptionFacade;

    @EJB
    private PatientFacade patientFacade;

    @EJB
    private AccountFacade accountFacade;

    @EJB
    private SpecialistFacade specialistFacade;

    @EJB
    private SpecializationFacade specializationFacade;

    @Resource
    private SessionContext sessionContext;

    private Reservation reservationState;
    private Specialist specialistState;
    private Specialization specializationState;

    private List<Reservation> savedReservationStateList;

    public Reservation getReservationState() {
        return reservationState;
    }

    public void setReservationState(Reservation reservationState) {
        this.reservationState = reservationState;
    }

    private Patient loadCurrentPatient() throws AppBaseException {
        String patientLogin = sessionContext.getCallerPrincipal().getName();
        Patient patientAccount = patientFacade.findByLogin(patientLogin);
        if (patientAccount != null) {
            return patientAccount;
        } else {
            throw AppBaseException.createExceptionNotAuthorizedAction();
        }
    }

    public Reservation selectReservationWithIterator(Long id, List<Reservation> reservations) {
        Iterator<Reservation> iterator = reservations.iterator();
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            if (reservation.getId().equals(id)) {
                return reservation;
            }
        }
        return null;
    }

    @RolesAllowed({"Patient"})
    public void addReservation(ReservationDTO reservationDTO, SpecialistDTO specialistDTO, SpecializationDTO specializationDTO) throws AppBaseException {

        LocalDateTime selectedDateTime = (reservationDTO.getReservationDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDate selectedDate = selectedDateTime.toLocalDate();

        if (selectedDateTime.isBefore(LocalDateTime.now())) {
            ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.add.reservation3.past.date");
            throw ReservationException.createExceptionDateTimeProblemPastDate();
        }

       
        List<LocalDate> permanentHolidaysList = new ArrayList<>();
        permanentHolidaysList.add(LocalDate.of(2020, Month.JANUARY, 1)); 
        permanentHolidaysList.add(LocalDate.of(2020, Month.JANUARY, 6));
        permanentHolidaysList.add(LocalDate.of(2020, Month.MAY, 1)); 
        permanentHolidaysList.add(LocalDate.of(2020, Month.MAY, 3)); 
        permanentHolidaysList.add(LocalDate.of(2020, Month.AUGUST, 15)); 
        permanentHolidaysList.add(LocalDate.of(2020, Month.NOVEMBER, 1)); 
        permanentHolidaysList.add(LocalDate.of(2020, Month.NOVEMBER, 11)); 
        permanentHolidaysList.add(LocalDate.of(2020, Month.DECEMBER, 25)); 
        permanentHolidaysList.add(LocalDate.of(2020, Month.DECEMBER, 26)); 

        
        List<LocalDate> moveableHolidaysList = new ArrayList<>();
        moveableHolidaysList.add(LocalDate.of(2020, Month.APRIL, 12)); 
        moveableHolidaysList.add(LocalDate.of(2020, Month.APRIL, 13)); 
        moveableHolidaysList.add(LocalDate.of(2020, Month.MAY, 31)); 
        moveableHolidaysList.add(LocalDate.of(2020, Month.JUNE, 11)); 

        for (LocalDate permanentHoliday : permanentHolidaysList) {
            if ((permanentHoliday.getMonth().equals(selectedDate.getMonth())) && (permanentHoliday.getDayOfMonth() == selectedDate.getDayOfMonth())) {
                ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.add.reservation3.holiday");
                throw ReservationException.createExceptionDateTimeProblemHoliday();
            }
        }

        if (moveableHolidaysList.contains(selectedDate)) {
            ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.add.reservation3.holiday");
            throw ReservationException.createExceptionDateTimeProblemHoliday();
        }

        if (selectedDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || selectedDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.add.reservation3.weekend");
            throw ReservationException.createExceptionDateTimeProblemWeekend();
        }

        if (selectedDate.isBefore(LocalDate.now().plusDays(1))) {
            ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.add.reservation3.next.day.restriction");
            throw ReservationException.createExceptionDateTimeProblemNextDayRestriction();
        }
        if (selectedDate.isAfter(LocalDate.now().plusMonths(3))) {
            ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.add.reservation3.three.months.restriction.");
            throw ReservationException.createExceptionDateTimeProblemMaxThreeMonthsRestriction();
        }
        if (selectedDate.equals(LocalDate.of(2020, Month.FEBRUARY, 20)) && selectedDateTime.getHour() == 9) {
            ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.houston.we.have.a.problem");
            throw ReservationException.createExceptionDateTimeEasterEggBirthdaySurprise();
        }

        if (selectedDateTime.getHour() < 8 || selectedDateTime.getHour() > 17) {
            ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.add.reservation3.off.hours");
            throw ReservationException.createExceptionDateTimeProblemOffHours();
        }

        specialistState = specialistFacade.findByLicenseNumber(specialistDTO.getLicenseNumber());
        specializationState = specializationFacade.findBySpecializationCode(specializationDTO.getSpecializationCode());
        if (!specializationState.getSpecializationName().equals(specialistDTO.getSpecialization().getSpecializationName())) {
            throw SpecializationException.createExceptionSpecializationEdited(specializationState);
        }

        if (specialistState.isActive()) {
            if (specialistState.getSpecialistName().equals(specialistDTO.getSpecialistName()) && specialistState.getSpecialistSurname().equals(specialistDTO.getSpecialistSurname())) {

                Reservation reservation = new Reservation();
                reservation.setReservedSpecialist(specialistState);
                reservation.setReservationDate(reservationDTO.getReservationDate());
                reservation.setReservationCreator(loadCurrentPatient());
                reservationFacade.create(reservation);
            } else {
                throw SpecialistException.createExceptionEditedSpecialist(specialistState);
            }
        } else {
            throw SpecialistException.createExceptionInactiveSpecialist(specialistState);
        }
    }

    @RolesAllowed({"Reception"})
    public List<ReservationDTO> listCurrentReservations(Date currentDate) throws AppBaseException {
        List<Reservation> listCurrentReservations = reservationFacade.findCurrentReservations(currentDate);
        savedReservationStateList = listCurrentReservations;
        List<ReservationDTO> listCurrentReservationsDTO = new ArrayList<>();
        for (Reservation reservation : listCurrentReservations) {
            ReservationDTO reservationDTO = new ReservationDTO(
                    reservation.getReservationDate(),
                    reservation.getReservedSpecialist(),
                    reservation.getReservationCreator()
            );
            listCurrentReservationsDTO.add(reservationDTO);
        }
        Collections.sort(listCurrentReservationsDTO);
        return listCurrentReservationsDTO;
    }

    @RolesAllowed({"Reception"})
    public List<ReservationDTO> listPastReservations(Date currentDate) throws AppBaseException {
        List<Reservation> listPastReservations = reservationFacade.findPastReservations(currentDate);
        savedReservationStateList = listPastReservations;
        List<ReservationDTO> listPastReservationsDTO = new ArrayList<>();
        for (Reservation reservation : listPastReservations) {
            ReservationDTO reservationDTO = new ReservationDTO(
                    reservation.getReservationDate(),
                    reservation.getReservedSpecialist(),
                    reservation.getReservationCreator()
            );
            listPastReservationsDTO.add(reservationDTO);
        }
        Collections.sort(listPastReservationsDTO);
        return listPastReservationsDTO;
    }

    @RolesAllowed({"Patient"})
    public List<ReservationDTO> listMyCurrentReservations(Date currentDate) throws AppBaseException {
        List<Reservation> listMyCurrentReservations = reservationFacade.findCurrentPatientReservations(loadCurrentPatient().getLogin(), currentDate);
        savedReservationStateList = listMyCurrentReservations;
        List<ReservationDTO> listMyCurrentReservationsDTO = new ArrayList<>();
        for (Reservation reservation : listMyCurrentReservations) {
            ReservationDTO reservationDTO = new ReservationDTO(
                    reservation.getReservationDate(),
                    reservation.getReservedSpecialist(),
                    reservation.getReservationCreator()
            );
            listMyCurrentReservationsDTO.add(reservationDTO);
        }
        Collections.sort(listMyCurrentReservationsDTO);
        return listMyCurrentReservationsDTO;
    }

    @RolesAllowed({"Patient"})
    public List<ReservationDTO> listMyPastReservations(Date currentDate) throws AppBaseException {
        List<Reservation> listMyPastReservations = reservationFacade.findPastPatientReservations(loadCurrentPatient().getLogin(), currentDate);
        savedReservationStateList = listMyPastReservations;
        List<ReservationDTO> listMyPastReservationsDTO = new ArrayList<>();
        for (Reservation reservation : listMyPastReservations) {
            ReservationDTO reservationDTO = new ReservationDTO(
                    reservation.getReservationDate(),
                    reservation.getReservedSpecialist(),
                    reservation.getReservationCreator()
            );
            listMyPastReservationsDTO.add(reservationDTO);
        }
        Collections.sort(listMyPastReservationsDTO);
        return listMyPastReservationsDTO;
    }

    @RolesAllowed({"Reception"})
    public List<ReservationDTO> listSpecialistReservations(String licenseNumber) throws AppBaseException {
        List<Reservation> listSpecialistReservations = reservationFacade.findByReservedSpecialistLicenseNumber(licenseNumber);
        savedReservationStateList = listSpecialistReservations;
        List<ReservationDTO> listSpecialistReservationsDTO = new ArrayList<>();
        for (Reservation reservation : listSpecialistReservations) {
            ReservationDTO reservationDTO = new ReservationDTO(
                    reservation.getReservationDate(),
                    reservation.getReservedSpecialist(),
                    reservation.getReservationCreator()
            );
            listSpecialistReservationsDTO.add(reservationDTO);
        }
        Collections.sort(listSpecialistReservationsDTO);
        return listSpecialistReservationsDTO;
    }

    @RolesAllowed({"Reception"})
    public List<ReservationDTO> listCurrentSpecialistReservations(String licenseNumber, Date currentDate) throws AppBaseException {
        List<Reservation> listCurrentSpecialistReservations = reservationFacade.findCurrentSpecialistReservations(licenseNumber, currentDate);
        savedReservationStateList = listCurrentSpecialistReservations;
        List<ReservationDTO> listCurrentSpecialistReservationsDTO = new ArrayList<>();
        for (Reservation reservation : listCurrentSpecialistReservations) {
            ReservationDTO reservationDTO = new ReservationDTO(
                    reservation.getReservationDate(),
                    reservation.getReservedSpecialist(),
                    reservation.getReservationCreator()
            );
            listCurrentSpecialistReservationsDTO.add(reservationDTO);
        }
        Collections.sort(listCurrentSpecialistReservationsDTO);
        return listCurrentSpecialistReservationsDTO;
    }

    @RolesAllowed({"Reception"})
    public List<ReservationDTO> listPastSpecialistReservations(String licenseNumber, Date currentDate) throws AppBaseException {
        List<Reservation> listPastSpecialistReservations = reservationFacade.findPastSpecialistReservations(licenseNumber, currentDate);
        savedReservationStateList = listPastSpecialistReservations;
        List<ReservationDTO> listPastSpecialistReservationsDTO = new ArrayList<>();
        for (Reservation reservation : listPastSpecialistReservations) {
            ReservationDTO reservationDTO = new ReservationDTO(
                    reservation.getReservationDate(),
                    reservation.getReservedSpecialist(),
                    reservation.getReservationCreator()
            );
            listPastSpecialistReservationsDTO.add(reservationDTO);
        }
        Collections.sort(listPastSpecialistReservationsDTO);
        return listPastSpecialistReservationsDTO;
    }

    @RolesAllowed({"Reception"})
    public void deleteReservation(ReservationDTO reservationDTO) throws AppBaseException {
        Reservation reservation = reservationFacade.findBySpecialistAndDate(reservationDTO.getReservedSpecialist(), reservationDTO.getReservationDate());

        reservationFacade.remove(reservation);
    }

    @RolesAllowed({"Patient"})
    public void deleteMyReservation(ReservationDTO reservationDTO) throws AppBaseException {
        Reservation reservation = reservationFacade.findBySpecialistAndDate(reservationDTO.getReservedSpecialist(), reservationDTO.getReservationDate());
        Date currentDate = new Date(System.currentTimeMillis());
        if (reservationDTO.getReservationDate().before(currentDate)) {
            throw ReservationException.createExceptionReservationOutdated(reservation);
        }
        reservationFacade.remove(reservation);
    }

    @RolesAllowed({"Reception"})
    public void deleteAllPastReservations(Date currentDate) throws AppBaseException {
        List<Reservation> listAllPastReservations = reservationFacade.findPastReservations(currentDate);
          if (listAllPastReservations.isEmpty()) {
            throw ReservationException.createExceptionReservationEmptyList();
          }
        for (Reservation reservation : savedReservationStateList) {
            reservationFacade.remove(reservation);
        }
    }

    @RolesAllowed({"Reception"})
    public void deleteAllSpecialistReservations(String licenseNumber) throws AppBaseException {
        List<Reservation> listAllSpecialistReservations = reservationFacade.findByReservedSpecialistLicenseNumber(licenseNumber);
        if (listAllSpecialistReservations.isEmpty()) {
            throw ReservationException.createExceptionReservationEmptyList();
        }
        for (Reservation reservation : savedReservationStateList) {
            reservationFacade.remove(reservation);
        }
    }

    @RolesAllowed({"Patient"})
    public void editReservation(ReservationDTO reservationDTO) throws AppBaseException {
        if (reservationState.getReservationCreator().equals(reservationDTO.getReservationCreator()) && reservationState.getReservedSpecialist().equals(reservationDTO.getReservedSpecialist())) {
            reservationState.setReservationDate(reservationDTO.getReservationDate());
            Date currentDate = new Date(System.currentTimeMillis());
            if (reservationDTO.getReservationDate().before(currentDate)) {
                throw ReservationException.createExceptionReservationOutdated(reservationState);
            }
            reservationFacade.edit(reservationState);
        } else {
            throw ReservationException.createExceptionWrongState(reservationState);
        }
    }

    public ReservationDTO rememberSelectedReservationInState(ReservationDTO reservationDTO) throws AppBaseException {
        reservationState = reservationFacade.findBySpecialistAndDate(reservationDTO.getReservedSpecialist(), reservationDTO.getReservationDate());
        return new ReservationDTO(
                reservationState.getReservationDate(),
                reservationState.getReservedSpecialist(),
                reservationState.getReservationCreator()
        );
    }

}
