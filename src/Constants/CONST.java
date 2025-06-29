package Constants;

import java.awt.*;

public class CONST {
    public static final int ROWS = 8;
    public static final int COLS = 8;
    public static final int squareSize = 100;
    public static final int WIDTH = squareSize * COLS;
    public static final int HEIGHT = squareSize * ROWS;
    public static final Color TEMP = new Color(235, 61, 61);

    // Sound effects paths
    public static final String MOVE_SOUND = "sounds\\move-self.wav";
    public static final String CAPTURE_SOUND = "sounds\\capture.wav";
    public static final String MOVE_CHECK_SOUND = "sounds\\move-check.wav";
    public static final String PROMOTE_SOUND = "sounds\\promote.wav";
    public static final String ILLEGAL_SOUND = "sounds\\illegal.wav";
    public static final String CASTLE_SOUND = "sounds\\castle.wav";
    public static final String GAME_START_SOUND = "sounds\\game-start.wav";

    // Config file paths
    public static final String BOARD_THEMES = "config\\board_themes.txt";
    public static final String DEFAULT = "config\\piece_themes\\default.txt";
}
