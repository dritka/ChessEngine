import java.awt.*;

import Constants.*;

import javax.swing.*;
import java.awt.event.*;

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.getBackground().equals(Board.themes.get(Board.themeIndex)[2])) {
            if (Board.pieceToMove != null) {
                if (Board.pieceToMove.canReach(this.row, this.col)) {
                    if (this.isEmpty() || (!this.isEmpty() && !Board.pieceToMove.pieceColor.equals(this.piece.pieceColor))) {
                        boolean emptySquare = this.isEmpty();

                        if (Board.playerTurn.equals(Enums.Color.WHITE)) {
                            Board.playerTurn = Enums.Color.BLACK;
                        } else {
                            Board.playerTurn = Enums.Color.WHITE;
                        }

                        Board.startingSquare.piece = null;
                        Board.startingSquare.addPieceImage(null);
                        Board.pieceToMove.row = this.row;
                        Board.pieceToMove.col = this.col;
                        this.piece = Board.pieceToMove;
                        this.addPieceImage(Board.pieceToMove.imagePath);

                        if (emptySquare) {
                            Board.playMoveSound();
                        } else {
                            Board.playCaptureSound();
                        }

                        for (int row = 0; row < CONST.ROWS; row++) {
                            for (int col = 0; col < CONST.COLS; col++) {
                                Square square = Board.board[row][col];
                                Color color = square.color;
                                if (square.piece == null) square.addPieceImage(null);
                                square.setBackground(color);
                                Board.pieceToMove = null;
                            }
                        }

                        Board.calculateMoves();
                    }
                }
            }
        } else if (piece != null) {
            if (Board.playerTurn.equals(piece.pieceColor)) {
                Board.startingSquare = this;
                Board.pieceToMove = piece;

                for (int row = 0; row < CONST.ROWS; row++) {
                    for (int col = 0; col < CONST.COLS; col++) {
                        Square square = Board.board[row][col];
                        Piece piece = square.piece;
                        Color color = square.color;

                        if (piece != null) {
                            square.addPieceImage(piece.imagePath);
                        } else {
                            square.addPieceImage(null);
                        }

                        square.setBackground(color);
                    }
                }

                for (int[] valid : piece.validMoves) {
                    Square square = Board.board[valid[0]][valid[1]];

                    if (square.isEmpty() || (!square.isEmpty() && !square.isTeamPiece(piece))) {
                        square.setBackground(Board.themes.get(Board.themeIndex)[2]);
                    }
                }
            }
        } else {
            Board.startingSquare = null;
            Board.pieceToMove = null;

            for (int row = 0; row < CONST.ROWS; row++) {
                for (int col = 0; col < CONST.COLS; col++) {
                    Square square = Board.board[row][col];
                    Piece piece = square.piece;
                    Color color = square.color;

                    if (piece != null) {
                        square.addPieceImage(piece.imagePath);
                    } else {
                        square.addPieceImage(null);
                    }

                    square.setBackground(color);
                }
            }
        }
    }
}
