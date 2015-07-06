package fr.chklang.dontforget.android.activity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import fr.chklang.dontforget.android.R;
import fr.chklang.dontforget.android.business.Configuration;
import fr.chklang.dontforget.android.business.Token;
import fr.chklang.dontforget.android.database.DatabaseManager;
import fr.chklang.dontforget.android.rest.AbstractRest.Result;
import fr.chklang.dontforget.android.rest.TokensRest;
import fr.chklang.dontforget.android.rest.TokensRest.ConnectionStatus;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		DatabaseManager.transaction(this, new DatabaseManager.Transaction() {
			@Override
			public void execute() {
				init();
			}
		});
	}
	
	private void init() {
		//Create device uuid if not already defined
		Configuration lDeviceId = Configuration.dao.get("DEVICE_ID");
		if (lDeviceId == null) {
			// Generate a device id
			lDeviceId = new Configuration();
			lDeviceId.setKey("DEVICE_ID");
			lDeviceId.setValue(UUID.randomUUID().toString());
			Configuration.dao.save(lDeviceId);
		}
		
		Collection<Token> lTokens = Token.dao.getAll();
		//Try to connect to each server

		boolean lHasTokenOk = false;
		Map<Integer, Result<ConnectionStatus>> lTestsConnections = new HashMap<Integer, Result<ConnectionStatus>>();
		for (Token lToken : lTokens) {
			Result<ConnectionStatus> lTestConnection = TokensRest.connexion(lToken.toServerConfiguration(), lToken, lDeviceId.getValue());
			lTestsConnections.put(lToken.getIdToken(), lTestConnection);
		}
		
		int lTokenId = -1;
		for (Entry<Integer, Result<ConnectionStatus>> lTestConnection : lTestsConnections.entrySet()) {
			switch (lTestConnection.getValue().get()) {
			case OK	:
				lHasTokenOk = true;
				break;
			case UNAUTHORIZED:
				lTokenId = lTestConnection.getKey().intValue();
			case FAIL:
				break;
			}
		}
		
		if (!lHasTokenOk) {
			Intent lIntent = new Intent(WelcomeActivity.this, ConnectionActivity.class);
			lIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			lIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			lIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			if (lTokenId > -1) {
				lIntent.putExtra("tokenId", lTokenId);
			}
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
