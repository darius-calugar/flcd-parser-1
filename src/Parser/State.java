package Parser;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class State {
    private final List<LRItem> _items;
    private Integer _hashCode; // lazily computed

    public State(List<LRItem> items) {
        // protect against modifications, in order to compute the hash only once
        this._items = Collections.unmodifiableList(items);
    }

    public List<LRItem> getItems() {
        return _items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        State state = (State) o;
        return _hashCode != null &&
               _hashCode.equals(state._hashCode) &&
               Objects.equals(_items, state._items);
    }

    @Override
    public int hashCode() {
        if (_hashCode == null)
            return _hashCode = Objects.hash(_items);
        return _hashCode;
    }

    @Override
    public String toString() {
        return _items.toString();
    }
}
