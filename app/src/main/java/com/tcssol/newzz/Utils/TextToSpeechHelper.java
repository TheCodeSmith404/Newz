package com.tcssol.newzz.Utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.Locale;

public class TextToSpeechHelper implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private boolean initialized = false;
    private Context context;
    private boolean isPaused = false;
    private OnSpeechProgressListener progressListener;

    public TextToSpeechHelper(Context context, OnSpeechProgressListener progressListener) {
        this.context = context;
        this.progressListener = progressListener;
        initializeTextToSpeech();
    }

    private void initializeTextToSpeech() {
        textToSpeech = new TextToSpeech(context, this);
    }

    public void speak(final String text) {
        if (initialized) {

            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    if (progressListener != null) {
                        progressListener.onSpeechStarted();
                    }
                }

                @Override
                public void onDone(String utteranceId) {
                    if (progressListener != null) {
                        progressListener.onSpeechCompleted();
                    }
                }

                @Override
                public void onError(String utteranceId) {
                    if (progressListener != null) {
                        progressListener.onSpeechStopped();
                    }
                }

                @Override
                public void onStop(String utteranceId, boolean interrupted) {
                    if (progressListener != null) {
                        progressListener.onSpeechStopped();
                    }
                }

                @Override
                public void onRangeStart(String utteranceId, int start, int end, int frame) {
                    float progress = ((float) end / (float) text.length()) * 100;
                    if (progressListener != null) {
                        progressListener.onSpeechProgress(progress);
                    }
                }
            });


            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utteranceId");
        } else {
            Log.d("TTS", "Error: TextToSpeech engine not initialized yet");
        }
    }

    public void setLanguage(Locale locale) {
        if (initialized) {
            textToSpeech.setLanguage(locale);
        }
    }

    public void setSpeechRate(float speechRate) {
        if (initialized) {
            textToSpeech.setSpeechRate(speechRate);
        }
    }

    public void pauseSpeech() {
        if (textToSpeech != null && !isPaused) {
            textToSpeech.stop();
            isPaused = true;
        }
    }

    public void resumeSpeech() {
        if (textToSpeech != null && isPaused) {
            textToSpeech.speak("", TextToSpeech.QUEUE_FLUSH, null, "resumeUtteranceId");
            isPaused = false;
        }
    }

    public void stopSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            progressListener.ttsReady();
            initialized = true;
        } else {
            initialized = false;
        }
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    public interface OnSpeechProgressListener {
        void ttsReady();
        void onSpeechStarted();
        void onSpeechCompleted();
        void onSpeechStopped();
        void onSpeechProgress(float progress);
    }
}
