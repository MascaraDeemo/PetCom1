package petcom.sydney.edu.au.petcom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class replyPage extends AppCompatActivity {
    EditText replyMsg;
    Button cancelBtn;
    Button replyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.replay_page);

        replyMsg= (EditText)findViewById(R.id.edit_msg);
        cancelBtn = (Button)findViewById(R.id.reply_cancel_btn);
        replyBtn = (Button)findViewById(R.id.reply_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
