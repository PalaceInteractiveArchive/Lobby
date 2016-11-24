package com.thepalace;

import lombok.Getter;
import org.bukkit.Material;

public class ServerInfo {

    @Getter private String name;
    @Getter private String location;
    @Getter private int position;
    @Getter private Material item;

    public ServerInfo(String name, int position, Material item) {
        this(name, name, position, item);
    }

    public ServerInfo(String name, String location, int position, Material item) {
        this.name = name;
        this.location = location;
        this.position = position;
        this.item = item;
    }
}
