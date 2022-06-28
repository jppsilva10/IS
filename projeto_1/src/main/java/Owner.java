
import jakarta.xml.bind.annotation.*;
import org.msgpack.annotation.Message;

@Message
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Owner {
    @XmlAttribute
    int id;
    String name;
    String birthday;
    String telephone;
    String address;

    public Owner(){
    }

    public Owner(int id, String name, String birthday, String telephone, String address){
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.telephone = telephone;
        this.address = address;
    }

}

