package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Comment extends NotificationModel {
	public Comment(String txt) {
		notificationText = txt;
	}

	public void addUserID(Integer notifierID) {
		user = notifierID;
	}

	public void deattach(Integer notifierID) {
		user = 0;

	}

	public Integer getnumberofNotification(Integer userID) {
		try {
			Connection connection = DBConnection.getActiveConnection();
			String sql = "Select numberofnotification from users where `id` = ? ";
			PreparedStatement stmt;
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, userID);
			ResultSet rs = stmt.executeQuery();
			Integer number = 0;
			if (rs.next())
				number = rs.getInt(1);
			return number;
		}

		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public void notifyUser() {
		// for all user table count increment by one
		Connection connection = DBConnection.getActiveConnection();

		int number = this.getnumberofNotification(user);
		String sql = "UPDATE `users` SET `numberofnotification` = ? WHERE `id` = ?";
		try {
			PreparedStatement stmt;
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, (number + 1));
			stmt.setInt(2, user);// .get(i));
			System.out.println(stmt.toString());
			stmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// }


	public void addNotificationText(Integer fromID, Integer toID,Integer postID) {
		// add to the table notification user id and notification id and sender
		// id and text
		String sql = "INSERT INTO `notification`(`notfID`, `toID`, `FromID`,`postID`,`seen`, `type` `text`)"
				+ " VALUES (NULL,?,?,?,?,?)";
		Connection conn = DBConnection.getActiveConnection();
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, toID);
			stmt.setInt(2, fromID);
			stmt.setInt(3, postID);
			stmt.setInt(4, 0);// Zero inducate comment type ...
			stmt.setInt(5, 0);// Zero inducate unseen...
			stmt.setString(6, notificationText);
			System.out.println(stmt.toString());
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		notifyUser();
	}

	public void updateSeenofNotification(int ID)
	{
		String sql = "UPDATE `notification` SET"
				+ "`seen`=1 WHERE `NotfID`= ?";
		Connection conn = DBConnection.getActiveConnection();
		PreparedStatement stmt;
		//ArrayList<NotificationModel> notf=new ArrayList<NotificationModel>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, ID);
			
			stmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public String toString() {
		return notificationText;
		
	}




}
