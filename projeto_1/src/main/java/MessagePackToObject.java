import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.msgpack.unpacker.Unpacker;

import java.io.*;

public class MessagePackToObject {
    public static void main(String[] args) throws Exception {

        long time = System.currentTimeMillis();

        MessagePack msgpack = new MessagePack();

        InputStream in = new FileInputStream("C:\\Users\\ASUS\\Desktop\\IS\\Projeto_1\\src\\main\\java\\data.bin");
        byte[] bytes = in.readAllBytes();
        Data d = msgpack.read(bytes, Data.class);

        time = System.currentTimeMillis() - time;

        System.out.println(time);
    }
}
