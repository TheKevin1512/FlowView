package dom.studios.flowview.part;

import android.view.View;

import dom.studios.flowview.listener.OnStateChangedListener;
import dom.studios.flowview.model.MyModel;
import dom.studios.flowview.view.FlowView;

/**
 * Created by kevindom on 10/10/17.
 */

public class CategoryPart implements FlowView.Part<MyModel> {

    private int layoutId;
    private OnStateChangedListener listener;

    public CategoryPart(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public boolean isValidated() {
        return false;
    }

    @Override
    public void update(MyModel model, View view) {

    }

    @Override
    public int getLayout() {
        return layoutId;
    }

    @Override
    public String getTitle() {
        return "Category";
    }

    @Override
    public void setListener(OnStateChangedListener listener) {
        this.listener = listener;
    }
}
