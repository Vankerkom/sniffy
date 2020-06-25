package be.vankerkom.sniffy.mappers;

import be.vankerkom.sniffy.dto.Device;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapNetworkInterface;

import static java.util.stream.Collectors.toList;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Mapping(target = "addresses", ignore = true)
    Device toDto(PcapNetworkInterface networkInterface);

    @AfterMapping
    default void addAddresses(PcapNetworkInterface networkInterface, @MappingTarget Device deviceDto) {
        final var addresses = networkInterface.getAddresses()
                .stream()
                .map(PcapAddress::getAddress)
                .map(Object::toString)
                .collect(toList());

        deviceDto.getAddresses().addAll(addresses);
    }

}
