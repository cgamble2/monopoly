import java.util.ArrayList;
enum PropertyGroup { Brown, LightBlue, Pink, Orange, Red, Yellow, Green, Blue, RailRoad, Utility }
public class Property extends BoardSpace {
	public String name, color;
	public int price, mortgage, houseCost, hotelCost, numHotels, numHouses;
	public Player owner;
	public final int RENT = 0;
	public final int COLOR_SET = 1;
	public final int TWO_RR = 1;
	public final int ONE_HOUSE = 2;
	public final int THREE_RR = 2;
	public final int TWO_HOUSES = 3;
	public final int FOUR_RR = 3;
	public final int THREE_HOUSES = 4;
	public final int FOUR_HOUSES = 5;
	public final int HOTEL = 6;
	public Property() {}
	public Property(String name, int price, int mortgage, String color) {
		this.name = name;
		this.price = price;
		this.mortgage = mortgage;
		this.color = color;
		this.numHouses = 0;
		this.numHotels = 0;
		this.houseCost = 0;
		this.hotelCost = 0;
		owner = null;
	}
	public int[] getPayoutList() {
		switch(name) {
			case "MEDITERRANEAN AVENUE" -> {
				return new int[] {2, 4, 10, 30, 90, 160, 250};
			}
			case "BALTIC AVENUE" -> {
				return new int[] {4, 8, 20, 60, 180, 320, 450};
			}
			case "ORIENTAL AVENUE", "VERMONT AVENUE" -> {
				return new int[] {6, 12, 30, 90, 270, 400, 550};
			}
			case "CONNECTICUT AVENUE" -> {
				return new int[] {8, 16, 40, 100, 300, 450, 600};
			}
			case "ST. CHARLES PLACE", "STATES AVENUE" -> {
				return new int[] {10, 20, 50, 150, 450, 625, 750};
			}
			case "VIRGINIA AVENUE" -> {
				return new int[] {12, 24, 60, 180, 500, 700, 900};
			}
			case "ST. JAMES PLACE", "TENNESSEE AVENUE" -> {
				return new int[] {14, 28, 70, 200, 550, 750, 950};
			}
			case "NEW YORK AVENUE" -> {
				return new int[] {16, 32, 80, 220, 600, 800, 1000};
			}
			case "KENTUCKY AVENUE", "INDIANA AVENUE" -> {
				return new int[] {18, 36, 90, 250, 700, 875, 1050};
			}
			case "ILLINOIS AVENUE" -> {
				return new int[] {20, 40, 100, 300, 750, 925, 1100};
			}
			case "ATLANTIC AVENUE", "VENTNOR AVENUE" -> {
				return new int[] {22, 44, 110, 330, 800, 975, 1150};
			}
			case "MARVIN GARDENS" -> {
				return new int[] {24, 48, 120, 360, 850, 1025, 1200};
			}
			case "PACIFIC AVENUE", "NORTH CAROLINA AVENUE" -> {
				return new int[] {26, 52, 130, 390, 900, 1100, 1275};
			}
			case "PENNSYLVANIA AVENUE" -> {
				return new int[] {28, 56, 150, 450, 1000, 1200, 1400};
			}
			case "PARK PLACE" -> {
				return new int[] {35, 70, 175, 500, 1100, 1300, 1500};
			}
			case "BOARDWALK" -> {
				return new int[] {50, 100, 200, 600, 1400, 1700, 2000};
			}
			case "READING RAILROAD", "PENNSYLVANIA RAILROAD", "B. & O. RAILROAD", "SHORT LINE" -> {
				return new int[] {25, 50, 100, 200};
			}
		}
		return null;
	}
	public String getPropertyColor() {
		return color;
	}
	public boolean hasHotel() {
		return numHotels == 1;
	}
	public boolean hasHouses() {
		return numHouses > 0;
	}
	public Player getOwner() {
		return owner;
	}
	
}
