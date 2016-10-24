import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Chris Loftus
 * @version 1.0 (25th February 2016)
 *
 * @author Pip Turner
 * @version 2.0 (15th February 2016)
 */

public class Map {
	private ArrayList<Settlement> settlements;
	private ArrayList<Road> roads;
	private ArrayList<ArrayList<Road>> fullyExpandedRoads; //list of lists of sorted roads
	private String[] fileNames;


	/**
	 * Constructor for map. Initialised various values
	 */
	public Map() {
		settlements = new ArrayList<>();
		roads = new ArrayList<>();
		fullyExpandedRoads = new ArrayList<>();
		fileNames = new String[2];
		fileNames[0] = "";
		fileNames[1] = "";
	}

	/**
	 * Displays the map Calls the to string, depending on the user's choice Also
	 * calls displayRoadChart
	 */
	public void display() {
		String choice;
		Scanner in = new Scanner(System.in);
		System.out.println("1. Display Settlements" +
						"\n2. Display Roads" + 
						"\n3. Display Settlements and Roads" +
						"\n4. Display Road as Chart");
		choice = in.nextLine();
		switch (choice) {
		case "1":
			System.out.println(toString(1));
			break;
		case "2":
			System.out.println(toString(2));
			break;
		case "3":
			System.out.println(toString(3));
			break;
		case "4":
			displayRoadChart(in);
			break;
		default:
			System.out.println("Incorrect input, please enter a number from 1 to 4.");
			break;
		}
	}

	/**
	 * Gets the file names Asks the user to either continue with the current
	 * filenames or input new ones
	 */
	@SuppressWarnings("resource")
	public void getFilenames() {

		Scanner in = new Scanner(System.in);
		String choice;
		boolean inputNames = true, correctChoice = false;
		if (!(fileNames[0].equals("") && fileNames[1].equals(""))) { // if there filesnames sorted, check to see if the user is happy with them or not
					System.out.println( "The current Settlement file name is: " + fileNames[0] + 
										"\nThe current Roads file name is: "+ fileNames[1] + 
											"\n 1. Continue with these names" + 
											"\n 2. Input new names");

			choice = in.nextLine();
			while (!correctChoice) {
				switch (choice) {
				case "1":
					inputNames = false;
					correctChoice = true;
					break;
				case "2":
					inputNames = true;
					correctChoice = true;
					break;
				default:
					System.out.println("Incorrect input. Please enter 1 or 2.");
					break;
				}
			}
		}
		if (inputNames) { // if the user wants to, or if there are no file
							// names, input some!
			System.out.println("Please enter the Settlements filename (Default: \"settlements.txt\"):");
			fileNames[0] = in.nextLine();
			System.out.println("Please enter the Roads filename (Default: \"roads.txt\"):");
			fileNames[1] = in.nextLine();
		}

	}

	/**
	 * Adds a settlement to the arrayList
	 * 
	 * @param newSettlement
	 * @throws IllegalArgumentException
	 */
	public void addSettlement(Settlement newSettlement) throws IllegalArgumentException {
		settlements.add(newSettlement);
	}

	/**
	 * Deletes a settlement Removes it from the arraylist, along with any
	 * associated roads
	 * 
	 * @param settlement
	 */
	public void deleteSettlement(Settlement settlement) {

		settlements.remove(settlement);

		for (int i = 0; i < roads.size(); i++) {
			Road r = roads.get(i);
			if (r.getDestinationSettlement().equals(settlement) || r.getSourceSettlement().equals(settlement)) { // if a settlemend is equal to the one we're looking for, delete the correct road
				for (int j = 0; i < settlements.size(); j++) {
					if (settlements.get(j).equals(r.getDestinationSettlement())) {
						settlements.get(j).delete(r);
					}
				}
				roads.remove(r);
			}
		}
	}

	/**
	 * Gets the settlements
	 * 
	 * @return settlements
	 */
	public ArrayList<Settlement> getSettlements() {
		return settlements;
	}

	/**
	 * Adds a road to the arrayList
	 */
	public void addRoad(Road road) {
		roads.add(road);
	}

	/**
	 * Deletes a road Removes it from the arrayList and any settlements it was
	 * connected to
	 * 
	 * @param road
	 */
	public void deleteRoad(Road road) {
		for (int i = 0; i < settlements.size(); i++) { // loop through all
														// settlements
			for (Road r : settlements.get(i).getAllRoads()) { // loop through
																// all roads
																// within that
																// settlement
				if (road.equals(r)) { // if the road is equal to the road passed
										// to it, delete it from the settlement
					settlements.get(i).delete(r);
				}
			}
		}
		roads.remove(road);
	}

	/**
	 * Loads Settlements and Roads to the appropriate text file Loads
	 * settlements before roads, as roads can't be loaded without settlements to
	 * connect them to
	 * 
	 * @throws FileNotFoundException
	 */
	public void load() throws FileNotFoundException {
		try (Scanner load = new Scanner(new FileReader(fileNames[0]));) { // load settlements																			
			load.useDelimiter(":|\r?\n|\r");
			loadSettlements(load);
			System.out.println("Finished loading Settlements from " + fileNames[0]);
		}
		try (Scanner load = new Scanner(new FileReader(fileNames[1]))) { // load roads																			
			load.useDelimiter(":|\r?\n|\r");
			loadRoads(load);
			System.out.println("Finished loading Roads from " + fileNames[1]);
		}
	}

	/**
	 * Saves Settlements and Roads to the appropriate text file
	 */
	public void save() {
		saveSettlements(fileNames[0]); // saves settlements
		System.out.println("Finished saving Settlements to " + fileNames[0]);
		saveRoads(fileNames[1]); // saves roads
		System.out.println("Finished saving Roads to " + fileNames[1]);
	}

	/**
	 * getFullRoads is a recursive method who's function is to: Group roads
	 * together Sort them into order by settlement
	 * 
	 * @param listOfRoads
	 * @return
	 */
	public String getFullRoads(ArrayList<Road> roadsPassed) {
	
		ArrayList<Road> listOfRoads = new ArrayList<Road>(roadsPassed);
		ArrayList<Road> tempRoads = new ArrayList<>();
		ArrayList<Road> sortedRoads = new ArrayList<>();
		String nameID, sourceName, destinationName;
		Road templateRoad;
		String result = "";
	
		if (roadsPassed.isEmpty()) { // end condition for recursion - once we
										// empty that list, we're done
			return result;
		}
	
		templateRoad = listOfRoads.get(0); // gets all roads with the same name!
		nameID = templateRoad.getName();
		for (Road r : listOfRoads) {
			if ((r.getName().equals(nameID))) {
				tempRoads.add(r);
			}
		}
	
		for (Road r : tempRoads) { // removes roads to be sorted from the list
									// to be passed
			listOfRoads.remove(r);
	
		}
	
		// initial values for sorting
		sortedRoads.add(tempRoads.get(0));
		tempRoads.remove(0);
	
		// sorts roads into order by destination!
		// Loop through array of roads with same name
		while (tempRoads.size() > 0) {	//loop through until tempRoads is empty
			for (int i = 0; i < tempRoads.size(); i++) { 
				templateRoad = tempRoads.get(i);
				if (tempRoads.get(i).getSourceSettlement()
						.equals(sortedRoads.get(sortedRoads.size() - 1).getDestinationSettlement())) { 	//if the source settlement of tempRoads is equal to the destination settlement of sortedRoads
					sortedRoads.add(sortedRoads.size(), tempRoads.get(i));								//Then add to the end of sortedRoads and remove from temp roads
					tempRoads.remove(templateRoad);
					break;
				} else if (tempRoads.get(i).getDestinationSettlement()									//if the destination settlement of tempRoads is equal to the source settlement of sortedRoads
						.equals(sortedRoads.get(0).getSourceSettlement())) {							//Then add to the start of sortedRoads and remove from temp roads
					sortedRoads.add(0, tempRoads.get(i));
					tempRoads.remove(templateRoad);
					break;
				}
			}
		}
		fullyExpandedRoads.add(sortedRoads);	//add a list of sorted roads to the list of lists of sorted roads
		// build string based on
		result = "\n" + nameID + "." + "\n" + nameID + " is an " + templateRoad.getClassification().name()
				+ "-road, running through: \n";
		for (Road r : sortedRoads) {
			result += "	" + r.getSourceSettlement().getName() + " to " + r.getDestinationSettlement().getName() + "("
					+ r.getLength() + " miles)\n";
		}
		// return the result, plus the other roads.
		return result + getFullRoads(listOfRoads);
	}

	/**
	 * Finds a settlement Used to check if a settlement exists. Returns the
	 * settlement if it does.
	 * 
	 * @param name
	 * @param printNull
	 * @return Settlement
	 */
	public Settlement findSettlement(String name, boolean printNull) {
		for (Settlement s : settlements) {
			if (name.equals(s.getName())) { // if a settlement's name is equal
											// to the name we're trying to find,
											// return the settlement
				return s;
			}
		}
		if (printNull) { // otherwise return null and tell the user it doesn't
							// exist
			System.out.println("The map does not contain the Settlement: " + name);
		}
		return null;
	}

	/**
	 * Finds a road Used to check if a road exists. Returns the road if it does.
	 * 
	 * @param name
	 * @param source
	 * @param destination
	 * @return
	 */
	public Road findRoad(String name, Settlement source, Settlement destination) {
		for (Road r : roads) {
			if (r.getName().equals(name) && r.getSourceSettlement().equals(source) // if a road's name and settlements are equal to the name we're trying to find, return the road																			
					&& r.getDestinationSettlement().equals(destination)) {
				return r;
			}
	
		}
		System.out.println(name + "does not exist");
		return null;
	}

	/**
	 * Saves settlements to the appropriate text file
	 * 
	 * @param fileName
	 */
	private void saveSettlements(String fileName) { // saves in the format
													// "name:population:kind"
		try (PrintWriter save = new PrintWriter(new FileWriter(fileName))) {
			for (Settlement s : settlements) {
				save.println(s.getName() + ":" + s.getPopulation() + ":" + s.getKind().toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves roads to the appropriate text file
	 * 
	 * @param fileName
	 */
	private void saveRoads(String fileName) { // saves in the format
												// "name:classification:length:source:destination"
		try (PrintWriter save = new PrintWriter(new FileWriter(fileName))) {
			for (Road r : roads) {
				save.println(r.getName() + ":" + r.getClassification().toString() + ":" + r.getLength() + ":"
						+ r.getSourceSettlement().getName() + ":" + r.getDestinationSettlement().getName());
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Loads settlements, from the appropriate text file
	 * 
	 * @param load
	 */
	private void loadSettlements(Scanner load) {
		String name, kindString;
		int population;
		SettlementType kind = null;

		while (load.hasNext()) { // while there are still settlements in the
									// file, load them into the array
			Application app = new Application();
			name = load.next();
			population = load.nextInt();
			kindString = load.next();

			try {
				kind = SettlementType.valueOf(kindString);
			} catch (IllegalArgumentException iae) {
				System.out.println(kindString + " is not a settlement kind. Please correct this.");
				kind = app.askForSettlementType();
			}

			settlements.add(new Settlement(name, population, kind));
		}

	}

	/**
	 * Loads roads, checking to see if any of the values are incorrect, whilst
	 * loading
	 * 
	 * @param load
	 */
	private void loadRoads(Scanner load) {
		String name = null, length = null, settlementName;
		Classification classification = null;
		Settlement sourceSettlement = null;
		Settlement destinationSettlement = null;
		boolean makeRoad = true;

		while (load.hasNext()) { // whilst there are still roads to load, load
									// them into the ArrayList
			makeRoad = true;
			name = load.next();

			try {
				classification = Classification.valueOf(load.next());
			} catch (IllegalArgumentException iae) {
				System.out.println("Classification error.");
				makeRoad = false;
			}
			length = load.next();
			settlementName = load.next();

			sourceSettlement = findSettlement(settlementName, true);
			if (sourceSettlement == null) {
				makeRoad = false;
			}

			settlementName = load.next();

			destinationSettlement = findSettlement(settlementName, true);
			if (destinationSettlement == null) {
				makeRoad = false;
			}
			if (makeRoad) {
				roads.add(new Road(name, classification, sourceSettlement, destinationSettlement,
						Double.parseDouble(length)));
			}
		}

	}

	/**
	 * Gets all the settlements as a string that is nice on the eyes
	 * 
	 * @return settlementStrings
	 */
	private String displaySettlements() {
		String settlementStrings = "\n-----Settlements----- ";
		for (Settlement s : settlements) {
			settlementStrings += s.toString();
		}
		return settlementStrings;
	}

	/**
	 * Gets all the roads as a string that is nice on the eyes
	 * 
	 * @return roadStrings
	 */
	private String displayRoads() {
		String roadStrings = "\n\n-----Roads----- \n";
		// fullyExpandedRoads needs to be empty
		if (!(fullyExpandedRoads.isEmpty())) {
			fullyExpandedRoads.clear();
		}
		roadStrings += getFullRoads(roads);
		return roadStrings;
	}

	/**
	 * Displays a road as a chart
	 * 
	 * @param in
	 */
	private void displayRoadChart(Scanner in) {
		String roadName;
		RoadChart roadChart = new RoadChart();
		boolean roadFound=false;
		if (!(fullyExpandedRoads.isEmpty())) { // if fullyExpandedRoads isn't
												// empty, empty it
			fullyExpandedRoads.clear();
		}
		getFullRoads(roads); // group and sort the roads
		System.out.println("Please enter the name of the road you want to display:");
		roadName = in.nextLine();

		for (int i = 0; i < fullyExpandedRoads.size(); i++) {
			if (fullyExpandedRoads.get(i).get(0).getName().equals(roadName)) {
				roadChart.displayRoadChart(fullyExpandedRoads.get(i));
				roadFound=true;
			}
		}
		if(!roadFound){
			System.out.println(roadName + " does not exist");
		}

	}

	/**
	 * Returns a string based on what choice is given to it
	 * 
	 * @param choice
	 * @return
	 */
	public String toString(int choice) {
		String result = "";

		switch (choice) {
		case 1:
			result = displaySettlements();
			break;
		case 2:
			result = displayRoads();
			break;
		case 3:
			result = displaySettlements() + displayRoads();
			break;
		default:
			System.out.println("Incorrect number passed to toString");
			break;
		}

		return result;
	}

}
