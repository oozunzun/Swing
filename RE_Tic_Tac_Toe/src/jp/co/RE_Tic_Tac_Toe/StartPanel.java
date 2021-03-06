package jp.co.RE_Tic_Tac_Toe;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StartPanel extends JPanel {
	int width = Params.VIEW_WIDTH;
	int height = Params.VIEW_HEIGHT;
	int centerWidth = width / 2;
	int centerHeight = height / 2;
	int buttonWidth = 100;
	int buttonHeight = 50;

	public StartPanel() {
		setLayout(null);

		JLabel title = new JLabel("○×ゲーム");
		title.setFont(new Font(Font.SERIF, Font.BOLD, 35));
		title.setBounds(0, centerHeight / 2, width, 50);
		title.setHorizontalAlignment(JLabel.CENTER);
		add(title);

		JButton startButton = new JButton("ゲーム開始");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// コンフィグ画面へ遷移
				View.layout.next(View.cardPanel);
			}
		});
		startButton.setFocusPainted(false);
		startButton.setBounds(centerWidth - buttonWidth / 2, centerHeight - buttonHeight / 2, buttonWidth,
				buttonHeight);
		add(startButton);

		JButton endButton = new JButton("終了");
		endButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// プログラム終了
				System.exit(0);
			}
		});
		endButton.setFocusPainted(false);
		endButton.setBounds(centerWidth - buttonWidth / 2, centerHeight + buttonHeight, buttonWidth, buttonHeight);
		add(endButton);
	}
}
