package com.siesque.dwrt;

import java.util.HashMap;

public final class Consts {
    public static final HashMap<String, String> COLOR_MAP = new HashMap<>();
    public static final String[] COLORS;

    static {
        COLOR_MAP.put("Dark Red", "§4");
        COLOR_MAP.put("Red", "§c");
        COLOR_MAP.put("Gold", "§6");
        COLOR_MAP.put("Yellow", "§e");
        COLOR_MAP.put("Dark Green", "§2");
        COLOR_MAP.put("Green", "§a");
        COLOR_MAP.put("Aqua", "§b");
        COLOR_MAP.put("Dark Aqua", "§3");
        COLOR_MAP.put("Dark Blue", "§1");
        COLOR_MAP.put("Blue", "§9");
        COLOR_MAP.put("Light Purple", "§d");
        COLOR_MAP.put("Dark Purple", "§5");
        COLOR_MAP.put("White", "§f");
        COLOR_MAP.put("Gray", "§7");
        COLOR_MAP.put("Dark Gray", "§8");
        COLOR_MAP.put("Black", "§0");

        COLORS = COLOR_MAP.keySet().toArray(new String[0]);
    }
}
