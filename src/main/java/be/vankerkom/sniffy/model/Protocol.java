package be.vankerkom.sniffy.model;

import lombok.Value;

@Value
public class Protocol {

    int id;
    String name;
    String description;
    String filter;

}
