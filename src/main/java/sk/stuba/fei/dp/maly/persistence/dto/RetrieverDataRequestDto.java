package sk.stuba.fei.dp.maly.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Patrik on 05/01/2017.
 */
@Getter
@Setter
@ToString
public class RetrieverDataRequestDto {

    private Long ontologyId;
    private String reasonerName;
    private String query;
    private boolean cwaMode;
}
