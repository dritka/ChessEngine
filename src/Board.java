import Constants.CONST;
import Enums.*;

import java.awt.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.Color;
import java.util.List;
import java.awt.event.*;

import static Enums.Type.*;
import static Enums.SoundType.*;
import static Constants.CONST.*;

public class Board extends JPanel {
    public static Enums.Color playerTurn;
    public static Square[][] board;

    public static List<Piece> whitePieces;
    public static List<Piece> blackPieces;

    public static Piece pieceToMove;
    public static Square startingSquare;


    public static Map<Map<Type, Enums.Color>, String> imagePaths;

    public static List<Color[]> themes;
    public static Color[] theme;
    public static int themeIndex;

    public Board() {
        PreProcess.loadThemes();
        PreProcess.loadImages();

        playerTurn = Enums.Color.WHITE;
        board = new Square[ROWS][COLS];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        pieceToMove = null;
        startingSquare = null;
        themeIndex = 0;
        theme = themes.get(themeIndex);

        this.setPreferredSize(new Dimension(CONST_WIDTH, CONST_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(ROWS, COLS));
        this.setFocusable(true);

        // KEY BINDINGS
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('t'), "t");
        this.getActionMap().put("t", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeTheme();
            }
        });

        setup();
        SoundEffects.playSound(GAME_START);
    }

    private void setup() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Square square = squareSetup(row, col);
                board[row][col] = square;
                this.add(square);
            }
        }


        // CODE TO SET UP THE WHITE PIECES
        Piece piece;
        HashMap<Type, Enums.Color> map = new HashMap<>();
        map.put(Type.PAWN, Enums.Color.WHITE);

        for (int col = 0; col < COLS; col++) {
            piece = new Piece(Type.PAWN, Enums.Color.WHITE, 6, col, 1, imagePaths.get(map));
            addPiece(piece);
        }

        map.clear();
        map.put(Type.ROOK, Enums.Color.WHITE);
        piece = new Piece(Type.ROOK, Enums.Color.WHITE, 7, 0, 5, imagePaths.get(map));
        addPiece(piece);
        piece = new Piece(Type.ROOK, Enums.Color.WHITE, 7, 7, 5, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(Type.KNIGHT, Enums.Color.WHITE);
        piece = new Piece(Type.KNIGHT, Enums.Color.WHITE, 7, 1, 3, imagePaths.get(map));
        addPiece(piece);
        piece = new Piece(Type.KNIGHT, Enums.Color.WHITE, 7, 6, 3, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(Type.BISHOP, Enums.Color.WHITE);
        piece = new Piece(Type.BISHOP, Enums.Color.WHITE, 7, 2, 3, imagePaths.get(map));
        addPiece(piece);
        piece = new Piece(Type.BISHOP, Enums.Color.WHITE, 7, 5, 3, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(Type.QUEEN, Enums.Color.WHITE);
        piece = new Piece(Type.QUEEN, Enums.Color.WHITE, 7, 3, 9, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(KING, Enums.Color.WHITE);
        piece = new Piece(KING, Enums.Color.WHITE, 7, 4, Integer.MAX_VALUE, imagePaths.get(map));
        addPiece(piece);

        map.clear();
        map.put(Type.PAWN, Enums.Color.BLACK);
        // CODE TO SET UP THE BLACK PIECES
        for (int col = 0; col < COLS; col++) {
            piece = new Piece(Type.PAWN, Enums.Color.BLACK, 1, col, 1, imagePaths.get(map));
            addPiece(piece);
        }

        map.clear();
        map.put(Type.ROOK, Enums.Color.BLACK);
        piece = new Piece(Type.ROOK, Enums.Color.BLACK, 0, 0, 5, imagePaths.get(map));
        addPiece(piece);
        piece = new Piece(Type.ROOK, Enums.Color.BLACK, 0, 7, 5, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(Type.KNIGHT, Enums.Color.BLACK);
        piece = new Piece(Type.KNIGHT, Enums.Color.BLACK, 0, 1, 3, imagePaths.get(map));
        addPiece(piece);
        piece = new Piece(Type.KNIGHT, Enums.Color.BLACK, 0, 6, 3, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(Type.BISHOP, Enums.Color.BLACK);
        piece = new Piece(Type.BISHOP, Enums.Color.BLACK, 0, 2, 3, imagePaths.get(map));
        addPiece(piece);
        piece = new Piece(Type.BISHOP, Enums.Color.BLACK, 0, 5, 3, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(Type.QUEEN, Enums.Color.BLACK);
        piece = new Piece(Type.QUEEN, Enums.Color.BLACK, 0, 3, 9, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(KING, Enums.Color.BLACK);
        piece = new Piece(KING, Enums.Color.BLACK, 0, 4, Integer.MAX_VALUE, imagePaths.get(map));
        addPiece(piece);

        calculateMoves();
    }

    // Helper methods for the setup method, thus it's private
    private static Square squareSetup(int row, int col) {
        Square square;

        if (row % 2 == 0) {
            if (col % 2 == 0) {
                square = new Square(theme[1], row, col);
            } else {
                square = new Square(theme[0], row, col);
            }
        } else {
            if (col % 2 == 0) {
                square = new Square(theme[0], row, col);
            } else {
                square = new Square(theme[1], row, col);
            }
        }
        return square;
    }

    public static Square getSquare(int row, int col) {
        return board[row][col];
    }

    private void addPiece(Piece piece) {
        Square square = board[piece.row][piece.col];
        square.addPiece(piece);

        if (piece.pieceColor.equals(Enums.Color.BLACK)) {
            blackPieces.add(piece);
        } else {
            whitePieces.add(piece);
        }
    }

    public static void calculateMoves() {
        for (Piece piece : whitePieces) {
            try {
                calculate(piece, Enums.Color.WHITE);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        for (Piece piece : blackPieces) {
            try {
                calculate(piece, Enums.Color.BLACK);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void calculate(Piece piece, Enums.Color color) throws Exception {
        piece.clearValidMoves();
        int[][] directions = piece.directions;

        switch (piece.pieceType) {
            case PAWN -> {
                int[] direction = directions[0];
                int newRow = piece.row + direction[0];
                int newCol = piece.col + direction[1];
                if (isValidMove(newRow, newCol, color)) {
                    piece.addValidMove(newRow, newCol);

                    direction = directions[1];
                    newRow = piece.row + direction[0];
                    newCol = piece.col + direction[1];
                    Square square = getSquare(newRow, newCol);
                    if (piece.moves == 0 && square.isEmpty())
                        piece.addValidMove(newRow, newCol);
                }

                direction = directions[2];
                newRow = piece.row + direction[0];
                newCol = piece.col + direction[1];
                if (isValidMove(newRow, newCol, color) && !getSquare(newRow, newCol).isEmpty())
                    piece.addValidMove(newRow, newCol);

                direction = directions[3];
                newRow = piece.row + direction[0];
                newCol = piece.col + direction[1];
                if (isValidMove(newRow, newCol, color) && !getSquare(newRow, newCol).isEmpty())
                    piece.addValidMove(newRow, newCol);
            }

            case KING, KNIGHT -> {
                if (piece.pieceType.equals(KING)) // checkCastle();

                for (int[] dir : directions) {
                    int newRow = piece.row + dir[0];
                    int newCol = piece.col + dir[1];

                    if (isValidMove(newRow, newCol, color))
                        piece.addValidMove(newRow, newCol);
                }
            }

            case BISHOP, ROOK, QUEEN -> {
                for (int[] dir : directions) {
                    int multiplier = 1;

                    while (isInBounds(piece.row + dir[0] * multiplier, piece.col + dir[1] * multiplier)) {
                        int row = piece.row + dir[0] * multiplier;
                        int col = piece.col + dir[1] * multiplier;
                        Square square = getSquare(row, col);
                        if (!square.isEmpty()) break;
                        piece.addValidMove(row, col);
                        multiplier++;
                    }

                    int row = piece.row + dir[0] * multiplier;
                    int col = piece.col + dir[1] * multiplier;
                    if (isValidMove(row, col, color))
                        piece.addValidMove(row, col);
                }
            }

            default -> throw new Exception("Unexpected value: " + piece.pieceType); // Should never occur
        }
    }

    private static boolean isInBounds(int row, int col) {
        return (row >= 0 && row < ROWS) && (col >= 0 && col < COLS);
    }

    private static boolean isValidSquare(int row, int col, Enums.Color color) {
        return board[row][col].isEmpty() ||
               (!board[row][col].isEmpty() &&
               !board[row][col].piece.pieceColor.equals(color));
    }

    private static boolean isValidMove(int row, int col, Enums.Color color) {
        return isInBounds(row, col) && isValidSquare(row, col, color);
    }

    private void changeTheme() {
        themeIndex++;
        if (themeIndex == 7) themeIndex = 0;
        theme = themes.get(themeIndex);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Square square = board[row][col];

                if (row % 2 == 0) {
                    if (col % 2 == 0) {
                        square.setBackground(theme[1]);
                        square.color = theme[1];
                    } else {
                        square.setBackground(theme[0]);
                        square.color = theme[0];
                    }
                } else {
                    if (col % 2 == 0) {
                        square.setBackground(theme[0]);
                        square.color = theme[0];
                    } else {
                        square.setBackground(theme[1]);
                        square.color = theme[1];
                    }
                }
            }
        }
    }

    public static void refresh() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
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

    /*
    private void checkForCastle() {
        Square firstRookSquare;
        Square secondRookSquare;
        Square kingSquare;

        if (playerTurn.equals(Enums.Color.BLACK)) {
            firstRookSquare = board[7][0];
            secondRookSquare = board[7][7];
            kingSquare = board[7][4];
        } else {
            firstRookSquare = board[0][0];
            secondRookSquare = board[0][7];
            kingSquare = board[0][4];
        }
    }
     */
}