package swing;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

public class Tetriminos {
	/**
	 * テトリミノの定義<br>
	 * [ミノの種類] [ミノの回転方向] [ミノの座標]
	 */
	public static final Point[][][] Tetriminos = {

			// Ｉ
			{
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
					{ new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3) },
					{ new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
			},

			// Ｏ
			{
					{ new Point(1, 1), new Point(1, 2), new Point(2, 1), new Point(2, 2) },
					{ new Point(1, 1), new Point(1, 2), new Point(2, 1), new Point(2, 2) },
					{ new Point(1, 1), new Point(1, 2), new Point(2, 1), new Point(2, 2) },
					{ new Point(1, 1), new Point(1, 2), new Point(2, 1), new Point(2, 2) }
			},

			// Ｓ

			{
					{ new Point(0, 2), new Point(1, 1), new Point(1, 2), new Point(2, 1) },
					{ new Point(1, 1), new Point(1, 2), new Point(2, 2), new Point(2, 3) },
					{ new Point(0, 3), new Point(1, 2), new Point(1, 3), new Point(2, 2) },
					{ new Point(0, 1), new Point(0, 2), new Point(1, 2), new Point(1, 3) }
			},

			// Ｚ
			{
					{ new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
					{ new Point(1, 2), new Point(1, 3), new Point(2, 1), new Point(2, 2) },
					{ new Point(0, 2), new Point(1, 2), new Point(1, 3), new Point(2, 3) },
					{ new Point(0, 2), new Point(0, 3), new Point(1, 1), new Point(1, 2) }
			},

			// Ｊ
			{
					{ new Point(0, 1), new Point(0, 2), new Point(1, 2), new Point(2, 2) },
					{ new Point(1, 1), new Point(1, 2), new Point(1, 3), new Point(2, 1) },
					{ new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(2, 3) },
					{ new Point(0, 3), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
			},

			// Ｌ
			{
					{ new Point(0, 2), new Point(1, 2), new Point(2, 1), new Point(2, 2) },
					{ new Point(1, 1), new Point(1, 2), new Point(1, 3), new Point(2, 3) },
					{ new Point(0, 2), new Point(0, 3), new Point(1, 2), new Point(2, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
			},

			// Ｔ

			{
					{ new Point(0, 2), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
					{ new Point(1, 1), new Point(1, 2), new Point(1, 3), new Point(2, 2) },
					{ new Point(0, 2), new Point(1, 2), new Point(1, 3), new Point(2, 2) },
					{ new Point(0, 2), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
			}
	};

	/**
	 * テトリミノの色の定義
	 */
	public static final Color[] tetriminoColors = {
			//Ｉ
			new Color(50, 200, 255),

			//Ｏ
			new Color(255, 255, 100),

			//Ｓ
			new Color(100, 255, 50),

			//Ｚ
			new Color(200, 0, 0),

			//Ｊ
			new Color(0, 100, 200),

			//Ｌ
			new Color(255, 150, 50),

			//Ｔ
			new Color(200, 50, 150)
	};

	/**
	 * ミノの現在座標
	 */
	public static Point pieceOrigin;

	/**
	 * ミノの回転方向
	 */
	public static int rotation;

	/**
	 * 操作中のミノ
	 */
	public static int currentPiece;

	/**
	 * ネクストミノ
	 */
	public static ArrayList<Integer> nextPieces = new ArrayList<Integer>();

	/**
	 * 新しいミノを配置する
	 */
	public void newPiece() {
		//ミノの初期情報
		pieceOrigin = new Point(4, 0);
		rotation = 0;

		if (nextPieces.isEmpty()) {
			Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
			Collections.shuffle(nextPieces);
		}

		currentPiece = nextPieces.get(0);
		nextPieces.remove(0);
	}

}
