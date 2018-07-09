package nl.dvberkel.bin;

import nl.dvberkel.game.Strategy;
import nl.dvberkel.game.tictactoe.TicTacToe;
import nl.dvberkel.game.tictactoe.NodeGenerator;

import java.util.Optional;

import static nl.dvberkel.game.strategies.Random.randomStrategy;

public class AutoPlay {
    public static void main(String[] args) {
        AutoPlay game = new AutoPlay();
        game.run(randomStrategy(NodeGenerator.instance()));
    }

    private void run(Strategy<TicTacToe> strategy) {
        TicTacToe current = TicTacToe.empty();
        System.out.println(String.format("%s", current.format()));
        Optional<TicTacToe> play = strategy.best(current);
        while (!current.won().isPresent() && play.isPresent()) {
            current = play.get();
            System.out.println(String.format("%s\n", current.format()));
            play = strategy.best(current);
        }

        Optional<?> winner = current.won();
        if (winner.isPresent()) {
            System.out.println(String.format("Won by %s", winner.get()));
        } else {
            System.out.println("draw");
        }
    }
}
