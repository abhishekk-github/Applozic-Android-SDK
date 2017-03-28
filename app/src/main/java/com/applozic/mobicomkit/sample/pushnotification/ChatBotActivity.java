package com.applozic.mobicomkit.sample.pushnotification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.applozic.mobicomkit.sample.R;
import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class ChatBotActivity extends AppCompatActivity implements AIListener {

  private AIService aiService;
  private Button listenButton;
  private TextView resultTextView;
  private TextView responceTextView;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_bot);
    init();
  }

  private void init(){
    listenButton = (Button) findViewById(R.id.button_chatbot);
    resultTextView = (TextView) findViewById(R.id.editText_cb);
    responceTextView = (TextView) findViewById(R.id.editText_ans);
    final AIConfiguration config = new AIConfiguration("60caf77baa2147d19435af476a0adeb3",
        AIConfiguration.SupportedLanguages.English,
        AIConfiguration.RecognitionEngine.System);
    aiService = AIService.getService(this, config);
    aiService.setListener(this);
    listenButtonOnClickListener();
  }

  private void listenButtonOnClickListener() {
    listenButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        aiService.startListening();
      }
    });
  }

  @Override
  public void onResult(AIResponse response) {
    Result result = response.getResult();

    // Get parameters
    String parameterString = "";
    if (result.getParameters() != null && !result.getParameters().isEmpty()) {
      for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
        parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
      }
    }

    // Show results in TextView.
    resultTextView.setText("Query:" + result.getResolvedQuery() +
        "\n Action: " + result.getAction() +
        "\n Parameters: " + parameterString);

    final String speech = result.getFulfillment().getSpeech();
    responceTextView.setText(speech);
  }

  @Override
  public void onError(AIError error) {
    resultTextView.setText(error.toString());
  }

  @Override
  public void onAudioLevel(float level) {

  }

  @Override
  public void onListeningStarted() {

  }

  @Override
  public void onListeningCanceled() {

  }

  @Override
  public void onListeningFinished() {

  }
}
