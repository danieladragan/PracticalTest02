package practicaltest02.eim.systems.cs.pub.ro.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Daniela on 5/18/2017.
 */

import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.general.*;

public class ClientThread extends  Thread {
    private String address;
    private int port;
    private String hour;
    private TextView weatherForecastTextView;

    private Socket socket;

    public ClientThread(String address, int port, String hour, TextView weatherForecastTextView) {
        this.address = address;
        this.port = port;
        this.hour = hour;
        this.weatherForecastTextView = weatherForecastTextView;
    }

    @Override
    public void run() {

        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(hour);
            printWriter.flush();
//            printWriter.println(informationType);
//            printWriter.flush();
            String result;
            while ((result = bufferedReader.readLine()) != null) {
                final String finalizedResult = result;
                weatherForecastTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        weatherForecastTextView.setText(finalizedResult);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }

    }
}
