package net.ion.et.sspsvoice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    SpeechRecognizer mRecognizer;
    TextView messageView;
    WebSocketClient mWebSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageView = (TextView)findViewById(R.id.textMessage);

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationContext().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        mRecognizer.setRecognitionListener(listener);

        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initVoiceRecognizer();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectToServer();
        showBallCount(1, 2, 2);
    }

    @Override
    protected void onStop() {
        super.onStop();
        disconnectFromServer();
    }

    private void initVoiceRecognizer()
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            try {
                mRecognizer.startListening(intent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void showBallCount(int strike, int ball, int out)
    {
        if (strike == 3 || ball == 4 || out == 3)
        {
            strike = 0;
            ball = 0;
            out = 0;
        }

        ImageView ivStrike1 = (ImageView)findViewById(R.id.ivStrike1);
        ImageView ivStrike2 = (ImageView)findViewById(R.id.ivStrike2);

        if (strike == 0)
        {
            ivStrike1.setImageResource(R.drawable.circle_gray);
            ivStrike2.setImageResource(R.drawable.circle_gray);
        }
        else if (strike == 1)
        {
            ivStrike1.setImageResource(R.drawable.circle_yellow);
            ivStrike2.setImageResource(R.drawable.circle_gray);
        }
        else if (strike == 2)
        {
            ivStrike1.setImageResource(R.drawable.circle_yellow);
            ivStrike2.setImageResource(R.drawable.circle_yellow);
        }

        ImageView ivBall1 = (ImageView)findViewById(R.id.ivBall1);
        ImageView ivBall2 = (ImageView)findViewById(R.id.ivBall2);
        ImageView ivBall3 = (ImageView)findViewById(R.id.ivBall3);

        if (ball == 0)
        {
            ivBall1.setImageResource(R.drawable.circle_gray);
            ivBall2.setImageResource(R.drawable.circle_gray);
            ivBall3.setImageResource(R.drawable.circle_gray);
        }
        else if (ball == 1)
        {
            ivBall1.setImageResource(R.drawable.circle_green);
            ivBall2.setImageResource(R.drawable.circle_gray);
            ivBall3.setImageResource(R.drawable.circle_gray);
        }
        else if (ball == 2)
        {
            ivBall1.setImageResource(R.drawable.circle_green);
            ivBall2.setImageResource(R.drawable.circle_green);
            ivBall3.setImageResource(R.drawable.circle_gray);
        }
        else if (ball == 3)
        {
            ivBall1.setImageResource(R.drawable.circle_green);
            ivBall2.setImageResource(R.drawable.circle_green);
            ivBall3.setImageResource(R.drawable.circle_green);
        }

        ImageView ivOut1 = (ImageView)findViewById(R.id.ivOut1);
        ImageView ivOut2 = (ImageView)findViewById(R.id.ivOut2);

        if (out == 0)
        {
            ivOut1.setImageResource(R.drawable.circle_gray);
            ivOut2.setImageResource(R.drawable.circle_gray);
        }
        else if (out == 1)
        {
            ivOut1.setImageResource(R.drawable.circle_red);
            ivOut2.setImageResource(R.drawable.circle_gray);
        }
        else if (out == 2)
        {
            ivOut1.setImageResource(R.drawable.circle_red);
            ivOut2.setImageResource(R.drawable.circle_red);
        }
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            System.out.println("onReadyForSpeech");
            messageView.setText("지금 말씀해 주십시오");
        }

        @Override
        public void onBeginningOfSpeech() {
            System.out.println("onBeginningOfSpeech");
            // messageView.setText("지금 말씀해 주십시오");
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            System.out.println("onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            System.out.println("onEndOfSpeech");
        }

        @Override
        public void onError(int error) {
            System.out.println("onError");
            messageView.setText("음성 인식에 실패했습니다. 다시 천천히 말씀해 주십시오.");

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRecognizer.startListening(intent);
                }
            }, 1000);
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> resultList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String[] arr = new String[resultList.size()];
            resultList.toArray(arr);

            messageView.setText((String)arr[0] + " : 다시 시도하려면 시작버튼을 눌러주십시오.");
            Toast.makeText(getApplicationContext(), arr[0], Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            System.out.println("onPartialResults");
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            System.out.println("onEvent");
        }
    };

    private void connectToServer()
    {
        URI uri;

        try
        {
            uri = new URI("ws://localhost:8090");
        }
        catch (URISyntaxException e) {

            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {

            }

            @Override
            public void onMessage(String message) {

                try {

                    JSONObject object = new JSONObject(message);
                    object.getInt("strike");
                    object.getInt("ball");
                    object.getInt("out");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };

        mWebSocketClient.connect();
    }

    private void disconnectFromServer()
    {
        mWebSocketClient.close();
    }

    private void sendMessage(String message) throws Exception
    {
        JSONObject object = new JSONObject();
        object.put("messageType", "voice");
        object.put("data", message);

        mWebSocketClient.send(object.toString());
    }
}
