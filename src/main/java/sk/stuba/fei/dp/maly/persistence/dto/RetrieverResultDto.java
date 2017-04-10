package sk.stuba.fei.dp.maly.persistence.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Patrik on 10/04/2017.
 */
@Getter
@Setter
@EqualsAndHashCode
public class RetrieverResultDto {
    private String namedIndividual;
    private String individualClass;
    private boolean cwa;

    public RetrieverResultDto(){

    }


}
