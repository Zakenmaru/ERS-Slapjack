package ers.slapjack;

import java.util.ArrayList;
import java.util.Stack;

//add an AddCard method to the ers.slapjack.PlayerDeck
public class MasterDeck {
    //maintains the stack of Cards
    private Stack<Card> cardDeck;
    //maintains the top card of the deck
    private Card topCard;

    public MasterDeck() {
        cardDeck = new Stack<>();
        topCard = null;
    }

    /**
     * Returns the deck as a string
     *
     * @return "Empty ers.slapjack.Deck" if deck is empty, a card on each line
     * if not
     */
    public String toString() {
        if (cardDeck.isEmpty())
        {
            return "Empty ers.slapjack.Deck!";
        }
        return cardDeck.toString();
    }

    /**
     * Returns the cardDeck
     *
     * @return cardDeck
     */
    public Stack<Card> getCardDeck() {
        return cardDeck;
    }

    /**
     * Adds a card to the top of the deck
     *
     * @param card the ers.slapjack.Card to be added
     */
    public void addCard(Card card) {
        topCard = card;
        cardDeck.push(card);
    }

    /**
     * Returns the top 3 cards of the ers.slapjack.Deck
     *
     * @return the top 3 cards of the deck as an ArrayList<ers.slapjack.Card>(),
     * with the top card being at index 0.
     */
    public ArrayList<Card> getTopThreeCards() {
        ArrayList<Card> cardArrayList = new ArrayList<>(cardDeck);
        ArrayList<Card> theTopThreeCards = new ArrayList<>();
        if (cardArrayList.isEmpty())
        {
            return theTopThreeCards;
        }
        else if (cardArrayList.size() < 3)
        {
            for (int i = cardArrayList.size() - 1; i > -1; i--) {
                theTopThreeCards.add(cardArrayList.get(i));
            }
        }
        else
        {
            for (int i = cardArrayList.size() - 1; i > cardArrayList.size() - 4; i--)
            {
                theTopThreeCards.add(cardArrayList.get(i));
            }
        }
        return theTopThreeCards;
    }
    /**
     * Removes all cards from the cardDeck and
     * Returns it as a flipped stack
     *
     * @return a flipped stack of the now empty cardDeck
     */
    public Stack<Card> removeCards() {
        Stack<Card> flippedStack = new Stack<>();
        while (!cardDeck.isEmpty()) {
            flippedStack.push(cardDeck.pop());
        }
        return flippedStack;

    }

    //returns whether or not the stack is empty
    public boolean isEmpty() {
        return cardDeck.isEmpty();
    }

    //returns the top card for drawing
    public Card topCard() {
        if (cardDeck.isEmpty()) {
            return null;
        }
        topCard = cardDeck.peek();
        return topCard;
    }

    //returns the size of the stack
    public int size() {
        return cardDeck.size();
    }
}