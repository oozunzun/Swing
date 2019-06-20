package swing;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
	DrawCanvas game = new DrawCanvas();

	public GameWindow(String title, int width, int height) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(width, height);
		setLocationRelativeTo(null);
		setResizable(false);

		game.init();

		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					rotate(-1);
					break;
				case KeyEvent.VK_DOWN:
					rotate(+1);
					break;
				case KeyEvent.VK_LEFT:
					move(-1);
					break;
				case KeyEvent.VK_RIGHT:
					move(+1);
					break;
				case KeyEvent.VK_SPACE:
					dropDown();
					break;
				case KeyEvent.VK_0:
					DrawCanvas.tetriminos.newPiece();
					repaint();
					break;
				}
			}
		});

//		new Thread() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						Thread.sleep(1000);
//						dropDown();
//					} catch (InterruptedException e) {
//					}
//				}
//			}
//		}.start();

	}

	/**
	 * ミノが移動できるかチェックする
	 * @param x 移動先のｘ座標
	 * @param y 移動先のｙ座標
	 * @param rotation 回転方向
	 * @return 移動不可ならtrue<br>移動可能ならfalse
	 */
	private boolean collidesAt(int x, int y, int rotation) {
		for (Point p : Tetriminos.Tetriminos[Tetriminos.currentPiece][rotation]) {
			if (DrawCanvas.boardColor[p.x + x][p.y + y] != Color.BLACK) {
				return true;
			}
		}
		return false;
	}

	/**
	* 左右移動
	* @param i
	*/
	public void move(int i) {
		if (!collidesAt(
				Tetriminos.pieceOrigin.x + i,
				Tetriminos.pieceOrigin.y,
				Tetriminos.rotation)) {
			Tetriminos.pieceOrigin.x += i;
		}
		repaint();
	}

	/**
	 * 下方向移動
	 */
	public void dropDown() {
		if (!collidesAt(
				Tetriminos.pieceOrigin.x,
				Tetriminos.pieceOrigin.y + 1,
				Tetriminos.rotation)) {
			Tetriminos.pieceOrigin.y += 1;
		} else {
			fixToWell();
		}
		repaint();
	}

	public void fixToWell() {
		int x = Tetriminos.pieceOrigin.x;
		int y = Tetriminos.pieceOrigin.y;

		for (Point p : Tetriminos.Tetriminos[Tetriminos.currentPiece][Tetriminos.rotation]) {
			DrawCanvas.boardColor[x + p.x][y + p.y] = Tetriminos.tetriminoColors[Tetriminos.currentPiece];
		}
		clearRows();
		DrawCanvas.tetriminos.newPiece();
	}

	/**
	 * 回転
	 * @param i
	 */
	public void rotate(int i) {
		boolean rotateFlg = false;
		int newRotation = (Tetriminos.rotation + i) % 4;

		if (newRotation < 0) {
			newRotation = 3;
		}

		int x = Tetriminos.pieceOrigin.x;
		int y = Tetriminos.pieceOrigin.y;

		if (x == -1) {
			x += 2;
		} else if (i == 1 && x == 0) {
			x += 1;
		} else if (Tetriminos.currentPiece == 0 && x + Params.MINO_WIDTH >= Params.FIELD_WIDTH + 2) {
			x = Params.FIELD_WIDTH - 3;
		} else if (x + Params.MINO_WIDTH > Params.FIELD_WIDTH + 2) {
			x = Params.FIELD_WIDTH - 2;
		}

		if (!collidesAt(x, y, newRotation)) {
			rotateFlg = true;
		} else {
			if ((Tetriminos.rotation == 0 && i == -1) || (Tetriminos.rotation == 2 && i == 1)) {
				if (!collidesAt(x + 1, y, newRotation)) {
					x += 1;
					rotateFlg = true;
				} else if (!collidesAt(x + 1, y - 1, newRotation)) {
					x += 1;
					y -= 1;
					rotateFlg = true;
				} else if (!collidesAt(x, y + 2, newRotation)) {
					y += 2;
					rotateFlg = true;
				} else if (!collidesAt(x + 1, y + 2, newRotation)) {
					x += 1;
					y += 2;
					rotateFlg = true;
				}
			} else if ((Tetriminos.rotation == 0 && i == 1) || (Tetriminos.rotation == 2 && i == -1)) {
				if (!collidesAt(x - 1, y, newRotation)) {
					x -= 1;
					rotateFlg = true;
				} else if (!collidesAt(x - 1, y - 1, newRotation)) {
					x -= 1;
					y -= 1;
					rotateFlg = true;
				} else if (!collidesAt(x, y + 2, newRotation)) {
					y += 2;
					rotateFlg = true;
				} else if (!collidesAt(x - 1, y + 2, newRotation)) {
					x -= 1;
					y += 2;
					rotateFlg = true;
				}
			} else if (Tetriminos.rotation == 1 && (i == -1 || i == 1)) {
				if (!collidesAt(x + 1, y, newRotation)) {
					x += 1;
					rotateFlg = true;
				} else if (!collidesAt(x + 1, y + 1, newRotation)) {
					x += 1;
					y += 1;
					rotateFlg = true;
				} else if (!collidesAt(x, y - 2, newRotation)) {
					y -= 2;
					rotateFlg = true;
				} else if (!collidesAt(x + 1, y - 2, newRotation)) {
					x += 1;
					y -= 2;
					rotateFlg = true;
				}
			} else if (Tetriminos.rotation == 3 && (i == -1 || i == 1)) {
				if (!collidesAt(x - 1, y, newRotation)) {
					x -= 1;
					rotateFlg = true;
				} else if (!collidesAt(x - 1, y + 1, newRotation)) {
					x -= 1;
					y += 1;
					rotateFlg = true;
				} else if (!collidesAt(x, y - 2, newRotation)) {
					y -= 2;
					rotateFlg = true;
				} else if (!collidesAt(x - 1, y - 2, newRotation)) {
					x -= 1;
					y -= 2;
					rotateFlg = true;
				}
			}
		}

		if (rotateFlg) {
			Tetriminos.pieceOrigin.x = x;
			Tetriminos.pieceOrigin.y = y;
			Tetriminos.rotation = newRotation;
		}
		repaint();
	}

	/**
	 * 盤面を１つずつ下にずらす
	 * @param row ずらし始めるｙ座標
	 */
	public void deleteRow(int row) {
		for (int y = row - 1; y > 0; y--) {
			for (int x = 1; x <= Params.FIELD_WIDTH + 1; x++) {
				DrawCanvas.boardColor[x][y + 1] = DrawCanvas.boardColor[x][y];
			}
		}
	}

	/**
	 * ラインを削除する
	 */
	public void clearRows() {
		boolean gap;

		for (int y = Params.FIELD_HEIGHT - 1; y > 0; y--) {
			gap = false;
			for (int x = 1; x <= Params.FIELD_WIDTH; x++) {
				if (DrawCanvas.boardColor[x][y] == Color.BLACK) {
					gap = true;
					break;
				}
			}

			if (!gap) {
				deleteRow(y);
				y += 1;
			}
		}
	}
}
