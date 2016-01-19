package e.aakriti.work.podcast_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Search extends AppCompatActivity {
    TextView headerText;
    ImageView headerBackImage;
    View header;
    EditText searchEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        header = (View)findViewById(R.id.layout_header);
        headerText = (TextView)header.findViewById(R.id.titleTxt);
        headerBackImage = (ImageView) header.findViewById(R.id.menuImg);
        headerBackImage.setImageResource(R.drawable.back);
        headerText.setText("Search Results");

        headerBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    /*searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
           if(actionId == EditorInfo.IME_ACTION_SEARCH){
               performSearch();
               return true;
           }
            return false;
        }
    });
    */
    }
    //public void performSearch(){}

    public void onBackPressed()
    {
        finish();
    };
}
