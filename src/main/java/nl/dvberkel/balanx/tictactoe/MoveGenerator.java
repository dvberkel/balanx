package nl.dvberkel.balanx.tictactoe;

import java.util.List;
import java.util.stream.Collectors;

class MoveGenerator implements nl.dvberkel.balanx.MoveGenerator<TicTacToe> {
    public static nl.dvberkel.balanx.MoveGenerator instance() {
        return new MoveGenerator();
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
