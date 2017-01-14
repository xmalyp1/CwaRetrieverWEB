package sk.stuba.fei.dp.maly.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.dp.maly.persistence.enums.OWLFileTypeEnum;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

/**
 * Created by Patrik on 03/01/2017.
 */
@Getter
@Setter
public class OntologyDto {

    private String name;

    private String description;

    private OWLFileTypeEnum fileType;
}
