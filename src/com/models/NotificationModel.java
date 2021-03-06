package com.models;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;




//observer pattern
public abstract class NotificationModel {
	
	public Integer user = 0;
	public Integer NotfID=0;
	public String notificationText="";
	public Integer postID=0;
	
	
	
	
	public Integer getPostID() {
		return postID;
	}

	public void setPostID(Integer postID) {
		this.postID = postID;
	}

	public Integer getUser() {
		return user;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

	public Integer getNotfID() {
		return NotfID;
	}

	public void setNotfID(Integer notfID) {
		NotfID = notfID;
	}

	public String getNotificationText() {
		return notificationText;
	}

	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}
	
	public abstract void addUserID(Integer notifierID);
	
	public abstract void deattach(Integer notifierID);
	
	public abstract Integer getnumberofNotification(Integer userID);

	
	public abstract void notifyUser();
	

	public abstract void  addNotificationText(Integer fromID,Integer toID,Integer postID);
	
	public ArrayList<Integer> toID(int postID){
		String Sql="Select FromID FROM `notification` WHERE `notification`.postID =  ?";
		Connection conn=DBConnection.getActiveConnection();
		ArrayList<Integer> toid=new ArrayList<Integer>();
		PreparedStatement stmt;
		try{
			stmt=conn.prepareStatement(Sql);
			stmt.setInt(1, postID);
			ResultSet rs=stmt.executeQuery();
			while(rs.next())
			{
				toid.add(rs.getInt(1));
			}
			return toid;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public static ArrayList<NotificationModel> getNotificationText(Integer UserID){
		
		
			String sql = "SELECT `notfID`,  `FromID`,  `txt` , 'postID' FROM `notification` "
					+ "WHERE `seen`=0 AND `toID`=?";
			Connection conn = DBConnection.getActiveConnection();
			PreparedStatement stmt;
			ArrayList<NotificationModel> notf=new ArrayList<NotificationModel>();
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, UserID);
				//System.out.println(stmt);
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()) {
				
					Comment temp=new Comment("");
					temp.NotfID=rs.getInt(1);
					temp.user=rs.getInt(2);
					temp.notificationText=rs.getString(3);
					temp.postID =rs.getInt(4);
					notf.add(temp);
					
					temp.updateSeenofNotification(temp.NotfID);
					updatenumberofNotification(UserID);
				}
				//System.out.println("OKai");
				return notf;

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}

	private static void updatenumberofNotification(int UserID) {
		// TODO Auto-generated method stub
		String sql = "UPDATE `users` SET `numberofnotification` = '0' WHERE `users`.`id` = ?";
		Connection conn = DBConnection.getActiveConnection();
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, UserID);
			
			ResultSet rs = stmt.executeQuery();
	}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
}


