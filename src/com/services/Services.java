package com.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.models.DBConnection;
import com.models.NotificationModel;
import com.models.UserModel;
import com.models.comment;

@Path("/")
public class Services {



	@POST
	@Path("/signup")
	@Produces(MediaType.TEXT_PLAIN)
	public String signUp(@FormParam("name") String name,
			@FormParam("email") String email, @FormParam("pass") String pass) {
		UserModel user = UserModel.addNewUser(name, email, pass);
		JSONObject json = new JSONObject();
		json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		json.put("pass", user.getPass());
		json.put("lat", user.getLat());
		json.put("long", user.getLon());
		return json.toJSONString();
	}

	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@FormParam("email") String email,
			@FormParam("pass") String pass) {
		UserModel user = UserModel.login(email, pass);
		JSONObject json = new JSONObject();
		json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		json.put("pass", user.getPass());
		json.put("lat", user.getLat());
		json.put("long", user.getLon());
		return json.toJSONString();
	}
	
	@POST
	@Path("/updatePosition")
	@Produces(MediaType.TEXT_PLAIN)
	public String updatePosition(@FormParam("id") String id,
			@FormParam("lat") String lat, @FormParam("long") String lon) {
		Boolean status = UserModel.updateUserPosition(Integer.parseInt(id), Double.parseDouble(lat), Double.parseDouble(lon));
		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return json.toJSONString();
	}

	
	@POST
	@Path("/follow")
	@Produces(MediaType.TEXT_PLAIN)
	public String follow(@FormParam("followerID") String followerID, @FormParam("followedID") String followedID){
		
		Boolean status = UserModel.follow(Integer.parseInt(followerID), Integer.parseInt(followedID));
		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return json.toJSONString();
	}
	
	@POST
	@Path("/getUserPosition")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserPosition(@FormParam("id") String userID ){
	
		double lat = UserModel.getLatById(Integer.parseInt(userID));
		double lon = UserModel.getLonById(Integer.parseInt(userID));
		JSONObject json = new JSONObject();
		json.put("lat", lat);
		json.put("long", lon);
		return json.toJSONString();
	}

	@POST
	@Path("/unfollow")
	@Produces(MediaType.TEXT_PLAIN)
	public String unfollow(@FormParam("followerID") String followerID, @FormParam("followedID") String followedID) {
		Boolean status = UserModel.unfollow(Integer.parseInt(followerID), Integer.parseInt(followedID)); 

		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return json.toJSONString();
	}
	
	@POST 
	@Path("/getFollowers")
	@Produces(MediaType.TEXT_PLAIN)
	public String getFollowers(@FormParam("userID")Integer id)
	{
		JSONObject jsons=new JSONObject();
		ArrayList<UserModel> followers = new ArrayList<>(UserModel.getFollowersIDs(id)) ;
		if(followers.size()!=0){
			/*for(int i=0;i<followedby.length;i++)
			{
			UserModel userfollowedby = UserModel.dataFollower(followedby[i]);
			jsons.put("id["+i+"]", userfollowedby.getId());
			jsons.put("name["+i+"]", userfollowedby.getName());
			jsons.put("email["+i+"]", userfollowedby.getEmail());
			}*/
			System.out.println("Services.getFollowers()" + followers.toString());
			

		    JSONArray jsArray = new JSONArray();
		    JSONObject jObject = new JSONObject();
			    for (UserModel user : followers)
			    {
			         JSONObject userJson = new JSONObject();
			         userJson.put("id", user.getId());
			         userJson.put("name", user.getName());
			         userJson.put("pass", user.getPass());
			         userJson.put("email", user.getEmail());
			         userJson.put("lat", user.getLat());
			         userJson.put("long", user.getLon());
			         
			         jsArray.add(userJson);
			    }
			    jObject.put("followersList", jsArray);
			
			return jObject.toJSONString();
		}
		else {
			jsons.put("String", "nodata");
			return jsons.toJSONString();
		}
	}
	
	@POST 
	@Path("/getAllUsers")
	@Produces(MediaType.TEXT_PLAIN)
	public String getFollowers()
	{
		JSONObject jsons=new JSONObject();
		ArrayList<UserModel> users = new ArrayList<>(UserModel.getAllUsers()) ;
		if(users.size()!=0){
			System.out.println("Services.getAllUsers()" + users.toString());
			

		    JSONArray jsArray = new JSONArray();
		    JSONObject jObject = new JSONObject();
			    for (UserModel user : users)
			    {
			         JSONObject userJson = new JSONObject();
			         userJson.put("id", user.getId());
			         userJson.put("name", user.getName());
			         userJson.put("pass", user.getPass());
			         userJson.put("email", user.getEmail());
			         userJson.put("lat", user.getLat());
			         userJson.put("long", user.getLon());
			         
			         jsArray.add(userJson);
			    }
			    jObject.put("userList", jsArray);
			
			return jObject.toJSONString();
		}
		else {
			jsons.put("String", "nodata");
			return jsons.toJSONString();
		}
	}
	
	@POST 
	@Path("/getFollowedBy")
	@Produces(MediaType.TEXT_PLAIN)
	public String getFollowedBy(@FormParam("userID")Integer id)
	{
		JSONObject jsons=new JSONObject();
		ArrayList<UserModel> followedByUser = new ArrayList<>(UserModel.getFollowedBy(id)) ;

		if(followedByUser.size() > 0){
			JSONArray jsArray = new JSONArray();
			JSONObject jObject = new JSONObject();
			for (UserModel user : followedByUser)
			{
				JSONObject userJson = new JSONObject();
				userJson.put("id", user.getId());
				userJson.put("name", user.getName());
				userJson.put("pass", user.getPass());
				userJson.put("email", user.getEmail());
				userJson.put("lat", user.getLat());
				userJson.put("lon", user.getLon());

				jsArray.add(userJson);
			}
			jObject.put("followedByUser" , jsArray);

			return jObject.toJSONString();
		}
		else {
			jsons.put("String", "empty set");
			return jsons.toJSONString();
		}
	}
	
	@POST 
	@Path("/getsavedplaces")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSavedPlace(@FormParam("userID")Integer id)
	{
		
		JSONObject jsons=new JSONObject();
		ArrayList<Integer> UserSavedPlaces = new ArrayList<>(UserModel.getsavePlace(id)) ;

		if(UserSavedPlaces.size() > 0){
			JSONArray jsArray = new JSONArray();
			JSONObject jObject = new JSONObject();
			for (Integer user : UserSavedPlaces)
			{
				JSONObject userJson = new JSONObject();
				userJson.put("Place id: ", user);
				
				jsArray.add(userJson);
			}
			jObject.put("UserSavedPlaces:" , jsArray);

			return jObject.toJSONString();
		}
		else {
			jsons.put("String", "empty set");
			return jsons.toJSONString();
		}
	}
	@POST 
	@Path("/saveplaces")
	@Produces(MediaType.TEXT_PLAIN)
	public String SavePlace(@FormParam("userID")Integer id,@FormParam("placeID") Integer placeid)
	{
		Boolean status = UserModel.savePlace(id,placeid);
		JSONObject json = new JSONObject();
		json.put("status", status ? "Done sucessfully" : "Failed to add");
		return json.toJSONString();
		
	}
	
	@POST 
	@Path("/numberofnotification")
	@Produces(MediaType.TEXT_PLAIN)
	public String getnumberofnotification(@FormParam("userID")Integer ID)
	{
		JSONObject jsons=new JSONObject();
		NotificationModel not1=new comment("Hello");
		int number=not1.getnumberofNotification(ID);
		//System.out.println(password);
		if(number!=0){
		//JSONObject userJson = new JSONObject();
		jsons.put("you have ", (number+" notification "));
		//NotificationModel.addUserID(ID);
		//NotificationModel.notifyUser();
					return jsons.toJSONString();
		}
		else {
			jsons.put("you have", "No notification");
			return jsons.toJSONString();
		}
	}
	@POST
	@Path("/sendnotification")
	@Produces(MediaType.TEXT_PLAIN)
	public String makenote(@FormParam("fromID")Integer fromID,@FormParam("toID")Integer toID,@FormParam("txt")String commnt)
	{
		JSONObject jsons=new JSONObject();
		NotificationModel notification1=new comment(commnt);
		
		int number=notification1.getnumberofNotification(toID);
		notification1.addUserID(toID);
		
		notification1.addNotificationText(fromID, toID);
		
		jsons.put("you have ", (number+" notification "));
		
		return jsons.toJSONString();	
	}
	
	@POST 
	@Path("/resetpassword")
	@Produces(MediaType.TEXT_PLAIN)
	public String getpassword(@FormParam("userEmail")String email)
	{
		JSONObject jsons=new JSONObject();
	
		String password=UserModel.restorePassword(email);
		if(password!=null){
		
		jsons.put("password", password);
					return jsons.toJSONString();
		}
		else {
			jsons.put("String", "empty set");
			return jsons.toJSONString();
		}
	}

	

	@GET
	@Path("/")
	@Produces(MediaType.TEXT_PLAIN)
	public String getJson() {
		return "Hello after editing";

	}
	

	

}

