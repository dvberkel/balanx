package nl.dvberkel.game.tictactoe;

import java.util.List;
import java.util.stream.Collectors;

class NodeGenerator implements nl.dvberkel.game.NodeGenerator<TicTacToe> {
    public static nl.dvberkel.game.NodeGenerator instance() {
        return new nl.dvberkel.game.tictactoe.NodeGenerator();
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
