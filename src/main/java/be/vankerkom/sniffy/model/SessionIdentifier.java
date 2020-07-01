package be.vankerkom.sniffy.model;

import lombok.Value;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.TransportPacket;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;

import java.net.InetAddress;

@Value(staticConstructor = "of")
public class SessionIdentifier {

    IpVersion ipVersion;
    IpNumber protocol;
    InetAddress source;
    InetAddress destination;
    int sourcePort;
    int destinationPort;

    public static SessionIdentifier of(boolean inbound,
                                       IpPacket.IpHeader ipHeader,
                                       TransportPacket.TransportHeader transportHeader) {
        return of(
                ipHeader.getVersion(),
                ipHeader.getProtocol(),
                inbound ? ipHeader.getSrcAddr() : ipHeader.getDstAddr(),
                inbound ? ipHeader.getDstAddr() : ipHeader.getSrcAddr(),
                inbound ? transportHeader.getSrcPort().valueAsInt() : transportHeader.getDstPort().valueAsInt(),
                inbound ? transportHeader.getDstPort().valueAsInt() : transportHeader.getSrcPort().valueAsInt()
        );
    }
}
