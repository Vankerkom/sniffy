package be.vankerkom.sniffy.packets;

import org.pcap4j.util.ByteArrays;

public final class Util {

    public static String tooShortErrorMessage(String type, int size, byte[] rawData, int offset, int length) {
        StringBuilder sb = new StringBuilder(200);

        sb.append("The data is too short to build a ")
                .append(type)
                .append(" (")
                .append(size)
                .append(" bytes). data: ")
                .append(ByteArrays.toHexString(rawData, " "))
                .append(", offset: ")
                .append(offset)
                .append(", length: ")
                .append(length);

        return sb.toString();
    }

}
