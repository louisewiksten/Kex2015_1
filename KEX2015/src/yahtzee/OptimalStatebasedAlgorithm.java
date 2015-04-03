package yahtzee;

import java.util.Arrays;

/**
 * Trying to solve yahtzee by saving states.
 * 
 * Each state is defined by a set of dice values, arranged in order of size.
 * Each state has a set of edges containing the probability of getting to the
 * next state.
 * 
 * @author D Jendeberg and L Wiksten
 *
 */
public class OptimalStatebasedAlgorithm {
	private ScoreCard sc;
	private Tuple[] V1;
	private static final int V1LENGTH = 1000;
	private Element[] V2;
	private static final int V2LENGTH = 1000;
	private Element[] V3;
	private static final int V3LENGTH = 1000;
	private Element[] V4;
	private static final int V4LENGTH = 1000;
	private Element[] V5;
	private static final int V5LENGTH = 1000;
	private Element[] V6;
	private static final int V6LENGTH = 1000;

	/**
	 * Objects for V1.
	 * 
	 * @author D Jendeberg and L Wiksten
	 *
	 */
	private class Tuple {
		public boolean[] playableCategories;
		public int upper;

		public Tuple(boolean[] categories, int upper) {
			this.playableCategories = categories;
			this.upper = upper;
		}

		public boolean equals(Tuple a) {
			if (this.playableCategories.equals(a.playableCategories))
				return true;
			return false;
		}
	}

	/**
	 * Objects for V2, V4, V6
	 * 
	 * @author D Jendeberg L Wiksten
	 *
	 */
	private class Element {
		public Tuple u;
		public int[] dices;
		public String vertices;

		public Element(Tuple u, int[] dices, String vertices) {
			this.u = u;
			this.dices = new int[5];
			this.dices = dices.clone();
			this.vertices = vertices;
		}

	}

	public OptimalStatebasedAlgorithm() {
		V1 = new Tuple[V1LENGTH];
		boolean[] playable = new boolean[16];
		playable[0] = false;
		for (int i = 1; i < 16; i++) {
			playable[i] = true;
		}
		V1[0] = new Tuple(playable, 0);

		// TODO
	}

	/**
	 * @param u
	 *            Element from V1.
	 * @param v
	 *            Element from V6.
	 * @return Score returned by following that path.
	 */
	public int score(Tuple u, Element v) {
		int score = 0;
		for (int i = 0; i < 16; i++)
			if (u.playableCategories[i]) {
				int tmpScore = pointsEarned(v.dices, i, u.upper);
				if (tmpScore > score)
					score = tmpScore;
			}
		return score;
	}

	/**
	 * Calculates the points generated by the scoreing from the wraparound from
	 * V6 to V1, also known as scoring the points.
	 * 
	 * @param v
	 *            An element from V6.
	 * @param u
	 *            A tuple from V1.
	 * @return The score generated.
	 */
	public int S(Element v, Tuple u) {
		int score = 0;
		for (int i = 1; i < 16; i++) {
			if (v.u.playableCategories[i] != u.playableCategories[i])
				// Finds the scored position.
				score = pointsEarned(v.dices, i, v.u.upper);
		}
		return score;
	}

	/**
	 * Calculates score from wraparound without bonus.
	 * 
	 * @param v
	 *            An element from V6.
	 * @param u
	 *            A tuple from V1.
	 * @return The score generated without bonus.
	 */
	public int SPrime(Element v, Tuple u) {
		int score = 0;
		for (int i = 1; i < 16; i++) {
			if (v.u.playableCategories[i] != u.playableCategories[i]) {
				// Finds the scored position.
				score = pointsEarned(v.dices, i, v.u.upper);
				if (score > 50) { // Must be upper and contain bonus
					score -= 50;
				}
			}
		}
		return score;
	}

	/**
	 * Calculates the score generated by a certain set of dices in a given
	 * category.
	 * 
	 * @param dices
	 *            Dice values.
	 * @param category
	 *            Category (1-15).
	 * @param upper
	 *            Sum of top category scores.
	 * @return The score generated by the parameters.
	 */
	public int pointsEarned(int[] dices, int category, int upper) {
		int score = 0;
		Arrays.sort(dices);

		switch (category) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			for (int i = 0; i < 5; i++)
				if (dices[i] == category)
					score += category;
			if ((upper + category) > 62 && !(upper > 63))
				score += 50;
			break;
		case 7: // pair
			for (int i = 0; i < 4; i++)
				if (dices[i] == dices[i + 1])
					score = 2 * dices[i];
			break;
		case 8: // twopair
			for (int i = 0; i < 2; i++)
				if (dices[i] == dices[i + 1])
					for (int j = 2 + i; j < 4; j++)
						if (dices[j] == dices[j + 1])
							score = 2 * dices[i] + 2 * dices[j];
			break;
		case 9: // Three of a kind
			for (int i = 0; i < 3; i++)
				if (dices[i] == dices[i + 2])
					score = 3 * dices[i];
			break;
		case 10: // Four of a kind
			for (int i = 0; i < 2; i++)
				if (dices[i] == dices[i + 3])
					score = 4 * dices[i];
			break;
		case 11:// Small straight
			if (dices[0] == 1 && dices[1] == 2 && dices[2] == 3
					&& dices[3] == 4 && dices[4] == 5)
				score = 15;
			break;
		case 12: // Large straight
			if (dices[0] == 2 && dices[1] == 3 && dices[2] == 4
					&& dices[3] == 5 && dices[4] == 6)
				score = 20;
			break;
		case 13: // Full House
			if (dices[0] == dices[2] && dices[3] == dices[4]
					&& dices[0] != dices[4])
				score = 3 * dices[0] + 2 * dices[4];
			else if (dices[0] == dices[1] && dices[2] == dices[4]
					&& dices[0] != dices[4])
				score = 2 * dices[0] + 3 * dices[4];
			break;
		case 14: // Chance
			for (int dice : dices) {
				score += dice;
			}
			break;
		case 15: // Yahtzee
			if (dices[0] == dices[4]) {
				score = 50;
			}
		}
		return score;
	}

	/**
	 * Method described in literature
	 * 
	 * @param u
	 *            A terminal value of V1, this means a tuple where the entire
	 *            category list is scored. If not terminal, calculate future
	 *            score.
	 * @return The total score of a path leading to this state.
	 */
	public double X(Tuple u) {
		int sum = 0;
		boolean terminal = true;
		for (boolean playableCategory : u.playableCategories)
			if (playableCategory) {
				terminal = false;
				break;
			}
		if (terminal) {
			return 0;
		} else {
			// Not a terminal tuple.
			for (int i = 0; i < V2.length; i++) {
				if (V2[i].u.equals(u)) {
					// We found a matching state
					sum += X(V2[i]);
				}
			}
			return sum;
		}
	}

	/**
	 * 
	 * @param v
	 *            An element from V2-V6, never Terminal
	 * @return A double representing the expected score from here.
	 */
	public double X(Element v) {
		if (v.vertices == "V3" || v.vertices == "V5") {
			double sum = 0;
			if (v.vertices == "V3") {
				for (int i = 0; i < V4.length; i++) {
					if (V4[i].u.equals(v.u)) {
						sum += probability(v, V4[i]) * X(V4[i]);
					}
				}
			} else { // if (v.vertices == "V5")
				for (int i = 0; i < V6.length; i++) {
					if (V6[i].u.equals(v.u)) {
						sum += probability(v, V6[i]) * X(V6[i]);
					}
				}
			}
			return sum;
		} else if (v.vertices == "V2" || v.vertices == "V4") {
			double max = 0;
			if (v.vertices == "V2") {
				for (int i = 0; i < V3.length; i++) {
					if (V3[i].u.equals(v.u))
						max = Math.max(max, X(V3[i]));
				}
			} else {// if (v.vertices == "V4")
				for (int i = 0; i < V5.length; i++) {
					if (V5[i].u.equals(v.u))
						max = Math.max(max, X(V5[i]));
				}
			}
			return max;
		} else if (v.vertices == "V6") {
			double max = 0;
			for (int i = 1; i < 16; i++) {
				if (v.u.playableCategories[i]) {
					Tuple u = null;
					int iter = 0;
					while (iter < V1.length) {

					}
					double tmp = S(v, V1[iter]) + X(V1[iter]);
					if (tmp > max) {
						max = tmp;
					}
				}
			}
			return max;
		}
		System.out.println("Reached unreachable point in X(Element v)");
		System.err.println(" Shit just got bad in method X(Element v) ");
		return -1;
	}

	private double probability(Element v, Element u) {
		return NewOptimalAlgorithm.probability(v.dices, u.dices, 1);
	}

	/**
	 * Static method for playing yahtzee
	 */
	public static void play() {
		ScoreCard sc = new ScoreCard();

	}
}
