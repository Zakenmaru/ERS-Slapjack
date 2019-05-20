package ers.slapjack;

import java.util.Stack;

public interface Deck {
    public Stack<Card> cardDeck = new Stack<>();
    public String toString();
    public Stack<Card> getCardDeck(); // possibly delete after testing
    //public ers.slapjack.Card dealCard();
    //public ers.slapjack.Card addCard(String card);
    //returns whether or not the stack is empty
    public boolean isEmpty();
    //returns the top card for drawing
    // only master public ers.slapjack.Card topCard();
    //clears the deck of all cards
    public void clear();
    //returns the size of the stack
    public int size();
}