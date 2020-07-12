package be.vankerkom.sniffy.packets;

import org.pcap4j.packet.AbstractPacket;
import org.pcap4j.packet.IllegalRawDataException;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.ByteArrays;

import java.util.ArrayList;
import java.util.List;

import static be.vankerkom.sniffy.packets.Util.tooShortErrorMessage;
import static org.pcap4j.util.ByteArrays.toByteArray;

public class DirectPlay8Packet extends AbstractPacket {

    private final DirectPlay8Header header;

    public static DirectPlay8Packet newPacket(byte[] rawData, int offset, int length) throws IllegalRawDataException {
        ByteArrays.validateBounds(rawData, offset, length);
        return new DirectPlay8Packet(rawData, offset, length);
    }

    private DirectPlay8Packet(byte[] rawData, int offset, int length) throws IllegalRawDataException {
        this.header = new DirectPlay8Header(rawData, offset, length);
    }

    public DirectPlay8Packet(Builder builder) {
        // Set stuff.
        this.header = new DirectPlay8Header(builder);
    }

    @Override
    public Builder getBuilder() {
        return new Builder(this);
    }

    public static final class DirectPlay8Header extends AbstractHeader {

        private static final int COMMAND_OFFSET = 0;
        private static final int DIRECT_PLAY_8_MIN_HEADER_SIZE = 1;

        private final byte command;

        public DirectPlay8Header(byte[] rawData, int offset, int length) throws IllegalRawDataException {
            if (length < DIRECT_PLAY_8_MIN_HEADER_SIZE) {
                throw new IllegalRawDataException(tooShortErrorMessage("DirectPlay8Header", DIRECT_PLAY_8_MIN_HEADER_SIZE, rawData, offset, length));
            }

            this.command = ByteArrays.getByte(rawData, COMMAND_OFFSET + offset);
        }

        public DirectPlay8Header(Builder builder) {
            this.command = builder.command;
        }

        @Override
        protected List<byte[]> getRawFields() {
            final var rawFields = new ArrayList<byte[]>();

            rawFields.add(toByteArray(command));

            return rawFields;
        }

    }

    public static final class Builder extends AbstractBuilder {

        private byte command;

        public Builder() {
        }

        public Builder command(byte command) {
            this.command = command;
            return this;
        }

        @Override
        public Packet build() {
            return new DirectPlay8Packet(this);
        }

        private Builder(DirectPlay8Packet packet) {
            this.command = packet.header.command;
        }

    }

}
