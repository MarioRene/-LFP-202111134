package main.java.com.mapanarrativo.models;

public class Connection {
    private String from;
    private String to;
    private String type;

    public Connection(String from, String to, String type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}