package nl.dvberkel.game;

import nl.dvberkel.game.tictactoe.TicTacToe;

import java.util.List;

public interface Node<T, P> {
    List<P> playablePositions();

    T tokenToPlay();
}
