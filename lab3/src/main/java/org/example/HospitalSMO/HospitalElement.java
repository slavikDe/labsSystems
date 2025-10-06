package org.example.HospitalSMO;


import lombok.Getter;
import lombok.Setter;
import org.example.smo_universal.Element;

@Getter
@Setter
public class HospitalElement extends Element {
    private int type;

    public HospitalElement(int type){
        super();
        this.type = type;
    }

    public HospitalElement(int type, int delay){
        super(delay);
        this.type=type;
    }

    public HospitalElement(int type, int delay, String name){
        super(name, delay);
        this.type =type;
    }

    public int getPossibleTypes() {
        return type;
    }

}
