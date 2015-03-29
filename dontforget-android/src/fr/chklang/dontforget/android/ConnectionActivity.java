package fr.chklang.dontforget.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import fr.chklang.dontforget.android.services.AbstractService.CallbackOnException;
import fr.chklang.dontforget.android.services.AbstractService.Result;
import fr.chklang.dontforget.android.services.UsersService;

public class ConnectionActivity extends Activity {

	private EditText connection_url;
	private EditText connection_port;
	private EditText connection_login;
	private EditText connection_password;
	private Button connection_send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);

		connection_url = (EditText) this.findViewById(R.id.connection_url);
		connection_port = (EditText) this.findViewById(R.id.connection_port);
		connection_login = (EditText) this.findViewById(R.id.connection_login);
		connection_password = (EditText) this.findViewById(R.id.connection_password);
		connection_send = (Button) this.findViewById(R.id.connection_send);

		// Default values
		connection_url.setText("192.168.0.11");
		connection_port.setText("9000");
		connection_login.setText("Chklang");
		connection_password.setText("password");

		connection_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				connection();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connection, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void connection() {
		final String lUrl = connection_url.getText().toString();
		final int lPort = Integer.parseInt(connection_port.getText().toString());
		final String lLogin = connection_login.getText().toString();
		final String lPassword = connection_password.getText().toString();
		Configuration.set(lUrl, lPort);

		Result<Boolean> lResult = UsersService.connexion(lLogin, lPassword);
		lResult.setOnException(new CallbackOnException() {
			@Override
			public void call(Exception pException) {
				Toast.makeText(ConnectionActivity.this, "Connection error", Toast.LENGTH_LONG).show();
				;
			}
		});
		boolean lConnectionIsOk = lResult.get();
		if (!lConnectionIsOk) {
			Toast.makeText(ConnectionActivity.this, "Connection error", Toast.LENGTH_LONG).show();
			;
		} else {
			Intent lIntent = new Intent(ConnectionActivity.this, TasksActivity.class);
			startActivity(lIntent);
		}
	}
}
