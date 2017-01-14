package sk.stuba.fei.dp.maly.persistence.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sk.stuba.fei.dp.maly.persistence.entities.Ontology;

import java.util.Date;
import java.util.List;

import static sk.stuba.fei.dp.maly.persistence.entities.QOntology.ontology;

/**
 * Created by Patrik on 27/12/2016.
 */
@Repository
@Transactional
public class OntologyDAO extends AbstractDAO<Ontology,Long> {

    @Override
    public void persist(Ontology entity) {
        entity.setLastUpdate(new Date());
        super.persist(entity);
    }

    public List<Ontology> findAll(){
        return queryFactory.selectFrom(ontology).fetch();
    }

}
