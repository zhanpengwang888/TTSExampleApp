package edu.haverford.cs.ttsexampleapp;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextToSpeech mTextToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialization of TextToSpeech Instance
        mTextToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // if initialization is successful, define the behaviors of the TextToSpeech engine
                if (status == TextToSpeech.SUCCESS) {
                    // set the language to be American English
                    Locale language = Locale.US;
                    int isSupported = mTextToSpeech.setLanguage(language);
                    if (isSupported != TextToSpeech.LANG_AVAILABLE && isSupported != TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                        Toast.makeText(getApplicationContext(),"The current language " + language.toString()+ " is not available.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // it is supported
                        int statusForPitch = mTextToSpeech.setPitch(2); // set the pitch to be normal
                        int statusForRate = mTextToSpeech.setSpeechRate(2); // set the speech rate to 2
                        if (statusForPitch != TextToSpeech.SUCCESS) {
                            Toast.makeText(getApplicationContext(), "Fail to set pitch", Toast.LENGTH_SHORT).show();
                        }
                        if (statusForRate != TextToSpeech.SUCCESS) {
                            Toast.makeText(getApplicationContext(), "Fail to set the Speech rate", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
