package dom.studios.flowview.part;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import dom.studios.flowview.R;
import dom.studios.flowview.listener.OnStateChangedListener;
import dom.studios.flowview.model.MyModel;
import dom.studios.flowview.view.FlowView;

/**
 * Created by kevindom on 25/09/17.
 */

public class DescriptionPart implements FlowView.Part<MyModel> {
    private static final String TAG = "Description Part";

    private TextView mDescription;

    //Redundant
    private OnStateChangedListener mListener;
    private int layoutId;

    public DescriptionPart(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public void update(final MyModel model, View view) {
        this.mDescription = (TextView) view.findViewById(R.id.descriptionText);
        this.mDescription.setText(model.getDescription());
        this.mDescription.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mListener != null) mListener.onStateChanged();
                model.setDescription(String.valueOf(mDescription.getText()));
                return false;
            }
        });
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
    public boolean isValidated() {
        return mDescription.getText().length() > 10;
    }

    @Override
    public String getTitle() {
        return "Description";
    }
}
