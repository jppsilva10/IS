
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.msgpack.annotation.Message;

@Message
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Pet {
    @XmlAttribute
    int id;
    String name;
    String specie;
    boolean gender;
    float weight;
    String birthday;
    String description;
    int ownerId;

    public Pet(){

    }

    public Pet(int id, String name, String specie, boolean gender, float weight, String birthday, String description, int ownerId){
        this.id = id;
        this.name = name;
        this.specie = specie;
        this.gender = gender;
        this.weight = weight;
        this.birthday = birthday;
        this.description = description;
        this.ownerId = ownerId;
    }
}
