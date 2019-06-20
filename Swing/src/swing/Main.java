package swing;

public class Main {
	public static void main(String[] args) {
		GameWindow window = new GameWindow(
				"テトリス",
				(Params.CELL_WIDTH + 1) * (Params.FIELD_WIDTH + 2) + 5,
				(Params.CELL_WIDTH + 1) * (Params.FIELD_HEIGHT + 1) + 28);
		window.add(window.game);
		window.setVisible(true);
	}
}
