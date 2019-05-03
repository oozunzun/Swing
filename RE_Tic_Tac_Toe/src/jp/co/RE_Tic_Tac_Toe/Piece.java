package jp.co.RE_Tic_Tac_Toe;

import javax.swing.JButton;

public class Piece extends JButton {
	/**
	 * 盤面の座標
	 */

	static int[][] square = new int[Params.ROWS][Params.COLS];

	int NULL = Params.NULL;
	int CROSS = Params.CROSS;
	int CIRCLE = Params.CIRCLE;

	/**
	 * 盤面を初期化する
	 */
	public void pieceSetting() {
		square = new int[Params.ROWS][Params.COLS];
		// ボタンを配置
		for (int x = 0; x < Params.ROWS; x++) {
			for (int y = 0; y < Params.COLS; y++) {
				square[x][y] = NULL;
			}
		}
	}

	/**
	 * １列揃っているか判定
	 * @return
	 */
	public int winChecker() {
		int check;
		int winner = NULL;

		// 横
		for (int x = 0; x < Params.ROWS; x++) {
			check = 0;
			for (int y = 0; y < Params.COLS; y++) {
				check += square[x][y];
			}
			if (check == Params.COLS) {
				winner = CIRCLE;
				break;
			} else if (check == 0 - Params.COLS) {
				winner = CROSS;
				break;
			}
		}

		// 縦
		for (int y = 0; y < Params.ROWS; y++) {
			check = 0;
			for (int x = 0; x < Params.COLS; x++) {
				check += square[x][y];
			}
			if (check == Params.COLS) {
				winner = CIRCLE;
				break;
			} else if (check == 0 - Params.COLS) {
				winner = CROSS;
				break;
			}
		}

		// 斜め
		check = 0;
		for (int x = 0; x < Params.ROWS; x++) {
			for (int y = 0; y < Params.COLS; y++) {
				if (x + y == Params.ROWS - 1) {
					check += square[x][y];
				}
			}
			if (check == Params.COLS) {
				winner = CIRCLE;
				break;
			} else if (check == 0 - Params.COLS) {
				winner = CROSS;
				break;
			}
		}

		// 斜め
		check = 0;
		for (int x = 0; x < Params.ROWS; x++) {
			for (int y = 0; y < Params.COLS; y++) {
				if (x == y) {
					check += square[x][y];
				}
			}
			if (check == Params.COLS) {
				winner = CIRCLE;
				break;
			} else if (check == 0 - Params.COLS) {
				winner = CROSS;
				break;
			}
		}
		return winner;
	}
}
