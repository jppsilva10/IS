import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class XmlToObject {
    public static void main(String[] args) throws Exception{

        long time = System.currentTimeMillis();

        File file = new File("C:\\Users\\ASUS\\Desktop\\IS\\Projeto_1\\src\\main\\java\\data.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Data d = (Data) jaxbUnmarshaller.unmarshal(file);

        time = System.currentTimeMillis() - time;

        System.out.println(time);

    }
}
