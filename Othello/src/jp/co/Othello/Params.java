package jp.co.Othello;

public class Params {
	/**
	 * 行
	 */
	public static final int ROWS = 8;

	/**
	 * 列
	 */
	public static final int COLS = 8;

	/**
	 * ウィンドウ幅
	 */
	public static final int VIEW_WIDTH = COLS * 83;

	/**
	 * ウィンドウ高さ
	 */
	public static final int VIEW_HEIGHT = ROWS * 100;

	enum color {
		/**
		 * 空白
		 */
		NULL,

		/**
		 * 黒
		 */
		BLACK,

		/**
		 * 白
		 */
		WHITE
	}
}
