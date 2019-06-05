package au.com.mongodb.persistence;

import au.com.mongodb.constants.Constant;
import au.com.mongodb.model.JSONSchemaModel;
import au.com.mongodb.persistence.entities.DataEntity;
import au.com.mongodb.persistence.entities.SchemaEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class NoSQLCRUDMaster {

    private final String PU = "NoSQLPersistenceUnit";

    private EntityManagerFactory emf;
    private EntityManager em;


    /**
     * Create the Entity Manager from the Persistence Unit
     */
    public NoSQLCRUDMaster() {
        emf = Persistence.createEntityManagerFactory(PU);
        em = emf.createEntityManager();
    }



    /**
     * searchById
     *
     * @param searchField
     * @param searchValue
     * @param namedQuery
     * @return
     */
    public <T> List<T> searchById(final String searchField, final String searchValue, final String namedQuery) {
        final List<T> toReturn;
        if (namedQuery == null) {
            toReturn = null;
        } else {
            final Query query = em.createNamedQuery(namedQuery);
            if (searchField != null && searchValue != null) {
                query.setParameter(searchField, searchValue);
            }
            toReturn = query.getResultList();
        }
        return toReturn;
    }


    /**
     * searchByNameAndVersion
     *
     * @param schemaName
     * @param majorVersion
     * @param minorVersion
     * @param namedQuery
     * @param <T>
     * @return
     */
    public <T> List<T> searchByNameAndVersion(final String schemaName, final int majorVersion, final int minorVersion, final String namedQuery) {
        final List<T> toReturn;
        if (namedQuery == null) {
            toReturn = null;
        } else {
            final Query query = em.createNamedQuery(namedQuery);
            if (schemaName != null) {
                query.setParameter(Constant.SCHEMA_NAME, schemaName);
                query.setParameter(Constant.MAJOR_VERSION, majorVersion);
                query.setParameter(Constant.MINOR_VERSION, minorVersion);
            }
            toReturn = query.getResultList();
        }
        return toReturn;
    }


    /**
     * searchDataByUserIdSchemaAndMajorVersionRange
     *
     * @param userId
     * @param schemaName
     * @param majorVersionFrom
     * @param majorVersionTo
     * @param namedQuery
     * @return
     */
    public List<DataEntity> searchDataByUserIdSchemaAndMajorVersionRange(final String userId, final String schemaName,
                                                                    final int majorVersionFrom, final int majorVersionTo,
                                                                    final String namedQuery) {
        final List<DataEntity> toReturn;
        if (namedQuery == null) {
            toReturn = null;
        } else {
            final Query query = em.createNamedQuery(namedQuery);
            if (schemaName != null) {
                query.setParameter(Constant.USER_ID, userId);
                query.setParameter(Constant.SCHEMA_NAME, schemaName);
                query.setParameter(Constant.MAJOR_VERSION_FROM, majorVersionFrom);
                query.setParameter(Constant.MAJOR_VERSION_TO, majorVersionTo);
            }
            toReturn = query.getResultList();
        }
        return toReturn;
    }


    /**
     * searchDataByUserIdSchema
     *
     * @param userId
     * @param schemaName
     * @param namedQuery
     * @return
     */
    public List<DataEntity> searchDataByUserIdSchema(final String userId, final String schemaName,
                                                     final String namedQuery) {
        final List<DataEntity> toReturn;
        if (namedQuery == null) {
            toReturn = null;
        } else {
            final Query query = em.createNamedQuery(namedQuery);
            if (schemaName != null) {
                query.setParameter(Constant.USER_ID, userId);
                query.setParameter(Constant.SCHEMA_NAME, schemaName);
            }
            toReturn = query.getResultList();
        }
        return toReturn;
    }


    /**
     * Generic Persistence Save
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T extends Object> T mergeData(final T entity) {
        em.getTransaction().begin();
        final T returnEntity = em.merge(entity);
        em.flush();
        em.getTransaction().commit();
        return returnEntity;
    }


    /**
     * Generic Persistence Update
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T extends Object> T saveData(final T entity) {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        return entity;
    }



    /**
     * Generic Persistence Delete
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T extends Object> T delete(final T entity) {
        em.getTransaction().begin();
        em.remove(entity);
        em.getTransaction().commit();
        return entity;
    }




    /**
     * Close Entity manager and factory
     */
    public void closeEntityManager() {
        if (em != null) {
            em.close();
        }

        if (emf != null) {
            emf.close();
        }
    }


}
