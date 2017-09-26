package dom.studios.flowview.part;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import dom.studios.flowview.R;
import dom.studios.flowview.listener.OnStateChangedListener;
import dom.studios.flowview.model.MyModel;
import dom.studios.flowview.view.FlowView;

/**
 * Created by kevindom on 25/09/17.
 */

public class ButtonPart implements FlowView.Part<MyModel> {
    private static final String TAG = "Button Part";
    private Button mButton;

    //Redundant
    private OnStateChangedListener mListener;
    private int layoutId;

    public ButtonPart(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public void update(final MyModel model, View view) {
        this.mButton = (Button) view.findViewById(R.id.buttonToPress);
        this.mButton.setText(model.isButtonClicked() ? "Clicked!" : "Click me mf!");
        this.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onStateChanged();
                model.setButtonClicked(!model.isButtonClicked());
                mButton.setText(model.isButtonClicked() ? "Clicked!" : "Click me mf!");
            }
        });
    }

    @Override
    public boolean isValidated() {
        return true;
    }


    @Override
    public void setListener(OnStateChangedListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getLayout() {
        return layoutId;
    }


    @Override
    public String getTitle() {
        return "Click the button";
    }
}
