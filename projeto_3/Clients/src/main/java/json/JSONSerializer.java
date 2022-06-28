package json;

public class JSONSerializer {
    String str = "{" +
                        "\"schema\":{" +
                            "\"type\":\"struct\","+
                            "\"fields\":["+
                                "{"+
                                    "\"type\":\"int64\","+
                                    "\"optional\":false,"+
                                    "\"field\":\"id\""+
                                "},"+
                                "{"+
                                    "\"type\":\"string\","+
                                    "\"optional\":true,"+
                                    "\"field\":\"name\""+
                                "}"+
                            "],"+
                            "\"optional\":false,"+
                            "\"name\":\"client\""+
                        "},"+
                        "\"payload\":{"+
                            "\"id\":1,"+
                            "\"name\":\"Joao\""+
                        "}"+
                    "}";

    public static String totalMetrics(String id, float metric){
        return "{" +
                        "\"schema\":{" +
                            "\"type\":\"struct\","+
                            "\"fields\":["+
                                "{"+
                                    "\"type\":\"string\","+
                                    "\"optional\":false,"+
                                    "\"field\":\"id\""+
                                "},"+
                                "{"+
                                    "\"type\":\"float\","+
                                    "\"optional\":true,"+
                                    "\"field\":\"metric\""+
                                "}"+
                            "],"+
                            "\"optional\":false,"+
                            "\"name\":\"total_metric\""+
                        "},"+
                        "\"payload\":{"+
                            "\"id\":"+"\""+id+"\","+
                            "\"metric\":"+metric+
                        "}"+
                    "}";
    }
    public static String highestMetrics(String id, Long metric){
        return "{" +
                        "\"schema\":{" +
                            "\"type\":\"struct\","+
                            "\"fields\":["+
                                "{"+
                                    "\"type\":\"string\","+
                                    "\"optional\":false,"+
                                    "\"field\":\"id\""+
                                "},"+
                                "{"+
                                    "\"type\":\"int64\","+
                                    "\"optional\":true,"+
                                    "\"field\":\"metric\""+
                                "}"+
                            "],"+
                            "\"optional\":false,"+
                            "\"name\":\"highest_metric\""+
                        "},"+
                        "\"payload\":{"+
                            "\"id\":"+"\""+id+"\","+
                            "\"metric\":"+metric+
                        "}"+
                    "}";
    }
    public static String PerClientMetrics(String topic, Long id, float metric){
        return "{" +
                        "\"schema\":{" +
                            "\"type\":\"struct\","+
                            "\"fields\":["+
                                "{"+
                                    "\"type\":\"int64\","+
                                    "\"optional\":false,"+
                                    "\"field\":\"id\""+
                                "},"+
                                "{"+
                                    "\"type\":\"float\","+
                                    "\"optional\":true,"+
                                    "\"field\":\"metric\""+
                                "}"+
                            "],"+
                            "\"optional\":false,"+
                            "\"name\":"+"\""+topic+"\""+
                        "},"+
                        "\"payload\":{"+
                            "\"id\":"+id+","+
                            "\"metric\":"+metric+
                        "}"+
                    "}";
    }
}
