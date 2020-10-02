public class BoardSpace {
	public int position;
	public Player owner;
	public String name;
	private final int PASS_GO = 0; private final int MEDITERRANEAN_AVE = 1; private final int COMMUNITY_CHEST_1 = 2; private final int BALTIC_AVE = 3;
	private final int INCOME_TAX = 4; private final int READING_RR = 5; private final int ORIENTAL_AVE = 6; private final int CHANCE_1 = 7;
	private final int VERMONT_AVE = 8; private final int CONNECTICUT_AVE = 9; private final int JAIL_VISIT = 10; private final int ST_CHARLES_PLACE = 11;
	private final int ELECTRIC_COMPANY = 12; private final int STATES_AVE = 13; private final int VIRGINIA_AVE = 14; private final int PENNSYLVANIA_RR = 15;
	private final int ST_JAMES_PLACE = 16; private final int COMMUNITY_CHEST_2 = 17; private final int TENNESSEE_AVE = 18; private final int NEW_YORK_AVE = 19;
	private final int FREE_PARKING = 20; private final int KENTUCKY_AVE = 21; private final int CHANCE_2 = 22; private final int INDIANA_AVE = 23;
	private final int ILLINOIS_AVE = 24; private final int BO_RR = 25; private final int ATLANTIC_AVE = 26; private final int VENTNOR_AVE = 27;
	private final int WATER_WORKS = 28; private final int MARVIN_GARDENS = 29; private final int GO_TO_JAIL = 30; private final int PACIFIC_AVE = 31;
	private final int NORTH_CAROLINA_AVE = 32; private final int COMMUNITY_CHEST_3 = 33; private final int PENNSYLVANIA_AVE = 34; private final int SHORT_LINE = 35;
	private final int CHANCE_3 = 36; private final int PARK_PLACE = 37; private final int LUX_TAX = 38; private final int BOARDWALK = 39;
	public BoardSpace() {}
	public BoardSpace(int position, String name) {
		this.name = name;
		this.position = position;
		owner = null;
	}
	public boolean isRailRoad() {
		return position == READING_RR || position == PENNSYLVANIA_RR || position == BO_RR || position == SHORT_LINE;
	}
	public boolean isGoToJail() {
		return position == GO_TO_JAIL;
	}
	public boolean isFreeParking() {
		return position == FREE_PARKING;
	}
	public boolean isChance() { return position == CHANCE_1 || position == CHANCE_2 || position == CHANCE_3; }
	public boolean isCommunityChest() {
		return position == COMMUNITY_CHEST_1 || position == COMMUNITY_CHEST_2 || position == COMMUNITY_CHEST_3;
	}
	public boolean isTax() {
		return position == INCOME_TAX || position == LUX_TAX;
	}
	public boolean isNormalProperty() {
		return position == MEDITERRANEAN_AVE || position == BALTIC_AVE || position == ORIENTAL_AVE || position == VERMONT_AVE || position == CONNECTICUT_AVE ||
		       position == ST_CHARLES_PLACE || position == STATES_AVE || position == VIRGINIA_AVE || position == ST_JAMES_PLACE || position == TENNESSEE_AVE ||
		       position == NEW_YORK_AVE || position == KENTUCKY_AVE || position == INDIANA_AVE || position == ILLINOIS_AVE || position == ATLANTIC_AVE ||
		       position == VENTNOR_AVE || position == MARVIN_GARDENS || position == PACIFIC_AVE || position == NORTH_CAROLINA_AVE || position == PENNSYLVANIA_AVE ||
		       position == PARK_PLACE || position == BOARDWALK;
	}
	public boolean isUtility() { return position == 12 || position == 28; }
	public String getName() {
		return name;
	}
	public int getPosition() {return position;}
	@Override
	public String toString() {
		return name;
	}
}
