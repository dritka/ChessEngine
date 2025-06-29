import java.awt.*;

import javax.swing.*;
import java.awt.event.*;

import static Enums.Color.*;
import static Constants.CONST.*;
import static Enums.SoundType.*;

public class Square extends JButton implements ActionListener {
    public Piece piece;
    public Color color;
    public int row;
    public int col;

    public Square(Color color, int row, int col) {
        piece = null;
        this.color = color;
        this.row = row;
        this.col = col;
        this.setBackground(color);
        this.addActionListener(this);
    }

    public void addPiece(Piece piece) {
        this.piece = piece;
        addPieceImage(piece.imagePath);
    }

    public void addPieceImage(String imagePath) {
        this.setIcon(new ImageIcon(imagePath));
    }

    public boolean isTeamPiece(Piece piece) {
        return this.piece.pieceColor.equals(piece.pieceColor);
    }

    public boolean isEmpty() {
        return piece == null;
    }

    /*
    This method handles the case when the player
    has already clicked the piece that they want
    to move and have know chosen to move that piece
    to one of its valid squares.
     */
    private boolean checkMoveOrCaptureConditions() {
        return Board.pieceToMove != null &&
               Board.pieceToMove.canReach(this.row, this.col) &&
               (this.isEmpty() || (!this.isEmpty() &&
               !Board.pieceToMove.pieceColor.equals(this.piece.pieceColor))) &&
               (this.getBackground().equals(Board.themes.get(Board.themeIndex)[2]) ||
               this.getBackground().equals(TEMP));
    }

    private boolean checkValidMovesConditions() {
        return piece != null &&
               Board.playerTurn.equals(piece.pieceColor);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (checkValidMovesConditions()) {
            Board.startingSquare = this;
            Board.pieceToMove = piece;

            Board.refresh();

            for (int[] valid : piece.validMoves) {
                Square square = Board.board[valid[0]][valid[1]];
                if (square.piece != null && !square.piece.pieceColor.equals(piece.pieceColor))
                    square.setBackground(TEMP);
                else
                    square.setBackground(Board.themes.get(Board.themeIndex)[2]);
            }
        } else if (checkMoveOrCaptureConditions()) {
            boolean emptySquare = this.isEmpty();

            Board.playerTurn = Board.playerTurn.equals(WHITE) ?
                    BLACK : WHITE;

            Board.startingSquare.piece = null;
            Board.startingSquare.addPieceImage(null);
            Board.pieceToMove.row = this.row;
            Board.pieceToMove.col = this.col;
            this.piece = Board.pieceToMove;
            this.addPieceImage(Board.pieceToMove.imagePath);

            if (emptySquare)
                SoundEffects.playSound(MOVE);
            else
                SoundEffects.playSound(CAPTURE);
            Board.pieceToMove.moves += 1;
            Board.refresh();
            Board.calculateMoves();
        } else {
            Board.startingSquare = null;
            Board.pieceToMove = null;
            Board.refresh();
            SoundEffects.playSound(ILLEGAL);
        }
    }
}
