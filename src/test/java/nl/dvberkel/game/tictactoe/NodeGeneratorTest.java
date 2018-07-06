package nl.dvberkel.game.tictactoe;

import nl.dvberkel.game.NodeGenerator;
import nl.dvberkel.game.tictactoe.exception.DuplicateTicTacToePositionPlacementException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static nl.dvberkel.game.tictactoe.TicTacToe.Position.*;
import static nl.dvberkel.game.tictactoe.TicTacToeBuilder.board;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class NodeGeneratorTest {
    private NodeGenerator generate;

    @Before
    public void createTicTacToeMoveGenerator() {
        generate = nl.dvberkel.game.tictactoe.NodeGenerator.instance();
    }

    @Test
    public void emptyBoardShouldHaveEmptyPositionsPlayable() throws DuplicateTicTacToePositionPlacementException {
        TicTacToe emptyBoard = TicTacToe.empty();

        List<TicTacToe> nextMoves = generate.movesFor(emptyBoard);

        TicTacToeBuilder emptyBoardWith = board();
        assertThat(nextMoves, contains(
                emptyBoardWith.crossAt(NW).build(),
                emptyBoardWith.crossAt(N).build(),
                emptyBoardWith.crossAt(NE).build(),
                emptyBoardWith.crossAt(W).build(),
                emptyBoardWith.crossAt(C).build(),
                emptyBoardWith.crossAt(E).build(),
                emptyBoardWith.crossAt(SW).build(),
                emptyBoardWith.crossAt(S).build(),
                emptyBoardWith.crossAt(SE).build()
        ));
    }

    @Test
    public void boardWithOneTokenShouldHaveEmptyPositionsPlayable() throws DuplicateTicTacToePositionPlacementException {
        TicTacToeBuilder startingBoardWith = board().crossAt(C);
        TicTacToe startingBoard = startingBoardWith.build();

        List<TicTacToe> nextMoves = generate.movesFor(startingBoard);

        assertThat(nextMoves, contains(
                startingBoardWith.dotAt(NW).build(),
                startingBoardWith.dotAt(N).build(),
                startingBoardWith.dotAt(NE).build(),
                startingBoardWith.dotAt(W).build(),
                startingBoardWith.dotAt(E).build(),
                startingBoardWith.dotAt(SW).build(),
                startingBoardWith.dotAt(S).build(),
                startingBoardWith.dotAt(SE).build()
        ));
    }

    @Test
    public void boardWithTwoTokensShouldHaveEmptyPositionsPlayable() throws DuplicateTicTacToePositionPlacementException {
        TicTacToeBuilder startingBoardWith = board().crossAt(C).dotAt(NW);
        TicTacToe startingBoard = startingBoardWith.build();

        List<TicTacToe> nextMoves = generate.movesFor(startingBoard);

        assertThat(nextMoves, contains(
                startingBoardWith.crossAt(N).build(),
                startingBoardWith.crossAt(NE).build(),
                startingBoardWith.crossAt(W).build(),
                startingBoardWith.crossAt(E).build(),
                startingBoardWith.crossAt(SW).build(),
                startingBoardWith.crossAt(S).build(),
                startingBoardWith.crossAt(SE).build()
        ));
    }
}

