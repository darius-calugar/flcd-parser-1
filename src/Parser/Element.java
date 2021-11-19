package Parser;

import java.util.Objects;

public abstract class Element {
    public final String value;

    public Element(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Element element = (Element) o;
        return Objects.equals(value.toLowerCase(), element.value.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
