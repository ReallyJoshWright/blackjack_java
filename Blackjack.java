import java.util.Scanner;
import java.util.Random;
import java.io.IOException;

public class Blackjack {
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        boolean keepPlaying = true;
        int playerEarnings = 0;
        int totalPlayerEarnings = 0;

        while (keepPlaying) {
            playerEarnings = playBlackjack();
            totalPlayerEarnings += playerEarnings;
            System.out.println("\nWould you like to play another hand?");
            System.out.println("Type 'y' or 'n'.");
            if (!scnr.next().equals("y")) {
                keepPlaying = false;
            }
        }
        System.out.println("\nYou have won/lost: " + totalPlayerEarnings + " dollars.");
    }

    public static int playBlackjack() {
        String decision = "";
        String betString = "";
        int bet = 0;
        int betTotal = 0;
        String dealerHand = "";
        String dealerHandTemp = "";
        String playerHand = "";
        int card = 0;
        String cardString = "";
        int[] dealerCards = new int[10];
        int[] playerCards = new int[10];
        int finalEarnings21 = 0;
        int earnings21 = 0;
        boolean keepGoing = true;
        int pIndex = 2;
        int dIndex = 2;
        int playerCardTotal = 0;
        int dealerCardTotal = 0;

        // Display the board
        drawBoard(betString, playerHand, dealerHand);

        // Get initial bet
        bet = requestBet(true, 0);
        betTotal = bet;
        betString = Integer.toString(bet);

        // Deal starting hand
        card = dealCard();
        cardString = getCardString(card);
        playerHand = cardString;
        playerCards[0] = card;

        card = dealCard();
        cardString = getCardString(card);
        playerHand = playerHand + " " + cardString;
        playerCards[1] = card;

        card = dealCard();
        cardString = getCardString(card);
        dealerHandTemp = cardString;
        dealerHand = cardString + " ??";
        dealerCards[0] = card;

        // Update the board
        drawBoard(betString, playerHand, dealerHand);

        // Check if player has initial blackjack
        if (playerCards[0] == 10 || playerCards[1] == 10) {
            if (playerCards[0] == 1 || playerCards[1] == 1) {
                System.out.println("\nYou got blackjack! You win!");
                // Any decimal values are intentionally truncated
                // The casino does not round up
                earnings21 = (bet * 3) / 2;
                finalEarnings21 = bet + (bet * 3) / 2;
                System.out.println("You won " + earnings21 + " dollars, for a total of " + finalEarnings21 + " dollars!");
                return finalEarnings21;
            }
        }

        // Get player decision
        while (keepGoing) {
            decision = getPlayersDecision(playerCards);
            if (decision.equals("s")) {
                playerCardTotal = getCardTotal(playerCards);
                keepGoing = false;
            }
            if (decision.equals("d") || decision.equals("ss")) {
                bet = requestBet(false, betTotal);
                betTotal += bet;
                betString = Integer.toString(bet);
            }
            if (!decision.equals("s")) {
                card = dealCard();
                cardString = getCardString(card);
                playerHand = playerHand + " " + cardString;
                playerCards[pIndex] = card;
                pIndex += 1;

                drawBoard(betString, playerHand, dealerHand);

                playerCardTotal = getCardTotal(playerCards);

                // Check if player busts
                if (playerCardTotal > 21) {
                    System.out.println("\nYou lose! Your hand total is " + playerCardTotal);
                    System.out.println("You lost " + betTotal + " dollars!");
                    keepGoing = false;
                    return betTotal * (-1);
                }
            }
        }

        // Reveal dealer's hand
        dealerHand = dealerHandTemp;

        card = dealCard();
        cardString = getCardString(card);
        dealerHand = dealerHand + " " + cardString;
        dealerCards[1] = card;

        drawBoard(betString, playerHand, dealerHand);

        // Check if dealer has blackjack
        if (dealerCards[0] == 10 || dealerCards[1] == 10) {
            if (dealerCards[0] == 1 || dealerCards[1] == 1) {
                System.out.println("\nDealer got blackjack!");
            }
        }

        //dealer's turn
        dealerCardTotal = getCardTotal(dealerCards);
        while (dealerCardTotal < 17) {
            card = dealCard();
            cardString = getCardString(card);
            dealerHand = dealerHand + " " + cardString;
            dealerCards[dIndex] = card;
            dIndex += 1;

            drawBoard(betString, playerHand, dealerHand);

            dealerCardTotal = getCardTotal(dealerCards);
        }

        // Check if dealer busts
        if (dealerCardTotal > 21) {
            System.out.println("\nDealer bust!");
            System.out.println("Dealer's hand total is " + dealerCardTotal);
            System.out.println("You won! You made $" + betTotal);
            System.out.println("Your score: " + getPlayerScore(playerCards));
            return betTotal;
        }

        // Determine winner
        System.out.println();
        if (getPlayerScore(playerCards) > getDealerScore(dealerCards)) {
            System.out.println("You won! You made $" + betTotal);
        }
        else if (getPlayerScore(playerCards) < getDealerScore(dealerCards)) {
            System.out.println("You lose. You lost $" + betTotal);
            betTotal *= (-1);
        }
        else {
            System.out.println("You tied.");
            betTotal = 0;
        }
        System.out.println("Your score: " + getPlayerScore(playerCards));
        System.out.println("Dealer score: " + getDealerScore(dealerCards));
        return betTotal;
    }

    public static void drawBoard(String bet, String playerHand, String dealerHand) {
        int n = 100;
        clearConsole();

        String title = "Welcome to Blackjack!";
        String dealerTitle = "Dealer's Cards:";
        String playerTitle = "Player's Cards:";
        String betTitle = "Player's Bet (10/1000):";
        String[] topBottomBorder = new String[n];
        String[] middleBorder = new String[n];

        // Create top and bottom border
        for (int i = 0; i < n; i++) {
            topBottomBorder[i] = "-";
        }

        // Create middle border
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                middleBorder[i] = "|";
            }
            else if (i == n - 1) {
                middleBorder[i] = "|";
            }
            else {
                middleBorder[i] = " ";
            }
        }

        // Print title
        System.out.println();
        System.out.printf("%60s %n", title);

        // Print top border
        for (int i = 0; i < n; i++) {
            System.out.print(topBottomBorder[i]);
        }
        System.out.println();

        // Print middle border height
        for (int i = 0; i < (n / 4); i++) {
            // Print dealer title
            if (i == 3) {
                System.out.print("|");
                System.out.printf("%55s", "" + dealerTitle);
                System.out.printf("%44s %n", "|");
            }
            // Print dealer's hand
            else if (i == 4) {
                System.out.print("|");
                System.out.printf("%55s", "" + dealerHand);
                System.out.printf("%44s %n", "|");
            }
            // Print player title
            else if (i == 11) {
                System.out.print("|");
                System.out.printf("%55s", "" + playerTitle);
                System.out.printf("%44s %n", "|");
            }
            // Print player's hand
            else if (i == 12) {
                System.out.print("|");
                System.out.printf("%55s", "" + playerHand);
                System.out.printf("%44s %n", "|");
            }
            // Print bet title
            else if (i == 19) {
                System.out.print("|");
                System.out.printf("%85s", "" + betTitle);
                System.out.printf("%14s %n", "|");
            }
            // Print player bet
            else if (i == 20) {
                System.out.print("|");
                System.out.printf("%85s", "" + bet);
                System.out.printf("%14s %n", "|");
            }
            // Print middle border width
            for (int j = 0; j < n; j++) {
                System.out.print(middleBorder[j]);
            }
            System.out.println();
        }

        // Print bottom border
        for (int i = 0; i < n; i++) {
            System.out.print(topBottomBorder[i]);
        }
    }

    public static String getSuit() {
        Random random = new Random();
        String value = "";

        // unicode values for suits
        final String SPADE = "\u2660";
        final String CLUB = "\u2663";
        final String DIAMOND = "\u2666";
        final String HEART = "\u2665";

        int suit = random.nextInt(4);
        switch (suit) {
            case 0: value = HEART;
                    break;
            case 1: value = SPADE;
                    break;
            case 2: value = DIAMOND;
                    break;
            case 3: value = CLUB;
                    break;
            default:
                    break;
        }
        return value;
    }

    public static String getTens() {
        Random random = new Random();
        String value = "";
        int ten = random.nextInt(4);
        switch (ten) {
            case 0: value = "K";
                    break;
            case 1: value = "Q";
                    break;
            case 2: value = "J";
                    break;
            case 3: value = "10";
                    break;
            default:
                    break;
        }
        return value;
    }

    public static int dealCard() {
        Random random = new Random();
        int card = random.nextInt(10) + 1;
        return card;
    }

    public static String getCardString(int card) {
        String suit = "";
        String cardValue = "";
        String cardString = "";

        if (card == 10) {
            cardValue = getTens();
        }
        else if (card == 1) {
            cardValue = "A";
        }
        else {
            cardValue = Integer.toString(card);
        }
        suit = getSuit();
        cardString = cardValue + suit;
        return cardString;
    }

    public static int requestBet(boolean firstBet, int previousBet) {
        Scanner scnr = new Scanner(System.in);
        int bet = 0;
        boolean isInValid = true;

        if (firstBet == true) {
            System.out.print("\nPlace your bet: ");
            while (bet < 10 || bet > 1000) {
                bet = scnr.nextInt();
                if (bet < 10 || bet > 1000) {
                    System.out.print("Please place a bet within the limits: ");
                }
            }
        }

        if (firstBet == false) {
            System.out.print("\nPlace your bet: ");
            while (isInValid) {
                bet = scnr.nextInt();
                if (bet < 10 || bet > 1000) {
                    System.out.print("Please place a bet within the limits: ");
                }
                else if (bet != (2 * previousBet)) {
                    System.out.print("Please place a bet that is double your bet: ");
                }
                else {
                    isInValid = false;
                }
            }
        }

        return bet;
    }

    public static String getPlayersDecision(int[] playerCards) {
        Scanner scnr = new Scanner(System.in);
        boolean canSplit = false;
        String decision = "";
        boolean isInValid = true;

        // Check for pairs
        for (int i = 0; i < playerCards.length - 1; i++) {
            for (int j = playerCards.length - 1; j > i; j--) {
                if (playerCards[i] == playerCards[j] && playerCards[i] != 0) {
                    canSplit = true;
                }
            }
        }

        if (canSplit == false) {
            System.out.print("\nType: hit(h), stand(s), or doubleDown(d): ");
            while (isInValid) {
                decision = scnr.next();
                if (decision.equals("hit") || decision.equals("h")) {
                    isInValid = false;
                }
                else if (decision.equals("stand") || decision.equals("s")) {
                    isInValid = false;
                }
                else if (decision.equals("doubleDown") || decision.equals("d")) {
                    isInValid = false;
                }
                else {
                    System.out.print("Type: hit(h), stand(s), or doubleDown(d): ");
                }
            }
        }

        if (canSplit == true) {
            System.out.print("\nType: hit(h), stand(s), split(ss), or doubleDown(d): ");
            while (isInValid) {
                decision = scnr.next();
                if (decision.equals("hit") || decision.equals("h")) {
                    isInValid = false;
                }
                else if (decision.equals("stand") || decision.equals("s")) {
                    isInValid = false;
                }
                else if (decision.equals("doubleDown") || decision.equals("d")) {
                    isInValid = false;
                }
                else if (decision.equals("split") || decision.equals("ss")) {
                    isInValid = false;
                }
                else {
                    System.out.print("Type: hit(h), stand(s), split(ss), or doubleDown(d): ");
                }
            }
        }

        // Assign decision to a single letter
        if (decision.equals("hit") || decision.equals("h")) {
            decision = "h";
        }
        if (decision.equals("stand") || decision.equals("s")) {
            decision = "s";
        }
        if (decision.equals("doubleDown") || decision.equals("d")) {
            decision = "d";
        }
        if (decision.equals("split") || decision.equals("ss")) {
            decision = "ss";
        }

        return decision;
    }

    public static int getCardTotal(int[] playerCards) {
        int sum = 0;

        for (int i = 0; i < playerCards.length; i++) {
            sum += playerCards[i];
        }

        return sum;
    }

    public static int handleAceValue(int score) {
        if (score + 10 > 21) {
            return score;
        }
        return score + 10;
    }

    public static int getPlayerScore(int[] playerCards) {
        boolean playerHasAce = false;
        int playerScore = 0;

        for (int i = 0; i < playerCards.length; i++) {
            if (playerCards[i] != 0) {
                playerScore += playerCards[i];
            }
            if (playerCards[i] == 1) {
                playerHasAce = true;
            }
        }
        if (playerHasAce) {
            playerScore = handleAceValue(playerScore);
        }
        return playerScore;
    }

    public static int getDealerScore(int[] dealerCards) {
        int dealerScore = 0;

        for (int i = 0; i < dealerCards.length; i++) {
            if (dealerCards[i] != 0) {
                dealerScore += dealerCards[i];
            }
        }
        return dealerScore;
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ex) {}
    } 
}
