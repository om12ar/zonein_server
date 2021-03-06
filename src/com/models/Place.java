package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class Place  {

	protected int ID;
	protected String name;
	protected double longitude;
	protected double latitude;
	protected String description;
	protected double rating;
	protected int numberOfCheckins;


	public Place( String name, double longitude, double latitude, String description) {
		super();
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

	public double getRating() {
		getPlaceByID(ID);

		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getNumberOfCheckins() {
		return numberOfCheckins;
	}

	public void setNumberOfCheckins(int numberOfCheckins) {
		this.numberOfCheckins = numberOfCheckins;
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
				place.rating = rs.getDouble("rating");
				place.numberOfCheckins = rs.getInt("checkins"); 
				return place;
			}
			return null;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}

	public static CheckinComment getCommentByID(int id) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select * from comment where `id` = ? ";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				CheckinComment comment = new CheckinComment();
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





	public static boolean setAverageRating (int userID , int placeID , double newRating){

		Place place = getPlaceByID(placeID); 
		Connection conn = DBConnection.getActiveConnection();
		String sql = "Insert into place_rating VALUES  (?,?,?)";
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			stmt.setInt(2, placeID);
			stmt.setDouble(3, newRating);
			stmt.executeUpdate();

			double rating = getAverageRating(placeID);
			String sql2 = "update places set rating = ? where  id = ?";
			PreparedStatement stmt2;
			stmt2 = conn.prepareStatement(sql2);
			stmt2.setDouble(1, rating);
			stmt2.setInt(2, placeID);
			stmt2.executeUpdate();
			return true; 

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return false; 

	}

	public static double getAverageRating(int placeID) {

		Connection conn = DBConnection.getActiveConnection();
		String sql = "select AVG(rating) from `checkin` where placeID = ? ";
		double rating;
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, placeID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				rating = rs.getDouble(1);
				return rating;
			}
		}catch (SQLException e) {

			e.printStackTrace();
		}
		return 0.0; 
	}


	public double getDistance (UserModel user){

		double userLongitude = user.getLon();
		double userLatitude = user.getLat();
		double distance = Math.pow(userLongitude - longitude , 2) +
				Math.pow(userLatitude - latitude, 2);

		return Math.sqrt(distance);
	}

	public static boolean removePlace (int placeID){
		
		Connection conn = DBConnection.getActiveConnection();
		String sql = "delete from places where id = ?";
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, placeID);
			stmt.executeUpdate();
			return true;
		}catch (SQLException e) {

			e.printStackTrace();
		}
		
		return false;
	}



}


