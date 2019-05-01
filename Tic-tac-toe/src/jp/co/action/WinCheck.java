package jp.co.action;

public class WinCheck {

	public static int winChecker() {
		int check;
		int winner = 0;

		// 00 01 02
		// 10 11 12
		// 20 21 22
		for (int i = 0; i < Params.ROWS; i++) {
			check = 0;
			for (int j = 0; j < Params.COLS; j++) {
				check += Integer.parseInt(MainPanel.buttons[i][j].getName());
			}
			switch (check) {
			case Params.COLS:
				winner = 1;
				break;
			case 0 - Params.COLS:
				winner = -1;
				break;
			}
		}

		// 00 10 20
		// 01 11 21
		// 02 12 22
		for (int i = 0; i < Params.ROWS; i++) {
			check = 0;
			for (int j = 0; j < Params.COLS; j++) {
				check += Integer.parseInt(MainPanel.buttons[j][i].getName());
			}
			switch (check) {
			case Params.ROWS:
				winner = 1;
				break;
			case 0 - Params.ROWS:
				winner = -1;
				break;
			}
		}

		// 20 11 02
		check = 0;
		for (int i = 0; i < Params.ROWS; i++) {
			for (int j = 0; j < Params.COLS; j++) {
				if (i + j == Params.ROWS - 1) {
					check += Integer.parseInt(MainPanel.buttons[i][j].getName());
				}
			}
			switch (check) {
			case Params.ROWS:
				winner = 1;
				break;
			case 0 - Params.ROWS:
				winner = -1;
				break;
			}
		}

		// 00 11 22
		check = 0;
		for (int i = 0; i < Params.ROWS; i++) {
			for (int j = 0; j < Params.COLS; j++) {
				if (i == j) {
					check += Integer.parseInt(MainPanel.buttons[i][j].getName());
				}
			}
			switch (check) {
			case Params.ROWS:
				winner = 1;
				break;
			case 0 - Params.ROWS:
				winner = -1;
				break;
			}
		}
		return winner;
	}

}
