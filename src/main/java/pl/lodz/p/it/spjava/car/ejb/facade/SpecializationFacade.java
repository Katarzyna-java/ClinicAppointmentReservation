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
import pl.lodz.p.it.spjava.car.exception.SpecializationException;
import pl.lodz.p.it.spjava.car.exception.AppBaseException;
import pl.lodz.p.it.spjava.car.model.Specialization;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class SpecializationFacade extends AbstractFacade<Specialization> {

    static final public String DB_UNIQUE_CONSTRAINT_SPECIALIZATION_CODE = "UNIQUE_SPECIALIZA";
    static final public String DB_FK_SPECIALIST_SPECIALIST_SPECIALIZATION = "SPCLISTSPCLIZATION";

    @PersistenceContext(unitName = "CAR_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SpecializationFacade() {
        super(Specialization.class);
    }

    @RolesAllowed({"Reception"})
    @Override
    public void create(Specialization entity) throws AppBaseException {
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
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_SPECIALIZATION_CODE)) {
                throw SpecializationException.createExceptionSpecializationCodeAlreadyExists(e, entity);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    @RolesAllowed({"Reception"})
    @Override
    public void edit(Specialization entity) throws AppBaseException {
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

    @RolesAllowed({"Reception"})
    @Override
    public void remove(Specialization entity) throws AppBaseException {
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
            if (cause instanceof DatabaseException && (cause.getMessage().contains(DB_FK_SPECIALIST_SPECIALIST_SPECIALIZATION))) {
                throw SpecializationException.createExceptionSpecializationInUseInSpecialist(e, entity);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    @RolesAllowed({"Reception", "Patient"})
    @Override
    public List<Specialization> findAll() throws AppBaseException {
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
    public Specialization findBySpecializationCode(String specializationCode) throws AppBaseException {
        TypedQuery<Specialization> tq = em.createNamedQuery("Specialization.findBySpecializationCode", Specialization.class);
        tq.setParameter("specializationCode", specializationCode);
        try {
            return tq.getSingleResult();
        } catch (NoResultException e) {
            throw SpecializationException.createExceptionNoSpecializationFound(e);
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
