package practicaltest02.eim.systems.cs.pub.ro.practicaltest02.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.R;
import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.network.*;
import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.general.*;

public class PracticalTest02MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button serverStartButton = null;
    private Button serverStopButton = null;

    // Client widgets
    private EditText clientHourEditText = null;
    private Button setButton = null;
    private Button resetButton = null;
    private Button pollButton = null;

    private TextView displayTextView = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private StartServerButtonClickListener connectButtonClickListener = new StartServerButtonClickListener();
    private class StartServerButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();

            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));

            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }

            serverThread.start();
        }

    }

    private StopServerButtonClickListener stopServerButtonClickListener = new StopServerButtonClickListener();
    private class StopServerButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            Log.i(Constants.TAG, "[MAIN ACTIVITY] server stoped");
            if (serverThread != null) {
                serverThread.stopThread();
            }
        }

    }

    private SetButtonClickListener setButtonClickListener = new SetButtonClickListener();
    private class SetButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String clientHour = clientHourEditText.getText().toString();
            String clientPort = serverPortEditText.getText().toString();
            String clientAddress = "localhost";

            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            String hour = clientHourEditText.getText().toString();
            if (hour == null || hour.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            displayTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), hour, displayTextView
            );
            clientThread.start();
        }

    }

    private ResetButtonClickListener resetButtonClickListener = new ResetButtonClickListener();
    private class ResetButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String clientHour = clientHourEditText.getText().toString();
            String clientPort = serverPortEditText.getText().toString();
            String clientAddress = "localhost";
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
//            String city = clientCityEditText.getText().toString();
//            String option = clientOptionEditText.getText().toString();
//            if (city == null || city.isEmpty()
//                    || option == null || option.isEmpty()) {
//                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
//                return;
//            }

            displayTextView.setText(Constants.EMPTY_STRING);

//            clientThread = new ClientThread(
//                    clientAddress, Integer.parseInt(clientPort), city, option, displayTextView
//            );
            clientThread.start();
        }

    }

    private PollButtonClickListener pollButtonClickListener = new PollButtonClickListener();
    private class PollButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String clientHour = clientHourEditText.getText().toString();
            String clientPort = serverPortEditText.getText().toString();
            String clientAddress = "localhost";
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
//            String city = clientCityEditText.getText().toString();
//            String option = clientOptionEditText.getText().toString();
//            if (city == null || city.isEmpty()
//                    || option == null || option.isEmpty()) {
//                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
//                return;
//            }

            displayTextView.setText(Constants.EMPTY_STRING);

//            clientThread = new ClientThread(
//                    clientAddress, Integer.parseInt(clientPort), city, option, displayTextView
//            );
            clientThread.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.serverPortEditText);
        serverStartButton = (Button)findViewById(R.id.serverStartButton);
        serverStartButton.setOnClickListener(connectButtonClickListener);

        serverStopButton = (Button)findViewById(R.id.serverStopbutton);
        serverStopButton.setOnClickListener(stopServerButtonClickListener);

        clientHourEditText = (EditText)findViewById(R.id.clientHourEditText);
        displayTextView = (TextView)findViewById(R.id.displayTextView);

        setButton = (Button)findViewById(R.id.setButton);
        setButton.setOnClickListener(setButtonClickListener);

        resetButton = (Button)findViewById(R.id.resetButton);
        resetButton.setOnClickListener(resetButtonClickListener);

        pollButton = (Button)findViewById(R.id.pollButton);
        pollButton.setOnClickListener(pollButtonClickListener);
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
