package sk.stuba.fei.dp.maly.persistence.dao;

import com.querydsl.jpa.JPQLQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Patrik on 27/12/2016.
 */
@Component
@Transactional
public abstract class AbstractDAO<T, ID> {

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    JPQLQueryFactory queryFactory;

    private Class<T> type;

    @SuppressWarnings("unchecked")
    AbstractDAO() {
        final Class<?>[] classes = GenericTypeResolver.resolveTypeArguments(getClass(), AbstractDAO.class);
        this.type = (Class<T>) classes[0];
    }
    public T findById(ID id) {
        return em.find(type, id);
    }
    public void persist(T entity) {
        em.persist(entity);
    }
    public void delete(T entity) {
        em.remove(entity);
    }
}
