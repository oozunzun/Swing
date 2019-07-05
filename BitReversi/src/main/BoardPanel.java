package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class BoardPanel extends JPanel implements Params, ActionListener {
	public static JButton[][] button = new JButton[CELL_LENGTH][CELL_LENGTH];

	public static long black;
	public static long white;
	public static int turn;

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

				add(button[x][y]);
			}
		}

		setLayout(new GridLayout(CELL_LENGTH, CELL_LENGTH));
		setBorder(new EmptyBorder(50, 50, 50, 50));

		black = 0x0000000810000000L;
		white = 0x0000001008000000L;

		drawPieceColor(black, Color.BLACK);
		drawPieceColor(white, Color.WHITE);

		turn = BLACK_TURN;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//押した場所のｘ座標、ｙ座標を取得
		int[] point = Stream
				.of(e.getActionCommand().split(",", 0))
				.mapToInt(Integer::parseInt)
				.toArray();
		int x = point[0];
		int y = point[1];

		//押した座標のみ立っているビットを取得
		long bit = coordinateToBit(x, y);

		long myBoard;
		if (turn == BLACK_TURN) {//黒のターン
			myBoard = black;
		} else {//白のターン
			myBoard = white;
		}

		if (canPut(bit, myBoard)) { //着手可否判定
			//反転処理
			reverse(bit, myBoard);
			turn *= -1;
		}

		if (turn == BLACK_TURN) {//黒のターン
			myBoard = black;
		} else {//白のターン
			myBoard = white;
		}

		if (isPass(myBoard)) {
			turn *= -1;
		}
		if (isGameFinished(myBoard)) {
			System.out.println("終了");
		}

		System.out.println(zeroFillForBit(createLegalBoard(myBoard)));
		System.out.println(zeroFillForBit(Long.lowestOneBit(createLegalBoard(myBoard))));
		drawPieceColor(black, Color.BLACK);
		drawPieceColor(white, Color.WHITE);
	}

	/**
	 * ピースの描画
	 * @param bitBoard 描画する盤面
	 * @param color 描画する色
	 */
	private void drawPieceColor(long bitBoard, Color color) {
		//盤面を取得
		String zeroFillblack = zeroFillForBit(bitBoard);

		//ビットが立っている場所を描画
		for (int i : indexOfsetFlgList(zeroFillblack)) {
			int x = i % CELL_LENGTH;
			int y = i / CELL_LENGTH;
			button[x][y].setForeground(color);
			button[x][y].setText("●");
		}
	}

	/**
	 * ビットの0埋めをする
	 * @param s 0埋めをする前のビット
	 * @return 0埋めをした後のビット
	 */
	private String zeroFillForBit(long bit) {
		//2進数の文字列に変換
		String s = Long.toBinaryString(bit);
		StringBuilder builder = new StringBuilder(s);

		//64桁で0埋めをする
		while (CELL_LENGTH * CELL_LENGTH - builder.length() > 0) {
			builder.insert(0, "0");
		}

		return builder.toString();
	}

	/**
	 * ビットが立っている場所を検索する
	 * @param bitList 検索するビット
	 * @return ビットが立っている場所のインデックス番号
	 */
	private List<Integer> indexOfsetFlgList(String bitList) {
		List<Integer> list = new ArrayList<>();
		int result;

		for (int i = 0; i < bitList.length(); i++) {
			result = bitList.indexOf("1", i);
			if (result != -1) {
				list.add(result);
				i = result;
			}
		}
		return list;
	}

	/**
	 * 座標をビットに変換する
	 * @param x ｘ座標
	 * @param y ｙ座標
	 * @return 座標位置のみ立っているビット
	 */
	private long coordinateToBit(int x, int y) {
		//座標をビットに変換
		long mask = 0x8000000000000000L;
		mask = mask >>> x;
		mask = mask >>> y * CELL_LENGTH;

		return mask;
	}

	/**
	 * 着手可否の判定をする
	 * @param put 置いた場所のみ立っているビット
	 * @param myBoard 自分の盤面
	 * @return 着手可能なら true
	 */
	private boolean canPut(long put, long myBoard) {
		long legalBoard = createLegalBoard(myBoard);
		return (put & legalBoard) == put;
	}

	/**
	 * 合法手を作成
	 * @param myBoard 自分の盤面
	 * @return 置ける場所のみ立っているビット
	 */
	private long createLegalBoard(long myBoard) {
		//敵側の盤面を取得
		long enemyBoard = (black | white) ^ myBoard;

		//左右辺の番人
		long horizontalSideBoard = enemyBoard & 0x7e7e7e7e7e7e7e7eL;

		//上下辺の番人
		long verticalSideBoard = enemyBoard & 0x00FFFFFFFFFFFF00L;

		//全辺の番人
		long allSideBoard = enemyBoard & 0x007e7e7e7e7e7e00L;

		//空きマスのみビットが立っている盤面
		long blankBoard = ~(black | white);

		//隣に相手の色があるか一時保存
		long temp;

		//戻り値
		long legalBoard;

		//左
		temp = horizontalSideBoard & (myBoard << 1);
		temp |= horizontalSideBoard & (temp << 1);
		temp |= horizontalSideBoard & (temp << 1);
		temp |= horizontalSideBoard & (temp << 1);
		temp |= horizontalSideBoard & (temp << 1);
		temp |= horizontalSideBoard & (temp << 1);
		legalBoard = blankBoard & (temp << 1);

		//右
		temp = horizontalSideBoard & (myBoard >>> 1);
		temp |= horizontalSideBoard & (temp >>> 1);
		temp |= horizontalSideBoard & (temp >>> 1);
		temp |= horizontalSideBoard & (temp >>> 1);
		temp |= horizontalSideBoard & (temp >>> 1);
		temp |= horizontalSideBoard & (temp >>> 1);
		legalBoard |= blankBoard & (temp >>> 1);

		//上
		temp = verticalSideBoard & (myBoard << CELL_LENGTH);
		temp |= verticalSideBoard & (temp << CELL_LENGTH);
		temp |= verticalSideBoard & (temp << CELL_LENGTH);
		temp |= verticalSideBoard & (temp << CELL_LENGTH);
		temp |= verticalSideBoard & (temp << CELL_LENGTH);
		temp |= verticalSideBoard & (temp << CELL_LENGTH);
		legalBoard |= blankBoard & (temp << CELL_LENGTH);

		//下
		temp = verticalSideBoard & (myBoard >>> CELL_LENGTH);
		temp |= verticalSideBoard & (temp >>> CELL_LENGTH);
		temp |= verticalSideBoard & (temp >>> CELL_LENGTH);
		temp |= verticalSideBoard & (temp >>> CELL_LENGTH);
		temp |= verticalSideBoard & (temp >>> CELL_LENGTH);
		temp |= verticalSideBoard & (temp >>> CELL_LENGTH);
		legalBoard |= blankBoard & (temp >>> CELL_LENGTH);

		//右斜め上
		temp = allSideBoard & (myBoard << CELL_LENGTH - 1);
		temp |= allSideBoard & (temp << CELL_LENGTH - 1);
		temp |= allSideBoard & (temp << CELL_LENGTH - 1);
		temp |= allSideBoard & (temp << CELL_LENGTH - 1);
		temp |= allSideBoard & (temp << CELL_LENGTH - 1);
		temp |= allSideBoard & (temp << CELL_LENGTH - 1);
		legalBoard |= blankBoard & (temp << CELL_LENGTH - 1);

		//左斜め上
		temp = allSideBoard & (myBoard << CELL_LENGTH + 1);
		temp |= allSideBoard & (temp << CELL_LENGTH + 1);
		temp |= allSideBoard & (temp << CELL_LENGTH + 1);
		temp |= allSideBoard & (temp << CELL_LENGTH + 1);
		temp |= allSideBoard & (temp << CELL_LENGTH + 1);
		temp |= allSideBoard & (temp << CELL_LENGTH + 1);
		legalBoard |= blankBoard & (temp << CELL_LENGTH + 1);

		//右斜め下
		temp = allSideBoard & (myBoard >>> CELL_LENGTH + 1);
		temp |= allSideBoard & (temp >>> CELL_LENGTH + 1);
		temp |= allSideBoard & (temp >>> CELL_LENGTH + 1);
		temp |= allSideBoard & (temp >>> CELL_LENGTH + 1);
		temp |= allSideBoard & (temp >>> CELL_LENGTH + 1);
		temp |= allSideBoard & (temp >>> CELL_LENGTH + 1);
		legalBoard |= blankBoard & (temp >>> CELL_LENGTH + 1);

		//左斜め下
		temp = allSideBoard & (myBoard >>> CELL_LENGTH - 1);
		temp |= allSideBoard & (temp >>> CELL_LENGTH - 1);
		temp |= allSideBoard & (temp >>> CELL_LENGTH - 1);
		temp |= allSideBoard & (temp >>> CELL_LENGTH - 1);
		temp |= allSideBoard & (temp >>> CELL_LENGTH - 1);
		temp |= allSideBoard & (temp >>> CELL_LENGTH - 1);
		legalBoard |= blankBoard & (temp >>> CELL_LENGTH - 1);

		return legalBoard;
	}

	/**
	 * 反転処理を行う
	 * @param put 置いた場所のみ立っているビット
	 * @param myBoard 自分の盤面
	 * @return 自分と相手の盤面
	 */
	private void reverse(long put, long myBoard) {
		//敵側の盤面を取得
		long enemyBoard = (black | white) ^ myBoard;

		//着手した場合の盤面を作成
		long rev = 0;

		for (int i = 0; i < 8; i++) {
			long rev_ = 0;
			long mask = createReversePointBoard(put, i);
			while ((mask != 0) && ((mask & enemyBoard) != 0)) {
				rev_ |= mask;
				mask = createReversePointBoard(mask, i);
			}
			if ((mask & myBoard) != 0) {
				rev |= rev_;
			}
		}
		if (myBoard == black) {
			myBoard ^= put | rev;
			enemyBoard ^= rev;
			black = myBoard;
			white = enemyBoard;
		} else {
			myBoard ^= put | rev;
			enemyBoard ^= rev;
			white = myBoard;
			black = enemyBoard;
		}
	}

	/**
	 * 反転箇所を検索する
	 * @param put 置いた場所のみ立っているビット
	 * @param k 反転方向（上から時計回りに8方向）
	 * @return 反転可能か検索する場所のみ立っているビット
	 */
	private long createReversePointBoard(long put, int k) {
		switch (k) {
		case 0://上
			return (put << CELL_LENGTH) & 0xffffffffffffff00L;

		case 1://右上
			return (put << CELL_LENGTH - 1) & 0x7f7f7f7f7f7f7f00L;

		case 2://右
			return (put >>> 1) & 0x7f7f7f7f7f7f7f7fL;

		case 3://右下
			return (put >>> CELL_LENGTH + 1) & 0x007f7f7f7f7f7f7fL;

		case 4://下
			return (put >>> CELL_LENGTH) & 0x00ffffffffffffffL;

		case 5://左下
			return (put >>> CELL_LENGTH - 1) & 0x00fefefefefefefeL;

		case 6://左
			return (put << 1) & 0xfefefefefefefefeL;

		case 7://左上
			return (put << CELL_LENGTH + 1) & 0xfefefefefefefe00L;

		default:
			return 0;
		}
	}

	/**
	 * パスになるかの判定をする
	 * @param myBoard 自分の盤面
	 * @return パスになるなら true
	 */
	private boolean isPass(long myBoard) {
		//自分の合法手ボードを作成
		long myLegalBoard = createLegalBoard(myBoard);

		//相手の合法手ボードを作成
		long enemyBoard = (black | white) ^ myBoard;
		long enemyLegalBoard = createLegalBoard(enemyBoard);

		return myLegalBoard == 0 && enemyLegalBoard != 0;
	}

	/**
	 * 終局になるかの判定をする
	 * @param myBoard 自分の盤面
	 * @return 終局になるなら true
	 */
	private boolean isGameFinished(long myBoard) {
		//自分の合法手ボードを作成
		long myLegalBoard = createLegalBoard(myBoard);

		//相手の合法手ボードを作成
		long enemyBoard = (black | white) ^ myBoard;
		long enemyLegalBoard = createLegalBoard(enemyBoard);

		return myLegalBoard == 0 && enemyLegalBoard == 0;
	}

	/**
	 * 合法手数を計算
	 * @param myBoard 自分の盤面
	 * @return 序盤の評価値
	 */
	private int reversePointScoreCount(long myBoard) {
		//自分の合法手数をカウント
		long myLegalBoard = createLegalBoard(myBoard);
		int myReversePoint = Long.bitCount(myLegalBoard);

		//相手の合法手数をカウント
		long enemyBoard = (black | white) ^ myBoard;
		long enemyLegalBoard = createLegalBoard(enemyBoard);
		int enemyReversePoint = Long.bitCount(enemyLegalBoard);

		return myReversePoint - enemyReversePoint;
	}

	private void a() {

	}

	public int alphaBeta(long myBoard, int myTurn, int alpha, int beta, int depth) {
		if (depth == 0) {
			//評価値計算
			return 0;
		}
		int bestScore = alpha;
		boolean pass = true;
		long moves = createLegalBoard(myBoard);
		long move;

		while ((move = Long.lowestOneBit(moves)) != 0) {
			//			long reverses = reverse(move, myBoard);
		}
		//		if (list.size() == 0) {
		//			child = alphaBeta(copyBoard, myTurn, alpha, beta, depth - 1);
		//			if (myTurn == turn) {
		//				if (child > score) {
		//					score = child;
		//					alpha = score;
		//				}
		//				if (score > beta) {
		//					return score;
		//				}
		//			} else {
		//				if (child < score) {
		//					score = child;
		//					beta = score;
		//				}
		//				if (score < alpha) {
		//					return score;
		//				}
		//			}
		//		} else {
		//			for (Point p : reverseAllPointList(myTurn, copyBoard)) {
		//				int x = p.x;
		//				int y = p.y;
		//
		//				for (Point pt : reverseAroundPointList(x, y, myTurn, copyBoard)) {
		//					copyBoard[pt.x][pt.y] = myTurn;
		//				}
		//				copyBoard[x][y] = myTurn;
		//
		//				child = alphaBeta(copyBoard, -myTurn, alpha, beta, depth - 1);
		//
		//				if (myTurn == turn) {
		//					if (child > score) {
		//						score = child;
		//						alpha = score;
		//						bestX = x;
		//						bestY = y;
		//					}
		//					if (score > beta) {
		//						return score;
		//					}
		//				} else {
		//					if (child < score) {
		//						score = child;
		//						beta = score;
		//						bestX = x;
		//						bestY = y;
		//					}
		//					if (score < alpha) {
		//						return score;
		//					}
		//				}
		//			}
		//		}

		//		if (depth == SEARCH_LEVEL) {
		//			if (bestX + bestY < 0) {
		//				System.out.println("パス");
		//				return score;
		//			} else {
		//				return bestX + bestY * CELL_LENGTH * CELL_LENGTH;
		//			}
		//		} else {
		//			return score;
		//		}
		return bestScore;
	}
}
