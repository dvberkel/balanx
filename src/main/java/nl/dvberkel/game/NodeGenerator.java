package nl.dvberkel.game;

import java.util.List;

public interface NodeGenerator<N> {
    List<N> movesFor(N node);
}
