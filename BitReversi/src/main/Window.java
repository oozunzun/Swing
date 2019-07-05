package main;

import javax.swing.JFrame;

public class Window extends JFrame {
	BoardPanel boardPanel = new BoardPanel();

	public Window(String title, int width, int height) {
		setTitle(title);
		setSize(width, height);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);

		add(boardPanel);
		boardPanel.init();
	}
}
