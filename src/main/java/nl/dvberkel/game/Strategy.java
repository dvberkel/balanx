package nl.dvberkel.game;

import java.util.Optional;

public interface Strategy<N> {
    Optional<N> best(N node);
}
