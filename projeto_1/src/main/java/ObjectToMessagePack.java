import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ObjectToMessagePack {

    public static void main(String[] args) throws Exception {

        Data d = new Data(1000000);

        long time = System.currentTimeMillis();

        MessagePack msgpack = new MessagePack();

        byte[] bytes = msgpack.write(d);
        OutputStream outputStream = new FileOutputStream("C:\\Users\\ASUS\\Desktop\\IS\\Projeto_1\\src\\main\\java\\data.bin");
        outputStream.write(bytes);

        time = System.currentTimeMillis() - time;

        System.out.println(time);
    }
}
