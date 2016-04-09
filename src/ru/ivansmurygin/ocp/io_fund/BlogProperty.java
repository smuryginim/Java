package ru.ivansmurygin.ocp.io_fund;

/**
 * The class doesn't implement serializable
 */
public class BlogProperty {
    private Double doubleProp;
    private String stringProp;

    public Double getDoubleProp() {
        return doubleProp;
    }

    public void setDoubleProp(Double doubleProp) {
        this.doubleProp = doubleProp;
    }

    public String getStringProp() {
        return stringProp;
    }

    public void setStringProp(String stringProp) {
        this.stringProp = stringProp;
    }
}
