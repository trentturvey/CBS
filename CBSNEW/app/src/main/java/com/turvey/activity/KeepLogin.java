package com.turvey.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.turvey.cbs.R;
import com.turvey.fragment.LoginSuccessFragment;
import com.turvey.utils.*;

public class KeepLogin extends AppCompatActivity {

    private final static int LOADING_DURATION = 2500;
    private View parent_view;
    public int tries = 5;
    static KeepLogin keepLogin;
    // Icon Animations
    Animation fromright, fadein, fromtop, frombottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keepLogin = this;
        setContentView(R.layout.loginlayout);
        startAnim();
        parent_view = findViewById(android.R.id.content);
        Tools.setSystemBarColor(this, R.color.blue_grey_900);
        ((View) findViewById(R.id.loginButton)).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                TextInputEditText loginusername = findViewById(R.id.loginusername);
                TextInputEditText loginpassword = findViewById(R.id.loginpassword);
                String user = loginusername.getText().toString(), pass = loginpassword.getText().toString();
                Users u = new Users();
                if (user.equalsIgnoreCase(u.loginUsername) && pass.equals(u.loginPassword) && tries > 0) {
                    //SharedPrefs
                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(KeepLogin.this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("Registered", true);
                    editor.apply();
                    loadingMain();
                } else {
                    tries--;

                    if (!user.equals(u.loginUsername) && !pass.equals(u.loginPassword) && tries > 0) {
                        Snackbar.make(parent_view, "ID & Password Incorrect: " + tries + " Attempts Remaining.", Snackbar.LENGTH_SHORT).show();
                    } else {
                        if (!user.equals(u.loginUsername) && tries > 0) {
                            Snackbar.make(parent_view, "ID Incorrect: " + tries + " Attempts Remaining.", Snackbar.LENGTH_SHORT).show();
                        }
                        if (!pass.equals(u.loginPassword) && tries > 0) {
                            Snackbar.make(parent_view, "Password Incorrect: " + tries + " Attempts Remaining.", Snackbar.LENGTH_SHORT).show();
                        }
                        if (tries <= 0) {
                            showDialogLockedOut();
                        }

                    }
                }


            }
        });


        ((View) findViewById(R.id.forgot_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "Forgot Password", Snackbar.LENGTH_SHORT).show();
            }
        });
        ((View) findViewById(R.id.authorisetext)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAbout();

            }
        });
    }

    public void startAnim() {
        //Animations for login icon and text);
        ImageView loginIcon = (ImageView) findViewById(R.id.loginicon);
        TextView loginText = (TextView) findViewById(R.id.logintext);
        TextView EmpID = (TextView) findViewById(R.id.EmployeeIDText);
        TextView PasswordText = (TextView) findViewById(R.id.PasswordText);
        TextView ForgotPassword = (TextView) findViewById(R.id.forgot_password);
        TextView Authorise = (TextView) findViewById(R.id.authorisetext);
        TextView NewUser = (TextView) findViewById(R.id.newusertext);
        EditText LoginUsername = (EditText) findViewById(R.id.loginusername);
        EditText LoginPassword = (EditText) findViewById(R.id.loginpassword);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        fromright = AnimationUtils.loadAnimation(this, R.anim.fromright);
        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        AnimationSet faderight = new AnimationSet(false);
        AnimationSet fadetop = new AnimationSet(false);
        AnimationSet fadebottom = new AnimationSet(false);
        faderight.addAnimation(fadein);
        faderight.addAnimation(fromright);
        fadetop.addAnimation(fromtop);
        fadetop.addAnimation(fadein);
        fadebottom.addAnimation(frombottom);
        fadebottom.addAnimation(fadein);
        loginIcon.startAnimation(fadetop);
        loginText.startAnimation(faderight);
        EmpID.startAnimation(fadebottom);
        PasswordText.startAnimation(fadebottom);
        ForgotPassword.startAnimation(fadebottom);
        Authorise.startAnimation(fadebottom);
        LoginUsername.startAnimation(fadebottom);
        LoginPassword.startAnimation(fadebottom);
        loginButton.startAnimation(fadebottom);
        NewUser.startAnimation(fadebottom);
        // end of animations
    }

    public void loadingMain() {
        final LinearLayout lyt_progress = (LinearLayout) findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewAnimation.fadeOut(lyt_progress);
            }
        }, LOADING_DURATION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMain();

            }
        }, LOADING_DURATION + 400);
    }

    public void loadingUnlock() {
        final LinearLayout lyt_progress = (LinearLayout) findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewAnimation.fadeOut(lyt_progress);
            }
        }, LOADING_DURATION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lyt_progress.setVisibility(View.INVISIBLE);
                tries = 5;
            }
        }, LOADING_DURATION + 500);
    }

    @Override
    public void onBackPressed() {
        doExitApp();
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "Press again to exit app", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    public void showDialogAbout() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton) dialog.findViewById(R.id.bt_getcode)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.turveyco.com"));
                startActivity(i);
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void showDialogLockedOut() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_lockedout);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_getcode)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingUnlock();
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void startMain() {
        //Start Main Menu
        showDialogLoginSuccess();


    }

    private void showDialogLoginSuccess() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        LoginSuccessFragment newFragment = new LoginSuccessFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    public static KeepLogin getInstance() {
        return keepLogin;
    }
}