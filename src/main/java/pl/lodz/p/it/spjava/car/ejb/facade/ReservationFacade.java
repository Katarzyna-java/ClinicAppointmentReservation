/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import org.eclipse.persistence.exceptions.DatabaseException;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.exception.ReservationException;
import pl.lodz.p.it.spjava.car.model.Reservation;
import pl.lodz.p.it.spjava.car.model.Specialist;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReservationFacade extends AbstractFacade<Reservation> {

    static final public String DB_UNIQUE_CONSTRAINT_SPECIALIST_AND_DATE = "UNIQUE_SPECIAL_DA"; 
    static final public String DB_UNIQUE_CONSTRAINT_PATIENT_AND_DATE = "UNIQUE_CREATOR_DA"; 


    @PersistenceContext(unitName = "CAR_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReservationFacade() {
        super(Reservation.class);
    }

    @RolesAllowed({"Patient"})
    @Override
    public void create(Reservation entity) throws AppBaseException {
        try {
            super.create(entity);
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_PATIENT_AND_DATE)) {
                throw ReservationException.createExceptionPatientAlreadyReservedOnThatDate(e, entity);
            }
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_SPECIALIST_AND_DATE)) {
                throw ReservationException.createExceptionSpecialistAlreadyReservedOnThatDate(e, entity);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    @RolesAllowed({"Patient"})
    @Override
    public void edit(Reservation entity) throws AppBaseException {
        try {
            super.edit(entity);
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        } catch (OptimisticLockException e) {
            throw AppBaseException.createExceptionOptimisticLock(e);
        } catch (PersistenceException e) {
            throw AppBaseException.createExceptionDatabaseQueryProblem(e);
        }
    }

    @RolesAllowed({"Reception", "Patient"})
    @Override
    public void remove(Reservation entity) throws AppBaseException {
        try {
            super.remove(entity);
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        } catch (OptimisticLockException e) {
            throw AppBaseException.createExceptionOptimisticLock(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            throw AppBaseException.createExceptionDatabaseQueryProblem(e);
        }
    }

    @RolesAllowed({"Patient"})
    @Override
    public List<Reservation> findAll() throws AppBaseException {
        try {
            return super.findAll();
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Reception", "Patient"})
    public Reservation findBySpecialistAndDate(Specialist reservedSpecialist, Date reservationDate) throws AppBaseException {
        TypedQuery<Reservation> tq = em.createNamedQuery("Reservation.findBySpecialistAndDate", Reservation.class);
        tq.setParameter("reservedSpecialist", reservedSpecialist);
        tq.setParameter("reservationDate", reservationDate);
        try {
            return tq.getSingleResult();
        } catch (NoResultException e) {
            throw ReservationException.createExceptionNoReservationFound(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }
    
    @RolesAllowed({"Reception", "Patient"})
    public List<Reservation> findByReservedSpecialistLicenseNumber(String licenseNumber) throws AppBaseException {
        TypedQuery<Reservation> tq = em.createNamedQuery("Reservation.findByReservedSpecialistLicenseNumber", Reservation.class);
        tq.setParameter("licenseNumber", licenseNumber);
        try {
            return tq.getResultList();
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }
    
    @RolesAllowed({"Reception"})
    public List<Reservation> findCurrentReservations(Date currentDate) throws AppBaseException {
        TypedQuery<Reservation> tq = em.createNamedQuery("Reservation.findCurrentReservations", Reservation.class);
        tq.setParameter("currentDate", currentDate);
        try {
            return tq.getResultList();
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }
    
    @RolesAllowed({"Reception"})
    public List<Reservation> findPastReservations(Date currentDate) throws AppBaseException {
        TypedQuery<Reservation> tq = em.createNamedQuery("Reservation.findPastReservations", Reservation.class);
        tq.setParameter("currentDate", currentDate);
        try {
            return tq.getResultList();
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }
    
    @RolesAllowed({"Patient"})
    public List<Reservation> findCurrentPatientReservations(String login, Date currentDate) throws AppBaseException {
        TypedQuery<Reservation> tq = em.createNamedQuery("Reservation.findCurrentPatientReservations", Reservation.class);
        tq.setParameter("login", login);
        tq.setParameter("currentDate", currentDate);
        try {
            return tq.getResultList();
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }
    
    @RolesAllowed({"Patient"})
    public List<Reservation> findPastPatientReservations(String login, Date currentDate) throws AppBaseException {
        TypedQuery<Reservation> tq = em.createNamedQuery("Reservation.findPastPatientReservations", Reservation.class);
        tq.setParameter("login", login);
        tq.setParameter("currentDate", currentDate);
        try {
            return tq.getResultList();
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }
    
    @RolesAllowed({"Reception"})
    public List<Reservation> findCurrentSpecialistReservations(String licenseNumber, Date currentDate) throws AppBaseException {
        TypedQuery<Reservation> tq = em.createNamedQuery("Reservation.findCurrentSpecialistReservations", Reservation.class);
        tq.setParameter("licenseNumber", licenseNumber);
        tq.setParameter("currentDate", currentDate);
        try {
            return tq.getResultList();
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }
    
    @RolesAllowed({"Reception"})
    public List<Reservation> findPastSpecialistReservations(String licenseNumber, Date currentDate) throws AppBaseException {
        TypedQuery<Reservation> tq = em.createNamedQuery("Reservation.findPastSpecialistReservations", Reservation.class);
        tq.setParameter("licenseNumber", licenseNumber);
        tq.setParameter("currentDate", currentDate);
        try {
            return tq.getResultList();
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }
   
}
