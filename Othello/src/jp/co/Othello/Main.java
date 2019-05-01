package jp.co.Othello;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		View view = new View();

		view.setResizable(false);
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.setSize(Params.VIEW_WIDTH, Params.VIEW_HEIGHT);
		view.setLocationRelativeTo(null);
		view.setTitle("オセロ");

		view.setVisible(true);
	}
}
