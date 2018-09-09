package me.rl24.propertymanager;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Property {

    private PropertyDescriptor descriptor;
    private final Map<String, Property> children = new HashMap<>();

    private Property parent;
    private Field field;

    public Property() {
        this.descriptor = getClass().getAnnotation(PropertyDescriptor.class);

        PropertyManager.register(this);

        Arrays.stream(getClass().getFields()).filter((f) -> f.isAnnotationPresent(PropertyDescriptor.class)).collect(Collectors.toList()).forEach((f) -> {
            Property child = new Property(this, f);
            registerChild(child);
        });
    }

    public Property(Property parent, Field field) {
        this.parent = parent;
        this.field = field;

        this.descriptor = field.getAnnotation(PropertyDescriptor.class);

        parent.registerChild(this);
    }

    public PropertyDescriptor getDescriptor() {
        return descriptor;
    }

    public String getId() {
        return !getDescriptor().parent().isEmpty() ? String.format("%s.%s", getDescriptor().parent(), getDescriptor().id()) : getDescriptor().id();
    }

    public String getName() {
        return getDescriptor().name();
    }

    public String getDescription() {
        return getDescriptor().description();
    }

    public Property getParent() {
        return parent;
    }

    private void setParent(Property parent) {
        this.parent = parent;
    }

    public Map<String, Property> getChildren() {
        return children;
    }

    public Property getChild(String id) {
        return getChildren().get(id);
    }

    private void registerChild(Property child) {
        this.getChildren().put(child.getId(), child);
    }

    public <T> T getFieldValue(Class<T> cls) throws IllegalAccessException {
        return cls.cast(field.get(getParent() != null ? getParent() : this));
    }

    public <T> void setFieldValue(T value) throws IllegalAccessException {
        Object oldValue = getFieldValue(Object.class);
        if (getParent() != null) {
            field.set(getParent(), value);
        } else {
            field.set(this, value);
        }
        onValueChange(this, oldValue, value);
    }

    protected void onValueChange(Property property, Object oldValue, Object newValue) {
        if (getParent() != null) {
            getParent().onValueChange(property, oldValue, newValue);
        }
    }

}
