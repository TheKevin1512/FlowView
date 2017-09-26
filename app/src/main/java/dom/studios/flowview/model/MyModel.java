package dom.studios.flowview.model;

/**
 * Created by kevindom on 25/09/17.
 */

public class MyModel {
    private String description;
    private boolean isButtonClicked;

    public boolean isButtonClicked() {
        return isButtonClicked;
    }

    public void setButtonClicked(boolean buttonClicked) {
        isButtonClicked = buttonClicked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
