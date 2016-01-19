package e.aakriti.work.podcast_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayList extends AppCompatActivity {
    TextView headerText;
    ImageView headerBackImage;
    View header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_list_activity);

        header = (View)findViewById(R.id.layout_header);
        headerText = (TextView)header.findViewById(R.id.titleTxt);
        headerBackImage = (ImageView) header.findViewById(R.id.menuImg);
        headerBackImage.setImageResource(R.drawable.back);
        headerText.setText("PlayList");

        headerBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void onBackPressed()
    {
        finish();
    };
}
