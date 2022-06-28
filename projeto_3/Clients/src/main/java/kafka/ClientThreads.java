package kafka;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import data.Client;
import data.Currency;
import json.Transaction;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;

import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.json.JSONObject;
import org.json.JSONException;


public class ClientThreads {
    private static String DBClientTopic = "DB-Info-test-client";
    private static String DBCurrencyTopic = "DB-Info-test-currency";

    public static void main(String[] args) throws Exception{

        KafkaStreams streams = startConnection();

        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", "127.0.0.1:9092");
        //Set acknowledgements for producer requests.
        props.put("acks", "all");
        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);
        //Specify buffer size in config
        props.put("batch.size", 16384);
        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);
        //The buffer.memory controls the total amount of memory available to the producer for
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        MakeCredits mc = new MakeCredits(props, streams);
        MakePayments mp = new MakePayments(props, streams);

        mc.start();
        mp.start();

    }

    static KafkaStreams startConnection(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "ClientThreads_test");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> clients = builder.stream(DBClientTopic);
        KStream<String, String> currencies = builder.stream(DBCurrencyTopic);


        KTable<Long, String> outclients = clients.map((k, v) -> manageTopic(v)).groupByKey().reduce(
                (oldval, newval) -> newval,
                Materialized.as("Clients")
        );

        KTable<Long, String> outcurrencies = currencies.map((k, v) -> manageTopic(v)).groupByKey().reduce(
                (oldval, newval) -> newval,
                Materialized.as("Currencies")
        );

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        return streams;
    }

    static private KeyValue<Long, String> manageTopic(String value){
        long k = -1;
        String v = value;
        try {
            JSONObject json = new JSONObject(value);
            json = json.getJSONObject("payload");
            k = json.getLong("id");
        }catch (JSONException err){
            System.out.println("error");
        }
        return new KeyValue<Long, String>(k, v);
    }

    static List<data.Client> getClients(KafkaStreams streams) throws InterruptedException {

        List<Client> clients = new ArrayList<Client>();

        while (true) {
            ReadOnlyKeyValueStore<Long, String> keyValueStore;
            try {
                keyValueStore = streams.store("Clients", QueryableStoreTypes.keyValueStore());
                // Get the values for a range of keys available in this application instance
                KeyValueIterator<Long, String> range = keyValueStore.all();
                while (range.hasNext()) {
                    KeyValue<Long, String> next = range.next();
                    Client c = getClient(next.value);
                    if(c!=null) clients.add(c);

                }
                range.close();
            }catch (Exception e){
                System.out.println("ERROR: Cant get clients from " + DBClientTopic);
                Thread.sleep(1000);
                continue;
            }
            break;
        }

        return clients;
    }

    static private Client getClient(String value){
        Client c = new Client();
        try {
            JSONObject json = new JSONObject(value);
            json = json.getJSONObject("payload");
            c.setId(json.getLong("id"));
            c.setName(json.getString("name"));
        }catch (JSONException err){
            System.out.println("ERROR: Cant parse JSON");
            return null;
        }
        return c;
    }

    static List<Currency> getCurrencies(KafkaStreams streams) throws InterruptedException {

        List<Currency> currencies = new ArrayList<Currency>();

        while (true) {
            ReadOnlyKeyValueStore<Long, String> keyValueStore;
            try {
                keyValueStore = streams.store("Currencies", QueryableStoreTypes.keyValueStore());
                // Get the values for a range of keys available in this application instance
                KeyValueIterator<Long, String> range = keyValueStore.all();
                while (range.hasNext()) {
                    KeyValue<Long, String> next = range.next();
                    Currency c = getCurrency(next.value);
                    if(c!=null) currencies.add(c);

                }
                range.close();
            }catch (Exception e){
                System.out.println("ERROR: Cant get currencies from " + DBCurrencyTopic);
                Thread.sleep(1000);
                continue;
            }
            break;
        }

        return currencies;
    }

    static private Currency getCurrency(String value){
        Currency c = new Currency();
        try {
            JSONObject json = new JSONObject(value);
            json = json.getJSONObject("payload");
            c.setId(json.getLong("id"));
            c.setName(json.getString("name"));
            c.setExchangeRate(json.getFloat("exchangerate"));
        }catch (JSONException err){
            System.out.println("ERROR: Cant parse JSON");
            return null;
        }
        return c;
    }
}

class MakeCredits extends Thread{
    private static String Topic = "Credits-test";
    private Properties props;
    private KafkaStreams streams;

    public MakeCredits(Properties props, KafkaStreams streams){
        this.props = props;
        this.streams = streams;
    }

    public void run(){
        Producer<Long, byte[]> producer = new KafkaProducer<>(props);

        List<data.Client> clients = null;
        List<Currency> currencies = null;

        while(true){

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                clients = ClientThreads.getClients(streams);
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }

            if(clients.size()<=0){
                System.out.println("WARN: the data base has no clients");
                continue;
            }
            try {
                currencies = ClientThreads.getCurrencies(streams);
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }

            if(currencies.size()<=0){
                System.out.println("WARN: the data base has no currencies");
                continue;
            }
            int index = (int) (Math.random() * clients.size());
            data.Client client = clients.get(index);
            index = (int) (Math.random() * currencies.size());
            Currency currency = currencies.get(index);
            float value = (float) (0.01 + Math.random() * 1000);

            Transaction.Builder transaction = Transaction.newBuilder();

            transaction.setValue(value);
            transaction.setCurrency(currency.getId());

            Transaction t = transaction.build();

            byte[] b = t.toByteArray();

            producer.send(new ProducerRecord<Long, byte[]>(Topic, client.getId(), b ));

            System.out.println();
            System.out.println("" + value + " " + currency.getName() + "s " + "credit " + "from " + client.getName());
            System.out.println();

        }
    }
}

class MakePayments extends Thread{
    private static String Topic = "Payments-test";
    private Properties props;
    private KafkaStreams streams;

    public MakePayments(Properties props, KafkaStreams streams){
        this.props = props;
        this.streams = streams;
    }

    public void run(){
        Producer<Long, byte[]> producer = new KafkaProducer<>(props);

        List<data.Client> clients = null;
        List<Currency> currencies = null;

        while(true){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                clients = ClientThreads.getClients(streams);
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }

            if(clients.size()<=0){
                System.out.println("WARN: the data base has no clients");
                continue;
            }
            try {
                currencies = ClientThreads.getCurrencies(streams);
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }

            if(currencies.size()<=0){
                System.out.println("WARN: the data base has no currencies");
                continue;
            }
            int index = (int) (Math.random() * clients.size());
            data.Client client = clients.get(index);
            index = (int) (Math.random() * currencies.size());
            Currency currency = currencies.get(index);
            float value = (float) (0.01 + Math.random() * 1000);

            Transaction.Builder transaction = Transaction.newBuilder();

            transaction.setValue(value);
            transaction.setCurrency(currency.getId());

            Transaction t = transaction.build();

            byte[] b = t.toByteArray();

            producer.send(new ProducerRecord<Long, byte[]>(Topic, client.getId(), b ));
            System.out.println();
            System.out.println("" + value + " " + currency.getName() + "s " + "payment " + "from " + client.getName());
            System.out.println();

        }
    }
}