import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Main class to test the Road and Settlement classes
 * 
 * @author Chris Loftus (add your name and change version number/date)
 * @version 1.0 (24th February 2016)
 *
 */
public class Application {

	private Scanner in;
	private Map map;

	/**
	 * Constructor
	 * Initialises values 
	 */
	public Application() {
		in = new Scanner(System.in);
		map = new Map();
	}

	/**
	 * Calls printMenu, gets the user's choice and then acts on it accordingly
	 * @throws FileNotFoundException 
	 */
	private void runMenu() {
		String userInput = "";
		
		while (!(userInput.equals("Q"))) {
			printMenu();

			userInput = in.nextLine();
			userInput = userInput.toUpperCase();

			switch (userInput) {
			case "1":
				createSettlement();
				break;
			case "2":
				deleteSettlement();
				break;
			case "3":
				createRoad();
				break;
			case "4":
				deleteRoad();
				break;
			case "5":
				displayMap();
				break;
			case "6":
				try {
					load();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case "7":
				save();
				break;
			case "Q":
				break;
			default:
				System.out.println("Invalid Input: " + userInput);
				break;
			}
		}
	}

	/**
	 * Asks for a road classifier. Loops through all classifier values,
	 * outputting them to the console. It then matches the user's input to one of
	 * them. If it is an invalid option, it lets the user try again
	 * 
	 * @return the classifier
	 */
	public Classification askForRoadClassifier() {
		Classification result = null;
		boolean valid;
		do {
			valid = false;
			System.out.print("Please enter a road classification: ");
			for (Classification cls : Classification.values()) {
				System.out.print(cls + " ");
			}
			System.out.println();
			String choice = in.nextLine().toUpperCase();
			try {
				result = Classification.valueOf(choice);
				valid = true;
			} catch (IllegalArgumentException iae) {
				System.out.println(choice + " is not one of the options. Try again.");
			}
		} while (!valid);
		return result;
	}

	/**
	 * Asks for a settlement kind. Loops through all the kind values,
	 * outputting them to the console. It then matches the user's input to one of
	 * them. If it is an invalid option, it lets the user try again
	 * 
	 * @return the kind
	 */
	public SettlementType askForSettlementType() {
		SettlementType result = null;
		boolean valid;
		do {
			valid = false;
			System.out.print("Please enter your Settlement's type: ");
			for (SettlementType sType : SettlementType.values()) {
				System.out.print(sType + " ");
			}
			String choice = in.nextLine().toUpperCase();
			try {
				result = SettlementType.valueOf(choice);
				valid = true;
			} catch (IllegalArgumentException iae) {
				System.out.println(choice + " is not one of the options. Try again.");
			}
		} while (!valid);
		return result;
	}
	
	/**
	 * Gets the names to save the files to, before saving the files
	 */
	private void save() {
		map.getFilenames();
		map.save();
	}

	/**
	 * Gets the names to load the files to, before loading the files
	 * @throws FileNotFoundException 
	 */
	private void load() throws FileNotFoundException {
		map.getFilenames();
		map.load();
	}


	/**
	 * Prints the menu
	 */
	private void printMenu() {
		System.out.println(" 1 - Create a Settlement" + 
						"\n 2 - Delete a Settlement" + 
						"\n 3 - Create a Road" +
						"\n 4 - Delete a Road" + 
						"\n 5 - Display Map" + 
						"\n 6 - Load" +
						"\n 7 - Save" +
						"\n q - Quit");
	}

	/**
	 * Creates a settlement. Gets the settlement's information,
	 * checks to see if the settlement already exists and then adds the settlement
	 */
	private void createSettlement() {
		String name;
		int population;

		SettlementType settlementType;

		System.out.println("Please enter your Settlement's name:");
		name = in.nextLine();

		if (map.findSettlement(name, false) == null) { //if the settlement entered doesn't already exist, then continue
			System.out.println("Please enter your Settlement's population:");
			population = in.nextInt();
			in.nextLine();
			settlementType = askForSettlementType();

			map.addSettlement(new Settlement(name, population, settlementType));

		}
		else{
			System.out.println("That settlement already exists.");
		}
	}

	/**
	 * Deletes a settlement. 
	 * Gets the settlement name, checks to see if it exists, asks the user if they are sure
	 * and then calls the method in the map to delete them, passing it the information.
	 */
	private void deleteSettlement() {
		Settlement settlement;
		String name;

		System.out.println("Please enter the Settlement's Name");
		name = in.nextLine();
		settlement = map.findSettlement(name, true);
		if (settlement != null) {
			if (areYouSure()) {
				map.deleteSettlement(settlement);
			}
		}
	}

	/**
	 * Creates a Road.
	 * Gets the road details, and calls the method in Map to add it, passing it the information.
	 */
	private void createRoad() {
		String name, settlementName;
		Classification classification;
		Settlement sourceSettlement, destinationSettlement;
		double length;

		System.out.println("Please enter your Road's name:");
		name = in.nextLine();

		classification = askForRoadClassifier();

		System.out.println("Please enter your Road's source Settlement's name:");
		settlementName = in.nextLine();
		sourceSettlement = map.findSettlement(settlementName, true);

		if (sourceSettlement != null) {
			System.out.println("Please enter your Road's destination Settlement's name:");
			settlementName = in.nextLine();
			destinationSettlement = map.findSettlement(settlementName, true);

			if (destinationSettlement != null) {
				System.out.println("Please enter your Road's length:");
				length = in.nextDouble();
				in.nextLine();
				map.addRoad(new Road(name, classification, sourceSettlement, destinationSettlement, length));
			}
		}

	}

	/**
	 * Deletes a road. 
	 * Gets the road name, source and destination settlement,
	 * checks to see if they exist, asks the user if they are sure
	 * and then calls the method in the map to delete them, passing it the information.
	 */
	private void deleteRoad() {
		Road road = null;
		String name, settlementName;
		Settlement source, destination;

		System.out.println("Please enter the Road's name: ");
		name = in.nextLine();
		System.out.println("Please enter the Road's source Settlement's name");
		settlementName = in.nextLine();
		source = map.findSettlement(settlementName, true);
		if (source != null) {
			System.out.println("Please enter the Road's destination Settlement's name");
			settlementName = in.nextLine();
			destination = map.findSettlement(settlementName, true);
			if (destination != null) {
				road = map.findRoad(name, source, destination);
				if (road != null) {
					if (areYouSure()) {
						map.deleteRoad(road);
					}
				} else {
					System.out.println("The map does not contain the Road: " + name + ", running through "
							+ source.getName() + " to " + destination.getName());
				}
			}
		}

	}
	
	/**
	 * Displays the map
	 */
	private void displayMap() {
		map.display();
	}

	/**
	 * Asks the user if they are sure they want to delete something
	 * Gets their answer
	 * @return a Boolean (True=Yes, False=No)
	 */
	private boolean areYouSure() {
		String userIn = "";

		while (!(userIn.equals("Y") || userIn.equals("N"))) {
			System.out.println("Are you sure you want to delete this? y/n");
			userIn = in.nextLine();
			userIn = userIn.toUpperCase();
			switch (userIn) {
			case "Y":
				return true;
			case "N":
				return false;
			default:
				System.out.println("Invalid input. Please enter Y or N");
				break;
			}
		}
		return false;
	}

	/**
	 * Runs the code!
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String args[]) throws FileNotFoundException {
		String choice = "";
		Application app = new Application();
		app.load();
		app.runMenu();
		
		
		System.out.println("Would you like to save? (y/n)");
		
		while (!(choice.equals("Y") || choice.equals("N"))) {
			choice = app.in.nextLine();
			choice = choice.toUpperCase();
			switch (choice) {
			case "Y":
				app.save();
			case "N":
				break;
			default:
				System.out.println("Invalid input. Please enter Y or N");
				break;
			}
		}
		app.in.close();

	}

}
