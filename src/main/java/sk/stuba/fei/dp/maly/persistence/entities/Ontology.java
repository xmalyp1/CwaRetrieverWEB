package sk.stuba.fei.dp.maly.persistence.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sk.stuba.fei.dp.maly.persistence.enums.OWLFileTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Patrik on 27/12/2016.
 */
@Entity
@Data
@EqualsAndHashCode(of = "ontologyId")
@ToString(of = {"ontologyId", "name"})
public class Ontology {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long ontologyId;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    private String description;

    @Column(nullable = false)
    @NotNull
    private String destinationFile;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private OWLFileTypeEnum fileType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date lastUpdate;

}
