package dom.studios.flowview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dom.studios.flowview.R;
import dom.studios.flowview.listener.OnStateChangedListener;

/**
 * Created by kevindom on 25/09/17.
 */

public class FlowView<T> extends FrameLayout implements OnStateChangedListener {
    private static final String TAG = "FlowView";

    private TextView mTitle;

    //          Navigation Buttons
    private Button mNextButton;
    private ImageView mPrevButton;

    //          Current Part Of The Flow
    private FrameLayout mCurrentPart;

    //          All The Parts Of The Flow
    private List<Part<T>> mParts;
    private int mCurrentPartIndex = 0;

    //          The Model Which Will Be Filled In By The Flow.
    private T model;

    //          The Listener Which Will Be Notified At The End Of The Flow.
    private OnFlowListener<T> mListener;


    //          Customizable Attributes.
    private int titleColor;
    private int backgroundColor;
    private int nextTextColor;
    private int validatedStepColor;
    private int rejectedStepColor;

    private int previousStepIcon;

    private String finalStepText;
    private String nextStepText;


    public FlowView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public FlowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FlowView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (context == null) return;
        View root = inflate(context, R.layout.flow_layout, this);
        root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onFlowCanceled();
            }
        });
        this.mTitle = (TextView) root.findViewById(R.id.flowTitle);

        this.mNextButton = (Button) root.findViewById(R.id.flowNextButton);
        this.mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toNextPart();
            }
        });

        this.mPrevButton = (ImageView) root.findViewById(R.id.flowPrevButton);
        this.mPrevButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toPrevPart();
            }
        });
        this.mPrevButton.setVisibility(GONE);

        this.mCurrentPart = (FrameLayout) root.findViewById(R.id.currentStepView);
        if (attrs == null) return;
        applyStyling(context.obtainStyledAttributes(attrs, R.styleable.FlowView));
    }

    private void applyStyling(TypedArray arr) {
        this.titleColor = arr.getColor(R.styleable.FlowView_titleColor, Color.GRAY);
        mTitle.setTextColor(titleColor);

        this.backgroundColor = arr.getColor(R.styleable.FlowView_backgroundColor, Color.BLACK);
        setBackgroundColor(backgroundColor);

        this.nextTextColor = arr.getColor(R.styleable.FlowView_nextTextColor, Color.WHITE);
        mNextButton.setTextColor(nextTextColor);

        this.validatedStepColor = arr.getColor(R.styleable.FlowView_validatedStepColor, Color.GREEN);
        this.rejectedStepColor = arr.getColor(R.styleable.FlowView_rejectedStepColor, Color.RED);

        this.previousStepIcon = arr.getInteger(R.styleable.FlowView_previousStepIcon, R.drawable.back_arrow);
        this.mPrevButton.setImageResource(previousStepIcon);

        String nextStepText = arr.getString(R.styleable.FlowView_nextButtonText);
        if (nextStepText == null) this.nextStepText = "Next";
        else this.nextStepText = nextStepText;
        mNextButton.setBackgroundColor(rejectedStepColor);
        mNextButton.setTypeface(null, Typeface.BOLD);
        mNextButton.setText(this.nextStepText);

        String finalStepText = arr.getString(R.styleable.FlowView_finalStepText);
        if (finalStepText == null) this.finalStepText = "Complete";
        else this.finalStepText = finalStepText;

        arr.recycle();
    }

    private void replacePart() {
        if (mCurrentPartIndex + 1 == mParts.size()) mNextButton.setText(finalStepText);
        else mNextButton.setText(nextStepText);

        mTitle.setText(mParts.get(mCurrentPartIndex).getTitle());
        inflate(getContext(), mParts.get(mCurrentPartIndex).getLayout(), mCurrentPart);
        mParts.get(mCurrentPartIndex).update(model, mCurrentPart);
        mParts.get(mCurrentPartIndex).setListener(this);
    }

    private void updateFlow(boolean toNextPart) {
        if (toNextPart) {
            if (mCurrentPartIndex + 1 < mParts.size()) {
                mCurrentPartIndex++;
                mPrevButton.setVisibility(VISIBLE);
            } else mListener.onFlowComplete(model);
        } else {
            if (mCurrentPartIndex - 1 >= 0) mCurrentPartIndex--;
            if (mCurrentPartIndex == 0) mPrevButton.setVisibility(GONE);
        }

        mCurrentPart.removeAllViews();
        replacePart();
        onStateChanged();
    }

    public void dismiss(){
        this.removeAllViews();
    }

    public void setParts(List<Part<T>> parts) {
        this.mParts = parts;
        replacePart();
    }

    public void setListener(OnFlowListener<T> listener) {
        this.mListener = listener;
    }

    @Override
    public void onStateChanged() {
        if (mParts.get(mCurrentPartIndex).isValidated())
            mNextButton.setBackgroundColor(validatedStepColor);
        else
            mNextButton.setBackgroundColor(rejectedStepColor);
    }

    public void toNextPart() {
        if (mParts.get(mCurrentPartIndex).isValidated())
            updateFlow(true);
    }

    public void toPrevPart() {
        updateFlow(false);
    }

    public void reset() {
        try {
            model = (T) model.getClass().newInstance();
            mCurrentPartIndex = 0;
            toPrevPart();
        } catch (InstantiationException e) {
            Log.e(TAG, e.getMessage(), e.getCause());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage(), e.getCause());
        }
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBgColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getNextTextColor() {
        return nextTextColor;
    }

    public void setNextTextColor(int nextTextColor) {
        this.nextTextColor = nextTextColor;
    }

    public int getValidatedStepColor() {
        return validatedStepColor;
    }

    public void setValidatedStepColor(int validatedStepColor) {
        this.validatedStepColor = validatedStepColor;
    }

    public int getRejectedStepColor() {
        return rejectedStepColor;
    }

    public void setRejectedStepColor(int rejectedStepColor) {
        this.rejectedStepColor = rejectedStepColor;
    }

    public int getPreviousStepIcon() {
        return previousStepIcon;
    }

    public void setPreviousStepIcon(int resId) {
        this.previousStepIcon = resId;
    }

    public interface OnFlowListener<T> {
        void onFlowComplete(T model);
        void onFlowCanceled();
    }

    public interface Part<T> {

        boolean isValidated();

        void update(T model, View view);

        int getLayout();

        String getTitle();

        void setListener(OnStateChangedListener listener);
    }
}
