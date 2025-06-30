import Enums.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.Color;
import java.util.List;

import static Enums.Type.*;
import static Enums.Color.*;
import static Enums.Castle.*;
import static Enums.SoundType.*;
import static Constants.CONST.*;
import static Enums.CastleType.*;

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

        playerTurn = WHITE;
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

        /* KEY BINDINGS
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('t'), "t");
        this.getActionMap().put("t", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeTheme();
            }
        });
         */

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
        map.put(PAWN, WHITE);

        for (int col = 0; col < COLS; col++) {
            piece = new Piece(PAWN, WHITE, 6, col, 1, imagePaths.get(map));
            addPiece(piece);
        }

        map.clear();
        map.put(ROOK, WHITE);
        piece = new Piece(ROOK, WHITE, 7, 0, 5, imagePaths.get(map));
        addPiece(piece);
        piece = new Piece(ROOK, WHITE, 7, 7, 5, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(KNIGHT, WHITE);
        piece = new Piece(KNIGHT, WHITE, 7, 1, 3, imagePaths.get(map));
        addPiece(piece);
        piece = new Piece(KNIGHT, WHITE, 7, 6, 3, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(BISHOP, WHITE);
        piece = new Piece(BISHOP, WHITE, 7, 2, 3, imagePaths.get(map));
        addPiece(piece);
        piece = new Piece(BISHOP, WHITE, 7, 5, 3, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(QUEEN, WHITE);
        piece = new Piece(QUEEN, WHITE, 7, 3, 9, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(KING, WHITE);
        piece = new Piece(KING, WHITE, 7, 4, Integer.MAX_VALUE, imagePaths.get(map));
        addPiece(piece);

        map.clear();
        map.put(PAWN, BLACK);
        // CODE TO SET UP THE BLACK PIECES
        for (int col = 0; col < COLS; col++) {
            piece = new Piece(PAWN, BLACK, 1, col, 1, imagePaths.get(map));
            addPiece(piece);
        }

        map.clear();
        map.put(ROOK, BLACK);
        piece = new Piece(ROOK, BLACK, 0, 0, 5, imagePaths.get(map));
        addPiece(piece);
        piece = new Piece(ROOK, BLACK, 0, 7, 5, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(KNIGHT, BLACK);
        piece = new Piece(KNIGHT, BLACK, 0, 1, 3, imagePaths.get(map));
        addPiece(piece);
        piece = new Piece(KNIGHT, BLACK, 0, 6, 3, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(BISHOP, BLACK);
        piece = new Piece(BISHOP, BLACK, 0, 2, 3, imagePaths.get(map));
        addPiece(piece);
        piece = new Piece(BISHOP, BLACK, 0, 5, 3, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(QUEEN, BLACK);
        piece = new Piece(QUEEN, BLACK, 0, 3, 9, imagePaths.get(map));
        addPiece(piece);
        map.clear();
        map.put(KING, BLACK);
        piece = new Piece(KING, BLACK, 0, 4, Integer.MAX_VALUE, imagePaths.get(map));
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

        if (piece.pieceColor.equals(BLACK)) {
            blackPieces.add(piece);
        } else {
            whitePieces.add(piece);
        }
    }

    public static void calculateMoves() {
        for (Piece piece : whitePieces) {
            try {
                calculate(piece, WHITE);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        for (Piece piece : blackPieces) {
            try {
                calculate(piece, BLACK);
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
                if (piece.pieceType.equals(KING) && piece.castled.equals(NO)) checkForCastle();

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

    public static void movePiece(Square to) {
        boolean isCastling = isCastling(to);

        boolean isEmpty = to.isEmpty();
        startingSquare.piece = null;
        startingSquare.addPieceImage(null);
        pieceToMove.row = to.row;
        pieceToMove.col = to.col;
        to.piece = pieceToMove;
        to.addPieceImage(pieceToMove.imagePath);
        if (isCastling) {
            pieceToMove.castled = YES;
            castle(getCastlingType(to) ? KING_SIDE : QUEEN_SIDE);
        } else {
            if (isEmpty)
                SoundEffects.playSound(MOVE);
            else
                SoundEffects.playSound(CAPTURE);
        }
    }

    /*
    true -> king side castling
    false -> queen side castling
     */
    private static boolean getCastlingType(Square to) {
        return (to.row == 0 && to.col == 6) || (to.row == 7 && to.col == 6);
    }

    private static void castle(CastleType type) {
        Square rookSquare = null;
        Square to = null;
        switch (type) {
            case KING_SIDE -> {
                if (playerTurn.equals(WHITE)) {
                    rookSquare = getSquare(7, 7);
                    to = getSquare(7, 5);
                } else {
                    rookSquare = getSquare(0, 7);
                    to = getSquare(0, 5);
                }
            }
            case QUEEN_SIDE -> {
                if (playerTurn.equals(WHITE)) {
                    rookSquare = getSquare(7, 0);
                    to = getSquare(7, 3);
                } else {
                    rookSquare = getSquare(0, 0);
                    to = getSquare(0, 3);
                }
            }
        }

        startingSquare = rookSquare;
        pieceToMove = rookSquare.piece;
        movePiece(to);
        SoundEffects.playSound(CASTLE);
    }

    public static boolean isCastling(Square to) {
        Piece piece = pieceToMove;
        return piece.pieceType.equals(KING) &&
               (piece.canReach(0, 6) ||
               piece.canReach(0, 2) ||
               piece.canReach(7, 6) ||
               piece.canReach(7, 2));
    }

    private static boolean checkQueenSideCastlingConditions(Square kingSquare, Square rookSquare, Square inBetweenFirst, Square inBetweenSecond, Square inBetweenThird) {
        return kingSquare.piece.moves == 0 &&
               rookSquare.piece.moves == 0 &&
               inBetweenFirst.isEmpty() &&
               inBetweenSecond.isEmpty() &&
               inBetweenThird.isEmpty();
    }

    private static boolean checkKingSideCastlingConditions(Square kingSquare, Square rookSquare, Square inBetweenFirst, Square inBetweenSecond) {
        return kingSquare.piece.moves == 0 &&
               rookSquare.piece.moves == 0 &&
               inBetweenFirst.isEmpty() &&
               inBetweenSecond.isEmpty();
    }

    private static void checkForCastle() {
        Square firstRookSquare;
        Square secondRookSquare;
        Square kingSquare;

        if (playerTurn.equals(BLACK)) {
            firstRookSquare = board[0][0];
            secondRookSquare = board[0][7];
            kingSquare = board[0][4];

            // Get the squares in between
            Square inBetweenFirst = board[0][1];
            Square inBetweenSecond = board[0][2];
            Square inBetweenThird = board[0][3];
            Square inBetweenFourth = board[0][5];
            Square inBetweenFifth = board[0][6];

            boolean canCastleQueenSide = checkQueenSideCastlingConditions(kingSquare, firstRookSquare, inBetweenFirst, inBetweenSecond, inBetweenThird);
            boolean canCastleKingSide = checkKingSideCastlingConditions(kingSquare, secondRookSquare, inBetweenFourth, inBetweenFifth);

            if (canCastleKingSide) {
                kingSquare.piece.castled = YES;
                kingSquare.piece.addValidMove(0, 6);
            } else if (canCastleQueenSide) {
                kingSquare.piece.castled = YES;
                kingSquare.piece.addValidMove(0, 2);
            }
        } else {
            firstRookSquare = board[7][0];
            secondRookSquare = board[7][7];
            kingSquare = board[7][4];

            // Get the squares in between
            Square inBetweenFirst = board[7][1];
            Square inBetweenSecond = board[7][2];
            Square inBetweenThird = board[7][3];
            Square inBetweenFourth = board[7][5];
            Square inBetweenFifth = board[7][6];

            boolean canCastleQueenSide = checkQueenSideCastlingConditions(kingSquare, firstRookSquare, inBetweenFirst, inBetweenSecond, inBetweenThird);
            boolean canCastleKingSide = checkKingSideCastlingConditions(kingSquare, secondRookSquare, inBetweenFourth, inBetweenFifth);

            if (canCastleKingSide) {
                kingSquare.piece.castled = YES;
                kingSquare.piece.addValidMove(7, 6);
            } else if (canCastleQueenSide) {
                kingSquare.piece.castled = YES;
                kingSquare.piece.addValidMove(7, 2);
            }
        }
    }

    /*
    This method reset the colors of the chessboard to their
    original states after a move is made or when the player
    clicks another piece/empty square.
     */
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
     */
}