/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.web.reservation;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.car.dto.SpecialistDTO;
import pl.lodz.p.it.spjava.car.dto.SpecializationDTO;
import pl.lodz.p.it.spjava.car.dto.ReservationDTO;
import pl.lodz.p.it.spjava.car.ejb.endpoint.SpecialistEndpoint;
import pl.lodz.p.it.spjava.car.ejb.endpoint.SpecializationEndpoint;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.web.utils.ContextUtils;

@Named(value = "addReservationPageBean3")
@ViewScoped
public class AddReservationPageBean3 implements Serializable {

    @EJB
    private SpecializationEndpoint specializationEndpoint;
    @EJB
    private SpecialistEndpoint specialistEndpoint;

    @Inject
    private ReservationControllerBean reservationControllerBean;

    private SpecializationDTO specializationDTO;
    private SpecialistDTO specialistDTO;
    private ReservationDTO reservationDTO;

    private List<SpecializationDTO> listSpecializations;
    private List<SpecialistDTO> listSpecialists;

    public AddReservationPageBean3() {
    }

    public List<SpecializationDTO> getListSpecializations() {
        return listSpecializations;
    }

    public void setListSpecializations(List<SpecializationDTO> listSpecializations) {
        this.listSpecializations = listSpecializations;
    }

    public SpecializationDTO getSpecializationDTO() {
        return specializationDTO;
    }

    public void setSpecializationDTO(SpecializationDTO specializationDTO) {
        this.specializationDTO = specializationDTO;
    }

    public List<SpecialistDTO> getListSpecialists() {
        return listSpecialists;
    }

    public void setListSpecialists(List<SpecialistDTO> listSpecialists) {
        this.listSpecialists = listSpecialists;
    }

    public SpecialistDTO getSpecialistDTO() {
        return specialistDTO;
    }

    public void setSpecialistDTO(SpecialistDTO specialistDTO) {
        this.specialistDTO = specialistDTO;
    }

    public ReservationDTO getReservationDTO() {
        return reservationDTO;
    }

    public void setReservationDTO(ReservationDTO reservationDTO) {
        this.reservationDTO = reservationDTO;
    }

    @PostConstruct
    public void init() {
        reservationDTO = reservationControllerBean.getSelectedReservationForCalendarDisplayDTO();
        specialistDTO = reservationControllerBean.getSelectedSpecialistDTO();
        specializationDTO = reservationControllerBean.getSelectedSpecializationDTO();
    }

    public String addReservationAction() {

        LocalDateTime selectedDateTime = (reservationDTO.getReservationDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDate selectedDate = selectedDateTime.toLocalDate();

        if (selectedDateTime.isBefore(LocalDateTime.now())) {
            ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.add.reservation3.past.date");
            return null;
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
                return null;
            }
        }

        if (moveableHolidaysList.contains(selectedDate)) {
            ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.add.reservation3.holiday");
            return null;
        }

        if (selectedDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || selectedDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.add.reservation3.weekend");
            return null;
        }

        if (selectedDate.isBefore(LocalDate.now().plusDays(1))) {
            ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.add.reservation3.next.day.restriction");
            return null;
        }
        if (selectedDate.isAfter(LocalDate.now().plusMonths(3))) {
            ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.add.reservation3.three.months.restriction");
            return null;
        }
        if (selectedDate.equals(LocalDate.of(2020, Month.FEBRUARY, 20)) && selectedDateTime.getHour() == 9) {
            ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.houston.we.have.a.problem");
            return null;
        }

        if (selectedDateTime.getHour() < 8 || selectedDateTime.getHour() > 17) {
            ContextUtils.emitI18NMessage("AddReservationForm3:reservationDate", "error.add.reservation3.off.hours");
            return null;
        }

        try {
            reservationControllerBean.addReservation(reservationDTO, specialistDTO, specializationDTO);
            reservationControllerBean.setSelectedReservationForCalendarDisplayDTO(null);
        } catch (AppBaseException ex) {
            Logger.getLogger(AddReservationPageBean3.class.getName()).log(Level.SEVERE, null, ex);
            String messageLocation;
            switch (ex.getMessage()) {
                case "error.specialist.is.inactive.problem":
                case "error.specialist.was.edited.problem":
                case "error.no.result.specialist.problem":
                    messageLocation = "AddReservationForm3:specialist";
                    break;
                case "error.no.result.specialization.problem":
                case "error.specialization.already.edited.problem":
                    messageLocation = "AddReservationForm3:specialization";
                    break;
                case "error.reservation.outdated.problem":
                case "error.add.reservation3.past.date":
                case "error.add.reservation3.holiday":
                case "error.add.reservation3.weekend":
                case "error.add.reservation3.next.day.restriction":
                case "error.add.reservation3.three.months.restriction":
                case "error.houston.we.have.a.problem":
                case "error.add.reservation3.off.hours":
                    messageLocation = "AddReservationForm3:reservationDate";
                    break;
                default:
                    messageLocation = null;
                    break;
            }
            ContextUtils.emitI18NMessage(messageLocation, ex.getMessage());
            init();
            return null;
        }
        return "listMyCurrentReservations";
    }
}
