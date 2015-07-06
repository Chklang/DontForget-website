package fr.chklang.dontforget.helpers;

import play.mvc.Http.Session;
import fr.chklang.dontforget.business.Token;
import fr.chklang.dontforget.business.User;

public class SessionHelper {
	
	private static final String USER_ID_KEY = "userId";
	
	private static final String TOKEN_ID_KEY = "token";

	public static void clearPlayerId(Session pSession) {
		pSession.remove(USER_ID_KEY);
	}
	
	public static void clearTokenId(Session pSession) {
		pSession.remove(TOKEN_ID_KEY);
	}
	
	public static void setPlayerId(Session pSession, User pUser) {
		pSession.put(USER_ID_KEY, Integer.toString(pUser.getIdUser()));
	}
	
	public static void setPlayerId(Session pSession, int pPlayerId) {
		pSession.put(USER_ID_KEY, Integer.toString(pPlayerId));
	}
	
	public static void setTokenId(Session pSession, Token pToken) {
		pSession.put(TOKEN_ID_KEY, pToken.getToken());
	}
	
	public static void setTokenId(Session pSession, String pToken) {
		pSession.put(TOKEN_ID_KEY, pToken);
	}
	
	public static int getUserId(Session pSession) {
		return Integer.parseInt(pSession.get(USER_ID_KEY));
	}
	
	public static String getTokenId(Session pSession) {
		return pSession.get(TOKEN_ID_KEY);
	}
	
	public static boolean hasPlayerId(Session pSession) {
		return pSession.containsKey(USER_ID_KEY);
	}
	
	public static boolean hasTokenId(Session pSession) {
		return pSession.containsKey(TOKEN_ID_KEY);
	}

}
