package jp.co.Tic_Tac_Toe;

import javax.swing.JFrame;

public class View extends JFrame {
	public MainPanel panel = new MainPanel();

	public View() {
		add("North", panel.turnPanel);
		add(panel);

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 150, Params.VIEW_WIDTH, Params.VIEW_HEIGHT);
		setTitle("○×ゲーム");
		setVisible(true);
	}
}
