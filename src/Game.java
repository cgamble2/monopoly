import javax.xml.transform.Source;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Game {
	public static final Board board = new Board();
	public static final LinkedList<Player> players = new LinkedList<>();
	public static Player currentPlayer;
	public static int currentPlayerNumber;
	public static ArrayList<Card> communityChestCards;
	private static ArrayList<Card> chanceCards;
	public static ArrayList<Property> properties;
	private static final String PROPERTY_FILE = "PropertyAttributes.csv";
	private static final String COMMUNITY_CARD_FILE = "CommunityChestCards.csv";
	private static final String CHANCE_CARD_FILE = "ChanceCards.csv";
	public static int rollValue;
	public static void welcomeMessage() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Welcome to Monopoly!\nHow many players? ");
		int numPlayers = sc.nextInt();
//		while(numPlayers > 6 || numPlayers < 2) {
//			System.out.print("Please enter a number between 2 & 6.\nHow many players? ");
//			numPlayers = sc.nextInt();
//		}
		board.setPlayerCount(numPlayers);
		for(int i = 1; i <= numPlayers; i++) {
			Player newPlayer = new Player();
			players.add(newPlayer);
			System.out.print("Enter Player " + i + " Name: ");
			String name = sc.next();
			newPlayer.setName(name);
			sc.nextLine();
		}
		nextPlayer();
	}
	public static void beginningPrompt() {
		Scanner sc = new Scanner(System.in);
		System.out.println();
		System.out.println(currentPlayer.getName() +  " will start! Press ENTER to roll.");
		System.out.println("Balance: " + currentPlayer.getFunds());
		sc.nextLine();
	}
	public static void prompt() {
		Scanner sc = new Scanner(System.in);
		if(currentPlayer.getJailTimer() > 0)
			return;
		System.out.println("***** It's " + currentPlayer.getName() +  "'s turn. Press ENTER to roll. *****");
		System.out.println("Balance: " + currentPlayer.getFunds());
		sc.nextLine();
	}
	public static void nextPlayer() {
		currentPlayerNumber++;
		if(currentPlayerNumber > board.getPlayerCount())
			currentPlayerNumber = 1;
		currentPlayer = players.get(currentPlayerNumber - 1);
	}
	public static void jailOptions(Player player) {
		Scanner sc = new Scanner(System.in);
		System.out.println("***** " + player.getName() + ", you're in jail. Press ENTER for options. ***** ");
		sc.nextLine();
		
		if(player.hasJailFreeCard()) {
			System.out.println("1) Pay $50.\n2) Try for doubles.\n3) Use 'Get Out Of Jail Free Card.\n");
		}
		else {
			System.out.println("1) Pay $50.\n2) Try for doubles.\n");
		}
		boolean valid = false;
		String ans = sc.nextLine();
		System.out.print("Which would you like to do? ");
		if(ans.equals("1")) {
			player.updateFunds(-50);
			player.updateJailTimer();
			player.updatePosition(10);
			System.out.println();
			System.out.println("$50 has been deducted from your account. You're free to go.\nPress ENTER to roll.");
			sc.nextLine();
		}
	}
	public static void playerRoll() { //NEEDS WORK
		Scanner sc = new Scanner(System.in);
		
		if(currentPlayer.getJailTimer() > 0) {
		
//			if(currentPlayer.hasJailFreeCard()) {
//				Card card = currentPlayer.jailCards.get(0);
//				useOutOfJailCard(card);
//				System.out.println("***** " + currentPlayer.getName() + ", your 'Get Out Of Jail Free' card has been applied. Press ENTER to roll. *****");
//				sc.nextLine();
//			}
			jailOptions(currentPlayer);
		}
		for(int turn = 1; turn < 5; turn++) {
			
			if(turn == 4) {
				System.out.println("Balance: " + currentPlayer.getFunds() + "\n");
				return;
			}
			int die1 = new Random().nextInt(6) + 1;
			int die2 = new Random().nextInt(6) + 1;
			int roll = die1 + die2;
			rollValue = roll;
			
			if(turn == 3 && die1 == die2) {
				currentPlayer.updatePosition(30);
				currentPlayer.setJailTimer();
				System.out.println("(" + die1 + " & " + die2 + ") GO TO JAIL. DO NOT COLLECT $200.\n");
				return;
			}
			boolean passedGo = currentPlayer.addToPosition(roll);
			int endPosition = currentPlayer.getPosition();
			String title = board.spotReference.get(endPosition);
			
			if(passedGo) {
				System.out.println("You rolled " + roll + " (" + die1 + " & " + die2 + "). Advance to " + title + ".\n\nYou passed " +
				                   "GO.\n$200 has been deposited into your account.\n");
			} else {
				System.out.println("You rolled " + roll + " (" + die1 + " & " + die2 + "). Advance to " + title + ".\n");
			}
			
			BoardSpace space = new BoardSpace(endPosition, title);
			
			if(space.isChance()) {
				drawCard(chanceCards);
			}
			else if(space.isCommunityChest()) {
				drawCard(communityChestCards);
			}
			else if(space.isGoToJail()) {
				currentPlayer.updatePosition(30);
				currentPlayer.setJailTimer();
				System.out.println("GO DIRECTLY TO JAIL. DO NOT COLLECT $200.\n");
				System.out.println("Balance: " + currentPlayer.getFunds() + "\n");
				return;
			}
			else if(space.isNormalProperty()) {
				int i = 0;
				while(!properties.get(i).name.equals(space.getName()))
					i++;
				Property property = properties.get(i);
				checkPropertyOwnership(property);
				
			}
			else if(space.isRailRoad()) {
				int i = 0;
				while(!properties.get(i).name.equals(space.getName()))
					i++;
				Property property = properties.get(i);
				checkRailRoadOwnership(property);
			}
			else if(space.isUtility()) {
				int i = 0;
				while(!properties.get(i).name.equals(space.getName()))
					i++;
				Property property = properties.get(i);
				checkUtilityOwnership(property, roll);
			}
			if(die1 != die2) {
				System.out.println("Balance: " + currentPlayer.getFunds() + "\n");
				return;
			}
			else {
				if(turn == 1)
					System.out.println("Doubles! Roll again, " + currentPlayer.getName() + ".\nPress ENTER when ready.");
				if(turn == 2)
					System.out.println("Doubles Again, " + currentPlayer.getName() + "!\nOne more & GO TO JAIL.\nPress ENTER when ready.");
				sc.nextLine();
			}
		}
	}
	
	public static void drawCard(ArrayList<Card> deck) {
		
		int rand = new Random().nextInt(deck.size());
		Card card = deck.get(rand);
		if(card.cardType == 5)
			deck.remove(card);
		
		System.out.println(card.displayCardInfo() + "\n");
		card.completeCardAction(currentPlayer);
	}
	public static void useOutOfJailCard(Card card) {
		currentPlayer.removeJailTimer();
		currentPlayer.jailCards.remove(0);
		if(card.parameter.equals("Chance"))
			chanceCards.add(card);
		else
			communityChestCards.add(card);
		currentPlayer.updatePosition(10);
	}
	public static int getGroupSize(PropertyGroup group) {
		return switch(group) {
			case Brown, Blue, Utility -> 2;
			case LightBlue, Pink, Orange, Red, Yellow, Green -> 3;
			case RailRoad -> 4;
		};
	}
	public static boolean hasColorSet() {
		return currentPlayer.brown == getGroupSize(PropertyGroup.Brown) || currentPlayer.lightBlue == getGroupSize(PropertyGroup.LightBlue) ||
		   currentPlayer.pink == getGroupSize(PropertyGroup.Pink) || currentPlayer.orange == getGroupSize(PropertyGroup.Orange) ||
		   currentPlayer.red == getGroupSize(PropertyGroup.Red) || currentPlayer.yellow == getGroupSize(PropertyGroup.Yellow) ||
		   currentPlayer.green == getGroupSize(PropertyGroup.Green) || currentPlayer.blue == getGroupSize(PropertyGroup.Blue) ||
		   currentPlayer.railRoads == getGroupSize(PropertyGroup.RailRoad) || currentPlayer.utilities == getGroupSize(PropertyGroup.Utility);
	}
	public static void checkPropertyOwnership(Property property) {
		Scanner sc = new Scanner(System.in);
		if(property.owner == null) {
			System.out.print("This property is not owned. Would you like to purchase it for $" + property.price + "? (y/n) ");
			String ans = sc.nextLine();
			if(ans.equalsIgnoreCase("y")) {
				property.owner = currentPlayer;
				currentPlayer.properties.add(property);
				currentPlayer.updateFunds(property.price * -1);
				System.out.println();
				System.out.println("Congratulations, " + currentPlayer.getName() + "! You own " + board.spotReference.get(currentPlayer.position) + ".");
				
				switch(property.color) {
					case "BROWN" -> currentPlayer.brown++;
					case "LIGHT BLUE" -> currentPlayer.lightBlue++;
					case "PINK" -> currentPlayer.pink++;
					case "ORANGE" -> currentPlayer.orange++;
					case "RED" -> currentPlayer.red++;
					case "YELLOW" -> currentPlayer.yellow++;
					case "GREEN" -> currentPlayer.green++;
					case "BLUE" -> currentPlayer.blue++;
				}
				
				if(hasColorSet())
					System.out.print("You now own the " + property.color + " color set.\nWould you like to purchase houses for these properties? (y/n) ");
			}
			else if(ans.equalsIgnoreCase("n")) {
				auction(property);
			}
			System.out.println();
		}
		else if(!currentPlayer.properties.contains(property)) {
			Player thisPlayer = currentPlayer;
			int thisNumber = currentPlayerNumber;
			while(!currentPlayer.properties.contains(property))
				nextPlayer();
			System.out.println(thisPlayer.getName() + ", this property is owned by " + currentPlayer.getName() + ".");
			int[] owe = property.getPayoutList();
			
			if(hasColorSet()) {
				
				if(property.hasHouses()) {
					if(property.numHouses == 1) {
						thisPlayer.updateFunds(owe[property.ONE_HOUSE] * -1);
						currentPlayer.updateFunds(owe[property.ONE_HOUSE]);
						System.out.println(currentPlayer.getName() + " owns one house on this property.\nA rent payment of $" + owe[property.ONE_HOUSE] +
						                   " has been transferred from your account to " + currentPlayer.getName() + "'s.\n");
					}
					else if(property.numHouses == 2) {
						thisPlayer.updateFunds(owe[property.TWO_HOUSES] * -1);
						currentPlayer.updateFunds(owe[property.TWO_HOUSES]);
						System.out.println(currentPlayer.getName() + " owns two houses on this property.\nA rent payment of $" + owe[property.TWO_HOUSES] +
						                   " has been transferred from your account to " + currentPlayer.getName() + "'s.\n");
					}
					else if(property.numHouses == 3) {
						thisPlayer.updateFunds(owe[property.THREE_HOUSES] * -1);
						currentPlayer.updateFunds(owe[property.THREE_HOUSES]);
						System.out.println(currentPlayer.getName() + " owns three houses on this property.\nA rent payment of $" + owe[property.THREE_HOUSES] +
						                   " has been transferred from your account to " + currentPlayer.getName() + "'s.\n");
					}
					else if(property.numHouses == 4) {
						thisPlayer.updateFunds(owe[property.FOUR_HOUSES] * -1);
						currentPlayer.updateFunds(owe[property.FOUR_HOUSES]);
						System.out.println(currentPlayer.getName() + " owns four houses on this property.\nA rent payment of $" + owe[property.FOUR_HOUSES] +
						                   " has been transferred from your account to " + currentPlayer.getName() + "'s.\n");
					}
					
					if(property.hasHotel()) {
						thisPlayer.updateFunds(owe[property.HOTEL] * -1);
						currentPlayer.updateFunds(owe[property.HOTEL]);
						System.out.println(currentPlayer.getName() + " owns a hotel on this property.\nA rent payment of $" + owe[property.HOTEL] +
						                   " has been transferred from your account to " + currentPlayer.getName() + "'s.\n");
					}
				}
				else {
					thisPlayer.updateFunds(owe[property.COLOR_SET] * -1);
					currentPlayer.updateFunds(owe[property.COLOR_SET]);
					System.out.println("A rent payment of $" + owe[property.COLOR_SET] + " has been transferred from your account to " + currentPlayer.getName() + "'s.\n");
				}
			}
			else {
				thisPlayer.updateFunds(owe[property.RENT] * -1);
				currentPlayer.updateFunds(owe[property.RENT]);
				System.out.println("A rent payment of $" + owe[property.RENT] + " has been transferred from your account to " + currentPlayer.getName() + "'s.\n");
			}
			currentPlayer = thisPlayer;
			currentPlayerNumber = thisNumber;
		}
		else {
			System.out.println("You own this property.\n");
		}
	}
	// TODO: finish auction method
	public static void auction(Property property) {
		System.out.println();
		Scanner sc = new Scanner(System.in);
		System.out.println("You decided not to purchase. " + property.name + " will now go up for auction.\nThe bidding will start at $10.\n");
		ArrayList<Player> bidders = new ArrayList<>();
		Player thisPlayer = currentPlayer;
		Player currentBidder = currentPlayer;
		Player highestBidder = null;
		int thisNumber = currentPlayerNumber;
		
		while(bidders.size() < board.getPlayerCount()) {
			bidders.add(currentPlayer);
			nextPlayer();
		}
		int highestBid = 10;
		int i = 0;
		System.out.println("---BIDDING OPEN---\n\nBid higher than the highest bid to stay in the bidding!\n");
		while(bidders.size() > 1) {
			System.out.println("Highest bid: $" + highestBid + "\n");
			System.out.print(currentBidder.getName() + ", continue to bid? (y/n) ");
			String ans = sc.nextLine();
			if(ans.equalsIgnoreCase("y")) {
				System.out.print("Bid: ");
				currentBidder.bid = sc.nextLine();
				System.out.println();
				if(Integer.parseInt(currentBidder.bid) > highestBid) {
					highestBid = Integer.parseInt(currentBidder.bid);
					highestBidder = currentBidder;
					i++;
					if(i >= bidders.size())
						i = 0;
					currentBidder = bidders.get(i);
				}
				else {
					bidders.remove(currentBidder);
					System.out.println(currentBidder.getName() + " didn't bid high enough.\n" + currentBidder.getName() + " has been removed from the bid.\n");
					currentBidder = bidders.get(i % bidders.size());
				}
			}
			else if(ans.equalsIgnoreCase("n")) {
				bidders.remove(currentBidder);
				System.out.println(currentBidder.getName() + " has been removed from the bid.\n");
				currentBidder = bidders.get(i % bidders.size());
			}
		}
		if(highestBidder == null)
			highestBidder = bidders.get(0);
		System.out.println(highestBidder.getName() + " wins " + property.name + " with a bid of $" + highestBid + "!\n\n---BIDDING CLOSED---");
		highestBidder.updateFunds(highestBid * -1);
		highestBidder.properties.add(property);
		property.owner = highestBidder;
		currentPlayer = thisPlayer;
		currentPlayerNumber = thisNumber;
	}
	public static void checkRailRoadOwnership(Property property) {
		Scanner sc = new Scanner(System.in);
		if(property.owner == null) {
			System.out.print("This railroad is not owned. Would you like to purchase it for $" + property.price + "? (y/n) ");
			String ans = sc.nextLine();
			if(ans.equalsIgnoreCase("y")) {
				property.owner = currentPlayer;
				currentPlayer.properties.add(property);
				currentPlayer.updateFunds(property.price * -1);
				System.out.println();
				System.out.println("Congratulations, " + currentPlayer.getName() + "! You own " + board.spotReference.get(currentPlayer.position) + ".");
				currentPlayer.railRoads++;
				if(hasColorSet()) {
					System.out.println("You now own every railroad.");
				}
			}
			else if(ans.equalsIgnoreCase("n")) {
				auction(property);
			}
			System.out.println();
		}
		else if(!currentPlayer.properties.contains(property)) {
			Player thisPlayer = currentPlayer;
			int thisNumber = currentPlayerNumber;
			while(!currentPlayer.properties.contains(property))
				nextPlayer();
			System.out.println(thisPlayer.getName() + ", this railroad is owned by " + currentPlayer.getName() + ".");
			
			int[] owe = property.getPayoutList();
			int paid;
			if(currentPlayer.railRoads == 1)
				paid = owe[property.RENT];
			else if(currentPlayer.railRoads == 2)
				paid = owe[property.TWO_RR];
			else if(currentPlayer.railRoads == 3)
				paid = owe[property.THREE_RR];
			else
				paid = owe[property.FOUR_RR];
			
			thisPlayer.updateFunds(paid * -1);
			currentPlayer.updateFunds(paid);
			
			System.out.println("A rent payment of $" + paid + " has been transferred from your account to " + currentPlayer.getName() + "'s.\n");
			
			currentPlayer = thisPlayer;
			currentPlayerNumber = thisNumber;
		} else {
			System.out.println("You own this railroad.\n");
		}
	}
	public static void checkRailRoadOwnershipAtCardDraw(Property property) {
		Scanner sc = new Scanner(System.in);
		if(property.owner == null) {
			System.out.print("This railroad is not owned. Would you like to purchase it for $" + property.price + "? (y/n) ");
			String ans = sc.nextLine();
			if(ans.equalsIgnoreCase("y")) {
				property.owner = currentPlayer;
				currentPlayer.properties.add(property);
				currentPlayer.updateFunds(property.price * -1);
				System.out.println();
				System.out.println("Congratulations, " + currentPlayer.getName() + "! You own " + board.spotReference.get(currentPlayer.position) + ".");
				currentPlayer.railRoads++;
				if(hasColorSet()) {
					System.out.println("You now own every railroad.");
				}
			}
			else if(ans.equalsIgnoreCase("n")) {
				auction(property);
			}
			System.out.println();
		}
		else if(!currentPlayer.properties.contains(property)) {
			Player thisPlayer = currentPlayer;
			int thisNumber = currentPlayerNumber;
			while(!currentPlayer.properties.contains(property))
				nextPlayer();
			System.out.println(thisPlayer.getName() + ", this railroad is owned by " + currentPlayer.getName() + ".");
			
			int[] owe = property.getPayoutList();
			int paid;
			if(currentPlayer.railRoads == 1)
				paid = owe[property.RENT];
			else if(currentPlayer.railRoads == 2)
				paid = owe[property.TWO_RR];
			else if(currentPlayer.railRoads == 3)
				paid = owe[property.THREE_RR];
			else
				paid = owe[property.FOUR_RR];
			
			thisPlayer.updateFunds(paid * -2);
			currentPlayer.updateFunds(paid);
			
			System.out.println("Since you drew a card to get here, your rent for this railroad has doubled. $" + paid + " has been transferred from your " +
			                   "account to " + currentPlayer.getName() + "'s.\n");
			
			currentPlayer = thisPlayer;
			currentPlayerNumber = thisNumber;
		} else {
			System.out.println("You own this railroad.\n");
		}
	}
	public static void checkUtilityOwnership(Property property, int roll) {
		Scanner sc = new Scanner(System.in);
		int rent;
		if(property.owner == null) {
			System.out.print("This utility is not owned. Would you like to purchase it for $" + property.price + "? (y/n) ");
			String ans = sc.nextLine();
			if(ans.equalsIgnoreCase("y")) {
				property.owner = currentPlayer;
				currentPlayer.properties.add(property);
				currentPlayer.updateFunds(property.price * -1);
				System.out.println();
				System.out.println("Congratulations, " + currentPlayer.getName() + "! You own " + board.spotReference.get(currentPlayer.position) + ".\n");
				currentPlayer.utilities++;
				if(hasColorSet()) {
					System.out.println("You now own both utilities.\n");
				}
			}
			else if(ans.equalsIgnoreCase("n")) {
				auction(property);
			}
		}
		else if(!currentPlayer.properties.contains(property)) {
			Player thisPlayer = currentPlayer;
			int thisNumber = currentPlayerNumber;
			while(!currentPlayer.properties.contains(property))
				nextPlayer();
			System.out.println(thisPlayer.getName() + ", this utility is owned by " + currentPlayer.getName() + ".\n");
			
			if(currentPlayer.utilities == 1) {
				rent = roll * 4;
				System.out.println(currentPlayer.getName() + " owns this utility only.\nPay four times the dice value.");
				thisPlayer.updateFunds(rent * -1);
				currentPlayer.updateFunds(rent);
				System.out.println("A rent payment of $" + rent + " has been transferred from your account to " + currentPlayer.getName() + "'s.\n");
			}
			else if(currentPlayer.utilities == 2) {
				rent = roll * 10;
				System.out.println(currentPlayer.getName() + " owns both utilities.\nPay ten times the dice value.");
				thisPlayer.updateFunds(rent * -1);
				currentPlayer.updateFunds(rent);
				System.out.println("A rent payment of $" + rent + " has been transferred from your account to " + currentPlayer.getName() + "'s.\n");
			}
			currentPlayer = thisPlayer;
			currentPlayerNumber = thisNumber;
		}
		else {
			System.out.println("You own this utility.\n");
		}
	}
	public static ArrayList<Card> getCardFileInfo(String fileName) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(fileName));
		ArrayList<Card> list = new ArrayList<>();
		while(sc.hasNext()) {
			String[] parse = sc.nextLine().split(",");
			list.add(new Card(Integer.parseInt(parse[0]), parse[1], parse[2]));
		} return list;
	}
	public static ArrayList<Property> getNormalPropertyInfo(String fileName) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(fileName));
		ArrayList<Property> list = new ArrayList<>();
		while(sc.hasNext()) {
			String[] parse = sc.nextLine().split(",");
			list.add(new Property(parse[0],Integer.parseInt(parse[1]),Integer.parseInt(parse[2]),parse[3]));
		} return list;
	}
	public static void main(String[] args) throws FileNotFoundException {
		communityChestCards = getCardFileInfo(COMMUNITY_CARD_FILE);
		chanceCards = getCardFileInfo(CHANCE_CARD_FILE);
		properties = getNormalPropertyInfo(PROPERTY_FILE);
		welcomeMessage();
		beginningPrompt();
		while(board.getPlayerCount() > 1) {
			playerRoll();
			nextPlayer();
			prompt();
		}
	}
}
//			System.out.println("Highest bid: $" + highestBid + "\n");
//					System.out.print(currentBidder.getName() + ", continue to bid? (y/n) ");
//					String ans = sc.nextLine();
//					if(ans.equalsIgnoreCase("y")) {
//					System.out.print("Bid: ");
//					currentBidder.bid = sc.nextLine();
//					System.out.println();
//					if(Integer.parseInt(currentBidder.bid) > highestBid) {
//					highestBid = Integer.parseInt(currentBidder.bid);
//					highestBidder = currentBidder;
//					i++;
//					if(i >= bidders.size())
//					i = 0;
//					currentBidder = bidders.get(i);
//					}
//					else {
//					bidders.remove(currentBidder);
//					System.out.println(currentBidder.getName() + " didn't bid high enough.\n" + currentBidder.getName() + " has been removed from the bid.\n");
//					currentBidder = bidders.get(i % bidders.size());
//					}
//					}
//					else if(ans.equalsIgnoreCase("n")) {
//					bidders.remove(currentBidder);
//					System.out.println(currentBidder.getName() + " has been removed from the bid.\n");
//					currentBidder = bidders.get(i % bidders.size());
//					}
