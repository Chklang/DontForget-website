package fr.chklang.dontforget.helpers;

import play.mvc.Http.Session;
import fr.chklang.dontforget.business.User;

public class SessionHelper {
	
	private static final String USER_ID_KEY = "userId";

	public static void clearPlayerId(Session pSession) {
		pSession.remove(USER_ID_KEY);
	}
	
	public static void setPlayerId(Session pSession, User pUser) {
		pSession.put(USER_ID_KEY, Integer.toString(pUser.getIdUser()));
	}
	
	public static void setPlayerId(Session pSession, int pPlayerId) {
		pSession.put(USER_ID_KEY, Integer.toString(pPlayerId));
	}
	
	public static int getUserId(Session pSession) {
		return Integer.parseInt(pSession.get(USER_ID_KEY));
	}
	
	public static boolean hasPlayerId(Session pSession) {
		return pSession.containsKey(USER_ID_KEY);
	}

}
