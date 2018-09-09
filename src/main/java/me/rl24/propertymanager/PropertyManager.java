package me.rl24.propertymanager;

import java.util.HashMap;
import java.util.Map;

public class PropertyManager {

    private static final Map<Class<? extends Property>, Property> PROPERTY_MAP = new HashMap<>();

    static void register(Property property) {
        PROPERTY_MAP.put(property.getClass(), property);
    }

    public static <T extends Property> T getProperty(Class<T> cls) {
        return cls.cast(PROPERTY_MAP.get(cls));
    }

}
