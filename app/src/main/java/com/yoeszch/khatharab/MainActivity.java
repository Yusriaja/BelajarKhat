package com.yoeszch.khatharab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {
    private Button mButton;
    private EditText mEditText;
    private TextView mTextView;
    private AdView mAdView;
    private int counter = 0;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private static final String LOG_TAG = "bannerAd";
    private String inputId = "";
    //Float textSize, diff = 1.0f;
    private int diff = 5;
    private int textSize = 40;
    FloatingActionButton mAddFab, infoFab, settingFab;
    Boolean isAllFabsVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
        mAddFab = findViewById(R.id.addFab);
        // FAB button
        infoFab = findViewById(R.id.infoFab);
        settingFab = findViewById(R.id.settingFab);

        // Also register the action name text, of all the FABs.

        // Now set all the FABs and all the action name
        // texts as GONE
        infoFab.setVisibility(View.GONE);
        settingFab.setVisibility(View.GONE);
        isAllFabsVisible = false;
        mAddFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {

                            // when isAllFabsVisible becomes
                            // true make all the action name
                            // texts and FABs VISIBLE.
                            infoFab.show();
                            settingFab.show();


                            // make the boolean variable true as
                            // we have set the sub FABs
                            // visibility to GONE
                            isAllFabsVisible = true;
                        } else {

                            // when isAllFabsVisible becomes
                            // true make all the action name
                            // texts and FABs GONE.
                            infoFab.hide();
                            settingFab.hide();


                            // make the boolean variable false
                            // as we have set the sub FABs
                            // visibility to GONE
                            isAllFabsVisible = false;
                        }
                    }
                });
        settingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SUBTYPE_SETTINGS);
                startActivity(intent);
            }
        });

        infoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.dialog_fab, null);
                dialogLayout.findViewById(R.id.textView2);
                builder2.setView(dialogLayout);
                builder2.setPositiveButton("Paham", (dialog, which) -> {

                });

                builder2.show();

            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mAdView.setVisibility(View.VISIBLE);
                Log.d(LOG_TAG, "banner show.");
            }
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                mAdView.setVisibility(View.GONE);
                Log.d(LOG_TAG, "banner gone.");
            }
        });
        RelativeLayout mLayout = (RelativeLayout) findViewById(R.id.mainRl);
        mEditText = (EditText) findViewById(R.id.add_hint);
        mTextView = (TextView) findViewById(R.id.hasilTv);
        mButton = findViewById(R.id.dropdownBtn);
        Button mButton2 = findViewById(R.id.resetBtn);
        Button buttonUp = findViewById(R.id.upBtn);
        Button buttonDown = findViewById(R.id.downBtn);
        buttonUp.setOnClickListener(onResizeUp());
        buttonDown.setOnClickListener(onResizeDown());

        mButton2.setOnClickListener(onClickReset());
        TextView textView = new TextView(this);
        textView.setText(mEditText.getText().toString());

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the input method manager
                InputMethodManager inputMethodManager = (InputMethodManager)
                        view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                // Hide the soft keyboard
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication application = (MyApplication) getApplication();
                counter++;
                application.showInterstitialAd();
                String editTextHuruf = mEditText.getText().toString();
                if (editTextHuruf.trim().equals("")) {
                    Snackbar mSnackbar = Snackbar.make(findViewById(R.id.mainRl), "Ketik sesuatu", Snackbar.LENGTH_LONG);
                    View mView = mSnackbar.getView();
                    TextView mTextView = mView.findViewById(com.google.android.material.R.id.snackbar_text);
                    mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mSnackbar.show();
                    return;

                } else {
                    PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), mButton);
                    dropDownMenu.getMenuInflater().inflate(R.menu.dropdown_menu, dropDownMenu.getMenu());
                    dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @SuppressLint("NonConstantResourceId")
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int id = menuItem.getItemId();
                            switch (id) {
                                case R.id.naskhiMenu:
                                    mButton.setText("Khat Naskhi");
                                    Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/naskhi1.otf");
                                    mTextView.setText(mEditText.getText().toString());
                                    mTextView.setTypeface(typeface);
                                    //mTextView.setTextSize(60);
                                    mTextView.setPadding(10, 10, 10, 10);
                                    mTextView.setTextColor(getResources().getColor(R.color.red));
                                    return true;

                                case R.id.tsulusMenu:
                                    mButton.setText("Khat Tsuluts");
                                    Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/arbfontsdiwanythuluthRegular.ttf");
                                    mTextView.setText(mEditText.getText().toString());
                                    mTextView.setTypeface(typeface2);
                                    //mTextView.setTextSize(60);
                                    mTextView.setPadding(10, 10, 10, 10);
                                    mTextView.setTextColor(getResources().getColor(R.color.red));
                                    return true;
                                case R.id.farisiMenu:
                                    mButton.setText("Khat Farisi");
                                    Typeface typeface3 = Typeface.createFromAsset(getAssets(), "fonts/kachfarisi.ttf");
                                    mTextView.setText(mEditText.getText().toString());
                                    mTextView.setTypeface(typeface3);
                                    // mTextView.setTextSize(60);
                                    mTextView.setPadding(10, 10, 10, 10);
                                    mTextView.setTextColor(getResources().getColor(R.color.red));
                                    return true;
                                case R.id.riqahMenu:
                                    mButton.setText("Khat Riq'ah");
                                    Typeface typeface4 = Typeface.createFromAsset(getAssets(), "fonts/riqaregular.ttf");
                                    mTextView.setText(mEditText.getText().toString());
                                    mTextView.setTypeface(typeface4);
                                    //mTextView.setTextSize(60);
                                    mTextView.setPadding(10, 10, 10, 10);
                                    mTextView.setTextColor(getResources().getColor(R.color.red));
                                    return true;
                                case R.id.diwaniMenu:
                                    mButton.setText("Khat Diwani");
                                    Typeface typeface5 = Typeface.createFromAsset(getAssets(), "fonts/diwani2.ttf");
                                    mTextView.setText(mEditText.getText().toString());
                                    mTextView.setTypeface(typeface5);
                                    //mTextView.setTextSize(60);
                                    mTextView.setPadding(10, 10, 10, 10);
                                    mTextView.setTextColor(getResources().getColor(R.color.red));
                                    return true;
                                case R.id.kufiMenu:
                                    mButton.setText("Khat Kufi");
                                    Typeface typeface6 = Typeface.createFromAsset(getAssets(), "fonts/kufy.ttf");
                                    mTextView.setText(mEditText.getText().toString());
                                    mTextView.setTypeface(typeface6);
                                    //mTextView.setTextSize(60);
                                    mTextView.setPadding(10, 10, 10, 10);
                                    mTextView.setTextColor(getResources().getColor(R.color.red));
                                    return true;
                                case R.id.diwanijaliMenu:
                                    mButton.setText("Khat Diwani Jaly");
                                    Typeface typeface7 = Typeface.createFromAsset(getAssets(), "fonts/diwanyjaly.ttf");
                                    mTextView.setText(mEditText.getText().toString());
                                    mTextView.setTypeface(typeface7);
                                    // mTextView.setTextSize(60);
                                    mTextView.setPadding(10, 10, 10, 10);
                                    mTextView.setTextColor(getResources().getColor(R.color.red));
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    dropDownMenu.show();
                }
            }
        });

    }
//mTextView.setTextSize(60);
    private View.OnClickListener onResizeDown() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication application = (MyApplication) getApplication();
                counter++;
                application.showInterstitialAd();
                String editTextHuruf = mEditText.getText().toString();
                if (editTextHuruf.trim().equals("")) {
                    Snackbar mSnackbar = Snackbar.make(findViewById(R.id.mainRl), "Ketik sesuatu", Snackbar.LENGTH_LONG);
                    View mView = mSnackbar.getView();
                    TextView mTextView = mView.findViewById(com.google.android.material.R.id.snackbar_text);
                    mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mSnackbar.show();
                    return;

                } else {
                    textSize = textSize-diff;
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);;
                }


            }
        };
    }

    private View.OnClickListener onResizeUp() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication application = (MyApplication) getApplication();
                counter++;
                application.showInterstitialAd();
                String editTextHuruf = mEditText.getText().toString();
                if (editTextHuruf.trim().equals("")) {
                    Snackbar mSnackbar = Snackbar.make(findViewById(R.id.mainRl), "Ketik sesuatu", Snackbar.LENGTH_LONG);
                    View mView = mSnackbar.getView();
                    TextView mTextView = mView.findViewById(com.google.android.material.R.id.snackbar_text);
                    mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mSnackbar.show();
                    return;

                } else {
                    textSize = textSize+diff;
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                }

            }
        };
    }

    private View.OnClickListener onClickReset() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication application = (MyApplication) getApplication();
                counter++;
                application.showInterstitialAd();
                String editTextHuruf = mEditText.getText().toString();
                if (editTextHuruf.trim().equals("")) {
                    Snackbar mSnackbar = Snackbar.make(findViewById(R.id.mainRl), "Ketik sesuatu", Snackbar.LENGTH_LONG);
                    View mView = mSnackbar.getView();
                    TextView mTextView = mView.findViewById(com.google.android.material.R.id.snackbar_text);
                    mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mSnackbar.show();
                    return;

                } else {
                    mEditText.setText("");
                    mTextView.setText("");
                }


            }
        };
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menuatas, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.rate:
                String appPackageName = "com.yoeszch.khatharab";
                String url = "https://play.google.com/store/apps/details?id=" + appPackageName;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                //your code
                return true;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app) + "\n" + "https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.links:
                Intent appmarket = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.play_store_more_apps)));

                startActivity(appmarket);
                // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.play_store_more_apps))));
                // mDrawerLayout.closeDrawers()

                return true;
            case R.id.about:

/*                String text = getString(R.string.about);
                Spannable centeredText = new SpannableString(text);
                centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0, text.length() - 1,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                Toast.makeText(getApplicationContext(), centeredText, Toast.LENGTH_LONG).show();*/
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.dialog, null);
                dialogLayout.findViewById(R.id.textView1);
                builder.setView(dialogLayout);
                builder.setPositiveButton("Ok", (dialog, which) -> {

                });

                builder.show();

                return true;
        }
        // return false;
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {

        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
           /*Toast.makeText(getApplicationContext(), "Tekan sekali lagi untuk keluar!",
                    Toast.LENGTH_SHORT).show();*/
            Snackbar mSnackbar = Snackbar.make(findViewById(R.id.mainRl), "Tekan sekali lagi untuk keluar", Snackbar.LENGTH_LONG);
            // get snackbar view
            View mView = mSnackbar.getView();
            // get textview inside snackbar view
            TextView mTextView = mView.findViewById(com.google.android.material.R.id.snackbar_text);
            // set text to center
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            // show the snackbar
            mSnackbar.show();
        }
        back_pressed = System.currentTimeMillis();
    }
}