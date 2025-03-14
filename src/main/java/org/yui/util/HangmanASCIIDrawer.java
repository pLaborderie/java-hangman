package org.yui.util;

import java.util.List;

public class HangmanASCIIDrawer {
    private static final String[] HANGMAN_STAGES = {
            """
+---+
|   |
O   |
/|\\  |
/ \\  |
    |
=========
""",

            """
+---+
|   |
O   |
/|\\  |
/    |
    |
=========
""",

            """
+---+
|   |
O   |
/|\\  |
    |
    |
=========
""",

            """
+---+
|   |
O   |
/|   |
    |
    |
=========
""",

            """
+---+
|   |
O   |
|   |
    |
    |
=========
""",

            """
+---+
|   |
O   |
    |
    |
    |
=========
""",

            """
+---+
|   |
    |
    |
    |
    |
=========
"""
    };

    public static String drawHangman(int livesLeft) {
        return HANGMAN_STAGES[livesLeft];
    }
}
