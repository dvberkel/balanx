package nl.dvberkel.game;

import java.util.List;

public interface Node<T, P> {
    List<P> playablePositions();

    T tokenToPlay();
}
