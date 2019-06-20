package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris extends JPanel {

	private static final long serialVersionUID = -8715353373678321308L;

	/**
	 * テトリミノの定義<br>
	 * [ミノの種類] [ミノの回転方向] [ミノの座標]
	 */
	private final Point[][][] Tetraminos = {

			// Ｉミノ
			{
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
			},

			// Ｊミノ
			{
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
			},

			// Ｌミノ
			{
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
			},

			// Ｏミノ
			{
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
			},

			// Ｓミノ

			{
					{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
					{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
			},

			// Ｔミノ

			{
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
			},

			// Ｚミノ
			{
					{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
					{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
			}
	};

	/**
	 * テトリミノの色の定義
	 */
	private final Color[] tetraminoColors = {
			Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.pink, Color.red
	};

	/**
	 * ミノの現在座標
	 */
	private Point pieceOrigin;

	/**
	 * 操作中のミノ
	 */
	private int currentPiece;

	/**
	 * ミノの回転方向
	 */
	private int rotation;

	/**
	 * ネクストミノ
	 */
	private ArrayList<Integer> nextPieces = new ArrayList<Integer>();
	private long score;

	/**
	 * 盤面の色
	 */
	private Color[][] well;

	/**
	 * 盤面を初期化する
	 */
	private void init() {
		well = new Color[12][24];

		for (int x = 0; x < 12; x++) {
			for (int y = 0; y < 23; y++) {
				if (x == 0 || x == 11 || y == 22) {
					well[x][y] = Color.GRAY;
				} else {
					well[x][y] = Color.BLACK;
				}
			}
		}
		newPiece();
	}

	/**
	 * 新しいミノを配置する
	 */
	public void newPiece() {
		//ミノの初期情報
		pieceOrigin = new Point(5, 2);
		rotation = 0;

		if (nextPieces.isEmpty()) {
			Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
			Collections.shuffle(nextPieces);
		}

		currentPiece = nextPieces.get(0);
		nextPieces.remove(0);
	}

	/**
	 * ミノが移動できるかチェックする
	 * @param x 移動先のｘ座標
	 * @param y 移動先のｙ座標
	 * @param rotation 回転方向
	 * @return 移動不可ならtrue<br>移動可能ならfalse
	 */
	private boolean collidesAt(int x, int y, int rotation) {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			if (well[p.x + x][p.y + y] != Color.BLACK) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 回転
	 * @param i
	 */
	public void rotate(int i) {
		int newRotation = (rotation + i) % 4;

		if (newRotation < 0) {
			newRotation = 3;
		}

		if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation)) {
			rotation = newRotation;
		}
		repaint();
	}

	/**
	* 左右移動
	* @param i
	*/
	public void move(int i) {
		if (!collidesAt(pieceOrigin.x + i, pieceOrigin.y, rotation)) {
			pieceOrigin.x += i;
		}
		repaint();
	}

	/**
	 * 下方向移動
	 */
	public void dropDown() {
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
			pieceOrigin.y += 1;
		} else {
			fixToWell();
		}
		repaint();
	}

	/**
	 * 移動処理
	 */
	public void fixToWell() {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			well[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = tetraminoColors[currentPiece];
		}
		clearRows();
		newPiece();
	}

	/**
	 * ラインを１つずつ下にずらす
	 * @param row ずらし始めるｙ座標
	 */
	public void deleteRow(int row) {
		for (int y = row - 1; y > 0; y--) {
			for (int x = 1; x < 11; x++) {
				well[x][y + 1] = well[x][y];
			}
		}
	}

	/**
	 * ラインを削除する
	 */
	public void clearRows() {
		boolean gap;
		int numClears = 0;

		for (int y = 21; y > 0; y--) {
			gap = false;
			for (int x = 1; x < 11; x++) {
				if (well[x][y] == Color.BLACK) {
					gap = true;
					break;
				}
			}

			if (!gap) {
				deleteRow(y);
				y += 1;
				numClears += 1;
			}
		}

		switch (numClears) {
		case 1:
			score += 100;
			break;
		case 2:
			score += 300;
			break;
		case 3:
			score += 500;
			break;
		case 4:
			score += 800;
			break;
		}
	}

	/**
	 * ミノの描画
	 * @param g
	 */
	private void drawPiece(Graphics g) {
		g.setColor(tetraminoColors[currentPiece]);

		for (Point p : Tetraminos[currentPiece][rotation]) {
			g.fillRect((p.x + pieceOrigin.x) * 26,
					(p.y + pieceOrigin.y) * 26, 25, 25);
		}
	}

	/**
	 * 盤面の描画
	 */
	@Override
	public void paintComponent(Graphics g) {
		//背景
		g.fillRect(0, 0, 26 * 12, 26 * 23);

		//盤面
		for (int x = 0; x < 12; x++) {
			for (int y = 0; y < 23; y++) {
				g.setColor(well[x][y]);
				g.fillRect(26 * x, 26 * y, 25, 25);
			}
		}

		//スコア表示
		g.setColor(Color.WHITE);
		g.drawString("" + score, 19 * 12, 25);

		//ミノ
		drawPiece(g);
	}

	public static void main(String[] args) {

		JFrame f = new JFrame("Tetris");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(12 * 26 + 10, 26 * 23 + 25);
		f.setVisible(true);

		final Tetris game = new Tetris();
		game.init();

		f.add(game);

		// キー入力処理
		f.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					game.rotate(-1);
					break;
				case KeyEvent.VK_DOWN:
					game.rotate(+1);
					break;
				case KeyEvent.VK_LEFT:
					game.move(-1);
					break;
				case KeyEvent.VK_RIGHT:
					game.move(+1);
					break;
				case KeyEvent.VK_SPACE:
					game.dropDown();
					game.score += 1;
					break;
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		});

		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						game.dropDown();
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
	}
}