package nl.dvberkel.game;

import java.util.List;

public interface NodeGenerator<T> {
    List<T> movesFor(T node);
}
