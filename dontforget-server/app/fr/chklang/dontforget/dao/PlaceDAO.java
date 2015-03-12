package fr.chklang.dontforget.dao;

import java.util.List;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.Place;
import fr.chklang.dontforget.business.User;

public class PlaceDAO extends Finder<Integer, Place> {

	public PlaceDAO() {
		super(Integer.class, Place.class);
	}
	
	public List<Place> findByUser(User pUser) {
		return this.where().eq("user", pUser).findList();
	}
	
	public Place findByPlaceAndUser(User pUser, String pPlace) {
		return this.where().eq("user", pUser).eq("place", pPlace).findUnique();
	}
}
