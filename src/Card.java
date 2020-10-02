import jdk.swing.interop.SwingInterOpUtils;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Card extends Board {
	public int cardType;
	public String parameter, instruction;
	private final int FUND_UPDATE = 1;
	private final int ADVANCE_TO_PLACE = 2;
	private final int ADVANCE_TO_GO = 3;
	private final int GENERAL_REPAIRS = 4;
	private final int GET_OUT_OF_JAIL = 5;
	private final int GO_TO_JAIL = 6;
	private final int UTILITY = 7;
	private final int INVOLVE_PLAYERS = 8;
	private final int BACK_3_SPACES = 9;
	private final int NEAREST_RAILROAD = 10;
	private final int STREET_REPAIRS = 11;
	
	public Card(int cardType, String instruction, String parameter) {
		this.cardType = cardType;
		this.instruction = instruction;
		this.parameter = parameter;
	}
	public String displayCardInfo() {
		return "READ: " + instruction + ".";
	}
	public int getCardType() {
		return cardType;
	}
	public void completeCardAction(Player player) {
		Scanner sc = new Scanner(System.in);
		if(cardType == FUND_UPDATE) {
			int amt = Integer.parseInt(parameter);
			player.updateFunds(amt);
			if(amt > 0)
				System.out.println("$" + Integer.parseInt(parameter) + " has been deposited into your account.\n");
			else
				System.out.println("$" + Integer.parseInt(parameter)*-1 + " has been deducted from your account.\n");
		}
		else if(cardType == ADVANCE_TO_PLACE) {
			int currentPosition = player.getPosition();
			int newPosition = Integer.parseInt(parameter);
			String place = Game.board.spotReference.get(newPosition);
			player.updatePosition(newPosition);
			
			if(newPosition < currentPosition) {
				player.updateFunds(200);
				System.out.println("You are now at " + place + ".\nYou received $200 for passing GO.\n");
			} else
				System.out.println("You are now at " + place + ".\n");
			
			BoardSpace space = new BoardSpace(newPosition, place);
			int i = 0;
			while(!Game.properties.get(i).name.equals(space.getName()))
				i++;
			Property property = Game.properties.get(i);
			if(space.isRailRoad())
				Game.checkRailRoadOwnership(property);
			else
				Game.checkPropertyOwnership(property);
		}
		else if(cardType == ADVANCE_TO_GO) {
			player.updatePosition(0);
			player.updateFunds(200);
			System.out.println("You are now on PASS GO.\n$200 has been deposited into your account.\n");
		}
		else if (cardType == GENERAL_REPAIRS) {
			int genRepairCost = -(player.getHotels()*100 + player.getHouses()*25);
			player.updateFunds(genRepairCost);
			System.out.println("You paid for general repairs on your hotels and houses.\n$" + genRepairCost + " has been deducted from your account.\n");
		}
		else if(cardType == STREET_REPAIRS) {
			int streetRepairCost = -(player.getHotels()*115 + player.getHouses()*40);
			player.updateFunds(streetRepairCost);
			System.out.println("You paid for street repairs near your hotels and houses.\n$" + streetRepairCost + " has been deducted from your account.\n");
		}
		else if(cardType == GET_OUT_OF_JAIL) {
			player.jailCards.add(this);
			System.out.println("You may choose to use this card if you end up in jail.\n");
		}
		else if(cardType == GO_TO_JAIL) {
			player.updatePosition(10);
			player.setJailTimer();
			System.out.println("GO DIRECTLY TO JAIL. DO NOT COLLECT $200.\n");
		}
		else if(cardType == UTILITY) {
			if(player.getPosition() < 12 || player.getPosition() > 28) {
				Property property = Game.properties.get(26);
				checkUtilityOwnership(property, player);
			}
			else if(player.getPosition() > 12 && player.getPosition() < 28) {
				Property property = Game.properties.get(27);
				checkUtilityOwnership(property, player);
			}
		}
		else if(cardType == INVOLVE_PLAYERS) {
			int amt = Integer.parseInt(parameter);
			player.updateFunds(amt * (Game.board.getPlayerCount() - 1));
			int excludedPlayer = Game.currentPlayerNumber;
			if(amt < 0) {
				for(int i = 1; i <= Game.board.getPlayerCount(); i++) {
					if(i == excludedPlayer)
						continue;
					Game.nextPlayer();
					Game.currentPlayer.updateFunds(-amt);
					String name = Game.currentPlayer.getName();
					System.out.println("You paid " + name + " $50.");
				}
			}
			else {
				for(int i = 1; i <= Game.board.getPlayerCount(); i++) {
					if(i == excludedPlayer)
						continue;
					Game.nextPlayer();
					Game.currentPlayer.updateFunds(-amt);
					String name = Game.currentPlayer.getName();
					System.out.println(name + " paid you $50.");
				}
			}
			System.out.println();
			Game.currentPlayer = player;
			Game.nextPlayer();
		}
		// TODO: FIXME
		else if(cardType == BACK_3_SPACES) {
			player.updatePosition(player.position - 3);
			System.out.println("You moved back three spaces. You are now at " + spotReference.get(player.position) + ".\n");
			if(spotReference.get(player.position).equals("NEW YORK AVENUE")) {
				Property property = Game.properties.get(10);
				Game.checkPropertyOwnership(property);
			}
			else if(spotReference.get(player.position).equals("INCOME TAX")) {
				player.updateFunds(-200);
				System.out.println("You paid $200 in income tax.\n");
			}
			else
				Game.drawCard(Game.communityChestCards);
		}
		else if(cardType == NEAREST_RAILROAD) {
			Property property;
			if(player.position == 7) {
				player.updatePosition(15);
				property = Game.properties.get(23);
			}
			else if(player.position == 22) {
				player.updatePosition(25);
				property = Game.properties.get(24);
			}
			else {
				player.updatePosition(35);
				property = Game.properties.get(25);
			}
			System.out.println("You've been advanced to " + spotReference.get(player.position) + ".\n");
			Game.checkRailRoadOwnershipAtCardDraw(property);
		}
	}
	public void checkUtilityOwnership(Property property, Player player) {
		Scanner sc = new Scanner(System.in);
		player.updatePosition(28);
		System.out.println("You've been advanced to " + property.name + "\n");
		if(property.owner != null && !property.owner.equals(player)) {
			System.out.println(property.owner.getName() + " owns this utility. Roll the dice & pay ten times the amount shown.\nPress ENTER to roll.");
			sc.nextLine();
			int die1 = new Random().nextInt(6) + 1;
			int die2 = new Random().nextInt(6) + 1;
			int roll = die1 + die2;
			player.updateFunds(-10 * roll);
			property.owner.updateFunds(10 * roll);
			System.out.println("You rolled " + roll + " (" + die1 + " & " + die2 + ")");
			System.out.println("$" + roll * 10 + " has been transferred from your account to " + property.owner.getName() + "'s.\n");
		}
		else if(property.owner == null) {
			System.out.print("This utility is not owned. Would you like to purchase it for $" + property.price + "? (y/n) ");
			if(sc.nextLine().equalsIgnoreCase("y")) {
				property.owner = player;
				player.properties.add(property);
				player.updateFunds(property.price * -1);
				System.out.println();
				System.out.println("Congratulations, " + player.getName() + "! You own " + Game.board.spotReference.get(player.position) + ".\n");
				player.railRoads++;
				if(Game.hasColorSet()) {
					System.out.println("You now own both utilities.\n");
				}
			}
		}
		else
			System.out.println("You own this utility.\n");
	}
}
