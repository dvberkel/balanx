package nl.dvberkel.game;

import java.util.Optional;

public interface Playable<N, T> {
    Optional<N> place(N node, T token);
}
