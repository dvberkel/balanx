package nl.dvberkel.balanx.tictactoe;

import nl.dvberkel.balanx.tictactoe.exception.DuplicateTicTacToePositionPlacementException;

import java.util.Optional;

public class TicTacToeBuilder {
    public static TicTacToeBuilder board() {
        return new TicTacToeBuilder();
    }

    private final TicTacToe board;

    private TicTacToeBuilder() {
        this.board = TicTacToe.empty();
    }

    private TicTacToeBuilder(TicTacToe board) {
        this.board = board;
    }

    public TicTacToeBuilder crossAt(TicTacToe.Position position) throws DuplicateTicTacToePositionPlacementException {
        return place(position, TicTacToe.Token.Cross);
    }

    public TicTacToeBuilder dotAt(TicTacToe.Position position) throws DuplicateTicTacToePositionPlacementException {
        return place(position, TicTacToe.Token.Dot);
    }

    private TicTacToeBuilder place(TicTacToe.Position position, TicTacToe.Token token) throws DuplicateTicTacToePositionPlacementException {
        Optional<TicTacToe> candidate = position.place(this.board, token);
        if (candidate.isPresent()) {
            return new TicTacToeBuilder(candidate.get());
        } else {
            throw new DuplicateTicTacToePositionPlacementException(position);
        }
    }

    public TicTacToe build() {
        return this.board;
    }
}
