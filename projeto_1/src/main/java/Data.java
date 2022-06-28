import jakarta.xml.bind.annotation.*;
import org.msgpack.annotation.Message;

import java.util.ArrayList;

@Message
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Data {
    ArrayList<Owner> owners;
    ArrayList<Pet> pets;

    public Data(){
    }

    public Data(int size){
        owners = new ArrayList<Owner>();
        pets = new ArrayList<Pet>();

        for(int i=0; i<size; i++){
            owners.add(new Owner(i, "Alberto" + i, "1/1/" + (2000+i), "" + (960000000+i), "adrress" + i));
            pets.add(new Pet(i, "Dog" + i, "specie" + i, i%2 == 0, i, "2/2/" + (2000+i), "description" + i, i));
        }
    }

    public Data(int size, int characters){
        owners = new ArrayList<Owner>();
        pets = new ArrayList<Pet>();

        String str = "";

        for(int i=0; i<characters; i++){
            str += "a";
        }

        for(int i=0; i<size; i++){
            owners.add(new Owner(i, "Alberto" + i, "1/1/" + (2000+i), "" + (960000000+i), "adrress" + i));
            pets.add(new Pet(i, "Dog" + i, "specie" + i, i%2 == 0, i, "2/2/" + (2000+i), str + i, i));
        }
    }
}
