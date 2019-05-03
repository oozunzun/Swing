package jp.co.Tic_Tac_Toe;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TurnPanel extends JPanel {
	public JLabel label = new JLabel();

	public TurnPanel() {
		setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		label.setText("○のターン");
		label.setFont(new Font("MSゴシック", Font.BOLD, 20));
		add(label);
	}
}
