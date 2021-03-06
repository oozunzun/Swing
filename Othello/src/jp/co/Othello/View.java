package jp.co.Othello;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class View extends JFrame {
	static Turn turn = new Turn();
	static JPanel cardPanel = new JPanel();
	static CardLayout layout = new CardLayout();
	static GamePanel gamePanel = new GamePanel();
	StartPanel startPanel = new StartPanel();

	public View() {
		//ウィンドウの初期設定
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Params.VIEW_WIDTH, Params.VIEW_HEIGHT);
		setLocationRelativeTo(null);
		setTitle("オセロ");

		//パネルを全て読み込み
		cardPanel.setLayout(layout);
		cardPanel.add(startPanel);
		cardPanel.add(gamePanel);

		add("North", turn);
		turn.setVisible(false);

		add(cardPanel, BorderLayout.CENTER);

		setVisible(true);
	}
}
