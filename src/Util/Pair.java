package Util;

import java.util.Objects;

public record Pair<T1, T2>(T1 item1, T2 item2) {
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(item1, pair.item1) && Objects.equals(item2, pair.item2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item1, item2);
    }

    @Override
    public String toString() {
        return "(" + item1 +
               ", " + item2 +
               ")";
    }
}
