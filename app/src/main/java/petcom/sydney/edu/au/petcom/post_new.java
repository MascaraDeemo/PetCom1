package petcom.sydney.edu.au.petcom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class post_new extends AppCompatActivity {
    protected static final int POST_NEW = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_new);
        final EditText editTitle = (EditText)findViewById(R.id.post_set_title);
        final EditText editItem = (EditText)findViewById(R.id.post_set_item);

        final Button publishBtn = (Button)findViewById(R.id.publish_btn);
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent publishNew = new Intent();
                publishNew.putExtra("title",editTitle.getText().toString());
                publishNew.putExtra("item",editItem.getText().toString());

                setResult(POST_NEW,publishNew);
                finish();
            }
        });
    }
}
