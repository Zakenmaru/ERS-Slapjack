import java.awt.*;
import java.util.ArrayList;

import javax.swing.JComponent;

public class ERSGraphicsComponent extends JComponent
{
	private static final int CARD_WIDTH = 250;
	private static final int CARD_HEIGHT = 350;
	private static final int DISTANCE_FROM_CORNER = 15;
	private static final int DISTANCE_FROM_BOTTOM = 30;
	private static final int TURN_INDICATOR_SIZE = 20;
	private static final int DISTANCE_OF_SIZE_TEXT_FROM_TOP_OF_SCREEN = 50;
	private static final int WIDTH_OF_SIZE_TEXT = 270;
	private static final int WIDTH_OF_SIZE_TEXT_EXTENDED = 470;
	private static final int INITIAL_VERTICAL_DISPLACEMENT = 100;
	private static final int VERTICAL_DISPLACEMENT_DIFFERENCE = 50;
	private static final int HORIZONTAL_DISTANCE_FROM_CENTER = 100;
	private static final int DISTANCE_PLAY_FROM_BOTTOM = 60;
	private static final int DISTANCE_SLAP_FROM_BOTTOM = 38;
	private static final int STROKE_SIZE = 3;
	private static final int CARD_SEPERATION_FACTOR = 3;
	private static final int SPADE = 1;
	private static final int HEARTS = 2;
	private static final int DIAMOND = 3;
	private static final int CLUBS = 4;
	private static final int HUGE_FONT = 128;
	private static final int LARGER_FONT = 110;
	private static final int LARGE_FONT = 36;
	private static final int MEDIUM_FONT = 24;
	private static final int SMALL_FONT = 20;
	private static final int DISPLAY_DIALOG_X_DISPLACEMENT = 135;
	private static final int DISPLAY_DIALOG_Y_DISPLACEMENT = 60;
	private static final int DECK_STRING_X_DISPLACEMENT = 90;
	private static final int DECK_STRING_Y_DISPLACEMENT = 225;
	private static final int DECK_STRING_X_DISPLACEMENT_FOR_TEN = 60;
	private static final int DECK_STRING_Y_DISPLACEMENT_FOR_TOP_LEFT = 45;
	private static final int HORIZONTAL_VERTICAL_MASTER_OFFSET_FACTOR = 3;
	private static final int TEN = 10;
	private static final int CARD_DIVISION_DIVISOR = 4;

	private ArrayList<Card> cards;
	private int frameWidth;
	private int frameHeight;
	private int curTurn;
	private ArrayList<PlayerDeck> players;
	private String displayDialog;
	private int sizeOfDeck;
	private int sizeOfBurnDeck;

	/*Creates the graphics component object.
	@param width initial width of window.
	@param height initial height of window.
	@param decks ArrayList of playerDecks.
	 */
	public ERSGraphicsComponent(int width, int height, ArrayList<PlayerDeck> decks)
	{
		cards = new ArrayList<Card>();
		frameHeight = height;
		frameWidth = width;
		players = decks;
		curTurn = 0;
		displayDialog = "";
		sizeOfDeck = 0;
		sizeOfBurnDeck = 0;
	}

	/*Draws the frame. Has many helper methods.
	@param gr graphics object used to draw.
	 */
	@Override
	public void paintComponent(Graphics gr)
	{
		frameWidth = getWidth();
		frameHeight = getHeight();
		Graphics2D g2 = (Graphics2D) gr;
		g2.setStroke(new BasicStroke(STROKE_SIZE));
		drawDeck(g2);
		drawPlayerDecks(g2);
		if (!displayDialog.equals(""))
			drawDisplayDialog(g2, displayDialog);
		drawDeckSize(g2);
	}

	/*Draws the grand deck's top 3 cards. Uses a helper method.
	@param g2 graphics2d object used to draw.
	 */
	private void drawDeck(Graphics2D g2)
	{
		int verticalDisplacement = INITIAL_VERTICAL_DISPLACEMENT;
		for (int curCard = cards.size() - 1; curCard >= 0; curCard--)
		{
			drawCardInDeck(g2, verticalDisplacement, curCard);
			verticalDisplacement -= VERTICAL_DISPLACEMENT_DIFFERENCE;
		}
	}

	/*Draws a card in the grand deck.
	@param g2 graphics2d object used to draw.
	@param verticalDisplacement distance of card from center of screen.
	@param cardIndex card index (0-3) to draw.
	 */
	private void drawCardInDeck(Graphics2D g2, int verticalDisplacement, int cardIndex)
	{
		Rectangle aCard = new Rectangle(frameWidth / 2 - CARD_WIDTH / 2 - verticalDisplacement /
				CARD_SEPERATION_FACTOR, frameHeight / 2 - CARD_HEIGHT / 2 - verticalDisplacement,
				CARD_WIDTH, CARD_HEIGHT);
		g2.setColor(Color.WHITE);
		g2.fill(aCard);
		if (cards.get(cardIndex).getSuitNumber() == SPADE)
			g2.setColor(Color.BLUE);
		else if (cards.get(cardIndex).getSuitNumber() == HEARTS)
			g2.setColor(Color.PINK);
		else if (cards.get(cardIndex).getSuitNumber() == DIAMOND)
			g2.setColor(Color.YELLOW);
		else if (cards.get(cardIndex).getSuitNumber() == CLUBS)
			g2.setColor(Color.GREEN);
		g2.draw(aCard);
		Font fontyFont = new Font("IMPACT", Font.BOLD, LARGE_FONT);
		g2.setColor(Color.BLACK);
		g2.setFont(fontyFont);
		g2.drawString(cards.get(cardIndex).getNumberName(),
				frameWidth / 2 - CARD_WIDTH / 2 + DISTANCE_FROM_CORNER - verticalDisplacement /
						HORIZONTAL_VERTICAL_MASTER_OFFSET_FACTOR, frameHeight / 2 - CARD_HEIGHT / 2 +
						DECK_STRING_Y_DISPLACEMENT_FOR_TOP_LEFT - verticalDisplacement);
		fontyFont = new Font("IMPACT", Font.BOLD, LARGER_FONT);
		g2.setFont(fontyFont);
		if (cards.get(cardIndex).getNumber() != TEN)
			g2.drawString(cards.get(cardIndex).getNumberName().substring(0,1),
					frameWidth / 2 - CARD_WIDTH / 2 + DECK_STRING_X_DISPLACEMENT - verticalDisplacement /
							HORIZONTAL_VERTICAL_MASTER_OFFSET_FACTOR, frameHeight / 2 - CARD_HEIGHT / 2 +
							DECK_STRING_Y_DISPLACEMENT - verticalDisplacement);
		else
			g2.drawString(cards.get(cardIndex).getNumberName().substring(0,2),
					frameWidth / 2 - CARD_WIDTH / 2 + DECK_STRING_X_DISPLACEMENT_FOR_TEN - verticalDisplacement /
							HORIZONTAL_VERTICAL_MASTER_OFFSET_FACTOR, frameHeight / 2 - CARD_HEIGHT / 2 +
							DECK_STRING_Y_DISPLACEMENT - verticalDisplacement);
	}

	/*Draws the players' decks. Uses helper methods.
	@param g2 graphics2d object used to draw.
	 */
	private void drawPlayerDecks(Graphics2D g2)
	{
		int maxHorizontalDisplacement = -1 * (frameWidth / 2 - HORIZONTAL_DISTANCE_FROM_CENTER);
		int horizontalDisplacement = maxHorizontalDisplacement;
		for (int player = 0; player < players.size(); player++)
		{
			drawPlayerDeckBackground(g2, player, horizontalDisplacement);
			drawTurnIndicator(g2, player, horizontalDisplacement);
			drawControlsText(g2, player, horizontalDisplacement);
			horizontalDisplacement += -1 * maxHorizontalDisplacement * 2 / (players.size() - 1);
		}
	}

	/*Draws the background for the player's deck.
	@param g2 graphics2d object used to draw.
	@param player player of deck to draw.
	@param horizontalDisplacement displacement from center of screen.
	 */
	private void drawPlayerDeckBackground(Graphics2D g2, int player, int horizontalDisplacement)
	{
		if (players.get(player).size() > 0)
		{
			Rectangle aCard = new Rectangle(frameWidth / 2 - CARD_WIDTH / 4 + horizontalDisplacement,
					frameHeight - CARD_HEIGHT / 2 - DISTANCE_FROM_BOTTOM, CARD_WIDTH / 2,
					CARD_HEIGHT / 2);
			g2.setColor(Color.RED);
			g2.fill(aCard);
			g2.setColor(Color.WHITE);
			g2.draw(aCard);
		}
	}

	/*Draws the turn indicator.
	@param g2 graphics2d object used to draw.
	@param player player to potentially draw turn indicator above deck of.
	@param horizontalDisplacement displacement from center of screen.
	 */
	private void drawTurnIndicator(Graphics2D g2, int player, int horizontalDisplacement)
	{
		if (player == curTurn)
		{
			g2.setColor(Color.WHITE);
			g2.fillOval(frameWidth / 2 - TURN_INDICATOR_SIZE / 2 + horizontalDisplacement,
					frameHeight - CARD_HEIGHT / 2 - DISTANCE_FROM_BOTTOM * 2, TURN_INDICATOR_SIZE,
					TURN_INDICATOR_SIZE);
			g2.setColor(Color.RED);
			g2.drawOval(frameWidth / 2 - TURN_INDICATOR_SIZE / 2 + horizontalDisplacement,
					frameHeight - CARD_HEIGHT / 2 - DISTANCE_FROM_BOTTOM * 2, TURN_INDICATOR_SIZE,
					TURN_INDICATOR_SIZE);
		}
	}

	/*Draws the controls text for the player.
	@param g2 graphics2d object used to draw.
	@param player player to draw controls of.
	@param horizontalDisplacement displacement from center of screen.
	 */
	private void drawControlsText(Graphics2D g2, int player, int horizontalDisplacement)
	{
		Font fontyFont = new Font("IMPACT", Font.BOLD, MEDIUM_FONT);
		g2.setColor(Color.WHITE);
		g2.setFont(fontyFont);
		if (players.get(player).playingGame)
		{
			int sizeOfDeck = players.get(player).size();
			if (sizeOfDeck != 1)
				g2.drawString(players.get(player).size() + " Cards",
						frameWidth / 2 - CARD_WIDTH / CARD_DIVISION_DIVISOR + horizontalDisplacement +
								DISTANCE_FROM_CORNER, frameHeight - CARD_HEIGHT / 2 + DISTANCE_FROM_CORNER);
			else
				g2.drawString(players.get(player).size() + " Card",
						frameWidth / 2 - CARD_WIDTH / CARD_DIVISION_DIVISOR + horizontalDisplacement +
								DISTANCE_FROM_CORNER, frameHeight - CARD_HEIGHT / 2 + DISTANCE_FROM_CORNER);
			g2.setColor(Color.BLACK);
			fontyFont = new Font("IMPACT", Font.ITALIC, SMALL_FONT);
			g2.setFont(fontyFont);
			//Draw correct controls for each player
			g2.drawString(players.get(player).getPlayButton() + " - Play",
					frameWidth / 2 - CARD_WIDTH / CARD_DIVISION_DIVISOR + horizontalDisplacement +
							DISTANCE_FROM_CORNER, frameHeight - DISTANCE_PLAY_FROM_BOTTOM);
			g2.drawString(players.get(player).getSlapButton() + " - Slap",
					frameWidth / 2 - CARD_WIDTH / CARD_DIVISION_DIVISOR + horizontalDisplacement +
							DISTANCE_FROM_CORNER, (int) (frameHeight - DISTANCE_SLAP_FROM_BOTTOM));
		}
		else
		{
			g2.drawString("OUT",
					frameWidth / 2 - CARD_WIDTH / CARD_DIVISION_DIVISOR + horizontalDisplacement +
							DISTANCE_FROM_CORNER, frameHeight - CARD_HEIGHT / 2 + DISTANCE_FROM_CORNER);
		}
	}

	/*Draws the display dialog of the frame.
	@param g2 graphics2d object used to draw.
	@param displayDialog string to display on frame.
	 */
	private void drawDisplayDialog(Graphics2D g2, String displayDialog)
	{
		Font fontyFont = new Font("IMPACT", Font.BOLD, HUGE_FONT);
		g2.setFont(fontyFont);
		g2.setColor(Color.WHITE);
		g2.drawString(displayDialog, DISPLAY_DIALOG_Y_DISPLACEMENT, frameHeight / 2);
		g2.drawString(" Esc to", frameWidth / 2 + DISPLAY_DIALOG_X_DISPLACEMENT,
				frameHeight / 2 - DISPLAY_DIALOG_Y_DISPLACEMENT);
		g2.drawString("Restart", frameWidth / 2 + DISPLAY_DIALOG_X_DISPLACEMENT,
				frameHeight / 2 + DISPLAY_DIALOG_Y_DISPLACEMENT);
	}

	/*Draws the string showing the current deck size.
	@param g2 graphics2d object used to draw.
	 */
	private void drawDeckSize(Graphics2D g2)
	{
		Font fontyFont = new Font("IMPACT", Font.BOLD, LARGE_FONT);
		g2.setFont(fontyFont);
		g2.setColor(Color.WHITE);
		String cardAmt = sizeOfDeck + " card";
		if (sizeOfDeck > 1)
			cardAmt += "s in deck";
		else
			cardAmt += " in deck";
		int curWidth = WIDTH_OF_SIZE_TEXT;
		if (sizeOfBurnDeck > 0)
		{
			cardAmt += "  ( " + sizeOfBurnDeck + " burned )";
			curWidth = WIDTH_OF_SIZE_TEXT_EXTENDED;
		}
		if (sizeOfDeck > 0)
			g2.drawString(cardAmt, frameWidth / 2 - curWidth / 2,
					DISTANCE_OF_SIZE_TEXT_FROM_TOP_OF_SCREEN);
	}

	/*Updates the top 3 cards in master deck and redraws.
	@param topThreeCards top 3 cards to update with.
	 */
	public void changeCards(ArrayList<Card> topThreeCards)
	{
		cards = topThreeCards;
		repaint();
	}

	/*Updates the playerDecks and redraws.
	@param decks playerDecks to update ArrayList with.
	 */
	public void updatePlayers(ArrayList<PlayerDeck> decks)
	{
		players = decks;
		repaint();
	}

	/*Updates the display dialog and redraws.
	@param dialog string to update display dialog with.
	 */
	public void setDisplayDialog(String dialog)
	{
		displayDialog = dialog;
		repaint();
	}

	/*Updates current turn and redraws.
	@param turn current player turn to update with.
	 */
	public void updateTurn(int turn)
	{
		curTurn = turn;
		repaint();
	}

	/*Updates size of current deck.
	@param newSize size to update with.
	 */
	public void setSizeOfDeck(int newSize, int burnCards)
	{
		sizeOfDeck = newSize;
		sizeOfBurnDeck = burnCards;
	}

}