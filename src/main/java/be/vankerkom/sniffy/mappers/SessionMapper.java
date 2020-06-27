package be.vankerkom.sniffy.mappers;

import be.vankerkom.sniffy.dto.SessionDto;
import be.vankerkom.sniffy.events.SessionCreateEvent;
import be.vankerkom.sniffy.model.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    @Mapping(target = "protocolId", source = "protocol.id")
    SessionDto toDto(Session session);

    List<SessionDto> toDtos(Iterable<Session> sessions);

    @Mapping(target = "protocolId", source = "protocol.id")
    SessionCreateEvent toCreateEvent(Session session);
}
