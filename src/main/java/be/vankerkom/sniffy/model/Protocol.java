package be.vankerkom.sniffy.model;

import lombok.Value;

import java.util.UUID;

@Value
public class Protocol {

    UUID id;
    String name;
    String description;
    String filter;

}
