package jp.co.RE_Tic_Tac_Toe;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {
	Piece piece = new Piece();
	Piece[][] pieces;
	Turn turn = new Turn();

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] button = e.getActionCommand().split(",");
		int x = Integer.parseInt(button[0]);
		int y = Integer.parseInt(button[1]);

		// 設置可能マスか判定
		if (Piece.square[x][y] == Params.NULL) {
			// ピースを設置
			Piece.square[x][y] = Turn.turn;
			// ターン変更処理
			turn.turnChange();
			// ピースを再描画
			viewPiece();
		}

		// ゲーム終了判定
		int winner = piece.winChecker();

		//勝敗判定
		String message = null;
		JFrame frame = new JFrame();
		switch (winner) {
		case Params.CIRCLE:
			message = "○の勝ち";
			break;
		case Params.CROSS:
			message = "×の勝ち";
			break;
		default:
			if (Turn.count == Params.ROWS * Params.COLS) {
				message = "引き分け";
			}
		}

		//結果を表示
		if (message != null) {
			JOptionPane.showMessageDialog(frame, message);
			//パネルを最初の画面へ変更
			View.layout.first(View.cardPanel);
			View.turn.setVisible(false);
		}

		// ターン表示を再描画
		View.turn.turnView();
		Turn.count++;
	}

	/**
	 * ボタンを設置する
	 */
	public void setPiece() {
		removeAll();
		setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
		setLayout(new GridLayout(Params.ROWS, Params.COLS));
		pieces = new Piece[Params.ROWS][Params.COLS];
		// ボタンの生成
		for (int x = 0; x < Params.ROWS; x++) {
			for (int y = 0; y < Params.COLS; y++) {
				pieces[x][y] = new Piece();
				pieces[x][y].setForeground(Color.BLACK);
				pieces[x][y].setBackground(Color.WHITE);
				pieces[x][y].setFont(new Font("MSゴシック", Font.BOLD, 270 / Params.ROWS));
				pieces[x][y].setFocusPainted(false);
				pieces[x][y].setActionCommand(String.valueOf(x + "," + y));
				pieces[x][y].addActionListener(this);
				add(pieces[x][y]);
			}
		}
	}

	/**
	 * 全てのマスを描画する
	 */

	public void viewPiece() {
		// 全マスチェック
		for (int x = 0; x < Params.ROWS; x++) {
			for (int y = 0; y < Params.COLS; y++) {
				int square = Piece.square[x][y];

				// ピースの描画処理
				switch (square) {
				case Params.CROSS:
					pieces[x][y].setText("×");
					break;
				case Params.CIRCLE:
					pieces[x][y].setText("○");
					break;
				case Params.NULL:
					pieces[x][y].setText("");
					break;
				}
			}
		}
	}
}
