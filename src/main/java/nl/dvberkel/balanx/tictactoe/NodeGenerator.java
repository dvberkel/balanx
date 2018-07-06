package nl.dvberkel.balanx.tictactoe;

import java.util.List;
import java.util.stream.Collectors;

class NodeGenerator implements nl.dvberkel.balanx.NodeGenerator<TicTacToe> {
    public static nl.dvberkel.balanx.NodeGenerator instance() {
        return new nl.dvberkel.balanx.tictactoe.NodeGenerator();
    }


    @Override
    public List<TicTacToe> movesFor(TicTacToe node) {
        List<TicTacToe.Position> playable = node.playablePositions();
        TicTacToe.Token tokenToPlay = node.tokenToPlay();
        return playable
                .stream()
                .map(position -> position.place(node, tokenToPlay).get())
                .collect(Collectors.toList());
    }
}
