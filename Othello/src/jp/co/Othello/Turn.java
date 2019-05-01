package jp.co.Othello;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import jp.co.Othello.Params.color;

public class Turn extends JLabel {
	/**
	 * ターンの保持
	 */
	public static color turn = Params.color.BLACK;

	public Turn() {
		setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		setFont(new Font("MSゴシック", Font.BOLD, 20));
		setSize(Params.VIEW_WIDTH, 10);
		setHorizontalAlignment(CENTER);
		turnView();
	}

	/**
	 * ターン表示する
	 */
	public void turnView() {
		switch (turn) {
		case BLACK:
			setText("黒のターン");
			break;
		case WHITE:
			setText("白のターン");
			break;
		default:
		}
	}

	/**
	 * ターンを入れ替える
	 */
	public void turnChange() {
		switch (turn) {
		case BLACK:
			turn = Params.color.WHITE;
			break;
		case WHITE:
			turn = Params.color.BLACK;
			break;
		default:
		}
	}
}
