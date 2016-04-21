package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class Place {

	private int ID;
	private String name;
	private double longitude;
	private double latitude;
	private String description;

	public Place(int iD, String name, double longitude, double latitude, String description) {
		super();
		ID = iD;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.description = description;
	}

	public Place() {

	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static Place addPlace(String name, String description, double longitude, double latitude) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Insert into places (`name`,`description`,`lat` ,`long` ) VALUES  (?,?,?,?)";

			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.setString(2, description);
			stmt.setDouble(3, latitude);
			stmt.setDouble(4, longitude);
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				Place place = new Place();
				place.name = name;
				place.ID = rs.getInt(1);
				place.description = description;
				place.longitude = longitude;
				place.latitude = latitude;
				return place;
			}
			return null;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}

	public static Place getPlaceByID(int id) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select * from places where `id` = ? ";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Place place = new Place();
				place.ID = rs.getInt(1);
				place.description = rs.getString("description");
				place.name = rs.getString("name");
				place.latitude = rs.getDouble("lat");
				place.longitude = rs.getDouble("long");
				return place;
			}
			return null;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}
	
	public static checkinComment getCommentByID(int id) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select * from comment where `id` = ? ";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				checkinComment comment = new checkinComment (); 
				comment.ID = rs.getInt(1);
				comment.checkinID = rs.getInt("checkinID");
				comment.comment = rs.getString("comment");
				
				return comment;
			}
			return null;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Place> getAllPlaces() {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "select `id` from places";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			ArrayList<Place> places = new ArrayList<>();

			while (rs.next()) {
				Place place = getPlaceByID(rs.getInt(1));
				places.add(place);

			}
			return places;
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static boolean checkIn(int placeID, int userID, String review) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Insert into checkin (`placeID`,`userID`, `review`) VALUES  (?,?,?)";

			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, placeID);
			stmt.setInt(2, userID);
			stmt.setString(3,review);
			stmt.executeUpdate();

			Place place = Place.getPlaceByID(placeID);
			double newLongitude = place.getLongitude();
			double newLatitude = place.getLatitude();
			UserModel.updateUserPosition(userID, newLatitude, newLongitude);

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;
	}

	public static boolean comment(int checkinID, String comment) {

		Connection conn = DBConnection.getActiveConnection();
		String sql = "Insert into comment (`checkinID`,`comment`) VALUES  (?,?)";
		try {
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, checkinID);
			stmt.setString(2, comment);
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false; 
	}

	public static boolean like (int checkinID){

		Connection conn = DBConnection.getActiveConnection();
		String sql = "update `checkin` set `likes` = `likes` + 1 where `id` = ?";
		try {
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, checkinID);
			stmt.executeUpdate();
			return true; 
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<checkinComment> getComments(int checkinID ) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select * from comment where `checkinID` = ? ";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, checkinID);
			ArrayList <checkinComment> comments = new ArrayList<checkinComment>(); 
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				checkinComment comment = getCommentByID(rs.getInt(1));
				comments.add(comment); 
				return comments; 
				
			}
			return null;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}
	
	
	}

