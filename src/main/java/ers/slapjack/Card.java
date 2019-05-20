package ers.slapjack;

public class Card
{
    //The number of the card in an integer
    private int num;
    //The suit of the card as a integer, Spades = 1, Hearts = 2, Diamonds = 3, clubs = 4
    private int suit;

    /**
     * Constructs a card with default values of 0, 0
     */
    public Card()
    {
        num = 0;
        suit = 0;
    }

    /**
     * Constructs a card with the values of num, and a int for the suit
     * @param num is the number of the card, Ace = 1, Jack = 11, Queen = 12, King = 13
     * @param suit is the suit of the card in an int, Spades = 1, Hearts = 2, Diamonds = 3,
     * clubs = 4
     */
    public Card(int num, int suit)
    {
        this.num = num;
        this.suit = suit;
    }

    /**
     * returns the number of the card
     * @return the number of the card
     */
    public int getNumber()
    {
        return num;
    }

    /**
     * returns the suit number of the card
     * @return the suit number
     */
    public int getSuitNumber()
    {
        return suit;
    }

    /**
     * Returns the suit number as a string for the name, returning "Suit not assigned"  if its not
     * been initialized
     * @return the name of the suit
     */
    public String getSuitName()
    {

        if (suit == 1)
            return "Spades";
        else if (suit == 2)
            return "Hearts";
        else if (suit == 3)
            return "Diamonds";
        else if (suit == 4)
            return "Clubs";
        else
            return "Suit not assigned";
    }

    /**
     * Returns the number of the card as a string, and the special numbers as card name
     * @return the number of the card
     */
    public String getNumberName()
    {
        if(num == 1)
            return "Ace";
        else if(num == 11)
            return "Jack";
        else if(num == 12)
            return "Queen";
        else if(num == 13)
            return "King";
        else if(num == 0)
            return "Number not assigned";
        else
        {
            String s = "" + num;
            return s;
        }
    }

    /**
     * Returns the card name and suit
     * @return the number and suit of the card
     */
    @Override
    public String toString()
    {
        return getNumberName() + " of " + getSuitName();
    }

    /**
     * checks to see if two cards are equal
     * @param c is the card to check against
     * @return is whether or not the cards are the same
     */
    public boolean equals(Card c)
    {
        if (c.getNumber() == num && c.getSuitNumber() == suit)
            return true;
        else
            return false;
    }

    /**
     * Compares the cards against each other dependent on their number
     * @param c is the card to check against
     * @return is the difference between the two cards
     */
    public int comparesTo(Card c)
    {
        if (this.equals(c))
            return 0;
        else
            return this.getSuitNumber() - c.getSuitNumber();
    }

}
