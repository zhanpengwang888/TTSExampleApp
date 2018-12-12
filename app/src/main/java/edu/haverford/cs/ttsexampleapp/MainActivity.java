package edu.haverford.cs.ttsexampleapp;

import android.os.Build;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    protected TextToSpeech mTextToSpeech;
    protected EditText editText;
    protected String audioFolderPath;
    protected boolean isSameLanguage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath();
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
                        int statusForPitch = mTextToSpeech.setPitch(1); // set the pitch to be normal
                        int statusForRate = mTextToSpeech.setSpeechRate(1); // set the speech rate to 2
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

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton languageRadioButton = group.findViewById(checkedId);
                if (languageRadioButton != null) {
                    switch (languageRadioButton.getText().toString()) {
                        case "English":
                            mTextToSpeech.setLanguage(Locale.US);
                            break;
                        case "French":
                            mTextToSpeech.setLanguage(Locale.FRANCE);
                            break;
                        case "Chinese":
                            mTextToSpeech.setLanguage(Locale.CHINESE);
                            break;
                        case "Spanish":
                            mTextToSpeech.setLanguage(new Locale("es"));
                            break;
                        default:
                            mTextToSpeech.setLanguage(Locale.US);
                            break;
                    }
                    isSameLanguage = false;
                }
            }
        });

        editText = findViewById(R.id.editText);
        Button speechButton = findViewById(R.id.button);
        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textForSpeech = editText.getText().toString();
                String audioFileName = textForSpeech+".mp3";
                String audioFileCompletePath = audioFolderPath + "/" + audioFileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "");
                File audioFile = new File(audioFileCompletePath);
                if (audioFile.exists() && isSameLanguage) {
                    mTextToSpeech.addSpeech(textForSpeech, audioFileCompletePath);
                } else {
                    if (Build.VERSION.SDK_INT >= 21) {
                        mTextToSpeech.synthesizeToFile(textForSpeech, null, audioFile, null);
                    } else {
                        mTextToSpeech.synthesizeToFile(textForSpeech, null, audioFileCompletePath);
                    }
                    isSameLanguage = true;
                }
                Toast.makeText(getApplicationContext(), textForSpeech, Toast.LENGTH_SHORT).show();
                mTextToSpeech.speak(textForSpeech, TextToSpeech.QUEUE_ADD, null);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTextToSpeech != null) {
            mTextToSpeech.shutdown();
        }
    }
}
