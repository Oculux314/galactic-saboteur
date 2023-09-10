package nz.ac.auckland.se206.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class IconButton extends ImageView {

  public IconButton(String url) {
    this(new Image(url));
  }

  public IconButton(Image image) {
    super(image);

    // Set size
    setFitHeight(80);
    setFitWidth(80);

    // Event listeners
    setOnMouseEntered((event) -> onMouseEntered(event));
    setOnMouseExited((event) -> onMouseExited(event));
    setOnMousePressed((event) -> onMousePressed(event));
    setOnMouseReleased((event) -> onMouseReleased(event));
  }

  public void onMouseEntered(MouseEvent event) {
    setScaleX(1.1);
    setScaleY(1.1);
  }

  private void onMouseExited(MouseEvent event) {
    setScaleX(1);
    setScaleY(1);
  }

  private void onMousePressed(MouseEvent event) {
    setScaleX(1);
    setScaleY(1);
    setOpacity(0.6);
  }

  private void onMouseReleased(MouseEvent event) {
    setScaleX(1.1);
    setScaleY(1.1);
    setOpacity(1);
  }
}
