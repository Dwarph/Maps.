import java.util.ArrayList;


/**
 * Represents a road that is linked to two settlements: source and destination.
 * 
 * @author Chris Loftus
 * @version 1.0 (20th February 2016)
 * 
 * @author Pip Turner
 * @version 2.0 (15th February 2016)
*/
public class Settlement {
	private String name;
	private int population;
	private SettlementType type;
	private ArrayList<Road> roads;

	public Settlement() {
		roads = new ArrayList<>();
	
	}

	/**
	 * Constructor to build a settlement
	 * 
	 * @param nm The name of the settlement
	 */
	public Settlement(String name, int population, SettlementType type) {
		this(); // Means call the other constructor
		this.name = name;
		this.type = type;
		this.population = population;
	}

	/**
	 * The name of the settlement. Note that there is no way to change the name
	 * once created.
	 * 
	 * @return The name of the settlement.
	 */
	public String getName() {
		return name;
	}

	/**
	 * The current population
	 * 
	 * @return The population
	 */
	public int getPopulation() {
		return population;
	}

	/**
	 * Change the population size
	 * 
	 * @param size
	 *            The new population size
	 */
	public void setPopulation(int size) {
		this.population = size;
	}

	/**
	 * The kind of settlement, e.g. village, town etc
	 * 
	 * @return The kind of settlement
	 */
	public SettlementType getKind() {
		return type;
	}

	/**
	 * The population has grown or the settlement has been granted a new status
	 * (e.g. city status)
	 * 
	 * @param kind
	 *            The new settlement kind
	 */
	public void setKind(SettlementType kind) {
		this.type = kind;
	}

	/**
	 * Add a new road to the settlement.
	 * 
	 * @param road
	 *            The new road to add.
	 * @throws IllegalArgumentException
	 *             if the settlement already contains the road
	 */
	public void add(Road road) throws IllegalArgumentException {

		for (Road r : roads) {
			if (r.equals(road)) {
				throw new IllegalArgumentException("Road is already connected to "+ road.getName());
			}

		}
		roads.add(road);

	}

	/**
	 * Returns a ArrayList of Roads that match the given name
	 * 
	 * @param name
	 *            The name of the road to find
	 * @return An ArrayList of Road objects (will be a maximum of two items for
	 *         any settlements: e.g. A487 goes from Aber to Penparcau and from
	 *         Aber to Bow Street
	 */
	public ArrayList<Road> findRoads(String name) {
		ArrayList<Road> roadsFound = new ArrayList<>();

		// INSERT CODE HERE
		return roadsFound;
	}

	/**
	 * Deletes all the roads attached to this settlement. It will also detach
	 * these roads from the settlements at the other end of each road
	 */
	public void deleteRoads() {

		for (Road r : roads) {
			delete(r);
		}
		roads.clear();
	}

	/**
	 * Deletes the given road attached to this settlement. It will also detach
	 * this road from the settlements at the other end of the road
	 * 
	 * @param road
	 *            The road to delete.
	 * @throws IllegalArgumentException
	 *             is thrown if the Road is not connected to this settlement
	 */
	public void delete(Road road) throws IllegalArgumentException {
		roads.remove(road);
		/*
		 * treats the settlement instance as the source of a road.
		 * If it is, then it deletes the road from the settlement.
		 * If it isn't, then the road has just been
		 * passed from the source which means that it has already been detached
		 * there and that we are currently in the destination
		 */
		if (road.getDestinationSettlement() != this) {
			road.getDestinationSettlement().delete(road);
		}
	}

	/**
	 * Returns a list of all the roads connected to this settlement
	 * 
	 * @return The roads attached to this settlement
	 */
	public ArrayList<Road> getAllRoads() {
		// Notice how we create a separate array list object
		// and return that instead of the roads. This is so
		// that we don't break encapsulation and data hiding.
		// If I returned roads, then the calling code could change
		// change it directly which would be dangerous
		ArrayList<Road> result = new ArrayList<>();
		for (Road rd : roads) {
			result.add(rd);
		}
		return result;
	}
	
	/**
	 * Checks to see if a settlement is equal to another
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Settlement other = (Settlement) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * The state of the settlement including information about connected roads
	 * 
	 * @return The data in the settlement object.
	 */
	public String toString() {
		String result, sources="", destinations = "", sourceName;
		for(Road r:roads){
			sourceName= r.getSourceSettlement().getName();
			if(sourceName.equals(name)){
				destinations += "\n  "+ r.getDestinationSettlement().getName() +" using the " + r.getName();
			}else{
				sources +="\n  "+ sourceName +" using the " + r.getName();
			}
			
		}
		
		if(sources.equals("")){
			sources="\n There are no settlements listed to get to.";
		}else{
			sources = "\n You can get to " + name + " from: " + sources;
		}
		
		if(destinations.equals("")){
			destinations="\n There are no settlements listed to get here from.";
		}else{
			destinations= "\n From " + name + ", you can get to:" + destinations;
		}
		
		 result = "\n\n" + name + ". \n" +
		 name + " is a " + type.toString() + " with a population of " + population+ "." + sources + destinations;
		return result;
	}

}
