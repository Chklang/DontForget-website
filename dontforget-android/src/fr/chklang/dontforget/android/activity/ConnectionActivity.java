package fr.chklang.dontforget.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import fr.chklang.dontforget.android.R;
import fr.chklang.dontforget.android.ServerConfiguration;
import fr.chklang.dontforget.android.business.Token;
import fr.chklang.dontforget.android.database.DatabaseManager;
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
	
	private int tokenId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
		
		Intent lIntent = getIntent();
		tokenId = lIntent.getIntExtra("tokenId", -1);

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
		
		if (tokenId != -1) {
			//Load token information
			DatabaseManager.transaction(this, new DatabaseManager.Transaction() {
				@Override
				public void execute() {
					Token lToken = Token.dao.get(tokenId);
					if (lToken == null) {
						return;
					}
					int lNbElementsProtocol = connection_protocol.getCount();
					for (int  i = 0; i < lNbElementsProtocol; i++) {
						Object lValue = connection_protocol.getItemAtPosition(i);
						String lValueString = String.valueOf(lValue);
						if (lToken.getProtocol().equals(lValueString)) {
							connection_protocol.setSelection(i);
							break;
						}
					}
					connection_host.setText(lToken.getHost());
					connection_port.setText(Integer.toString(lToken.getPort()));
					connection_context.setText(lToken.getContext());
					connection_login.setText(lToken.getPseudo());
				}
			});
		}
	}

	private void connection() {
		final String lProtocol = String.valueOf(connection_protocol.getSelectedItem());
		final String lHost = connection_host.getText().toString();
		final int lPort = Integer.parseInt(connection_port.getText().toString());
		final String lContext = connection_context.getText().toString();
		final String lLogin = connection_login.getText().toString();
		final String lPassword = connection_password.getText().toString();

		final StringBuilder lDeviceId = new StringBuilder();
		DatabaseManager.transaction(this, new DatabaseManager.Transaction() {
			
			@Override
			public void execute() {
				lDeviceId.append(ConfigurationHelper.getDeviceId());
			}
		});
		final ServerConfiguration lServerConfiguration = ServerConfiguration.newConfiguration(lProtocol, lHost, lPort, lContext);

		Result<TokenDTO> lResult = TokensRest.create(lServerConfiguration, lLogin, lPassword, lDeviceId.toString());
		lResult.setOnException(new CallbackOnException() {
			@Override
			public void call(Exception pException) {
				Toast.makeText(ConnectionActivity.this, "Connection error", Toast.LENGTH_LONG).show();
			}
		});
		final TokenDTO lTokenDTO = lResult.get();
		if (lTokenDTO == null) {
			Toast.makeText(ConnectionActivity.this, "Connection error", Toast.LENGTH_LONG).show();
		} else {
			// Save it
			DatabaseManager.transaction(this, new DatabaseManager.Transaction() {
				@Override
				public void execute() {
					Token lToken = Token.dao.findByPseudoProtocolHostPortAndContext(lLogin, lServerConfiguration);
					if (lToken == null) {
						lToken = new Token();
					}
					lToken.setPseudo(lLogin);
					lToken.setProtocol(lServerConfiguration.getProtocol());
					lToken.setHost(lServerConfiguration.getHost());
					lToken.setPort(lServerConfiguration.getPort());
					lToken.setContext(lServerConfiguration.getContext());
					lToken.setToken(lTokenDTO.getToken());
					Token.dao.save(lToken);
				}
			});
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
