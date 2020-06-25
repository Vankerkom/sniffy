package be.vankerkom.sniffy.mappers;

import be.vankerkom.sniffy.dto.ProtocolDto;
import be.vankerkom.sniffy.model.Protocol;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProtocolMapper {

    ProtocolDto toDto(Protocol protocol);

}
