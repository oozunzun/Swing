package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class BoardPanel extends JPanel implements Params, ActionListener, KeyListener {
	public static int[][] board = new int[CELL_LENGTH][CELL_LENGTH];
	public static JButton[][] button = new JButton[CELL_LENGTH][CELL_LENGTH];
	public static int[][] boardScore = {
			{ 100, -40, 20, 5, 5, 20, -40, 100 },
			{ -40, -80, -1, -1, -1, -1, -80, -40 },
			{ 20, -1, 5, 1, 1, 5, -1, 20 },
			{ 5, -1, 1, 0, 0, 1, -1, 5 },
			{ 5, -1, 1, 0, 0, 1, -1, 5 },
			{ 20, -1, 5, 1, 1, 5, -1, 20 },
			{ -40, -80, -1, -1, -1, -1, -80, -40 },
			{ 100, -40, 20, 5, 5, 20, -40, 100 }
	};

	private static int turn;
	private static Point[] checkPoint = {
			new Point(-1, -1), new Point(-1, 0), new Point(-1, 1),
			new Point(0, -1), new Point(0, 1),
			new Point(1, -1), new Point(1, 0), new Point(1, 1)
	};

	/**
	 * 盤面の初期化
	 */
	public void init() {
		for (int y = 0; y < CELL_LENGTH; y++) {
			for (int x = 0; x < CELL_LENGTH; x++) {
				button[x][y] = new JButton();
				button[x][y].setBorder(new LineBorder(Color.BLACK));
				button[x][y].setBackground(BOARD_COLOR);
				button[x][y].setForeground(BOARD_COLOR);
				button[x][y].setFocusPainted(false);
				button[x][y].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 38));
				button[x][y].setActionCommand(x + "," + y);
				button[x][y].setPressedIcon(null);
				button[x][y].addActionListener(this);
				button[x][y].addKeyListener(this);
				board[x][y] = BLANK;

				add(button[x][y]);
			}
		}

		setLayout(new GridLayout(CELL_LENGTH, CELL_LENGTH));
		setBorder(new EmptyBorder(50, 50, 50, 50));
		turn = 1;

		board[3][4] = board[4][3] = 1;
		board[3][3] = board[4][4] = -1;
		drawPieceColor();
		if (turn == 1) {
			drawReverseBoard(turn);
		}
		System.out.println("黒 : " + totalScoreCount(1, board));
		System.out.println("白 : " + totalScoreCount(-1, board));

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int[] point = Stream
				.of(e.getActionCommand().split(",", 0))
				.mapToInt(Integer::parseInt)
				.toArray();

		int x = point[0];
		int y = point[1];

		if (canAtack(x, y, turn, board)) {
			reversePieceColor(x, y, turn);
			drawPieceColor();

			turnChange();
			if (isPass(turn)) {
				turnChange();
				if (isPass(turn)) {
					System.out.println("終わり");
					try {
						Thread.sleep(10000);
						System.exit(0);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
			drawReverseBoard(turn);

			System.out.println("黒 : " + totalScoreCount(1, board));
			System.out.println("白 : " + totalScoreCount(-1, board));

			if (turn == -1) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						int i = alphaBeta(board, turn, Integer.MIN_VALUE, Integer.MAX_VALUE, SEARCH_LEVEL);
						int j = i % (CELL_LENGTH * CELL_LENGTH);
						int k = i / (CELL_LENGTH * CELL_LENGTH);
						button[j][k].doClick();
					}
				});
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int i = alphaBeta(board, turn, Integer.MIN_VALUE, Integer.MAX_VALUE, SEARCH_LEVEL);
		int j = i % (CELL_LENGTH * CELL_LENGTH);
		int k = i / (CELL_LENGTH * CELL_LENGTH);
		button[j][k].doClick();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * ピースの描画
	 */
	private void drawPieceColor() {
		for (int y = 0; y < CELL_LENGTH; y++) {
			for (int x = 0; x < CELL_LENGTH; x++) {
				if (board[x][y] == 1) {
					button[x][y].setForeground(Color.BLACK);
					button[x][y].setText("●");
				} else if (board[x][y] == -1) {
					button[x][y].setForeground(Color.WHITE);
					button[x][y].setText("●");
				}
				button[x][y].setBackground(BOARD_COLOR);
			}
		}
	}

	/**
	 * ピースを置けるマスの描画
	 * @param turn 手番
	 */
	private void drawReverseBoard(int turn) {
		for (Point p : reverseAllPointList(turn, board)) {
			button[p.x][p.y].setBackground(Color.GRAY);
		}
	}

	/**
	 * 手番を変更する
	 */
	private void turnChange() {
		turn = -turn;
	}

	/**
	 * ピースを引っくり返す
	 * @param x 置いたｘ座標
	 * @param y 置いたｙ座標
	 * @param turn 手番
	 */
	private void reversePieceColor(int x, int y, int turn) {
		for (Point p : reverseAroundPointList(x, y, turn, board)) {
			board[p.x][p.y] = turn;
		}
		board[x][y] = turn;
	}

	/**
	 * ピースを置くことができるかチェックする
	 * @param x チェックするｘ座標
	 * @param y チェックするｙ座標
	 * @param turn 手番
	 * @return 置けるなら true<br>置けないなら false
	 */
	private boolean canAtack(int x, int y, int turn, int[][] board) {
		return !reverseAroundPointList(x, y, turn, board).isEmpty();
	}

	/**
	 * パスになるかチェックする
	 * @param turn 手番
	 * @return パスなら true<br>パスでないなら false
	 */
	private boolean isPass(int turn) {
		return reverseAllPointList(turn, board).isEmpty();
	}

	/**
	 * 置くことのできる場所を調べる
	 * @param turn 手番
	 * @return 座標のリスト
	 */
	private List<Point> reverseAllPointList(int turn, int[][] board) {
		List<Point> reversePointList = new ArrayList<>();
		for (int y = 0; y < CELL_LENGTH; y++) {
			for (int x = 0; x < CELL_LENGTH; x++) {
				if (canAtack(x, y, turn, board)) {
					reversePointList.add(new Point(x, y));
				}
			}
		}
		return reversePointList;
	}

	/**
	 * 反転可能な場所を調べる
	 * @param x 置いたｘ座標
	 * @param y 置いたｙ座標
	 * @param turn 手番
	 * @return 座標のリスト
	 */
	private List<Point> reverseAroundPointList(int x, int y, int turn, int[][] board) {
		List<Point> reversePointList = new ArrayList<>();

		if (board[x][y] != BLANK) {
			return reversePointList;
		}

		for (Point p : checkPoint) {
			int checkX = x;
			int checkY = y;
			boolean checkFlg = false;

			while (checkX + p.x >= 0
					&& checkY + p.y >= 0
					&& checkX + p.x < CELL_LENGTH
					&& checkY + p.y < CELL_LENGTH) {
				checkX += p.x;
				checkY += p.y;
				if (checkFlg) {
					if (board[checkX][checkY] == turn) {
						while (board[checkX - p.x][checkY - p.y] == -turn) {
							checkX -= p.x;
							checkY -= p.y;
							reversePointList.add(new Point(checkX, checkY));
						}
						break;
					} else if (board[checkX][checkY] != -turn) {
						break;
					}
				} else {
					if (board[checkX][checkY] == -turn) {
						checkFlg = true;
					} else {
						break;
					}
				}
			}
		}
		return reversePointList;
	}

	/**
	 * 盤面評価値
	 * @return 盤位置の評価値
	 */
	private int boardScoreCount() {
		int score = 0;
		for (int y = 0; y < CELL_LENGTH; y++) {
			for (int x = 0; x < CELL_LENGTH; x++) {
				score += boardScore[x][y] * board[x][y];
			}
		}
		return score;
	}

	/**
	 * 確定石評価値
	 * @return 確定石の評価値
	 */
	private int notReverseScoreCount(int turn) {
		int score = (notReversePieceCount(turn) - notReversePieceCount(-turn)) * turn;
		return score;
	}

	/**
	 * 候補数評価値
	 * @param turn
	 * @return
	 */
	private int reversePointScoreCount(int turn, int[][] board) {
		int score = reverseAllPointList(turn, board).size() * 10 * turn;
		return score;
	}

	/**
	 * 評価関数
	 * @param turn 手番
	 * @return 総評価値
	 */
	private int totalScoreCount(int turn, int[][] board) {
		int bsMag = 2;
		int nsMag = 5;
		int psMag = 1;
		int score = boardScoreCount() * bsMag
				+ notReverseScoreCount(turn) * nsMag
				+ reversePointScoreCount(turn, board) * psMag;
		return score;
	}

	/**
	 * 確定石を数える
	 * @param turn 手番
	 * @return 自分の確定石の数
	 */
	private int notReversePieceCount(int turn) {
		int E = CELL_LENGTH - 1;
		int count = 0;
		boolean fullFlg;

		int UL = board[0][0]; //左上
		int LL = board[0][E]; //左下
		int UR = board[E][0]; //右上
		int LR = board[E][E]; //右下

		if (UL == turn) {
			count++;
		}
		if (UR == turn) {
			count++;
		}
		if (LL == turn) {
			count++;
		}
		if (LR == turn) {
			count++;
		}

		if (UL != BLANK || LL != BLANK || UR != BLANK || LR != BLANK) {

			//上辺
			fullFlg = true;
			for (int x = 0; x < CELL_LENGTH; x++) {
				if (board[x][0] == BLANK) {
					fullFlg = false;
				}
			}

			if (fullFlg) { //全て埋まってる
				//自分の色を数える
				for (int x = 1; x < E; x++) {
					if (board[x][0] == turn) {
						count++;
					}
				}
			} else { //全て埋まってない
				if (UL == turn) { //左上が自分の色
					int x = 1;
					while (x < E && board[x][0] == turn) {
						x++;
						count++;
					}
				}
				if (UR == turn) { //右上が自分の色
					int x = E - 1;
					while (x > 0 && board[x][0] == turn) {
						x--;
						count++;
					}
				}
			}

			//下辺
			fullFlg = true;
			for (int x = 0; x < CELL_LENGTH; x++) {
				if (board[x][E] == BLANK) {
					fullFlg = false;
				}
			}

			if (fullFlg) { //全て埋まってる
				//自分の色を数える
				for (int x = 1; x < E; x++) {
					if (board[x][E] == turn) {
						count++;
					}
				}
			} else { //全て埋まってない
				if (LL == turn) { //左下が自分の色
					int x = 1;
					while (x < E && board[x][E] == turn) {
						x++;
						count++;
					}
				}
				if (LR == turn) { //右下が自分の色
					int x = E - 1;
					while (x > 0 && board[x][E] == turn) {
						x--;
						count++;
					}
				}
			}

			//左辺
			fullFlg = true;
			for (int y = 0; y < CELL_LENGTH; y++) {
				if (board[0][y] == BLANK) {
					fullFlg = false;
				}
			}

			if (fullFlg) { //全て埋まってる
				//自分の色を数える
				for (int y = 1; y < E; y++) {
					if (board[0][y] == turn) {
						count++;
					}
				}
			} else { //全て埋まってない
				if (UL == turn) { //左上が自分の色
					int y = 1;
					while (y < E && board[0][y] == turn) {
						y++;
						count++;
					}
				}
				if (LL == turn) { //左下が自分の色
					int y = E - 1;
					while (y > 0 && board[0][y] == turn) {
						y--;
						count++;
					}
				}
			}

			//右辺
			fullFlg = true;
			for (int y = 0; y < CELL_LENGTH; y++) {
				if (board[E][y] == BLANK) {
					fullFlg = false;
				}
			}

			if (fullFlg) { //全て埋まってる
				//自分の色を数える
				for (int y = 1; y < E; y++) {
					if (board[E][y] == turn) {
						count++;
					}
				}
			} else { //全て埋まってない
				if (UR == turn) { //右上が自分の色
					int y = 1;
					while (y < E && board[E][y] == turn) {
						y++;
						count++;
					}
				}
				if (LR == turn) { //右下が自分の色
					int y = E - 1;
					while (y > 0 && board[E][y] == turn) {
						y--;
						count++;
					}
				}
			}
		}
		return count;
	}

	public int alphaBeta(int[][] nowBoard, int myTurn, int alpha, int beta, int depth) {
		int score = 0;
		int child;
		int bestX = -1;
		int bestY = -1;
		int[][] copyBoard = copyBoard(nowBoard);

		if (depth == 0) {
			return totalScoreCount(myTurn, copyBoard) * turn;
		}

		if (myTurn == turn) {
			score = Integer.MIN_VALUE;
		} else {
			score = Integer.MAX_VALUE;
		}

		List<Point> list = reverseAllPointList(myTurn, copyBoard);

		if (list.size() == 0) {
			child = alphaBeta(copyBoard, myTurn, alpha, beta, depth - 1);
			if (myTurn == turn) {
				if (child > score) {
					score = child;
					alpha = score;
				}
				if (score > beta) {
					return score;
				}
			} else {
				if (child < score) {
					score = child;
					beta = score;
				}
				if (score < alpha) {
					return score;
				}
			}
		} else {
			for (Point p : reverseAllPointList(myTurn, copyBoard)) {
				int x = p.x;
				int y = p.y;

				for (Point pt : reverseAroundPointList(x, y, myTurn, copyBoard)) {
					copyBoard[pt.x][pt.y] = myTurn;
				}
				copyBoard[x][y] = myTurn;

				child = alphaBeta(copyBoard, -myTurn, alpha, beta, depth - 1);

				if (myTurn == turn) {
					if (child > score) {
						score = child;
						alpha = score;
						bestX = x;
						bestY = y;
					}
					if (score > beta) {
						return score;
					}
				} else {
					if (child < score) {
						score = child;
						beta = score;
						bestX = x;
						bestY = y;
					}
					if (score < alpha) {
						return score;
					}
				}
			}
		}

		if (depth == SEARCH_LEVEL) {
			if (bestX + bestY < 0) {
				System.out.println("パス");
				return score;
			} else {
				return bestX + bestY * CELL_LENGTH * CELL_LENGTH;
			}
		} else {
			return score;
		}
	}

	/**
	 * 盤面をコピーする
	 * @param board コピー元の盤面
	 * @return 盤面
	 */
	public int[][] copyBoard(int[][] board) {
		int[][] copyBoard = new int[board.length][];

		for (int i = 0; i < board.length; i++) {
			copyBoard[i] = new int[board[i].length];
			System.arraycopy(board[i], 0, copyBoard[i], 0, board[i].length);
		}

		return copyBoard;
	}
}
