package be.vankerkom.sniffy.model;

import lombok.Value;

@Value
public class ProtocolPort {

    boolean tcp;
    int portValue;

    public static ProtocolPort tcp(int portValue) {
        return new ProtocolPort(true, portValue);
    }

    public static ProtocolPort udp(int portValue) {
        return new ProtocolPort(false, portValue);
    }

    public String toBpfFilter() {
        return (tcp ? "tcp" : "udp") + " port " + portValue;
    }

}
