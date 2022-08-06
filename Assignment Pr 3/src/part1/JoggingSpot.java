/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * Ahmad Dzaki Bin Osman, 21007915, 11 Jun 2022 8:24:47 pm
 */

package part1;

/**
 * @author ahmad
 *
 */
public abstract class JoggingSpot {

	private String id;
	private String name;
	private String category;
	
	public JoggingSpot(String id, String name, String category) {
		this.id = id;
		this.name = name;
		this.category = category;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	
	public abstract String display();

}
