package be.vankerkom.sniffy.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class Device {

    private final String name;

    private final String description;

    @Builder.Default
    private final List<String> addresses = new ArrayList<>();

}
