/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.exception;

import javax.persistence.NoResultException;
import pl.lodz.p.it.spjava.car.model.Specialist;

public class SpecialistException extends AppBaseException {

    static final public String KEY_SPECIALIST_LICENSE_NUMBER_EXISTS = "error.specialist.license.number.exist.problem";
    static final public String KEY_SPECIALIST_WRONG_STATE = "error.specialist.wrong.state.problem";
    static final public String KEY_SPECIALIST_WRONG_PASSWORD = "error.specialist.wrong.password.problem";
    static final public String KEY_NO_RESULT = "error.no.result.problem";
    static final public String KEY_NO_RESULT_SPECIALIST = "error.no.result.specialist.problem";
    static final public String KEY_SPECIALIST_ALREADY_ACTIVATED = "error.specialist.already.active.problem";
    static final public String KEY_SPECIALIST_ALREADY_DEACTIVATED = "error.specialist.already.deactive.problem";
    static final public String KEY_SPECIALIST_ALREADY_CHANGED = "error.specialist.already.changed.problem";
    static final public String KEY_SPECIALIST_SPECIALIST_ALREADY_RESERVED = "error.specialist.already.reserved.problem";
    static final public String KEY_SPECIALIST_IS_INACTIVE = "error.specialist.is.inactive.problem";
    static final public String KEY_SPECIALIST_WAS_EDITED = "error.specialist.was.edited.problem";

    private Specialist specialist;

    public Specialist getSpecialist() {
        return specialist;
    }

    private SpecialistException(String message, Specialist specialist) {
        super(message);
        this.specialist = specialist;
    }

    private SpecialistException(String message, Throwable cause, Specialist specialist) {
        super(message, cause);
        this.specialist = specialist;
    }

    private SpecialistException(String message) {
        super(message);
    }

    private SpecialistException(String message, Throwable cause) {
        super(message, cause);
    }

    static public SpecialistException createExceptionLicenseNumberAlreadyExists(Throwable cause, Specialist specialist) {
        return new SpecialistException(KEY_SPECIALIST_LICENSE_NUMBER_EXISTS, cause, specialist);
    }

    static public SpecialistException createExceptionSpecialistAlreadyReserved(Throwable cause, Specialist specialist) {
        return new SpecialistException(KEY_SPECIALIST_SPECIALIST_ALREADY_RESERVED, cause, specialist);
    }

    static public SpecialistException createExceptionWrongState(Specialist specialist) {
        return new SpecialistException(KEY_SPECIALIST_WRONG_STATE, specialist);
    }

    static public SpecialistException createExceptionInactiveSpecialist(Specialist specialist) {
        return new SpecialistException(KEY_SPECIALIST_IS_INACTIVE, specialist);
    }

    static public SpecialistException createExceptionEditedSpecialist(Specialist specialist) {
        return new SpecialistException(KEY_SPECIALIST_WAS_EDITED, specialist);
    }

    static public SpecialistException createExceptionWrongPassword(Specialist specialist) {
        return new SpecialistException(KEY_SPECIALIST_WRONG_PASSWORD, specialist);
    }

    public static AppBaseException createExceptionNoResult(NoResultException e) {
        return new AppBaseException(KEY_NO_RESULT, e);
    }

    public static SpecialistException createExceptionNoSpecialistFound(NoResultException e) {
        return new SpecialistException(KEY_NO_RESULT_SPECIALIST, e);
    }

    public static SpecialistException createExceptionSpecialistAlreadyActvivated(Specialist specialist) {
        return new SpecialistException(KEY_SPECIALIST_ALREADY_ACTIVATED, specialist);
    }

    public static SpecialistException createExceptionSpecialistAlreadyDeactvivated(Specialist specialist) {
        return new SpecialistException(KEY_SPECIALIST_ALREADY_DEACTIVATED, specialist);
    }

    static public SpecialistException ceateExceptionSpecialistChangedByAnotherAdmin(Specialist specialist) {
        return new SpecialistException(KEY_SPECIALIST_ALREADY_CHANGED, specialist);
    }

    static public SpecialistException ceateExceptionSpecialistAlreadyHaveThisAccessLevel(Specialist specialist) {
        return new SpecialistException(KEY_SPECIALIST_ALREADY_CHANGED, specialist);
    }

}
