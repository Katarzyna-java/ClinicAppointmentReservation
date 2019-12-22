/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.exception;

import javax.persistence.NoResultException;
import pl.lodz.p.it.spjava.car.model.Reservation;

public class ReservationException extends AppBaseException {

    static final public String KEY_PATIENT_AND_DATE_EXIST = "error.patient.and.date.exist.problem";
    static final public String KEY_SPECIALIST_AND_DATE_EXIST = "error.specialist.and.date.exist.problem";
    static final public String KEY_SPECIALIST_WRONG_STATE = "error.reservation.wrong.state.problem";
    static final public String KEY_SPECIALIST_WRONG_PASSWORD = "error.reservation.wrong.password.problem";
    static final public String KEY_NO_RESULT_RESERVATION = "error.no.result.reservation.problem";
    static final public String KEY_RESERVATION_OUTDATED = "error.reservation.outdated.problem";
    static final public String KEY_RESERVATION_DATETIME_PAST_DATE = "error.add.reservation3.past.date";
    static final public String KEY_RESERVATION_DATETIME_HOLIDAY = "error.add.reservation3.holiday";
    static final public String KEY_RESERVATION_DATETIME_WEEKEND = "error.add.reservation3.weekend";
    static final public String KEY_RESERVATION_DATETIME_NEXT_DAY = "error.add.reservation3.next.day.restriction";
    static final public String KEY_RESERVATION_DATETIME_3_MONTHS = "error.add.reservation3.three.months.restriction";
    static final public String KEY_RESERVATION_DATETIME_VERY_SPECIAL_DAY = "error.houston.we.have.a.problem";
    static final public String KEY_RESERVATION_DATETIME_OFF_HOURS = "error.add.reservation3.off.hours";
    static final public String KEY_RESERVATION_EMPTY_LIST = "error.reservation.empty.list.problem";

    private Reservation reservation;

    public Reservation getReservation() {
        return reservation;
    }

    private ReservationException(String message, Reservation reservation) {
        super(message);
        this.reservation = reservation;
    }

    private ReservationException(String message, Throwable cause, Reservation reservation) {
        super(message, cause);
        this.reservation = reservation;
    }

    private ReservationException(String message) {
        super(message);
    }

    private ReservationException(String message, Throwable cause) {
        super(message, cause);
    }

    static public ReservationException createExceptionPatientAlreadyReservedOnThatDate(Throwable cause, Reservation reservation) {
        return new ReservationException(KEY_PATIENT_AND_DATE_EXIST, cause, reservation);
    }

    static public ReservationException createExceptionSpecialistAlreadyReservedOnThatDate(Throwable cause, Reservation reservation) {
        return new ReservationException(KEY_SPECIALIST_AND_DATE_EXIST, cause, reservation);
    }

    static public ReservationException createExceptionWrongState(Reservation reservation) {
        return new ReservationException(KEY_SPECIALIST_WRONG_STATE, reservation);
    }

    static public ReservationException createExceptionWrongPassword(Reservation reservation) {
        return new ReservationException(KEY_SPECIALIST_WRONG_PASSWORD, reservation);
    }

    static public ReservationException createExceptionReservationOutdated(Reservation reservation) {
        return new ReservationException(KEY_RESERVATION_OUTDATED, reservation);
    }
    
    static public ReservationException createExceptionReservationEmptyList() {
        return new ReservationException(KEY_RESERVATION_EMPTY_LIST);
    }

    public static ReservationException createExceptionNoReservationFound(NoResultException e) {
        return new ReservationException(KEY_NO_RESULT_RESERVATION, e);
    }

    public static ReservationException createExceptionDateTimeProblemPastDate() {
        return new ReservationException(KEY_RESERVATION_DATETIME_PAST_DATE);
    }

    public static ReservationException createExceptionDateTimeProblemHoliday() {
        return new ReservationException(KEY_RESERVATION_DATETIME_HOLIDAY);
    }

    public static ReservationException createExceptionDateTimeProblemWeekend() {
        return new ReservationException(KEY_RESERVATION_DATETIME_WEEKEND);
    }

    public static ReservationException createExceptionDateTimeProblemNextDayRestriction() {
        return new ReservationException(KEY_RESERVATION_DATETIME_NEXT_DAY);
    }

    public static ReservationException createExceptionDateTimeProblemMaxThreeMonthsRestriction() {
        return new ReservationException(KEY_RESERVATION_DATETIME_3_MONTHS);
    }

    public static ReservationException createExceptionDateTimeEasterEggBirthdaySurprise() {
        return new ReservationException(KEY_RESERVATION_DATETIME_VERY_SPECIAL_DAY);
    }

    public static ReservationException createExceptionDateTimeProblemOffHours() {
        return new ReservationException(KEY_RESERVATION_DATETIME_OFF_HOURS);
    }

}
