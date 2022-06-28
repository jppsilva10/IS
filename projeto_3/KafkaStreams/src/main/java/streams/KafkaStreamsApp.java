package streams;

import com.google.protobuf.InvalidProtocolBufferException;
import data.Client;
import data.Currency;
import data.Manager;
import json.JSONSerializer;
import json.Transaction;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.json.JSONException;

public class KafkaStreamsApp {
    private static String creditsTopic = "Credits-test";
    private static String paymentsTopic = "Payments-test";

    private static String DBClientTopic = "DB-Info-test-client";
    private static String DBCurrencyTopic = "DB-Info-test-currency";

    private static Properties props;

    static final Logger logger = LoggerFactory.getLogger(KafkaStreamsApp.class);

    private static long mins;

    public static void main(String[] args) throws InterruptedException, IOException {

        mins = 30 * 24 * 60;

        if (args.length != 0) {
            mins = Long.parseLong(args[0]);
        }

        KafkaStreams streams = startConnection();

        props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "KafkaStreamsApp-test6");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass());

        startStatistics(streams);
    }

    static KafkaStreams startConnection(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "DB_info_Topics_test4");
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
            System.out.println("ERROR: Cant parse JSON");
        }
        return new KeyValue<Long, String>(k, v);
    }

    static List<Client> getClients(KafkaStreams streams) throws InterruptedException {

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
            Manager m = new Manager(null);
            m.setId(json.getLong("manager_id"));
            c.setManager_id(m);

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

    private static void startStatistics(KafkaStreams db_streams){
        StreamsBuilder builder = new StreamsBuilder();
        KStream<Long, byte[]> clines = builder.stream(creditsTopic);
        KStream<Long, byte[]> plines = builder.stream(paymentsTopic);

        clines = clines.mapValues(v->ConvertToEuro(v, db_streams));
        plines = plines.mapValues(v->ConvertToEuro(v, db_streams));

        // -------------------- Credits per client --------------------
        KTable<Long, byte[]> coutlines = clines.groupByKey().reduce(
                (oldval, newval) -> getTransactionsPerClient(oldval, newval, db_streams),
                Materialized.as("CreditsPerClient")
        );

        coutlines.toStream().map((k, v) -> new KeyValue<>(JSONSerializer.totalMetrics("", parseValue(v).getValue()), JSONSerializer.PerClientMetrics("credit", k, parseValue(v).getValue()))).to("credit", Produced.with(Serdes.String(), Serdes.String()));
        // ------------------------------------------------------------




        // -------------------- Payments per client --------------------
        KTable<Long, byte[]> poutlines = plines.groupByKey().reduce(
                (oldval, newval) -> getTransactionsPerClient(oldval, newval, db_streams),
                Materialized.as("PaymentsPerClient")
        );

        poutlines.toStream().map((k, v) -> new KeyValue<>(JSONSerializer.totalMetrics("", parseValue(v).getValue()), JSONSerializer.PerClientMetrics("payment", k, parseValue(v).getValue()))).to("payment", Produced.with(Serdes.String(), Serdes.String()));
        // ------------------------------------------------------------




        // -------------------- Balance per client --------------------
        ValueJoiner<byte[], byte[], byte[]> joiner = (leftValue, rightValue) -> getCreditsBalancePerClient(rightValue, leftValue, db_streams);

        KTable<Long, byte[]> balance = coutlines.join(poutlines,
                joiner,
                Materialized.as("BalancePerClient")
        );

        balance.toStream().map((k, v) -> new KeyValue<>(JSONSerializer.totalMetrics("", parseValue(v).getValue()), JSONSerializer.PerClientMetrics("balance", k, parseValue(v).getValue()))).to("balance", Produced.with(Serdes.String(), Serdes.String()));
        // ------------------------------------------------------------




        // -------------------- Total credits --------------------
        KTable<Long, byte[]> totalCredits = clines.groupBy((k,v)->new Long(0)).reduce(
                (oldval, newval) -> getTransactionsPerClient(oldval, newval, db_streams),
                Materialized.as("TotalCredits")
        );

        totalCredits.toStream().map((k, v) -> new KeyValue<>(JSONSerializer.totalMetrics("", parseValue(v).getValue()), JSONSerializer.totalMetrics("TotalCredits", parseValue(v).getValue()))).to("total_metric", Produced.with(Serdes.String(), Serdes.String()));
        // ------------------------------------------------------------




        // -------------------- Total payments --------------------
        KTable<Long, byte[]> totalPayments = plines.groupBy((k,v)->new Long(0)).reduce(
                (oldval, newval) -> getTransactionsPerClient(oldval, newval, db_streams),
                Materialized.as("TotalPayments")
        );

        totalPayments.toStream().map((k, v) -> new KeyValue<>(JSONSerializer.totalMetrics("", parseValue(v).getValue()), JSONSerializer.totalMetrics("TotalPayments", parseValue(v).getValue()))).to("total_metric", Produced.with(Serdes.String(), Serdes.String()));
        // ------------------------------------------------------------




        // -------------------- Total balance --------------------
        KTable<Long, byte[]> totalBalance = totalCredits.join(totalPayments,
                joiner,
                Materialized.as("TotalBalance")
        );

        totalBalance.toStream().map((k, v) -> new KeyValue<>(JSONSerializer.totalMetrics("", parseValue(v).getValue()), JSONSerializer.totalMetrics("TotalBalance", parseValue(v).getValue()))).to("total_metric", Produced.with(Serdes.String(), Serdes.String()));
        // ------------------------------------------------------------




        // -------------------- Balance per client for the last month--------------------
        KStream<Long, byte[]> transactions = plines.merge(clines.mapValues(v->ManageCredit(v)));

        KTable<Windowed<Long>, byte[]> balance_last_month = transactions.groupByKey().windowedBy(TimeWindows.of(TimeUnit.MINUTES.toMillis(mins))).reduce(
                (oldval, newval) -> getTransactionsPerClient(oldval, newval, db_streams),
                Materialized.as("BalancePerClient_LastMonth")
        );

        balance_last_month.toStream((wk, v) -> wk.key()).map((k, v) -> new KeyValue<>(JSONSerializer.totalMetrics("", parseValue(v).getValue()), JSONSerializer.PerClientMetrics("balance_last_month", k, parseValue(v).getValue()))).to("balance_last_month", Produced.with(Serdes.String(), Serdes.String()));
        // ------------------------------------------------------------











        // -------------------- Credits per client for the last 2 months--------------------
        KTable<Windowed<Long>, byte[]> coutlines_last_2months = clines.groupByKey().windowedBy(TimeWindows.of(TimeUnit.DAYS.toMillis(60))).reduce(
                (oldval, newval) -> getTransactionsPerClient(oldval, newval, db_streams),
                Materialized.as("CreditsPerClient_Last2Months")
        );
        // ------------------------------------------------------------




        // -------------------- Payments per client for the last 2 months--------------------
        KTable<Windowed<Long>, byte[]> poutlines_last_2months = plines.groupByKey().windowedBy(TimeWindows.of(TimeUnit.DAYS.toMillis(60))).reduce(
                (oldval, newval) -> getTransactionsPerClient(oldval, newval, db_streams),
                Materialized.as("PaymentsPerClient_Last2Months")
        );
        // ------------------------------------------------------------





        // -------------------- Highest Debt--------------------
        KStream<Long, byte[]> lines = balance.toStream().map((k, v) -> new KeyValue<Long, byte[]>(new Long(0), serializeValue(k,v)));

        KTable<Long, byte[]> highestDebt = lines.groupByKey().reduce(
                (oldval, newval) -> getHighestDebt(oldval, newval),
                Materialized.as("HighestDebt")
        );

        highestDebt.toStream().map((k, v) -> new KeyValue<>(JSONSerializer.totalMetrics("", parseValue(v).getValue()), JSONSerializer.highestMetrics("HighestDebt", parseValue(v).getCurrency()))).to("highest_metric", Produced.with(Serdes.String(), Serdes.String()));
        // ------------------------------------------------------------




        // -------------------- HighestRevenue --------------------
        KTable<Long, byte[]> revenuePerManager = plines.groupBy((k,v)->getManagerByClient(k, db_streams)).reduce(
                (oldval, newval) -> getTransactionsPerClient(oldval, newval, db_streams),
                Materialized.as("RevenuePerManager")
        );

        KStream<Long, byte[]> lines2 = revenuePerManager.toStream().map((k, v) -> new KeyValue<Long, byte[]>(new Long(0), serializeValue(k,v)));

        KTable<Long, byte[]> highestRevenue = lines2.groupByKey().reduce(
                (oldval, newval) -> getHighestRevenue(oldval, newval),
                Materialized.as("HighestRevenue")
        );


        highestRevenue.toStream().map((k, v) -> new KeyValue<>(JSONSerializer.totalMetrics("", parseValue(v).getValue()), JSONSerializer.highestMetrics("HighestRevenue", parseValue(v).getCurrency()))).to("highest_metric", Produced.with(Serdes.String(), Serdes.String()));



        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

    }

    private static Long getEuroId(KafkaStreams streams) throws InterruptedException {

        List<Currency> c = getCurrencies(streams);

        for(int i=0; i<c.size(); i++){
            Currency currency = c.get(i);
            if(currency.getName().equals("euro")) return currency.getId();
        }

        System.out.println("ERROR: Cant find euro");
        return (long)-1;
    }

    private static Currency getCurrencyById(long id, KafkaStreams streams) throws InterruptedException {

        List<Currency> c = getCurrencies(streams);

        for(int i=0; i<c.size(); i++){
            Currency currency = c.get(i);
            if(currency.getId() == id) return currency;
        }

        System.out.println("ERROR: Cant find currency " + id);

        return null;
    }

    private static float getExchangeRate(long id, KafkaStreams streams) throws InterruptedException {
        return getCurrencyById(id, streams).getExchangeRate();
    }

    private static Transaction parseValue(byte[] b){

        try {
            return Transaction.parseFrom(b);
        } catch (InvalidProtocolBufferException e) {
            System.out.println("ERROR: Cant parse bytes " + b);
        }
        return null;
    }

    private static byte[] serializeValue(long k, byte[] v){

        Transaction.Builder transaction = Transaction.newBuilder();

        transaction.setValue(parseValue(v).getValue());
        transaction.setCurrency(k);

        Transaction t = transaction.build();
        return t.toByteArray();
    }

    private static Transaction addValues(Transaction t1, Transaction t2, KafkaStreams streams) {

        Transaction.Builder t = Transaction.newBuilder();

        float value = 0;
        if(t1!=null){
            value = t1.getValue();
            t.setCurrency(t1.getCurrency());
        }
        if(t2!=null){
            value += t2.getValue();
            t.setCurrency(t2.getCurrency());
        }

        t.setValue(value);

        return t.build();
    }

    private static Transaction subValues(Transaction t1, Transaction t2, KafkaStreams streams) {

        Transaction.Builder t = Transaction.newBuilder();

        float value = 0;
        if (t1 != null){
            value = t1.getValue();
            t.setCurrency(t1.getCurrency());
        }
        if (t2 != null){
            value -= t2.getValue();
            t.setCurrency(t2.getCurrency());
        }

        t.setValue(value);

        return t.build();
    }

    public static byte[] getTransactionsPerClient(byte[] oldval, byte[] newval, KafkaStreams streams){

        Transaction t = addValues(parseValue(oldval), parseValue(newval), streams);

        return t.toByteArray();
    }

    public static byte[] getCreditsBalancePerClient(byte[] oldval, byte[] newval, KafkaStreams streams) {
        Transaction t = subValues(parseValue(oldval), parseValue(newval), streams);
        return t.toByteArray();
    }

    public static byte[]getHighestDebt(byte[] oldval, byte[] newval){

        Transaction t1 = parseValue(oldval);
        Transaction t2 = parseValue(newval);

        if(t1.getValue()>t2.getValue()){
            return newval;
        }
        return oldval;
    }

    public static byte[]getHighestRevenue(byte[] oldval, byte[] newval){

        Transaction t1 = parseValue(oldval);
        Transaction t2 = parseValue(newval);

        if(t1.getValue()<t2.getValue()){
            return newval;
        }
        return oldval;
    }

    public static long getManagerByClient(long id, KafkaStreams streams){

        List<Client> c;
        try {
            c = getClients(streams);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
        for(Client client : c){
            if(client.getId() == id);
            return client.getManager_id().getId();
        }

        System.out.println("ERROR: Cant find manager " + id);

        return -1;
    }

    public static byte[] ConvertToEuro(byte[] b, KafkaStreams streams){

        Transaction t = parseValue(b);
        if(t == null) return null;

        System.out.println("get " + t.getValue() + " id: " + t.getCurrency() );

        Transaction.Builder transaction = Transaction.newBuilder();
        float value = t.getValue();
        try {
            value *= getExchangeRate(t.getCurrency(), streams);
            transaction.setValue(value);
            transaction.setCurrency(getEuroId(streams));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("set " + transaction.getValue() + " id: " + transaction.getCurrency());

        return transaction.build().toByteArray();
    }

    public static byte[] ManageCredit(byte[] b){

        Transaction t = parseValue(b);
        if(t == null) return null;

        Transaction.Builder transaction = Transaction.newBuilder();

        transaction.setValue(-1 * t.getValue());
        transaction.setCurrency(t.getCurrency());

        return transaction.build().toByteArray();
    }

}
