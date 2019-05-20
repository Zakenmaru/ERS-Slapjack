package ers.slapjack;

import javax.swing.JFrame;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class PlayingField
{
	private static final int TITLE_FRAME_WIDTH = 500;
	private static final int TITLE_FRAME_HEIGHT = 400;
	private static final int FRAME_X_LOC = 300;
	private static final int FRAME_Y_LOC = 100;
	private static final int GAME_FRAME_WIDTH = 1280;
	private static final int GAME_FRAME_HEIGHT = 960;
	private static final int DECK_SIZE = 52;
	private static final int DELAY_TILL_CARD_ABSORB = 80;
	private static final int TICK_SPEED = 10;
	private static final int START_ROWS = 6;
	private static final int START_COLS = 1;
	private static final int STARTING_SELECTED_PLAYER_BUTTON = 3;
	private static final int STARTING_SELECTED_DECK_BUTTON = 1;
	private static final int LARGE_FONT_SIZE = 34;
	private static final int MEDIUM_FONT_SIZE = 15;
	private static final int MAX_PLAYERS = 10;
	private static final int MIN_PLAYERS = 2;
	private static final int MAX_DECKS = 4;
	private static final int MIN_DECKS = 1;
	private static final int JACK = 11;
	private static final int QUEEN = 12;
	private static final int KING = 13;
	private static final int ACE = 1;
	private static final int JACK_TRAP_AMOUNT = 1;
	private static final int QUEEN_TRAP_AMOUNT = 2;
	private static final int KING_TRAP_AMOUNT = 3;
	private static final int ACE_TRAP_AMOUNT = 4;
	private static final int TEN = 10;

	private static KeyInputter keyInput;
	private static boolean gameStarted;

	private static boolean slappable;
	private static int timeTillPlayerTakesDeck;
	private static int playerWhoPlayedSpecialCard;
	private static int cardsTillSpecialPause;

	private static ArrayList<PlayerDeck> players;
	private static int curPlayerTurn;
	private static int numPlayers;
	private static int numDecks;

	private static MasterDeck masterDeck;
	private static BurnDeck burnDeck;

	private static ERSGraphicsComponent graphics;
	private static JFrame startFrame;
	private static JFrame gameFrame;
	private static Font font;

	private static boolean slapJackMode;

	//timer that ticks every TICK_SPEED milliseconds
	public static class Ticker implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			if (timeTillPlayerTakesDeck > -1)
				timeTillPlayerTakesDeck--;
			if (timeTillPlayerTakesDeck == 0)
				playerTakeDeck();
			if (masterDeck.size() + burnDeck.size() == DECK_SIZE * numDecks)
			{
				gameStarted = false;
				graphics.setDisplayDialog("Loser!");
			}
			while (players.get(curPlayerTurn).size() == 0 && gameStarted)
			{
				curPlayerTurn++;
				if (curPlayerTurn >= numPlayers)
					curPlayerTurn = 0;
				graphics.updateTurn(curPlayerTurn);
			}
			if (!gameStarted && keyInput.curLetter != null && keyInput.curLetter.equals("Escape"))
			{
				graphics.setVisible(false);
				gameFrame.dispose();
				startFrame.setVisible(true);
			}
			else if (keyInput.curLetter != null && gameStarted)
			{
				for (int player = 0; player < numPlayers; player++)
				{
					if (keyInput.curLetter.equals(players.get(player).getPlayButton()))
						play(player);
					if (keyInput.curLetter.equals(players.get(player).getSlapButton()))
						slap(player);
				}
				keyInput.curLetter = null;
			}
		}
	}

	//Main method
	public static void main(String[] args)
	{
		slapJackMode = false;
		gameStarted = false;
		startFrame = new JFrame("Setup Game");

		startFrame.setSize(TITLE_FRAME_WIDTH, TITLE_FRAME_HEIGHT);
		startFrame.setLocation(FRAME_X_LOC, FRAME_Y_LOC);
		startFrame.getContentPane().setBackground(Color.BLACK);
		startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startFrame.setLayout(new GridLayout(START_ROWS, START_COLS));

		JPanel title = initializeTitle();
		JPanel firstRow = initializeFirstRow();
		JPanel secondRow = initializeSecondRow();
		JPanel thirdRow = initializeThirdRow();
		JPanel lastRow = initializeLastRow(startFrame);
		JPanel blankPanelForAsthetic = new JPanel();
		blankPanelForAsthetic.setBackground(Color.BLACK);

		startFrame.add(title);
		startFrame.add(blankPanelForAsthetic);
		startFrame.add(firstRow);
		startFrame.add(secondRow);
		startFrame.add(thirdRow);
		startFrame.add(lastRow);

		startFrame.setVisible(true);
	}

	//Creates the top row of the start screen which is the title.
	private static JPanel initializeTitle()
	{
		font = new Font("IMPACT", Font.BOLD, LARGE_FONT_SIZE);
		JPanel title = new JPanel();
		title.setBackground(Color.WHITE);
		JLabel titleText = new JLabel("Egyptian Rat Screw");
		titleText.setFont(font);
		title.add(titleText);
		return title;
	}

	/* Sets up first row of UI on start screen
     * @return JPanel of first row of options
     */
	private static JPanel initializeFirstRow()
	{
		font = new Font("IMPACT", Font.PLAIN, MEDIUM_FONT_SIZE);
		JPanel firstRow = new JPanel();
		firstRow.setOpaque(true);
		firstRow.setBackground(Color.RED);
		JRadioButton slapJack = new JRadioButton("Slapjack", false);
		JRadioButton classic = new JRadioButton("Classic ERS", true);
		slapJack.setFont(font);
		classic.setFont(font);
		firstRow.add(slapJack);
		firstRow.add(classic);
		JLabel gameModeText = new JLabel("Select Game Mode");
		gameModeText.setFont(font);
		firstRow.add(gameModeText);
		classic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				slapJackMode = false;
				if (slapJack.isSelected())
					slapJack.setSelected(false);
				else
					classic.setSelected(true);
			}});
		slapJack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				slapJackMode = true;
				if (classic.isSelected())
					classic.setSelected(false);
				else
					slapJack.setSelected(true);
			}});
		return firstRow;
	}

	/* Sets up second row of UI on start screen
	 * @return JPanel of second row of options
	 */
	private static JPanel initializeSecondRow()
	{
		JPanel secondRow = new JPanel();
		secondRow.setBackground(Color.WHITE);
		ButtonGroup group = new ButtonGroup();
		for (int playerButton = MIN_PLAYERS; playerButton <= MAX_PLAYERS; playerButton++)
		{
			JRadioButton buttonTemp = new JRadioButton(playerButton + "");
			buttonTemp.setFont(font);
			if (playerButton == STARTING_SELECTED_PLAYER_BUTTON) {
				numPlayers = STARTING_SELECTED_PLAYER_BUTTON;
				buttonTemp.setSelected(true);
			}
			group.add(buttonTemp);
			secondRow.add(buttonTemp);
			initializePlayerButtonActionListener(playerButton, buttonTemp);
		}
		JLabel playersText = new JLabel("Number of players");
		playersText.setFont(font);
		secondRow.add(playersText);
		return secondRow;
	}

	/* Creates actionlisteners for player amount buttons
	 * @param player player amount to create button for
	 * @param button to add actionlistener to
	 */
	private static void initializePlayerButtonActionListener(int player, JRadioButton button)
	{
		if (player == 2)
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					numPlayers = 2; }});
		else if (player == 3)
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					numPlayers = 3; }});
		else if (player == 4)
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					numPlayers = 4; }});
		else if (player == 5)
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					numPlayers = 5; }});
		else if (player == 6)
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					numPlayers = 6; }});
		else if (player == 7)
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					numPlayers = 7; }});
		else if (player == 8)
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					numPlayers = 8; }});
		else if (player == 9)
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					numPlayers = 9; }});
		else if (player == 10)
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					numPlayers = 10; }});
	}

	//Sets up the third row of UI on start screen. This creates the deck number selection buttons.
	private static JPanel initializeThirdRow()
	{
		JPanel thirdRow = new JPanel();
		thirdRow.setBackground(Color.RED);
		ButtonGroup group = new ButtonGroup();
		for (int deckButton = MIN_DECKS; deckButton <= MAX_DECKS; deckButton++)
		{
			JRadioButton buttonTemp = new JRadioButton(deckButton + "");
			buttonTemp.setFont(font);
			if (deckButton == STARTING_SELECTED_DECK_BUTTON)
			{
				numDecks = STARTING_SELECTED_DECK_BUTTON;
				buttonTemp.setSelected(true);
			}
			group.add(buttonTemp);
			thirdRow.add(buttonTemp);
			if (deckButton == 1)
				buttonTemp.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						numDecks = 1; }});
			else if (deckButton == 2)
				buttonTemp.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						numDecks = 2; }});
			else if (deckButton == 3)
				buttonTemp.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						numDecks = 3; }});
			else if (deckButton == 4)
				buttonTemp.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						numDecks = 4; }});
		}
		JLabel decksText = new JLabel("Number of decks to use");
		decksText.setFont(font);
		thirdRow.add(decksText);
		return thirdRow;
	}

	/* Sets up last row of UI on start screen
	 * @param frame JFrame of start game UI
	 * @return JPanel of last row of options
	 */
	private static JPanel initializeLastRow(JFrame frame)
	{
		JPanel lastRow = new JPanel();
		lastRow.setOpaque(true);
		lastRow.setBackground(Color.BLACK);
		JButton start = new JButton("Start");
		start.setFont(font);
		JButton exit = new JButton("Exit");
		exit.setFont(font);
		lastRow.add(start);
		lastRow.add(exit);
		start.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				initializeGame();
				initializeWindowAndGraphics();
				frame.setVisible(false);
			}
		});
		exit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		return lastRow;
	}

	//Sets up the player decks, the master deck, and the burn deck. Pt 1 of game setup
	private static void initializeGame()
	{
		players = new ArrayList<PlayerDeck>();
		for (int playerSetup = 0; playerSetup < numPlayers; playerSetup++)
		{
			if (playerSetup == 0)
				players.add(new PlayerDeck("X","Z"));
			else if (playerSetup == 1)
				players.add(new PlayerDeck("N","M"));
			else if (playerSetup == 2)
				players.add(new PlayerDeck("W","Q"));
			else if (playerSetup == 3)
				players.add(new PlayerDeck("O","P"));
			else if (playerSetup == 4)
				players.add(new PlayerDeck("K","L"));
			else if (playerSetup == 5)
				players.add(new PlayerDeck("S","A"));
			else if (playerSetup == 6)
				players.add(new PlayerDeck("2","1"));
			else if (playerSetup == 7)
				players.add(new PlayerDeck("V","B"));
			else if (playerSetup == 8)
				players.add(new PlayerDeck("R","T"));
			else if (playerSetup == 9)
				players.add(new PlayerDeck("Y","U"));

		}
		Stack<Card> dealDeck = new Stack<Card>();
		for (int decks = 0; decks < numDecks; decks++)
		{
			for (int number = 1; number <= 13; number++)
			{
				for (int suit = 1; suit <= 4; suit++)
				{
					Card toAdd = new Card(number, suit);
					dealDeck.push(toAdd);
				}
			}
		}
		Collections.shuffle(dealDeck);
		int curDealIndex = (int)(Math.random() * numPlayers);
		while (!dealDeck.isEmpty())
		{
			players.get(curDealIndex).addCard(dealDeck.pop());
			curDealIndex++;
			if (curDealIndex >= numPlayers)
				curDealIndex = 0;
		}
		curPlayerTurn = curDealIndex;
		masterDeck = new MasterDeck();
		burnDeck = new BurnDeck();
		playerWhoPlayedSpecialCard = -1;
		timeTillPlayerTakesDeck = -1;
		cardsTillSpecialPause = -1;
		gameStarted = true;
	}

	//Sets up the window, timer, and graphics. Pt 2 of game setup.
	private static void initializeWindowAndGraphics()
	{
		ActionListener ticker = new Ticker();
		Timer timer = new Timer(TICK_SPEED, ticker);
		timer.setRepeats(true);
		timer.start();
		gameFrame = new JFrame("ERS");
		gameFrame.setSize(GAME_FRAME_WIDTH, GAME_FRAME_HEIGHT);
		gameFrame.setLocation(FRAME_X_LOC, FRAME_Y_LOC);
		gameFrame.setResizable(true);
		gameFrame.getContentPane().setBackground(Color.BLACK);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		graphics = new ERSGraphicsComponent(GAME_FRAME_WIDTH, GAME_FRAME_HEIGHT, players);
		graphics.changeCards(masterDeck.getTopThreeCards());
		graphics.updateTurn(curPlayerTurn);
		gameFrame.add(graphics);
		keyInput = new KeyInputter();
		gameFrame.addKeyListener(keyInput);
		gameFrame.setVisible(true);
	}

	//Checks the slappability of the current array of top 3 cards in the master deck
	private static void refreshSlapStatus()
	{
		slappable = false;
		if (slapJackMode && masterDeck.size() > 0 && masterDeck.topCard().getNumber() == JACK)
			slappable = true;
		if (!slapJackMode)
		{
			ArrayList<Integer> top3Numbers = new ArrayList<Integer>();
			for (int player = 0; player < masterDeck.getTopThreeCards().size(); player++)
			{
				top3Numbers.add(masterDeck.getTopThreeCards().get(player).getNumber());
			}
			if (top3Numbers.size() >= 2 && top3Numbers.get(0) == top3Numbers.get(1))
				slappable = true;
			if (top3Numbers.size() >= 3 && top3Numbers.get(0) == top3Numbers.get(2))
				slappable = true;
			if (top3Numbers.size() >= 2 && top3Numbers.get(0) + top3Numbers.get(1) == TEN)
				slappable = true;
			if (top3Numbers.size() >= 2 && top3Numbers.get(0) + top3Numbers.get(1) == TEN)
				slappable = true;
			if (top3Numbers.size() >= 2 && top3Numbers.get(0) == QUEEN && top3Numbers.get(1) == KING)
				slappable = true;
			if (top3Numbers.size() >= 2 && top3Numbers.get(0) == KING && top3Numbers.get(1) == QUEEN)
				slappable = true;
			if (top3Numbers.size() >= 3 && top3Numbers.get(0) == QUEEN && top3Numbers.get(2) == KING)
				slappable = true;
			if (top3Numbers.size() >= 3 && top3Numbers.get(0) == KING && top3Numbers.get(2) == QUEEN)
				slappable = true;
			if (top3Numbers.size() >= 3 && top3Numbers.get(0) + top3Numbers.get(2) == TEN)
				slappable = true;
		}
		graphics.setSizeOfDeck(masterDeck.size() + burnDeck.size(), burnDeck.size());
	}

	/*Slaps a card for a player. If deck is slappable, player takes the deck. If not, player burns a card.
	 *@param player integer of ers.slapjack.PlayerDeck to alter in ArrayList of PlayerDecks
	 */
	private static void slap(int player)
	{
		if (slappable && players.get(player).playingGame)
		{
			players.get(player).addCard(burnDeck.removeCard());
			players.get(player).addCard(masterDeck.removeCards());
			cardsTillSpecialPause = -1;
			playerWhoPlayedSpecialCard = -1;
			timeTillPlayerTakesDeck = -1;
			curPlayerTurn = player;
			graphics.updateTurn(curPlayerTurn);
			refreshDeathStatus();
			refreshSlapStatus();
			checkForVictory();
			graphics.changeCards(masterDeck.getTopThreeCards());
		}
		else if (!slappable && players.get(player).playingGame && players.get(player).size() > 0)
		{
			burnDeck.addCard(players.get(player).removeCard());
			graphics.setSizeOfDeck(masterDeck.size() + burnDeck.size(), burnDeck.size());
		}
		if (gameStarted)
			graphics.updatePlayers(players);
	}

	//Refreshes the PlayerDecks to find who is out
	private static void refreshDeathStatus()
	{
		for (int player = 0; player < numPlayers; player++)
		{
			if (players.get(player).size() == 0)
			{
				players.get(player).playingGame = false;
			}
		}
		graphics.updatePlayers(players);
	}

	/*Plays a card for a player. If it is their turn, plays card. If not, player burns a card.
	 *@param player integer of ers.slapjack.PlayerDeck to alter in ArrayList of PlayerDecks
	 */
	private static void play(int player)
	{
		if (player == curPlayerTurn)
		{
			if (players.get(player).playingGame && players.get(player).size() > 0 && timeTillPlayerTakesDeck <= 0)
			{
				if (cardsTillSpecialPause > 0)
					cardsTillSpecialPause--;
				Card toPlay = players.get(player).removeCard();
				int numberOfCard = toPlay.getNumber();
				masterDeck.addCard(toPlay);
				if (!slapJackMode)
					checkCardERS(numberOfCard, player);
				else
				{
					curPlayerTurn++;
					graphics.updateTurn(curPlayerTurn);
				}
				if (curPlayerTurn >= numPlayers)
				{
					curPlayerTurn = 0;
					graphics.updateTurn(curPlayerTurn);

				}
				refreshSlapStatus();
				graphics.changeCards(masterDeck.getTopThreeCards());
			}
		}
		else if (players.get(player).size() > 0 && players.get(player).playingGame)
		{
			burnDeck.addCard(players.get(player).removeCard());
			graphics.setSizeOfDeck(masterDeck.size() + burnDeck.size(), burnDeck.size());
		}
		if (gameStarted)
			graphics.updatePlayers(players);
	}

	/*Checks the card played for being a special case card.
	 *jack traps next player for one turn, queen for two turns, king for three, ace for four.
	 *getting a 10 will break the current trap and advance to the next player's turn.
	 *@param numberOfCard number of card played.
	 *@param player player who played the card.
	 */
	private static void checkCardERS(int numberOfCard, int player)
	{
		if (cardsTillSpecialPause <= 0 | numberOfCard == ACE | numberOfCard == JACK | numberOfCard == QUEEN
				| numberOfCard == KING)
		{
			curPlayerTurn++;
			graphics.updateTurn(curPlayerTurn);
		}
		if (numberOfCard == ACE)
			trapPlayer(ACE_TRAP_AMOUNT, player);
		else if (numberOfCard == KING)
			trapPlayer(KING_TRAP_AMOUNT, player);
		else if (numberOfCard == QUEEN)
			trapPlayer(QUEEN_TRAP_AMOUNT, player);
		else if (numberOfCard == JACK)
			trapPlayer(JACK_TRAP_AMOUNT, player);
		else if (numberOfCard == TEN)
		{
			if (cardsTillSpecialPause >= 1)
			{
				curPlayerTurn++;
				graphics.updateTurn(curPlayerTurn);
			}
			cardsTillSpecialPause = -1;
			playerWhoPlayedSpecialCard = -1;
		}
		else if (cardsTillSpecialPause == 0)
		{
			timeTillPlayerTakesDeck = DELAY_TILL_CARD_ABSORB;
			cardsTillSpecialPause = -1;
		}
	}

	/*Executes the operations for if a player plays a special card in ERS
	 *@param trapLength number of cards next player needs to play
	 *@param player player index in players who played the special card
	 */
	private static void trapPlayer(int trapLength, int player)
	{
		cardsTillSpecialPause = trapLength;
		playerWhoPlayedSpecialCard = player;
	}

	/*Refreshes the PlayerDecks to find who has won (if any)
	 *and then calls the graphics component to show who has won.
	*/
	private static void checkForVictory()
	{
		for (int player = 0; player < numPlayers; player++)
		{
			if (players.get(player).size() == DECK_SIZE * numDecks)
			{
				gameStarted = false;
				graphics.setDisplayDialog("P" + (player + 1) + " wins!");
			}
		}
	}

	//Does the actions when a player automatically takes a deck after playing a special card.
	private static void playerTakeDeck()
	{
		timeTillPlayerTakesDeck = -1;
		players.get(playerWhoPlayedSpecialCard).addCard(burnDeck.removeCard());
		players.get(playerWhoPlayedSpecialCard).addCard(masterDeck.removeCards());
		graphics.changeCards(masterDeck.getTopThreeCards());
		refreshDeathStatus();
		refreshSlapStatus();
		checkForVictory();
		playerWhoPlayedSpecialCard = -1;
		timeTillPlayerTakesDeck = -1;
	}

}