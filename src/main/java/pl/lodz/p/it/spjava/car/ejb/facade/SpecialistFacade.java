/*
 * Projekt końcowy studiów podyplomowych:
 * "Nowoczesne aplikacje biznesowe Java EE" ed8
 */
package pl.lodz.p.it.spjava.car.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
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
import pl.lodz.p.it.spjava.car.exception.SpecialistException;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.model.Specialist;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class SpecialistFacade extends AbstractFacade<Specialist> {

    static final public String DB_UNIQUE_CONSTRAINT_LICENSE_NUMBER = "UNIQUE_LICENSE_NR";
    static final public String DB_FK_RESERVATION_RESERVED_SPECIALIST = "RSRVTNRSRVDSPCLIST";

    @PersistenceContext(unitName = "CAR_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SpecialistFacade() {
        super(Specialist.class);
    }

    @RolesAllowed({"Reception"})
    @Override
    public void create(Specialist entity) throws AppBaseException {
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
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_LICENSE_NUMBER)) {
                throw SpecialistException.createExceptionLicenseNumberAlreadyExists(e, entity);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    @RolesAllowed({"Reception"})
    @Override
    public void edit(Specialist entity) throws AppBaseException {
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
    public void remove(Specialist entity) throws AppBaseException {
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
            if (cause instanceof DatabaseException && (cause.getMessage().contains(DB_FK_RESERVATION_RESERVED_SPECIALIST))) {
                throw SpecialistException.createExceptionSpecialistAlreadyReserved(e, entity);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    @RolesAllowed({"Reception", "Patient"})
    @Override
    public List<Specialist> findAll() throws AppBaseException {
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
    public Specialist findByLicenseNumber(String licenseNumber) throws AppBaseException {
        TypedQuery<Specialist> tq = em.createNamedQuery("Specialist.findByLicenseNumber", Specialist.class);
        tq.setParameter("licenseNumber", licenseNumber);
        try {
            return tq.getSingleResult();
        } catch (NoResultException e) {
            throw SpecialistException.createExceptionNoSpecialistFound(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    public List<Specialist> findSpecificSpecialists(String specializationCode) throws AppBaseException {
        TypedQuery<Specialist> tq = em.createNamedQuery("Specialist.findBySpecializationCodeAndActive", Specialist.class);
        tq.setParameter("specializationCode", specializationCode);
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
    
    @RolesAllowed({"Reception", "Patient"})
    public List<Specialist> findBySpecializationCode(String specializationCode) throws AppBaseException {
        TypedQuery<Specialist> tq = em.createNamedQuery("Specialist.findBySpecializationCode", Specialist.class);
        tq.setParameter("specializationCode", specializationCode);
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
