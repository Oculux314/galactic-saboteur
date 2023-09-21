package nz.ac.auckland.se206.gpt;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;

public class Assistant {

  public class ApiCallTask extends Task<Void> {
    ChatCompletionRequest request;

    public ApiCallTask(ChatCompletionRequest request) {
      this.request = request;
    }

    @Override
    protected Void call() throws Exception {
      while (isWaitingForResponse) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      narrationBox.disableUserResponse();
      isWaitingForResponse = true;

      // Modulate ... loading effect in response field
      Thread loadEffectThread = new Thread(narrationBox.new LoadEffectTask());
      loadEffectThread.start();

      // Expensive API call
      try {
        ChatCompletionResult result = request.execute();
        ChatMessage response = result.getChoice(0).getChatMessage();
        addChatMessage(response);
        responseText = response.getContent();
      } catch (ApiProxyException e) {
        responseText = GptPromptEngineering.getInternetErrorMessage();
        addChatMessage("assistant", responseText);
      }

      narrationBox.enableUserResponse();
      isWaitingForResponse = false;
      return null;
    }
  }

  private NarrationBox narrationBox;
  private List<ChatMessage> chatMessages = new ArrayList<>();
  private ChatMessage systemMessage = null;
  private boolean onlySystemMessage = false;
  private String responseText = "";
  private boolean isWaitingForResponse = false;
  public String job;

  public Assistant(NarrationBox narrationBox) {
    this.narrationBox = narrationBox;
    this.job = narrationBox.getWaitingMessage();
    chatMessages.add(
        new ChatMessage(
            "system", GptPromptEngineering.getMainPrompt(narrationBox.getWaitingMessage())));
  }

  public void addChatMessage(ChatMessage message) {
    chatMessages.add(message);
  }

  public void addChatMessage(String role, String content) {
    chatMessages.add(new ChatMessage(role, content));
  }

  public void setSystemMessage(String content) {
    setSystemMessage(content, true);
  }

  public void setSystemMessage(String content, boolean onlySystemMessage) {
    this.onlySystemMessage = onlySystemMessage;
    systemMessage = new ChatMessage("system", content);
  }

  private Thread executeApiCall() {
    ChatCompletionRequest request =
        new ChatCompletionRequest().setTemperature(0.4).setTopP(0.6).setMaxTokens(100);

    if (!onlySystemMessage) {
      for (ChatMessage message : chatMessages) {
        request.addMessage(message);
      }
    }
    request.addMessage(systemMessage);

    Task<Void> apiCallTask = new ApiCallTask(request);

    Thread apiCallThread = new Thread(apiCallTask);
    apiCallThread.start();
    return apiCallThread;
  }

  private void executeApiCallWithCallback(Runnable callback) {
    Thread apiCallThread =
        new Thread(
            () -> {
              Thread sendMessageThread = executeApiCall();

              try {
                sendMessageThread.join();
              } catch (InterruptedException e) {
                // Nothing
              }

              Platform.runLater(
                  () -> {
                    callback.run();
                  });
            });
    apiCallThread.start();
  }

  private void renderNarrationBox() {
    narrationBox.showPane();
    narrationBox.setText(responseText);
    narrationBox.enableUserResponse();
  }

  public NarrationBox getNarrationBox() {
    return narrationBox;
  }

  public void respondToUser() {
    narrationBox.disableUserResponse();
    String userMessage = narrationBox.getUserResponse() + GameState.reactorRoomGameState;
    if (userMessage.toLowerCase().contains("hint")) {
      GameState.numberOfHintsAsked++;
      System.out.println(GameState.numberOfHintsAsked);
      System.out.println();
      System.out.println();
    }
    narrationBox.clearUserResponse();
    if (userMessage.equals("")) {
      narrationBox.enableUserResponse();
      return;
    }

    setSystemMessage(GptPromptEngineering.getUserInteractionPrompt(job), false);
    addChatMessage("user", userMessage);
    executeApiCallWithCallback(this::renderNarrationBox);
  }

  public void welcome() {
    setSystemMessage(GptPromptEngineering.getWelcomePrompt(job));
    executeApiCallWithCallback(
        () -> {
          renderNarrationBox();
        });
  }

  // public void getOptions(String topic, Runnable callback) {
  //   setSystemMessage(GptPromptEngineering.getOptionsPrompt(topic));
  //   executeApiCallWithCallback(
  //       () -> {
  //         String optionsString = chatMessages.get(chatMessages.size() - 1).getContent();
  //         String[] optionsTemp = optionsString.replace(" ", "").split(",");

  //         for (int i = 0; i < optionsTemp.length; i++) {
  //           GameState.options[i] = optionsTemp[i];
  //         }

  //         callback.run();
  //       });
  // }

  // public void getRiddle(String answer, Label labelRiddle) {
  //   setSystemMessage(GptPromptEngineering.getRiddlePrompt(answer));
  //   executeApiCallWithCallback(
  //       () -> {
  //         labelRiddle.setText(responseText);
  //       });
  // }

  // public void introduceRiddle() {
  //   narrationBox.setText(GameState.riddleIntroduction);
  // }

  // public void introduceDoor(Runnable callback) {
  //   setSystemMessage(GptPromptEngineering.getIntroduceDoorPrompt());
  //   executeApiCallWithCallback(
  //       () -> {
  //         renderNarrationBox();
  //         callback.run();
  //       });
  // }

  // public void getUserWinMessage() {
  //   setSystemMessage(GptPromptEngineering.getWinPrompt());
  //   executeApiCallWithCallback(
  //       () -> {
  //         renderNarrationBox();
  //         App.setTitle("EscAIpe Room: Escaped!");
  //         textToSpeech.speak("You escaped!");
  //         textToSpeech.terminate();
  //       });
  // }

  // public void getUserLoseMessage() {
  //   setSystemMessage(GptPromptEngineering.getLosePrompt());
  //   executeApiCallWithCallback(
  //       () -> {
  //         renderNarrationBox();
  //         App.setTitle("EscAIpe Room: Try Again");
  //         textToSpeech.speak("Try again!");
  //         textToSpeech.terminate();
  //       });
  // }

  // public void getUserTimeoutMessage() {
  //   setSystemMessage(GptPromptEngineering.getTimeoutPrompt());
  //   executeApiCallWithCallback(
  //       () -> {
  //         renderNarrationBox();
  //         App.setTitle("EscAIpe Room: Timeout");
  //         textToSpeech.speak("Out of time!");
  //         textToSpeech.terminate();
  //       });
  // }
}
