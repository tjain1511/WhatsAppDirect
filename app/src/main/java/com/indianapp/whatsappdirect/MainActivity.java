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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.hbb20.CountryCodePicker;
import java.util.regex.Pattern;
import com.google.android.gms.ads.AdView;
import static com.google.android.gms.ads.MobileAds.*;
public class MainActivity extends AppCompatActivity implements  View.OnKeyListener {
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
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
            mInterstitialAd.show();
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
        initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        MobileAds.initialize(this,
                "ca-app-pub-3560512023866532~7059385374");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3560512023866532/9561317831");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
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
                        android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.indianapp.whatsappchat");
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