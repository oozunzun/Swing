package jp.co.Othello;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import jp.co.Othello.Params.color;

public class Piece extends JButton {
	/**
	 * ８方向の座標<br>左上、左、左下、上、下、右上、右、右下 の順番
	 */

	static int[] points[] = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };

	/**
	 * 盤面の座標
	 */

	static color[][] square = new color[Params.ROWS][Params.COLS];

	int ROWS = Params.ROWS;
	int COLS = Params.COLS;

	color NULL = Params.color.NULL;
	color BLACK = Params.color.BLACK;
	color WHITE = Params.color.WHITE;

	/**
	 * 盤面を初期化する
	 */

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

	/**
	 * 反転可能かチェック
	 * @param setX 置いたマスの X座標
	 * @param setY 置いたマスの Y座標
	 * @return ８方向に対して反転可能 or 反転不可の int配列 <br> 2：反転可能、3：反転不可
	 */

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
						//空白
						checkFlg = 3;
					} else if (checkPoint != Turn.turn) {
						//違う色
						checkFlg = 1;
						checkX += x;
						checkY += y;
					} else {
						//同じ色
						if (checkFlg == 1) {
							//２周目以降
							checkFlg = 2;
							break;
						} else {
							//１週目
							checkFlg = 3;
						}
					}
				} else {
					//番外
					checkFlg = 3;
				}
			}
			flgs.add(checkFlg);
		}
		return flgs;
	}

	/**
	 * 色を反転する
	 * @param setX 置いたマスの X座標
	 * @param setY 置いたマスの Y座標
	 * @param isReverse 反転可能な方向の index
	 */

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

	/**
	 * すべてのマスに対して反転できるかチェック
	 * @return 反転できるマスがない場合 true <br> 反転できるマスがある場合 false
	 */

	public boolean isSkipTurn() {
		boolean skipFlg = true;

		for (int x = 0; x < ROWS; x++) {
			for (int y = 0; y < COLS; y++) {
				if (square[x][y] == NULL && isReversePieces(x, y).contains(2)) {
					skipFlg = false;
				}
			}
		}
		return skipFlg;
	}

	/**
	 * 盤面の色を数える
	 * @return 黒,白
	 * */

	public int[] pieceCollorCount() {
		int black = 0;
		int white = 0;
		int[] count = { 0, 0 };

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
