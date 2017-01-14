package sk.stuba.fei.dp.maly.persistence.enums;

/**
 * Created by Patrik on 27/12/2016.
 */
public enum OWLFileTypeEnum {
    TURTLE("Turtle"),RDF_XML("RDF/XML"),OWL_XML("OWL/XML"),MANCHESTER("Manchester");

    private final String value;

    private OWLFileTypeEnum(String s) {
        value = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : value.equals(otherName);
    }

    public String toString() {
        return this.value;
    }
}
