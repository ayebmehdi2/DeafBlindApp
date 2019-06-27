package com.mehdi.deafblindapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{


    TTSManager ttsManager = null;

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    private TextView outText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outText = findViewById(R.id.speech_to_text);

        ttsManager = new TTSManager();
        ttsManager.init(this);

        findViewById(R.id.text_to_speech_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = findViewById(R.id.text_to_speech);
                String t = text.getText().toString();
                if (t.length() != 0){
                    ttsManager.initQueue(t);
                }
            }
        });




        findViewById(R.id.speech_to_text_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ttsManager.shutDown();
    }



            private void startVoiceInput() {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello Am Mehdi, How can I help you?");
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {

                }
            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                switch (requestCode) {
                    case REQ_CODE_SPEECH_INPUT: {
                        if (resultCode == RESULT_OK && null != data) {
                            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            outText.setText(result.get(0));
                        }
                        break;
                    }

                }
            }
}
