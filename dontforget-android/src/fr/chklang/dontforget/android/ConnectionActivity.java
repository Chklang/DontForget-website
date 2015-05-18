package fr.chklang.dontforget.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import fr.chklang.dontforget.android.business.Token;
import fr.chklang.dontforget.android.business.TokenKey;
import fr.chklang.dontforget.android.dao.TokenDAO;
import fr.chklang.dontforget.android.dto.TokenDTO;
import fr.chklang.dontforget.android.helpers.ConfigurationHelper;
import fr.chklang.dontforget.android.rest.AbstractRest.CallbackOnException;
import fr.chklang.dontforget.android.rest.AbstractRest.Result;
import fr.chklang.dontforget.android.rest.TokensRest;

public class ConnectionActivity extends Activity {

	private Spinner connection_protocol;
	private EditText connection_host;
	private EditText connection_port;
	private EditText connection_context;
	private EditText connection_login;
	private EditText connection_password;
	private Button connection_send;
	private Button connection_skip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);

		connection_protocol = (Spinner) this.findViewById(R.id.connection_protocol);
		connection_host = (EditText) this.findViewById(R.id.connection_host);
		connection_port = (EditText) this.findViewById(R.id.connection_port);
		connection_context = (EditText) this.findViewById(R.id.connection_context);
		connection_login = (EditText) this.findViewById(R.id.connection_login);
		connection_password = (EditText) this.findViewById(R.id.connection_password);
		connection_send = (Button) this.findViewById(R.id.connection_send);
		connection_skip = (Button) this.findViewById(R.id.connection_skip);

		connection_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				connection();
			}
		});
		connection_skip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goToTasks();
			}
		});
	}

	private void connection() {
		final String lProtocol = String.valueOf(connection_protocol.getSelectedItem());
		final String lHost = connection_host.getText().toString();
		final int lPort = Integer.parseInt(connection_port.getText().toString());
		final String lContext = connection_context.getText().toString();
		final String lLogin = connection_login.getText().toString();
		final String lPassword = connection_password.getText().toString();

		ServerConfiguration lServerConfiguration = ServerConfiguration.newConfiguration(lProtocol, lHost, lPort, lContext);

		Result<TokenDTO> lResult = TokensRest.connexion(lServerConfiguration, lLogin, lPassword, ConfigurationHelper.getDeviceId());
		lResult.setOnException(new CallbackOnException() {
			@Override
			public void call(Exception pException) {
				Toast.makeText(ConnectionActivity.this, "Connection error", Toast.LENGTH_LONG).show();
			}
		});
		TokenDTO lTokenDTO = lResult.get();
		if (lTokenDTO == null) {
			Toast.makeText(ConnectionActivity.this, "Connection error", Toast.LENGTH_LONG).show();
		} else {
			// Save it
			TokenDAO lTokenDAO = new TokenDAO();
			Token lToken = lTokenDAO.get(new TokenKey(lLogin, lServerConfiguration));
			if (lToken == null) {
				lToken = new Token(lLogin, lServerConfiguration);
			}
			lToken.setToken(lTokenDTO.getToken());
			lTokenDAO.save(lToken);
			goToTasks();
		}
	}

	private void goToTasks() {
		Intent lIntent = new Intent(ConnectionActivity.this, TasksActivity.class);
		lIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		lIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);                  
		startActivity(lIntent);
	}

	public void onBackPressed() {
		finish();
		return;
	}
}
