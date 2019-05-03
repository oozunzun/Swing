package jp.co.RE_Tic_Tac_Toe;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class View extends JFrame {
	static Turn turn = new Turn();
	static JPanel cardPanel = new JPanel();
	static CardLayout layout = new CardLayout();
	static GamePanel gamePanel = new GamePanel();
	static ConfigPanel configPanel = new ConfigPanel();
	static StartPanel startPanel = new StartPanel();

	public View() {
		//ウィンドウの初期設定
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Params.VIEW_WIDTH, Params.VIEW_HEIGHT);
		setLocationRelativeTo(null);
		setTitle("○×ゲーム");

		//パネルを全て読み込み
		cardPanel.setLayout(layout);
		cardPanel.add(startPanel);
		cardPanel.add(configPanel);
		cardPanel.add(gamePanel);

		add("North", turn);
		turn.setVisible(false);

		add(cardPanel, BorderLayout.CENTER);

		setVisible(true);
	}
}
