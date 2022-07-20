/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * Ahmad Dzaki Bin Osman, 21007915, 11 Jun 2022 7:17:53 pm
 */

package project_3;

/**
 * @author ahmad
 *
 */
public class ParkConnector extends JoggingSpot {

	private double distance;

	public ParkConnector(String id, String name, String category, double distance) {
		super(id, name, category);
		this.distance = distance;
	}

	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}
	
	public String display() {
		String outputPC = "";
		outputPC += String.format("%-10s %-30s %-25s %.2fkm\n", getId(),getName(),getCategory(),getDistance());
		return outputPC;
	}
}
