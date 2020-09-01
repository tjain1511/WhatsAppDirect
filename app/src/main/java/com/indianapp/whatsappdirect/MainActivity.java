package com.indianapp.whatsappdirect;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.hbb20.CountryCodePicker;
import java.util.regex.Pattern;
public class MainActivity extends AppCompatActivity implements  View.OnKeyListener {
    EditText phoneNum;
    EditText message;
    CountryCodePicker ccp;
    private boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }
    public void onClickWhatsApp(View view) {
        ccp = findViewById(R.id.ccp);
        phoneNum = findViewById(R.id.phoneNum);
        String entNum = String.valueOf(phoneNum.getText());
        if (isValidMobile(entNum)) {
            String finalNum = ccp.getFullNumber() + entNum;
            message = findViewById(R.id.message);
            String msg = String.valueOf(message.getText());
            PackageManager pm = getPackageManager();
            try {
                String url = "https://wa.me/" + finalNum + "?text=" + msg;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                intent.setPackage("com.whatsapp");
                startActivity(intent);
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(this, "Whatsapp Not Installed", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message =findViewById(R.id.message);
        message.setOnKeyListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mShare:
                Intent i = new Intent(
                        android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(
                        android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.indianapp.whatsappdirect");
                startActivity(Intent.createChooser(
                        i,
                        "Share Via"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            onClickWhatsApp(v);
        }
        return false;
    }
}