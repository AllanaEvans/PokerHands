import java.util.Scanner;
import java.io.File;
/**
 * Reads from a file the hands of player 1 and player 2 and
 * determines how many games player 1 wins.
 * 
 * @author Allana Evans
 * @version March 4, 2015
 */

public class PokerHands {
	
	private Scanner hands = OpenFile.openToRead("poker.txt");
	private int[][] p1 = new int[2][5];
	private int[][] p2 = new int[2][5];
	
	public static void main (String [] args){
		PokerHands ph = new PokerHands();
		ph.run();
	}
	
	public void run(){
		int wins = 0; 
		int row = 1;
		while (hands.hasNext()){
			readCards(p1, p2);
			int[] best1 = findBest(p1);
			int[] best2 = findBest(p2);
			System.out.println(best1[0] + " " + best2[0] + " ");
			if (p1Wins(best1, best2, p1, p2)) wins++;
			row++;
		}
		System.out.println("Player 1 won " + wins + " times.");
		
	}
	
	/**
	 * Reads in a row of card values and puts them in arrays representing the 
	 * cards in each player's hand.
	 * @param int[][] p1   Player 1's hand
	 * @param int[][] p2   Player 2's hand
	 */
	public void readCards(int[][] p1, int[][] p2){
		for (int i = 0; i < 5; i++){
			String k = hands.next();
			p1[0][i] = getPointValue(k.charAt(0));
			p1[1][i] = getSuitValue(k.charAt(1));
		}
		for (int i = 0; i < 5; i++){
			String k = hands.next();
			p2[0][i] = getPointValue(k.charAt(0));
			p2[1][i] = getSuitValue(k.charAt(1));
		}
	}
	
	/**
	 * Finds the best hand of this player.
	 * @param int[][] player  The player's hand
	 * @return 1st the rank of the type of the player's best hand, 2nd & 3rd are related values 
	 * for ranking hands of the same type
	 */
	public int[] findBest(int[][] player){
		int[] best = new int[3];
		if (royalFlush(player)) {
			best[0] = 9; best[1] = 0; best[2] = 0;
			return best;
		}
		else if (straightFlush(player)){
			best[0] = 8; best[1] = 0; best[2] = 0;
			return best;
		}
		else if(fourOfAKind(player) != 0){
			best[0] = 7; best[1] = fourOfAKind(player); best[2] = 0;
			return best;
		}
		else if (fullHouse(player) != 0){
			best[0] = 6; best[1] = fullHouse(player); best[2] = 0;
			return best;
		}
		else if(flush(player)){
			best[0] = 5; best[1] = 0; best[2] = 0;
			return best;
		}
		else if(straight(player)){
			best[0] = 4; best[1] = 0; best[2] = 0;
			return best;
		}
		else if (threeOfAKind(player) != 0){
			best[0] = 3; best[1] = threeOfAKind(player); best[2] = 0;
			return best;
		}
		else if (twoPair(player)[0] != 0 && twoPair(player)[1] != 0){
			best[0] = 2; best[1] = twoPair(player)[0]; best[2] = twoPair(player)[1];
			return best;
		}
		else if (onePair(player) != 0){
			best[0] = 1; best[1] = onePair(player); best[2] = 0;
			return best;
		}
		else {
			best[0] = 0; best[1] = 0; best[2] = 0;
			return best;
		}
	}
	
	/**
	 * Compares player 1 and player 2's best hands to determine who won.
	 * @param int[] best1    The info about player 1's best hand
	 * @param int[] best2    The info about player 2's best hand
	 * @param int[][] p1     Player 1's hand
	 * @param int[][] p2     Player 2's hand
	 * @return true if player 1 wins, false otherwise
	 */
	public boolean p1Wins(int[] best1, int[] best2, int[][] p1, int[][] p2){
		if (best1[0] > best2[0]) return true;
		else if (best1[0] < best2[0]) return false;
		else {
			if (best1[1] > best2[1]) return true;
			else if (best1[1] < best2[1]) return false;
			else {
				if (best1[2] != 0 && best1[2] > best2[2]) return true;
				else if (best1[2] != 0 && best1[2] < best2[2]) return false;
				else{
					boolean end = false;
					while (!end){
						int high1 = highCard(p1);
						int high2 = highCard(p2);
						boolean found = false;
						if (high1 > high2) {end = true; return true;}
						else if (high1 < high2) {end = false; return false;}
						else{
							for (int i = 0; i < 5 && !found; i++){
								if (p1[0][i] == high1) {p1[0][i] = 0; found = true;}
							}
							found = false;
							for (int i = 0; i < 5 && !found; i++){
								if(p2[0][i] == high2){p2[0][i] = 0; found = true;}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Finds the highest card in the player's hand.
	 * @param int[][] player   The player's hand
	 * @return the value of the highest card in the player's hand
	 */
	public int highCard(int[][] player){
		int high = 0;
		for (int i = 0; i < 5; i++){
			if (player[0][i] > high) high = player[0][i];
		}
		return high;
	}
	
	/**
	 * Finds a single pair in the player's hand.
	 * @param int[][] player  The player's hand
	 * @return the value of the rank of the pair or 0 if a pair is not found
	 */
	public int onePair(int[][] player){
		for (int i = 0; i < 5; i++){
			for (int j = i+ 1; j < 5; j++){
				if (player[0][i] == player[0][j]) return player[0][i];
			}
		}
		return 0;
	}
	
	/**
	 * Finds two pairs in the player's hand.
	 * @param int[][] player   The player's hand
	 * @return The values of the ranks of the pairs or 0 if less than 2 pairs are found
	 */
	public int[] twoPair(int[][] player){
		int[] pair = {0,0};
		int k = 0;
		for (int i = 0; i < 4 && k <2; i++){
			for (int j = i+1; j < 5 && k<2; j++){
				if (player[0][i] == player[0][j]) {pair[k] = player[0][i]; k++; break;}
			}
		}
		return pair;
	}
	
	/**
	 * Finds a three of a kind in the player's hand.
	 * @param int[][] player    The player's hand
	 * @return   The value of the rank of the three of a kind or 0 if none is found
	 */
	public int threeOfAKind(int[][] player){
		for (int i = 0; i < 5; i++){
			for (int j = i+1; j < 5; j++){
				if (player[0][i] == player[0][j]){
					for (int k = j+1; k <5; k++){
						if (player[0][j] == player[0][k]) return player[0][j];
					}
				}
			}
		}
		return 0;
	}
	
	/**
	 * Finds if the player's hand is a straight.
	 * @param int[][] player   The player's hand
	 * @return true if the hand is a straight, false otherwise
	 */
	public boolean straight(int[][] player){
		int[][] order = new int[2][5];
		int[] high = {0,0,0,0,0};
		for (int i = 0; i < 5; i++){
			order[0][i] = player[0][i];
		}
		for (int i = 0; i <5; i++){
			high[i] = highCard(order);
			if (i != 0 && high[i] != (high[i-1] -1)) return false;
			boolean found = false;
			for (int j = 0; j <5 && !found; j++){
				if (order[0][j] == high[i]) {order[0][j] = 0; found = true;}
			}
		}
		return true;
	}
	
	/**
	 * Finds if the player's hand is a flush (all cards of same suit).
	 * @param int[][] player   The player's hand
	 * @return  true if it is a flush, false otherwise
	 */
	public boolean flush(int[][] player){
		if (player[1][0] == player[1][1] && player[1][0] == player[1][2] && player[1][0] == player[1][3] && player[1][0] == player[1][4]) return true;
		else return false;
	}
	
	/**
	 * Finds a full house in the player's hand.
	 * @param int[][] player  The player's hand
	 * @return  The value of the rank of the three of a kind or 0 if none found.
	 */
	public int fullHouse(int[][] player){
		int triple = threeOfAKind(player);
		if (triple == 0) return 0;
		int pair = 0;
		for (int i = 0; i < 5; i++){
			if (player[0][i] != triple){
				if (pair == 0) pair = player[0][i];
				else if (player[0][i] == pair) {
					return triple;
				}
				else return 0;
			}
		}
		return 0;
	}
	
	/**
	 * Finds a four of a kind in the player's hand.
	 * @param int[][] player  The player's hand
	 * @return  The value of the rank of the four of a kind or 0 if none is found
	 */
	public int fourOfAKind(int[][] player){
		for (int i = 0; i < 5; i++){
			for (int j = i+1; j < 5; j++){
				if (player[0][i] == player[0][j]){
					for (int k = j+1; k <5; k++){
						if (player[0][j] == player[0][k]) {
							for (int m = k+1; m <5; m++){
								if (player[0][k] == player[0][m]) return player[0][k];
							}
						}
					}
				}
			}
		}
		return 0;
	}
	
	/**
	 * Finds if the player's hand is a straight flush (straight & flush).
	 * @param int[][] player   The player's hand
	 * @return  true if it is a flush, false otherwise
	 */
	public boolean straightFlush(int[][] player){
		if (straight(player) && flush(player)) return true;
		else return false;
	}
	
	/**
	 * Finds if the player's hand is a royal flush (straight from 10 to Act & flush).
	 * @param int[][] player  The player's hand
	 * @return  true if it is a royal flush, false otherwise
	 */
	public boolean royalFlush(int[][] player){
		if (!flush(player) || !straight(player)) return false;
		else if (highCard(player) == 14) return true;
		else return false;
	}
	
	/**
	 * Finds the value of the card passed based on its rank.
	 * @param char card     The rank of the card
	 * @return  The card's point value
	 */
	private int getPointValue(char rank){
		switch(rank){
		case '2': return 2;
		case '3': return 3; 
		case '4': return 4; 
		case '5': return 5; 
		case '6': return 6; 
		case '7': return 7; 
		case '8': return 8; 
		case '9': return 9; 
		case 'T': return 10; 
		case 'J': return 11; 
		case 'Q': return 12; 
		case 'K': return 13;
		case 'A': return 14;
		default: return 0;
		}
	}
	
	/**
	 * Finds the value of the card passed based on its suit.
	 * @param char suit
	 * @return  An integer representing the suit's rank:
	 * 			1 to 4 in the order C, D, H, S
	 */
	private int getSuitValue(char suit){
		switch(suit){
		case 'C': return 1;
		case 'D': return 2;
		case 'H': return 3;
		case 'S': return 4;
		default: return 0;
		}
	}
	
}