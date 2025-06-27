import Enums.*;

import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.Color;
import java.util.List;

import Constants.CONST;

import java.awt.event.*;
import java.util.ArrayList;
import javax.sound.sampled.*;

public class Board extends JPanel {
    public static Enums.Color playerTurn;
    public static Square[][] board;

    public static List<Piece> whitePieces;
    public static List<Piece> blackPieces;

    public static Piece pieceToMove;
    public static Square startingSquare;


    public HashMap<HashMap<Type, Enums.Color>, String> imagePaths;

    public static List<Color[]> themes;
    public static Color[] theme;
    public static int themeIndex;

    public Board() {
        loadThemes();
        loadImages();

        playerTurn = Enums.Color.WHITE;
        board = new Square[CONST.ROWS][CONST.COLS];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        pieceToMove = null;
        startingSquare = null;
        themeIndex = 0;
        theme = themes.get(themeIndex);

        this.setPreferredSize(new Dimension(CONST.WIDTH, CONST.HEIGHT));
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(CONST.ROWS, CONST.COLS));
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
    }

    private void setup() {
        for (int row = 0; row < CONST.ROWS; row++) {
            for (int col = 0; col < CONST.COLS; col++) {
                Square square = squareSetup(row, col);
                board[row][col] = square;
                this.add(square);
            }
        }


        // CODE TO SET UP THE WHITE PIECES
        Piece piece;
        HashMap<Type, Enums.Color> map = new HashMap<>();
        map.put(Type.PAWN, Enums.Color.WHITE);

        for (int col = 0; col < CONST.COLS; col++) {
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
        map.put(Type.KING, Enums.Color.WHITE);
        piece = new Piece(Type.KING, Enums.Color.WHITE, 7, 4, Integer.MAX_VALUE, imagePaths.get(map));
        addPiece(piece);

        map.clear();
        map.put(Type.PAWN, Enums.Color.BLACK);
        // CODE TO SET UP THE BLACK PIECES
        for (int col = 0; col < CONST.COLS; col++) {
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
        map.put(Type.KING, Enums.Color.BLACK);
        piece = new Piece(Type.KING, Enums.Color.BLACK, 0, 4, Integer.MAX_VALUE, imagePaths.get(map));
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
            calculate(piece);
        }

        for (Piece piece : blackPieces) {
            calculate(piece);
        }
    }

    public static void calculate(Piece piece) {
        piece.clearValidMoves();
        int[][] directions = piece.directions;

        switch (piece.pieceType) {
            case PAWN -> {
                int[] direction = directions[0];
                int newRow = piece.row + direction[0];
                int newCol = piece.col + direction[1];
                Square square = getSquare(newRow, newCol);
                if (square.isEmpty()) {
                    piece.addValidMove(newRow, newCol);

                    direction = directions[1];
                    newRow = piece.row + direction[0];
                    newCol = piece.col + direction[1];
                    square = getSquare(newRow, newCol);
                    if ((piece.row == 6 || piece.row == 1) && square.isEmpty()) {
                        piece.addValidMove(newRow, newCol);
                    }
                }

                direction = directions[2];
                newRow = piece.row + direction[0];
                newCol = piece.col + direction[1];
                if (isInBounds(newRow, newCol)) {
                    square = getSquare(newRow, newCol);
                    if (!square.isEmpty() && !square.isTeamPiece(piece)) {
                        piece.addValidMove(newRow, newCol);
                    }
                }

                direction = directions[3];
                newRow = piece.row + direction[0];
                newCol = piece.col + direction[1];
                if (isInBounds(newRow, newCol)) {
                    square = getSquare(newRow, newCol);
                    if (!square.isEmpty() && !square.isTeamPiece(piece)) {
                        piece.addValidMove(newRow, newCol);
                    }
                }
            }

            case KNIGHT, KING -> {
                for (int[] dir : directions) {
                    int newRow = piece.row + dir[0];
                    int newCol = piece.col + dir[1];

                    if (isInBounds(newRow, newCol)) {
                        Square square = getSquare(newRow, newCol);
                        if (square.isEmpty() || (!square.isEmpty() && !square.isTeamPiece(piece))) {
                            piece.addValidMove(newRow, newCol);
                        }
                    }
                }
            }

            case BISHOP, ROOK, QUEEN -> {
                for (int[] dir : directions) {
                    int multiplier = 1;

                    while (isInBounds(piece.row + dir[0] * multiplier, piece.col + dir[1] * multiplier) && getSquare(piece.row + dir[0] * multiplier, piece.col + dir[1] * multiplier).isEmpty()) {
                        piece.addValidMove(piece.row + dir[0] * multiplier, piece.col + dir[1] * multiplier);
                        multiplier++;
                    }

                    if (isInBounds(piece.row + dir[0] * multiplier, piece.col + dir[1] * multiplier)) {
                        int newRow = piece.row + dir[0] * multiplier;
                        int newCol = piece.col + dir[1] * multiplier;
                        Square square = getSquare(newRow, newCol);
                        if (!square.isTeamPiece(piece)) {
                            piece.addValidMove(piece.row + dir[0] * multiplier, piece.col + dir[1] * multiplier);
                        }
                    }
                }
            }

            default -> throw new IllegalStateException("Unexpected value: " + piece.pieceType);
        }
    }

    public static boolean isInBounds(int row, int col) {
        return (row >= 0 && row < CONST.ROWS) && (col >= 0 && col < CONST.COLS);
    }

    private void loadThemes() {
        themes = new ArrayList<>();

        try (Scanner scanner = new Scanner(new FileReader("D:\\Programming\\Java\\current-java-masterclass-remaster\\Chess\\src\\config\\board_themes.txt"))) {
            scanner.useDelimiter(",");

            while (scanner.hasNextLine()) {
                String[] dark = scanner.next().split(" ");
                scanner.skip(scanner.delimiter());
                String[] light = scanner.next().split(" ");
                scanner.skip(scanner.delimiter());
                String[] move = scanner.nextLine().split(" ");

                Color darkColor = new Color(Integer.parseInt(dark[0]), Integer.parseInt(dark[1]), Integer.parseInt(dark[2]));
                Color lightColor = new Color(Integer.parseInt(light[0]), Integer.parseInt(light[1]), Integer.parseInt(light[2]));
                Color moveColor = new Color(Integer.parseInt(move[0]), Integer.parseInt(move[1]), Integer.parseInt(move[2]));
                Color[] theme = new Color[]{darkColor, lightColor, moveColor};
                themes.add(theme);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found exception: " + e.getMessage());
        }
    }

    private void loadImages() {
        imagePaths = new HashMap<>();

        try (Scanner scanner = new Scanner(new FileReader("D:\\Programming\\Java\\current-java-masterclass-remaster\\Chess\\src\\config\\piece_themes\\default.txt"))) {
            scanner.useDelimiter(",");

            while (scanner.hasNextLine()) {
                Type pieceType = Type.valueOf(scanner.next());
                scanner.skip(scanner.delimiter());
                Enums.Color pieceColor = Enums.Color.valueOf(scanner.next());
                scanner.skip(scanner.delimiter());
                String imagePath = scanner.nextLine();
                HashMap<Type, Enums.Color> config = new HashMap<>();
                config.put(pieceType, pieceColor);
                imagePaths.put(config, imagePath);
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e.getMessage());
        }
    }

    private void changeTheme() {
        themeIndex++;
        if (themeIndex == 7) themeIndex = 0;
        theme = themes.get(themeIndex);

        for (int row = 0; row < CONST.ROWS; row++) {
            for (int col = 0; col < CONST.COLS; col++) {
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

    public static void playMoveSound() {
        File file = new File("D:\\Programming\\Java\\current-java-masterclass-remaster\\Chess\\src\\sounds\\move-self.wav");

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public static void playCaptureSound() {
        File file = new File("D:\\Programming\\Java\\current-java-masterclass-remaster\\Chess\\src\\sounds\\capture.wav");

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
