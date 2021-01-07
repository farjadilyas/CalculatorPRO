/*
* App: Calculator Pro
* Developer: Muhammad Farjad Ilyas
* Start Date: 22/07/2020
* End Date: 28/07/2020
*/


package com.farjad.calculator;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


public class MainActivity extends AppCompatActivity {

    //Calculator Engine - uses nashorn combined with advEval() method for scientific calculation

    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("rhino");

    String[] ops = {"sin", "cos", "tan", "ln", "log", "abs", "sqrt"};
    int[] opsLength = {3,3,3,2,3,3,4};

    public double advEval(String arg) throws ScriptException
    {
        int t_index, opIndx = 0;

        while (opIndx < ops.length)
        {
            while (true)
            {
                t_index = arg.indexOf(ops[opIndx]);

                if (t_index != -1)
                    arg = eval(arg, ops[opIndx], opsLength[opIndx], t_index);
                else
                    break;
            }

            ++opIndx;
        }

        return basicEval(arg);
    }

    public String eval(String arg, String op, int opLength, int t_index) throws ScriptException
    {
        arg = arg.substring(0,t_index) + arg.substring(t_index + opLength, arg.length());

        int bracketBalance = 0, ind = t_index;
        double opResult = 0;

        while (ind < arg.length())
        {
            if (arg.charAt(ind) == '(')
                --bracketBalance;
            else if (arg.charAt(ind) == ')')
                ++bracketBalance;

            ++ind;

            if (bracketBalance == 0)
                break;
        }

        switch (op)
        {
            case("sin"):
                opResult = Math.sin(advEval(arg.substring(t_index, ind)));
                break;
            case("cos"):
                opResult = Math.cos(advEval(arg.substring(t_index, ind)));
                break;
            case("tan"):
                opResult = Math.tan(advEval(arg.substring(t_index, ind)));
                break;
            case("ln"):
                opResult = Math.log(advEval(arg.substring(t_index, ind)));
                break;
            case("log"):
                opResult = Math.log10(advEval(arg.substring(t_index, ind)));
                break;
            case("abs"):
                opResult = Math.abs(advEval(arg.substring(t_index, ind)));
                break;
            case("sqrt"):
                opResult = Math.sqrt(advEval(arg.substring(t_index, ind)));
                break;
        }

        arg = arg.substring(0,t_index) + opResult + arg.substring(ind, arg.length());

        return arg;
    }

    public Double basicEval(String arg) throws ScriptException
    {
        return Double.parseDouble(engine.eval(arg).toString());
    }



    //Theme constants - enumeration

    public final static int ORIG  = -1;
    public final static int LIGHT_THEME  = 0;
    public final static int DARK_THEME  = 1;



    //Save MainActivity context for batch OnClickListener calls inside methods

    Context context = this;


    //All Buttons

    Button CBTCAL, CBT0, CBT1, CBT2, CBT3, CBT4, CBT5, CBT6, CBT7, CBT8, CBT9, CBTD, CBTC, CBTPLUS, CBTSUB, CBTMUL, CBTDIV, CLRHIST, CBTOPBCK, CBTCLBCK, CBTPCNT,
    CBTSIN, CBTCOS, CBTTAN, CBTNLOG, CBTLOG, CBTABS, CBTPI, CBTE, CBTSQRT, CBTEXP, CBTPOW, CBTFRAC;
    ImageButton CBTHIST, CBTCONV, CBTADV, BTLD, CBTBKSP;


    //History Bar Views

    ConstraintLayout HistoryBar;
    LinearLayout HistoryLinearLayout;
    TextView textbox;


    // editText for parsing - CALOUT for display

    EditText editText, CALOUT;


    /*
     * OP_REQ - if an operation request preceeds current input
     * prevInput - if an answer is currently on screen 
     * histValid - if history button is clickable (history isnt empty)
     * histExp - stores all history strings - used for restoring history after orientation change
     */

    boolean OP_REQ, prevInput = false,
    histValid = false;
    ArrayList<String> histExp  = new ArrayList<String>();

    String output;


    //Toast data for invalid input

    CharSequence text = "Invalid input";
    int duration = Toast.LENGTH_SHORT;


    // Method: updateText() - handles all digit OnClick events

    public void updateText(Button button, final String token)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prevInput == true) {
                    CALOUT.setText("");
                    editText.setText("");
                    prevInput = false;
                }

                CALOUT.setText(CALOUT.getText() + token);
                editText.setText(editText.getText() + token);
                OP_REQ = false;
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }}
        );
    }


    // Method: functionRequest() - handles all function button OnClick events

    public void functionRequest(Button button, final String token)
    {
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                if (!(OP_REQ || editText.getText().toString().isEmpty()))
                    CBTMUL.performClick();

                CALOUT.setText(CALOUT.getText() + token);
                editText.setText(editText.getText() + token);
                OP_REQ = true;
            }
        });
    }


    // Method: processOperationRequest() - handles all +,-,*,/ OnClick events

    public boolean processOperationRequest()
    {
            if (editText.getText().toString().isEmpty()) {
                if (!prevInput) {
                    Toast.makeText(getApplicationContext(), text, duration).show();
                    return false;
                }
            }
            OP_REQ = true;
            return true;
    }




    // Saves editText, CALOUT, etc in Bundle

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("CALOUT", CALOUT.getText().toString());
        outState.putString("editText", editText.getText().toString());
        outState.putBoolean("OP_REQ", OP_REQ);
        outState.putStringArrayList("HIST", histExp);
        super.onSaveInstanceState(outState);
    }



    // Method: onCreate()

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // Retrieves static int from ThemeVar class to syncronize the App Theme

        switch(com.farjad.calculator.ThemeVar.getData())
        {
            case ORIG:
            case LIGHT_THEME:
                com.farjad.calculator.ThemeVar.setData(0);
                setTheme(R.style.LightTheme);
                break;
            case DARK_THEME:
                com.farjad.calculator.ThemeVar.setData(1);
                setTheme(R.style.DarkTheme);
                break;
            default:
        }


        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        // Find all Views

        CBTCAL = findViewById(R.id.CBTCAL);
        CBT0 = findViewById(R.id.CBT0);
        CBT1 = findViewById(R.id.CBT1);
        CBT2 = findViewById(R.id.CBT2);
        CBT3 = findViewById(R.id.CBT3);
        CBT4 = findViewById(R.id.CBT4);
        CBT5 = findViewById(R.id.CBT5);
        CBT6 = findViewById(R.id.CBT6);
        CBT7 = findViewById(R.id.CBT7);
        CBT8 = findViewById(R.id.CBT8);
        CBT9 = findViewById(R.id.CBT9);
        CBTD = findViewById(R.id.CBTD);
        CBTC = findViewById(R.id.CBTC);
        CBTPLUS = findViewById(R.id.CBTPLUS);
        CBTSUB = findViewById(R.id.CBTSUB);
        CBTMUL = findViewById(R.id.CBTMUL);
        CBTDIV = findViewById(R.id.CBTDIV);
        CBTHIST = findViewById(R.id.CBTHIST);
        CBTCONV = findViewById(R.id.CBTCONV);
        CBTADV = findViewById(R.id.CBTADV);
        CLRHIST = findViewById(R.id.CLRHIST);
        CBTOPBCK = findViewById(R.id.CBTOPBCK);
        CBTCLBCK = findViewById(R.id.CBTCLBCK);
        CBTBKSP = findViewById(R.id.CBTBKSP);
        CBTPCNT = findViewById(R.id.CBTPCNT);


        BTLD = findViewById(R.id.BTLD);
        HistoryBar = findViewById(R.id.HistoryBar);
        HistoryBar.bringToFront();
        HistoryLinearLayout = findViewById(R.id.HistoryLinearLayout);
        HistoryLinearLayout.bringToFront();

        editText = findViewById(R.id.editText);
        CALOUT = findViewById(R.id.CALOUT);
        CALOUT.setVisibility(View.VISIBLE);

        editText.setText("");
        editText.setVisibility(View.GONE);      // only used for parsing input


        // Retrieve saved data

        if (savedInstanceState != null) {
            CALOUT.setText(savedInstanceState.getString("CALOUT"));
            editText.setText(savedInstanceState.getString("editText"));
            OP_REQ = savedInstanceState.getBoolean("OP_REQ");
            histExp = savedInstanceState.getStringArrayList("HIST");

            int indx = 0, histSize = histExp.size();

            if (histSize > 0)
                histValid = true;

            

            // Fill history bar

            while (indx < histSize)
            {
                textbox = new TextView(context);
                textbox.setTextSize(16);
                textbox.setPadding(8,20,16,10);
                textbox.setText(histExp.get(indx));
                HistoryLinearLayout.addView(textbox);

                if (com.farjad.calculator.ThemeVar.getData() == 0)
                    textbox.setTextColor(getResources().getColor(R.color.pureBlack));
                else
                    textbox.setTextColor(getResources().getColor(R.color.pureWhite));
                textbox.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

                ++indx;

                textbox = new TextView(context);
                textbox.setTextSize(20);
                textbox.setTextColor(getResources().getColor(R.color.colorGreen));
                textbox.setPadding(8,0,16,32);
                textbox.setText(histExp.get(indx));
                textbox.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                HistoryLinearLayout.addView(textbox);
                ++indx;
            }


        }


        //Handle every digit OnClick event

        updateText(CBT0,"0");
        updateText(CBT1,"1");
        updateText(CBT2,"2");
        updateText(CBT3,"3");
        updateText(CBT4,"4");
        updateText(CBT5,"5");
        updateText(CBT6,"6");
        updateText(CBT7,"7");
        updateText(CBT8,"8");
        updateText(CBT9,"9");
        updateText(CBTD,".");




        // Backspace OnClickListener()

        CBTBKSP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                output = CALOUT.getText().toString();
                int length = output.length(), cut = 1;

                if (length != 0)
                {
                    if (output.charAt(length-1) == 32)
                        cut = 3;

                    CALOUT.setText(output.substring(0,output.length()-cut));

                    output = editText.getText().toString();
                    editText.setText(output.substring(0,output.length()-cut));
                }
            }
        });




        // Process extra buttons for scientific calculator (Landscape)

        if (context.getResources().getConfiguration().orientation == 2)
        {
            //Scientific Buttons - find Views

            CBTSIN = findViewById(R.id.CBTSIN);
            CBTCOS = findViewById(R.id.CBTCOS);
            CBTTAN = findViewById(R.id.CBTTAN);
            CBTFRAC = findViewById(R.id.CBTFRAC);
            CBTNLOG = findViewById(R.id.CBTNLOG);
            CBTLOG = findViewById(R.id.CBTLOG);
            CBTEXP = findViewById(R.id.CBTEXP);
            CBTE = findViewById(R.id.CBTE);
            CBTPI = findViewById(R.id.CBTPI);
            CBTABS = findViewById(R.id.CBTABS);
            CBTSQRT = findViewById(R.id.CBTSQRT);
            CBTPOW = findViewById(R.id.CBTPOW);



            // Handle onClick events

            functionRequest(CBTSIN, "sin(");
            functionRequest(CBTCOS, "cos(");
            functionRequest(CBTTAN, "tan(");
            functionRequest(CBTNLOG, "ln(");
            functionRequest(CBTLOG, "log(");
            functionRequest(CBTABS, "abs(");
            functionRequest(CBTSQRT, "sqrt(");


            CBTPI.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {

                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                    if (!(OP_REQ || editText.getText().toString().isEmpty()))
                        CBTMUL.performClick();

                    OP_REQ = false;

                    CALOUT.setText(CALOUT.getText() + "\u03C0");
                    editText.setText(editText.getText() + "3.1415926536");
                }
            });

            CBTE.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {

                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                    if (!(OP_REQ || editText.getText().toString().isEmpty()))
                        CBTMUL.performClick();

                    OP_REQ = false;

                    CALOUT.setText(CALOUT.getText() + "e");
                    editText.setText(editText.getText() + "2.7182818285");
                }
            });

            CBTFRAC.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                if (!(OP_REQ || editText.getText().toString().isEmpty()))
                    CBTMUL.performClick();

                CALOUT.setText(CALOUT.getText() + "1\u00F7");
                editText.setText(editText.getText() + "1/");
                OP_REQ = true;
            }
        });

        }


        // Open bracket: OnClickListener()

        CBTOPBCK.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                if (!(OP_REQ || editText.getText().toString().isEmpty()))
                    CBTMUL.performClick();

                CALOUT.setText(CALOUT.getText() + "(");
                editText.setText(editText.getText()+"(");
            }
        });


        // Close bracket: OnClickListener()

        CBTCLBCK.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                CALOUT.setText(CALOUT.getText() + ")");
                editText.setText(editText.getText() + ")");
            }
        });


        // Percentage button: OnClickListener()

        CBTPCNT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                CALOUT.setText(CALOUT.getText() + "%");
                editText.setText(editText.getText() + "/100");
                OP_REQ = false;
            }
        });


        // Scientific calculator shortcut button

        CBTADV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                switch (context.getResources().getConfiguration().orientation)
                {
                    case(1):
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        break;
                    case(2):
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                }

            }
        });



        // Clear History button

        CLRHIST.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                HistoryLinearLayout.removeAllViews();

                histValid = false;
            }
        });


        // Converter shortcut

        CBTCONV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                Intent intent = new Intent(context, Converter.class);
                intent.putExtra("position", com.farjad.calculator.ThemeVar.getData());
                startActivity(intent);
            }
        });


        // Expand History Bar button

        CBTHIST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (HistoryBar.getVisibility() == View.VISIBLE)
                {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    HistoryBar.setVisibility(View.INVISIBLE);
                }
                else if (histValid)
                {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                    if (HistoryBar.getVisibility() == View.INVISIBLE)
                        HistoryBar.setVisibility(View.VISIBLE);

                }
            }
        });


        CBTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                CALOUT.setText("");
                prevInput = false;
                OP_REQ = false;
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });


        CBTPLUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                if (processOperationRequest())
                {
                    CALOUT.setText(CALOUT.getText() + " + ");
                    editText.setText(editText.getText() + " + ");
                }

                OP_REQ = true;
                prevInput = false;
            }
        });

        CBTSUB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                if (processOperationRequest())
                {
                    CALOUT.setText(CALOUT.getText() + " - ");
                    editText.setText(editText.getText() + " - ");
                }

                OP_REQ = true;
                prevInput = false;
            }
        });


        CBTMUL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                if (processOperationRequest())
                {
                    CALOUT.setText(CALOUT.getText() + " \u00D7 ");
                    editText.setText(editText.getText() + " * ");
                }

                OP_REQ = true;
                prevInput = false;
            }
        });

        CBTDIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                if (processOperationRequest())
                {
                    CALOUT.setText(CALOUT.getText() + " \u00F7 ");
                    editText.setText(editText.getText() + " / ");
                }

                OP_REQ = true;
                prevInput = false;
            }
        });



        // Calculate button

        CBTCAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);


                // Add input to History

                output = CALOUT.getText().toString();

                textbox = new TextView(context);
                textbox.setTextSize(16);
                textbox.setPadding(8,20,16,10);
                textbox.setText(CALOUT.getText().toString());
                histExp.add(CALOUT.getText().toString());

                if (com.farjad.calculator.ThemeVar.getData() == 0)
                    textbox.setTextColor(getResources().getColor(R.color.pureBlack));
                else
                    textbox.setTextColor(getResources().getColor(R.color.pureWhite));
                textbox.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

                HistoryLinearLayout.addView(textbox);



                // Calculate output

                if (!editText.getText().toString().isEmpty()) {

                    try {
                        output = String.valueOf(advEval(editText.getText().toString()));
                    }
                    catch (ScriptException e) {
                        Toast.makeText(getApplicationContext(), text, duration).show();
                    }


                    // Add output to history

                    textbox = new TextView(context);
                    textbox.setTextSize(20);
                    textbox.setTextColor(getResources().getColor(R.color.colorGreen));
                    textbox.setPadding(8,0,16,32);
                    textbox.setText("= " + output);
                    textbox.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    histExp.add("= " + output);

                    HistoryLinearLayout.addView(textbox);

                    prevInput = true;
                    CALOUT.setText(output);
                    editText.setText(output);

                    histValid = true;

                }
                else
                {
                    Toast.makeText(getApplicationContext(), text, duration).show();     // invalid input toast
                }
            }
        });



        // Change theme button

        BTLD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.farjad.calculator.ThemeVar.setData(1-com.farjad.calculator.ThemeVar.getData());
                recreate();
                /*
                finish();
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("position", position);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
            }
        });


    }
}