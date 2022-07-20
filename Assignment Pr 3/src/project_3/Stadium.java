/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * Ahmad Dzaki Bin Osman, 21007915, 11 Jun 2022 7:18:14 pm
 */

package project_3;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author ahmad
 *
 */
public class Stadium extends JoggingSpot implements Unavailability{

	private LocalTime closeTime;

	public Stadium(String id, String name, String category, LocalTime closeTime) {
		super(id, name, category);
		this.closeTime = closeTime;
	}

	/**
	 * @return the closeTime
	 */
	public LocalTime getCloseTime() {
		return closeTime;
	}

	@Override
	public String announceUnavailability(String id) {
		String output = String.format("%-20s %s \n", "UNAVAILABLE DATE","EVENT");
		boolean haveDates = false;
		try {
			String connectionString = "jdbc:mysql://localhost:3306/assignment_pr_3";
			String userid = "root";
			String password = "";

			DBUtil.init(connectionString, userid, password);
			String sql = "SELECT * FROM unavailability_date WHERE ID = '" + id + "'";
			ResultSet rs = DBUtil.getTable(sql);
			
			while (rs.next()) {
				LocalDate date = rs.getDate("DateUnavailable").toLocalDate();
				String event = rs.getString("Event");
				if (dateWithinRange(date)) {
					output += String.format("%-20s %s \n",date,event);
					haveDates = true;
				}
			}

			DBUtil.close();

			
		}catch (SQLException se) {
			System.out.println("Error: " + se.getMessage());
		}

		if (haveDates == true) {
			return output;
		}
		else {
			return "There are no dates booked within the next two weeks.";
		}
		
	}
	private boolean dateWithinRange(LocalDate a) {
		boolean within = false;
		LocalDate today = LocalDate.now();
		LocalDate twoWeeksLater = today.plusDays(14);
		if (a.isBefore(twoWeeksLater) && (a.isAfter(today) || a.equals(today)))
// this is used for testing:
//		if (a.isAfter(today))
			within = true;
		
		return within;
	}
	public String display() {
		String outputStadium = "";
		outputStadium += String.format("\n%-10s %-30s %-25s %-30s\n","ID","NAME","CATEGORY", "CLOSING TIME");
		outputStadium += Helper.line(80, "=") + "\n";
		outputStadium += String.format("%-10s %-30s %-25s %-30s\n ", getId(),getName(),getCategory(),getCloseTime());
		
		return outputStadium;
	}
	
}
