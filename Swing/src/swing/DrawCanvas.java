package swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class DrawCanvas extends JPanel {
	public static Tetriminos tetriminos = new Tetriminos();
	public static Color[][] boardColor = new Color[Params.FIELD_WIDTH + 2][Params.FIELD_HEIGHT + 2];

	private int edge = 1;
	private int cellWidth = Params.CELL_WIDTH + edge;
	private int cellheight = Params.CELL_HEIGHT + edge;

	/**
	 * 盤面を初期化する
	 */
	public void init() {
		boardColor = new Color[Params.FIELD_WIDTH + 2][Params.FIELD_HEIGHT + 2];

		for (int x = 0; x < Params.FIELD_WIDTH + 2; x++) {
			for (int y = 0; y < Params.FIELD_HEIGHT + 1; y++) {
				if (x == 0 || x == 11 || y == 20) {
					boardColor[x][y] = Color.GRAY;
				} else {
					boardColor[x][y] = Color.BLACK;
				}
			}
		}
		tetriminos.newPiece();
	}

	/**
	 * ミノの描画
	 * @param g
	 */
	public void drawPiece(Graphics g) {
		g.setColor(Tetriminos.tetriminoColors[Tetriminos.currentPiece]);

		for (Point p : Tetriminos.Tetriminos[Tetriminos.currentPiece][Tetriminos.rotation]) {
			g.fill3DRect(
					(p.x + Tetriminos.pieceOrigin.x) * cellWidth,
					(p.y + Tetriminos.pieceOrigin.y) * cellheight,
					cellWidth, cellheight,
					true);
		}
	}

	/**
	 * 盤面の描画
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//背景
		g2.fillRect(0, 0,
				cellWidth * (Params.FIELD_WIDTH + 2),
				cellheight * (Params.FIELD_HEIGHT + 1));

		//盤面
		for (int x = 0; x < 12; x++) {
			for (int y = 0; y < 21; y++) {
				g2.setColor(boardColor[x][y]);
				g2.fill3DRect(cellWidth * x, cellheight * y, Params.CELL_WIDTH, Params.CELL_HEIGHT, true);
			}
		}
		drawPiece(g2);
	}
}
