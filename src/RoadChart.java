import java.util.ArrayList;
/**
 * Displays a group of roads given to it as a chart
 * @author Pip Turner
 * @version 2.0 (15th February 2016)
 */
public class RoadChart {
	
	/**
	 * Arbitrary Constructor
	 */
	public RoadChart() {

	}
	
	/**
	 * Displays the roads given to it in a chart
	 * Each settlement has its information displayed, with the settlement it links to
	 * @param roads
	 */
	public void displayRoadChart(ArrayList<Road> roads) {
		Road finalRoad = roads.get(roads.size()-1);
		String spaces;
		String[] names = new String[3];
		int i = roads.size();
		for (Road r : roads) {
			i--;
			names[0] = r.getSourceSettlement().getName();
			names[1] = Integer.toString(r.getSourceSettlement().getPopulation());
			names[2] = r.getSourceSettlement().getKind().name();
			spaces = displaySettlement(names);
			if (i >= 0) {
				displayRoad(r.getLength(), spaces);
			}
		}
		names[0] = finalRoad.getDestinationSettlement().getName();
		names[1] = Integer.toString(finalRoad.getDestinationSettlement().getPopulation());
		names[2] = finalRoad.getDestinationSettlement().getKind().name();
		spaces = displaySettlement(names);
		
	}

	/**
	 * Displays a length of road given to it
	 * Each |   | represents 1 mile (the distance given to it is rounded to the nearest mile)
	 *      | | |
	 *      |   |
	 * @param lengthD
	 * @param spaces
	 */
	private void displayRoad(double lengthD, String spaces) {
		Long longLength = Math.round(lengthD);
		int length = Integer.valueOf(longLength.intValue());
		for (int i = 0; i < length; i++) {

			System.out.println(spaces + "|     |");

			if (i == length / 2) {
				System.out.println(spaces + "|  |  |" + " (" + lengthD + " miles)");
			} else {
				System.out.println(spaces + "|  |  |");
			}

			System.out.println(spaces + "|     |");

		}
	}
	
	/**
	 * Displays a settlement
	 * Takes information about a settlement and displays it in a box.
	 * The box's width is dependent on the biggest string in it
	 * @param names
	 * @return
	 */
	private String displaySettlement(String[] names) {
		names[0] = "Name: " + names[0];
		names[1] = "Population: " + names[1];
		names[2] = "Settlement Kind: " + names[2];
		String spaces = "";
		int longestName = 0;
		for (int i = 0; i < 3; i++) {
			if (names[i].length() > longestName) {
				longestName = names[i].length();
			}
		}
		System.out.print(" ");
		for (int i = 1; i < longestName + 1; i++) {
			System.out.print("_");
		}
		System.out.print("\n|");
		for (int i = 0; i < longestName; i++) {
			System.out.print(" ");
		}
		for (int i = 0; i < 3; i++) {
			System.out.print("|\n|" + names[i]);
			for (int j = 0; j < longestName - names[i].length(); j++) {
				System.out.print(" ");
			}
		}
		System.out.print("|\n|");
		for (int i = 0; i < longestName; i++) {
			System.out.print("_");
		}
		System.out.print("|\n");
		for (int i = 0; i < ((longestName + 2) / 2) - 4; i++) {
			spaces += " ";
		}
		return spaces;
	}
}
