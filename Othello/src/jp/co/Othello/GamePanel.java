package jp.co.Othello;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jp.co.Othello.Params.color;

public class GamePanel extends JPanel implements ActionListener {
	int ROWS = Params.ROWS;
	int COLS = Params.COLS;
	color BLACK = Params.color.BLACK;
	color WHITE = Params.color.WHITE;

	Piece piece = new Piece();
	Piece[][] pieces = new Piece[ROWS][COLS];
	Color reverse = Color.LIGHT_GRAY;
	Turn turn = new Turn();

	public GamePanel() {

		// パネルの生成
		setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
		setLayout(new GridLayout(ROWS, COLS));

		// ボタンのの生成
		for (int x = 0; x < ROWS; x++) {
			for (int y = 0; y < COLS; y++) {
				pieces[x][y] = new Piece();
				pieces[x][y].setForeground(Color.BLACK);
				pieces[x][y].setBackground(Color.WHITE);
				pieces[x][y].setFont(new Font(Font.SERIF, Font.PLAIN, 35));
				pieces[x][y].setFocusPainted(false);
				pieces[x][y].setActionCommand(String.valueOf(x + "," + y));
				pieces[x][y].addActionListener(this);
				add(pieces[x][y]);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] button = e.getActionCommand().split(",");
		int x = Integer.parseInt(button[0]);
		int y = Integer.parseInt(button[1]);

		List<Integer> returnList = piece.isReversePieces(x, y);

		// 反転可能マスか判定
		if (pieces[x][y].getBackground() == reverse) {
			// ピースを設置
			Piece.square[x][y] = Turn.turn;

			// 反転処理
			for (int i = 0; i < 8; i++) {
				if (returnList.get(i) == 2) {
					piece.turnPiece(x, y, i);
				}
			}

			// ターン変更処理
			turn.turnChange();
		}

		// スキップ判定
		if (piece.isSkipTurn()) {

			// ターン変更処理
			turn.turnChange();

			// ゲーム終了判定
			if (piece.isSkipTurn()) {

				//盤面の数をカウント
				int black = piece.pieceCollorCount()[0];
				int white = piece.pieceCollorCount()[1];

				// ピースを再描画
				viewPiece();

				//結果を表示
				JFrame frame = new JFrame();
				String message = "黒" + black + "," + "白" + white + "\n";
				if (black < white) {
					JOptionPane.showMessageDialog(frame, message + "白の勝ち");
				} else if (white < black) {
					JOptionPane.showMessageDialog(frame, message + "黒の勝ち");
				} else {
					JOptionPane.showMessageDialog(frame, message + "引き分け");
				}

				//パネルを最初の画面へ変更
				View.layout.first(View.cardPanel);
				View.turn.setVisible(false);
			}
		}

		// ピースを再描画
		viewPiece();

		// ターン表示を再描画
		View.turn.turnView();
	}

	/**
	 * 全てのマスを描画する
	 */

	public void viewPiece() {
		// 全マスチェック
		for (int x = 0; x < ROWS; x++) {
			for (int y = 0; y < COLS; y++) {
				color square = Piece.square[x][y];

				// ピースの描画処理
				switch (square) {
				case BLACK:
					pieces[x][y].setText("●");
					break;
				case WHITE:
					pieces[x][y].setText("○");
					break;
				case NULL:
					pieces[x][y].setText("");
					break;
				}

				// 反転可能マスの色を変更
				if (piece.isReversePieces(x, y).contains(2) && square == Params.color.NULL) {
					pieces[x][y].setBackground(reverse);
				} else {
					pieces[x][y].setBackground(Color.WHITE);
				}
			}
		}
	}
}
