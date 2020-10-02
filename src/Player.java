import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

public class Player {
	public int cash, position, hotels, houses, brown, lightBlue, pink, orange, red, yellow, green, blue, railRoads, utilities, jailTimer;
	public ArrayList<Property> properties = new ArrayList<>();
	public ArrayList<Card> jailCards;
	public String name, bid;
	private final int MAX_TRAVEL = 40;
	public Player() {
		name = "";
		cash = 1500;
		position = 0;
		hotels = 0;
		houses = 0;
		
		//Owned properties from each group
		brown = lightBlue = pink = orange =
		yellow = green = blue = railRoads = utilities = 0;
		
		jailTimer = 0;
		bid = "";
		jailCards = new ArrayList<>();
	}
	public void updateFunds(int cash) {
		this.cash += cash;
	}
	public String getFunds() {
		return "$" + cash;
	}
	public boolean addToPosition(int number) {
		boolean passedGo = false;
		position += number;
		if(position >= MAX_TRAVEL) {
			passedGo = true;
			updateFunds(200);
			position %= MAX_TRAVEL;
		} return passedGo;
	}
	public void updatePosition(int number) {
		position = number;
	}
	public int getPosition() {
		return position;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public int getHotels() { return hotels; }
	public boolean hasHotels() { return hotels > 0; }
	public void updateHotelCount(int hotels) { this.hotels += hotels; }
	public int getHouses() { return houses; }
	public boolean hasHouses() { return houses > 0; }
	public void updateHouseCount(int houses) { this.houses += houses; }
	public boolean hasJailFreeCard() { return Game.currentPlayer.jailCards.size() > 0; }
	public void setJailTimer() { jailTimer = 1; }
	public void updateJailTimer() { jailTimer--; }
	public int getJailTimer() {return jailTimer;}
	public void removeJailTimer() {jailTimer = 0;}
}
