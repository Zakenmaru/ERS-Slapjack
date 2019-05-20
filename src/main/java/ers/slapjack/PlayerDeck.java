package ers.slapjack;

import java.util.LinkedList;
import java.util.Stack;
import java.util.Queue;

public class PlayerDeck
{
    // the queue of cards that the player has in his hand
    public Queue<Card> cardDeck;
    // whether or not the player is currently in the game
    public boolean playingGame;
    //key you press to slap
    public String slapButton;
    //key you press to play a card
    public String playButton;


    /**
     * initializes the queue of cards, and by default sets the player to playing the game
     */
    public PlayerDeck()
    {
        cardDeck = new LinkedList<>();
        playingGame = true;
    }

    /**
     * initializes the queue of cards, and by default sets the player to playing the game, and
     * the buttons that the player uses
     * @param slap the string of the key used to slap the masterDeck
     * @param play the string of the key used to play onto the maser deck
     */
    public PlayerDeck(String slap, String play)
    {
        cardDeck = new LinkedList<>();
        playingGame = true;
        slapButton = slap;
        playButton = play;
    }
    /**
     * returns the deck of cards as a queue
     * @return the deck of cards
     */
    public Queue<Card> getCardDeck() // possibly delete after testing
    {
        return cardDeck;
    }

    /**
     * removes a single card from the top of the players hand
     * @return the single card
     */
    public Card removeCard()
    {
        if(cardDeck.size() >= 1)
            return cardDeck.remove();
        return null;
    }

    /**
     * adds a stack of cards into the players hand
     * @param cards the stack of cards from either ers.slapjack.MasterDeck, or ers.slapjack.BurnDeck
     */
    public void addCard(Stack<Card> cards)
    {
        while(!cards.isEmpty())
        {
            cardDeck.add(cards.pop());
        }

    }

    /**
     * adds a single card to the players hand
     * @param  card a singular card to be added to the players hand
     */
    public void addCard(Card card)
    {
        cardDeck.add(card);
    }

    /**
     * returns the button assigned to the player for slapping
     * @return the slap button in a string
     */
    public String getSlapButton()
    {
        return slapButton;
    }
    /**
     * gets the button that you assigned to pplay cards for the player
     * @return the button to play cards in a string
     */
    public String getPlayButton()
    {
        return playButton;
    }

    /**
     * returns the status of the player, whether he is playing the game or not
     * @return whether the player is active in the game or not
     */
    public boolean getStatus()
    {
        return playingGame;
    }

    /**
     *  you can change the players stats so that a player is no longer playing the game
     * @param state whether the player is playing the game or not
     */
    public void modifyStatus(boolean state)
    {
        playingGame = state;
    }

    /**
     * checks to see if the players hand is empty
     * @return whther or not the players hand is empty
     */
    public boolean isEmpty()
    {
        if(cardDeck.size() == 0)
            return true;
        return false;
    }
    /**
     * Clears the players hand of their current cards
     */
    public void clear()
    {
        cardDeck = new LinkedList<>();
    }
    /**
     * returns how many cards are in a players hand
     * @return the amount of cards a player has
     */
    public int size()
    {
        return cardDeck.size();
    }

    /**
     * Returns the string of the card deck
     * @return the card deck as a string
     */
    public String toString()
    {
        return cardDeck.toString();
    }
}
