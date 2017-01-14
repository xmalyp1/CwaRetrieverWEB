package sk.stuba.fei.dp.maly.persistence.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QOntology is a Querydsl query type for Ontology
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOntology extends EntityPathBase<Ontology> {

    private static final long serialVersionUID = -1661260979L;

    public static final QOntology ontology = new QOntology("ontology");

    public final StringPath description = createString("description");

    public final StringPath destinationFile = createString("destinationFile");

    public final EnumPath<sk.stuba.fei.dp.maly.persistence.enums.OWLFileTypeEnum> fileType = createEnum("fileType", sk.stuba.fei.dp.maly.persistence.enums.OWLFileTypeEnum.class);

    public final DateTimePath<java.util.Date> lastUpdate = createDateTime("lastUpdate", java.util.Date.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> ontologyId = createNumber("ontologyId", Long.class);

    public QOntology(String variable) {
        super(Ontology.class, forVariable(variable));
    }

    public QOntology(Path<? extends Ontology> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOntology(PathMetadata metadata) {
        super(Ontology.class, metadata);
    }

}

