package fr.chklang.dontforget.android;

import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import fr.chklang.dontforget.android.business.Token;
import fr.chklang.dontforget.android.dao.TokenDAO;
import fr.chklang.dontforget.android.database.DatabaseManager;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		DatabaseManager.initConnection(this); 
		
		init();
	}
	
	private void init() {
		TokenDAO lTokenDAO = new TokenDAO();
		Collection<Token> lTokens = lTokenDAO.getAll();
		
		if (lTokens.isEmpty()) {
			Intent lIntent = new Intent(WelcomeActivity.this, ConnectionActivity.class);
			lIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			lIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);                  
			lIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(lIntent);
		} else {
			Intent lIntent = new Intent(WelcomeActivity.this, TasksActivity.class);
			lIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			lIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);                  
			lIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(lIntent);
		}
	}
}
