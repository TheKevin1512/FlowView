package dom.studios.flowview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import dom.studios.flowview.model.MyModel;
import dom.studios.flowview.part.ButtonPart;
import dom.studios.flowview.part.DescriptionPart;
import dom.studios.flowview.view.FlowView;

public class MainActivity extends AppCompatActivity implements FlowView.OnFlowListener<MyModel>, View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button resetButton;

    private FlowView<MyModel> mFlowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.resetButton = (Button) findViewById(R.id.resetButton);
        this.resetButton.setOnClickListener(this);

        this.mFlowView = (FlowView) findViewById(R.id.flowView);

        //Set Model to be filled in.
        this.mFlowView.setModel(new MyModel());

        //Set listener
        this.mFlowView.setListener(this);


        //Set Parts to fill in the model.
        List<FlowView.Part<MyModel>> parts = new ArrayList<>();
        parts.add(new DescriptionPart(R.layout.flowpart_description));
        parts.add(new ButtonPart(R.layout.flowpart_button));

        this.mFlowView.setParts(parts);
    }

    @Override
    public void onFlowComplete(MyModel model) {
        Log.d(TAG, "onFlowComplete: " + model.toString());
    }

    @Override
    public void onFlowCanceled() {
        Log.d(TAG, "onFlowCanceled: ");
    }

    @Override
    public void onClick(View v) {
        mFlowView.reset();
    }
}
