package e.aakriti.work.podcast_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Favourites extends AppCompatActivity {
    TextView headerText;
    ImageView headerBackImage;
    View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourites);

        header = (View)findViewById(R.id.layout_header);
        headerText = (TextView)header.findViewById(R.id.titleTxt);
        headerBackImage = (ImageView) header.findViewById(R.id.menuImg);
        headerBackImage.setImageResource(R.drawable.back);
        headerText.setText("Favourites");

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
