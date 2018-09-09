# Property Manager
![](https://forthebadge.com/images/badges/designed-in-ms-paint.svg)

A simple Java based property manager

### Usage:

```java
@PropertyDescriptor(id = "example", name = "Example", description = "An example class property")
class Example extends Property {

    @PropertyDescriptor(id = "volume", name = "Volume", description = "An example field property")
    public int volume = 50;

    public Example() {
        super();
    }
    
    @Override
    protected void onValueChange(Property property, Object oldValue, Object newValue) {
        // Handle value changing
    }
}
```