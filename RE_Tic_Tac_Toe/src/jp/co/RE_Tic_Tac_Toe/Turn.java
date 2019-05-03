package jp.co.RE_Tic_Tac_Toe;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class Turn extends JLabel {
	/**
	 * ターンの保持
	 */
	public static int turn = Params.CROSS;

	public static int count = 0;

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
		case Params.CIRCLE:
			setText("○のターン");
			break;
		case Params.CROSS:
			setText("×のターン");
			break;
		default:
		}
	}

	/**
	 * ターンを入れ替える
	 */
	public void turnChange() {
		switch (turn) {
		case Params.CIRCLE:
			turn = Params.CROSS;
			break;
		case Params.CROSS:
			turn = Params.CIRCLE;
			break;
		default:
		}
	}
}
