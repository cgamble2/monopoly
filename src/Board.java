import java.util.*;

public class Board {
	public final HashMap<Integer, BoardSpace> boardIndexes = new HashMap<>();
	public final HashMap<String, BoardSpace> boardSpaceNames = new HashMap<>();
	public final HashMap<BoardSpace, Integer> boardSpaces = new HashMap<>();
	public final HashMap<Integer, String> spotReference = new HashMap<>();
	private int numPlayers;
	private final String[] titles = {"PASS GO", "MEDITERRANEAN AVENUE", "COMMUNITY CHEST", "BALTIC AVENUE", "INCOME TAX", "READING RAILROAD",
	                                    "ORIENTAL AVENUE", "CHANCE", "VERMONT AVENUE", "CONNECTICUT AVENUE", "JUST VISITING JAIL", "ST. CHARLES PLACE",
	                                    "ELECTRIC COMPANY", "STATES AVENUE", "VIRGINIA AVENUE", "PENNSYLVANIA RAILROAD", "ST. JAMES PLACE",
	                                    "COMMUNITY CHEST", "TENNESSEE AVENUE", "NEW YORK AVENUE", "FREE PARKING", "KENTUCKY AVENUE", "CHANCE",
	                                    "INDIANA AVENUE", "ILLINOIS AVENUE", "B. & O. RAILROAD", "ATLANTIC AVENUE", "VENTNOR AVENUE", "WATER WORKS",
	                                    "MARVIN GARDENS", "GO TO JAIL", "PACIFIC AVENUE", "NORTH CAROLINA AVENUE", "COMMUNITY CHEST",
	                                    "PENNSYLVANIA AVENUE", "SHORT LINE", "CHANCE", "PARK PLACE", "LUXURY TAX", "BOARDWALK"};
	public Board() {
		for(int i = 0; i < titles.length; i++) {
			BoardSpace space = new BoardSpace(i, titles[i]);
			boardIndexes.put(i, space);
			boardSpaceNames.put(titles[i], space);
			boardSpaces.put(space, i);
			spotReference.put(i, titles[i]);
		}
		
	}
	public void setPlayerCount(int numPlayers) {
		this.numPlayers = numPlayers;
	}
	public int getPlayerCount() {
		return numPlayers;
	}
	public void subtractPlayerCount() {
		numPlayers--;
	}
	
}
