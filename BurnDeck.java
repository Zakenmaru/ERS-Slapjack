import java.util.Stack;

public class BurnDeck implements Deck
{
    public Stack<Card> cardDeck;

    /**
     * The constructor for burn deck that initializes the stack to empty
     */
    public BurnDeck()
    {
        cardDeck = new Stack<>();
    }

    /**
     * Return null if the deck is empty, and the stack if it isn't
     * @return the stack if its not empty, null if it is
     */
    public Stack<Card> getCardDeck() // possibly delete after testing
    {
        if(isEmpty())
            return null;
        return cardDeck;
    }

    /**
     * empties the stack and returns an upside down copy
     * @return the upside down stack
     */
    public Stack<Card> removeCard()
    {
        Stack<Card> temp = new Stack<>();
        if(isEmpty()) {
            return temp;
        }
        while(!cardDeck.isEmpty())
        {
            temp.add(cardDeck.pop());
        }
        cardDeck = new Stack<>();
        return temp;
    }

    /**
     * adds a single card to the burn deck
     * @param card the card to be added
     */
    public void addCard(Card card)
    {
        cardDeck.add(card);
    }

    /**
     * returns if the deck is empty or not
     * @return whether or not the stack is empty
     */
    public boolean isEmpty()
    {
        if( cardDeck.size() == 0)
            return true;
        return false;
    }

    /**
     * clears the stack of cards
     */
    public void clear()
    {
        cardDeck = new Stack<>();

    }

    /**
     * returns the amount of cards in the stack
     * @return size of the stack
     */
    public int size()
    {
        return cardDeck.size();
    }

    /**
     * Returns the stack of cards as a string
     * @return the card stack in a string
     */
    public String toString()
    {
        return cardDeck.toString();
    }
}
