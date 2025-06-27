import Enums.*;

import java.util.*;

public class Piece {
    public Type pieceType;
    public Color pieceColor;
    public int row;
    public int col;
    public int value;
    public String imagePath;
    public List<int[]> validMoves;
    public int[][] directions;

    public Piece(Type pieceType, Color pieceColor, int row, int col, int value, String imagePath) {
        this.pieceType = pieceType;
        this.pieceColor = pieceColor;
        this.row = row;
        this.col = col;
        this.value = value;
        this.imagePath = imagePath;
        validMoves = new ArrayList<>();
        directions = calculateDirections();
    }

    public int[][] calculateDirections() {
        switch (pieceType) {
            case PAWN -> {
                switch (pieceColor) {
                    case WHITE -> {
                        return new int[][] { {-1, 0}, {-2, 0}, {-1, 1}, {-1, -1} };
                    }
                    case BLACK -> {
                        return new int[][] { {1, 0}, {2, 0}, {1, 1}, {1, -1} };
                    }
                }
            }

            case KNIGHT -> {
                return new int[][] { {2, 1}, {2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {-2, 1}, {-2, -1} };
            }

            case BISHOP -> {
                return new int[][] { {1, 1}, {-1, 1}, {-1, -1}, {1, -1} };
            }

            case ROOK -> {
                return new int[][] { {1, 0}, {0, 1}, {-1, 0}, {0, -1} };
            }

            case QUEEN, KING -> {
                return new int[][] { {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1} };
            }
        }

        return null;
    }

    public boolean canReach(int row, int col) {
        for (int[] valid : validMoves) {
            if (valid[0] == row && valid[1] == col) return true;
        }

        return false;
    }

    public void addValidMove(int row, int col) {
        validMoves.add(new int[] {row, col});
    }

    public void clearValidMoves() {
        validMoves.clear();
    }
}
