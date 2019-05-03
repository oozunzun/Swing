package jp.co.Tic_Tac_Toe;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements ActionListener {
	public TurnPanel turnPanel = new TurnPanel();
	public static JButton[][] buttons = new JButton[Params.ROWS][Params.COLS];
	public JLabel label = new JLabel();

	public MainPanel() {
		// パネルの生成
		setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
		setLayout(new GridLayout(Params.ROWS, Params.COLS));

		// ボタンの配置
		for (int i = 0; i < Params.ROWS; i++) {
			for (int j = 0; j < Params.COLS; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].setForeground(Color.BLACK);
				buttons[i][j].setBackground(Color.WHITE);
				buttons[i][j].setFont(new Font("MSゴシック", Font.BOLD, 90));
				buttons[i][j].setFocusPainted(false);
				buttons[i][j].setName("0");
				buttons[i][j].addActionListener(this);
				add(buttons[i][j]);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = ((JButton) e.getSource());
		String turn = turnPanel.label.getText();
		if (button.getName().equals("0")) {
			switch (Params.count % 2) {
			case 0:
				button.setName("1");
				button.setText("○");
				turn = "×のターン";
				break;
			case 1:
				button.setName("-1");
				button.setText("×");
				turn = "○のターン";
				break;
			}
			Params.count++;
		}

		int winner = WinCheck.winChecker();
		String message = null;
		JFrame frame = new JFrame();

		switch (winner) {
		case 1:
			message = "○の勝ち";
			break;
		case -1:
			message = "×の勝ち";
			break;
		}

		if (Params.count == Params.ROWS * Params.COLS && winner == 0) {
			message = "引き分け";
		}

		if (message != null) {
			JOptionPane.showMessageDialog(frame, message);
			for (int i = 0; i < Params.ROWS; i++) {
				for (int j = 0; j < Params.COLS; j++) {
					buttons[i][j].setName("0");
					buttons[i][j].setText("");
				}
			}
			Params.count = 0;
			turn = "○のターン";
		}
		turnPanel.label.setText(turn);
	}
}
