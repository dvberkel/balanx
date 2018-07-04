package nl.dvberkel.balanx;

import java.util.List;

public interface MoveGenerator<T> {
    List<T> movesFor(T state);
}
