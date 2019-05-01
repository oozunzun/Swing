package jp.co.Othello;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import jp.co.Othello.Params.color;

public class Turn extends JLabel {
	color BLACK = Params.color.BLACK;
	color WHITE = Params.color.WHITE;

	public static color turn = Params.color.BLACK;

	public Turn() {
		setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		setFont(new Font("MSゴシック", Font.BOLD, 20));
		setSize(Params.VIEW_WIDTH, 10);
		setHorizontalAlignment(CENTER);
		turnView();
	}

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

	public void turnChange() {
		switch (turn) {
		case BLACK:
			turn = WHITE;
			break;
		case WHITE:
			turn = BLACK;
			break;
		default:
		}
	}
}
