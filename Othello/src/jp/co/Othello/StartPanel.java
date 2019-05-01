package jp.co.Othello;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jp.co.Othello.Params.color;

public class StartPanel extends JPanel {
	int width = Params.VIEW_WIDTH;
	int height = Params.VIEW_HEIGHT;
	int centerWidth = width / 2;
	int centerHeight = height / 2;
	int buttonWidth = 100;
	int buttonHeight = 50;
	color BLACK = Params.color.BLACK;

	public StartPanel() {
		setLayout(null);

		JLabel label = new JLabel("オセロゲーム");
		label.setFont(new Font(Font.SERIF, Font.BOLD, 35));
		label.setBounds(0, centerHeight / 2, width, 50);
		label.setHorizontalAlignment(JLabel.CENTER);

		JButton startButton = new JButton("ゲーム開始");

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ターンの初期化
				Turn.turn = BLACK;
				View.turn.turnView();
				View.turn.setVisible(true);

				// ピースの初期化
				Piece square = new Piece();
				square.pieceSetting();
				View.gamePanel.viewPiece();

				// ゲーム画面へ遷移
				View.layout.next(View.cardPanel);
			}
		});

		startButton.setFocusPainted(false);
		startButton.setBounds(centerWidth - buttonWidth / 2, centerHeight - buttonHeight / 2, buttonWidth,
				buttonHeight);

		JButton endButton = new JButton("終了");

		endButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		endButton.setFocusPainted(false);
		endButton.setBounds(centerWidth - buttonWidth / 2, centerHeight + buttonHeight, buttonWidth, buttonHeight);

		add(label);
		add(startButton);
		add(endButton);
	}
}
