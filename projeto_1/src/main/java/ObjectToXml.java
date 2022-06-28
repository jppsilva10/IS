import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import java.io.File;

public class ObjectToXml {
    public static void main(String[] args) throws Exception{

        Data d = new Data(10, 1000000);

        long time = System.currentTimeMillis();

        JAXBContext contextObj = JAXBContext.newInstance(Data.class);

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshallerObj.marshal(d, new File("C:\\Users\\ASUS\\Desktop\\IS\\Projeto_1\\src\\main\\java\\data.xml"));

        time = System.currentTimeMillis() - time;

        System.out.println(time);

    }
}
