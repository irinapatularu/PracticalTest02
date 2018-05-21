package ro.pub.cs.systems.eim.practicaltest02.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText edit_port = null;
    private EditText edit_url = null;

    private Button btn_start = null;
    private Button btn_get = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;


    private ConnectButtonClickListener start_lis = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = edit_port.getText().toString();
            String url = edit_url.getText().toString();

            if (serverPort == null || serverPort.isEmpty() || url == null || url.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port and url should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }


            serverThread = new ServerThread(Integer.parseInt(serverPort), url);
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread because is null!");
                return;
            }
            serverThread.start();
        }

    }

    private GetLis get_lis = new GetLis();
    private class GetLis implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = "127.0.0.1";
            String clientPort = edit_port.getText().toString();

            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = edit_url.getText().toString();
            clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), url);
            clientThread.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_practical_test02_main);
        edit_port = (EditText)findViewById(R.id.edit_port);
        edit_url = (EditText)findViewById(R.id.edit_url);

        btn_start = (Button)findViewById((R.id.button_start));
        btn_get = (Button)findViewById((R.id.button_get));

        btn_start.setOnClickListener(start_lis);




    /*
        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        cityEditText = (EditText)findViewById(R.id.city_edit_text);
        informationTypeSpinner = (Spinner)findViewById(R.id.information_type_spinner);
        getWeatherForecastButton = (Button)findViewById(R.id.get_weather_forecast_button);
        getWeatherForecastButton.setOnClickListener(getWeatherForecastButtonClickListener);
        weatherForecastTextView = (TextView)findViewById(R.id.weather_forecast_text_view);*/
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }


}
