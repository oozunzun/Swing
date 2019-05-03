package jp.co.RE_Tic_Tac_Toe;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ConfigPanel extends JPanel {
	final String THREE = "三目";
	final String FOUR = "四目";
	final String FIVE = "五目";

	public ConfigPanel() {
		setLayout(null);

		JLabel label = new JLabel("マス数を選択して下さい");
		label.setFont(new Font(Font.SERIF, Font.BOLD, 25));
		label.setBounds(0, 200, Params.VIEW_WIDTH, 50);
		label.setHorizontalAlignment(JLabel.CENTER);
		add(label);

		JRadioButton[] radio = new JRadioButton[3];
		radio[0] = new JRadioButton(THREE, true);
		radio[1] = new JRadioButton(FOUR);
		radio[2] = new JRadioButton(FIVE);

		ButtonGroup group = new ButtonGroup();

		for (int i = 0; i < 3; i++) {
			group.add(radio[i]);
			radio[i].setBounds(Params.VIEW_WIDTH / 2 + (i * 75 - 100), 300, 75, 25);
			radio[i].setFont(new Font(Font.SERIF, Font.BOLD, 20));
			radio[i].setFocusPainted(false);
			add(radio[i]);
		}

		JButton startButton = new JButton("ゲーム開始");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = "";
				for (int i = 0; i < radio.length; i++) {
					if (radio[i].isSelected()) {
						name = radio[i].getText();
					}
				}
				switch (name) {
				case THREE:
					Params.ROWS = Params.COLS = 3;
					break;
				case FOUR:
					Params.ROWS = Params.COLS = 4;
					break;
				case FIVE:
					Params.ROWS = Params.COLS = 5;
					break;
				default:
				}

				// ターンの初期化
				Turn.turn = Params.CROSS;
				Turn.count = 1;
				View.turn.turnView();
				View.turn.setVisible(true);

				// ピースの初期化
				Piece square = new Piece();
				square.pieceSetting();
				View.gamePanel.setPiece();
				View.gamePanel.viewPiece();

				// ゲーム画面へ遷移
				View.layout.next(View.cardPanel);
			}
		});
		startButton.setFocusPainted(false);
		startButton.setBounds(Params.VIEW_WIDTH / 2 - 50, 400, 100, 50);
		add(startButton);
	}
}
