package nl.dvberkel.balanx.tictactoe;

import nl.dvberkel.balanx.MoveGenerator;

import java.util.List;
import java.util.stream.Collectors;

class TicTacToeMoveGenerator implements MoveGenerator<TicTacToe> {
    public static MoveGenerator<TicTacToe> instance() {
        return new TicTacToeMoveGenerator();
    }


    @Override
    public List<TicTacToe> movesFor(TicTacToe state) {
        List<TicTacToe.Position> playable = state.playablePositions();
        TicTacToe.Token tokenToPlay = state.tokenToPlay();
        return playable
                .stream()
                .map(position -> position.place(state, tokenToPlay).get())
                .collect(Collectors.toList());
    }
}
