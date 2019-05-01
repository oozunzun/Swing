package jp.co.Othello;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import jp.co.Othello.Params.color;

public class Piece extends JButton {
	static int ROWS = Params.ROWS;
	static int COLS = Params.COLS;
	static int[] points[] = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };
	color NULL = Params.color.NULL;
	color BLACK = Params.color.BLACK;
	color WHITE = Params.color.WHITE;

	static color[][] square = new color[ROWS][COLS];

	public void pieceSetting() {

		// ボタンを配置
		for (int x = 0; x < ROWS; x++) {
			for (int y = 0; y < COLS; y++) {
				square[x][y] = NULL;
			}
		}

		// 初期配置を設定
		square[3][3] = square[4][4] = WHITE;
		square[3][4] = square[4][3] = BLACK;
	}

	public List<Integer> isReversePieces(int setX, int setY) {
		List<Integer> flgs = new ArrayList<>();

		// 8方向検索
		for (int i[] : points) {
			int checkFlg = 0; // 0：チェック開始、1：チェック中、2：反転可能、3：反転不可

			int x = i[0];
			int y = i[1];
			int checkX = setX + x;
			int checkY = setY + y;

			// 反転可能かチェック
			while (checkFlg < 3) {
				if (checkX >= 0 && checkX < ROWS && checkY >= 0 && checkY < ROWS) { // 盤外判定
					color checkPoint = square[checkX][checkY];

					if (checkPoint == NULL) {
						checkFlg = 3;
					} else if (checkPoint != Turn.turn) {
						checkFlg = 1;
						checkX += x;
						checkY += y;
					} else {
						if (checkFlg == 1) {
							checkFlg = 2;
							break;
						} else {
							checkFlg = 3;
						}
					}
				} else {
					checkFlg = 3;
				}
			}
			flgs.add(checkFlg);
		}
		return flgs;
	}

	public void turnPiece(int setX, int setY, int isReverse) {
		int[] point = points[isReverse];
		int x = point[0];
		int y = point[1];
		int checkX = setX + x;
		int checkY = setY + y;

		// 自分の色が出てくるまで反転
		while (true) {
			square[checkX][checkY] = Turn.turn;
			if (square[checkX + x][checkY + y] != Turn.turn) {
				checkX += x;
				checkY += y;
			} else {
				break;
			}
		}
	}

	public boolean isSkipTurn() {
		boolean skipFlg = true;

		// 反転できるマスが存在するかチェック
		for (int x = 0; x < ROWS; x++) {
			for (int y = 0; y < COLS; y++) {
				if (square[x][y] == NULL && isReversePieces(x, y).contains(2)) {
					skipFlg = false;
				}
			}
		}
		return skipFlg;
	}

	public int[] pieceCollorCount() {
		int black = 0;
		int white = 0;
		int[] count = { 0, 0 };

		// 盤面の色を数える
		for (int x = 0; x < ROWS; x++) {
			for (int y = 0; y < COLS; y++) {
				switch (square[x][y]) {
				case BLACK:
					black++;
					break;
				case WHITE:
					white++;
					break;
				default:
				}
			}
		}
		count[0] = black;
		count[1] = white;
		return count;
	}
}
