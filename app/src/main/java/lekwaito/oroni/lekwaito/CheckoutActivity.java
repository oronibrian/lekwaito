package lekwaito.oroni.lekwaito;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CheckoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ((TextView) findViewById(R.id.total_text_value)).setText("Total Amount : "+Integer.toString(getIntent().getIntExtra("total",0)));
    }



    public void onLog(View view) {

        //Finishing current DashBoard activity on button click.\
        finish();

        //startActivity(new Intent(this, Login.class));
        startActivity(new Intent(this, Login.class));

        Toast.makeText(this,"Log Out Successful", Toast.LENGTH_LONG).show();
    }
}
