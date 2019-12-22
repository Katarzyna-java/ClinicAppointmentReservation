/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.exception;

import javax.persistence.NoResultException;
import pl.lodz.p.it.spjava.car.model.Specialization;

public class SpecializationException extends AppBaseException {

    static final public String KEY_SPECIALIZATION_SPECIALIZATION_CODE_EXISTS = "error.specialization.specializationCode.exist.problem";
    static final public String KEY_SPECIALIZATION_WRONG_STATE = "error.specialization.wrong.state.problem";
    static final public String KEY_SPECIALIZATION_WRONG_PASSWORD = "error.specialization.wrong.password.problem";
    static final public String KEY_NO_RESULT = "error.no.result.problem";
    static final public String KEY_NO_RESULT_SPECIALIZATION = "error.no.result.specialization.problem";
    static final public String KEY_SPECIALIZATION_ALREADY_ACTIVATED = "error.specialization.already.active.problem";
    static final public String KEY_SPECIALIZATION_ALREADY_DEACTIVATED = "error.specialization.already.deactive.problem";
    static final public String KEY_SPECIALIZATION_ALREADY_EDITED = "error.specialization.already.edited.problem";
    static final public String KEY_SPECIALIZATION_USED_IN_SPECIALIST = "error.unique.specialization.used.in.specialists.problem";

    private Specialization specialization;

    public Specialization getSpecialization() {
        return specialization;
    }

    private SpecializationException(String message, Specialization specialization) {
        super(message);
        this.specialization = specialization;
    }

    private SpecializationException(String message, Throwable cause, Specialization specialization) {
        super(message, cause);
        this.specialization = specialization;
    }

    private SpecializationException(String message) {
        super(message);
    }

    private SpecializationException(String message, Throwable cause) {
        super(message, cause);
    }

    static public SpecializationException createExceptionSpecializationCodeAlreadyExists(Throwable cause, Specialization specialization) {
        return new SpecializationException(KEY_SPECIALIZATION_SPECIALIZATION_CODE_EXISTS, cause, specialization);
    }

    static public SpecializationException createExceptionSpecializationInUseInSpecialist(Throwable cause, Specialization specialization) {
        return new SpecializationException(KEY_SPECIALIZATION_USED_IN_SPECIALIST, cause, specialization);
    }

    static public SpecializationException createExceptionWrongState(Specialization specialization) {
        return new SpecializationException(KEY_SPECIALIZATION_WRONG_STATE, specialization);
    }

    static public SpecializationException createExceptionWrongPassword(Specialization specialization) {
        return new SpecializationException(KEY_SPECIALIZATION_WRONG_PASSWORD, specialization);
    }

    public static AppBaseException createExceptionNoResult(NoResultException e) {
        return new AppBaseException(KEY_NO_RESULT, e);
    }

    public static SpecializationException createExceptionNoSpecializationFound(NoResultException e) {
        return new SpecializationException(KEY_NO_RESULT_SPECIALIZATION, e);
    }

    static public SpecializationException createExceptionSpecializationAlreadyActvivated(Specialization specialization) {
        return new SpecializationException(KEY_SPECIALIZATION_ALREADY_ACTIVATED, specialization);
    }

    static public SpecializationException createExceptionSpecializationAlreadyDeactvivated(Specialization specialization) {
        return new SpecializationException(KEY_SPECIALIZATION_ALREADY_DEACTIVATED, specialization);
    }

    static public SpecializationException createExceptionSpecializationEdited(Specialization specialization) {
        return new SpecializationException(KEY_SPECIALIZATION_ALREADY_EDITED, specialization);
    }

}
