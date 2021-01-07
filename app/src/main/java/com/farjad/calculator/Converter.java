package com.farjad.calculator;


import android.content.Context;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Converter extends AppCompatActivity {

    public final static int ORIG  = -1;
    public final static int LIGHT_THEME  = 0;
    public final static int DARK_THEME  = 1;


    Context context = this;

    Button CBTCAL, CBT0, CBT1, CBT2, CBT3, CBT4, CBT5, CBT6, CBT7, CBT8, CBT9, CBTD, CBTC;

    ImageButton BTLD;

    EditText areaValueTop, areaValueBottom;

    TextView areaUnitTop, areaUnitBottom;

    Spinner areaSelectorTop, areaSelectorBottom;

    boolean prevInput = true;

    ArrayAdapter<CharSequence> adapter;
    int categoryId = 0;

    DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    String[][] areaUnitList =
            {{"ac", "a", "ha", "m\u00B2", "ft\u00B2", "in\u00B2", "m\u00B2"},
            {"mm", "cm", "m", "km", "in", "ft", "yd", "mi", "NM", "mil"},
            {"\u00B0C", "\u00B0F", "K"},
            {"gal", "gal", "l", "ml", "cm\u00B3", "m\u00B3", "in\u00B3", "ft\u00B3"},
            {"t", "t", "t", "lb", "oz", "kg", "g"},
            {"bit", "B", "KB", "MB", "GB", "TB"}};



    //Setting Area Conversion Array

    double[][][] areaConv = {
            {
                    {1, 40.468564224, 0.4046856422, 40468564.224, 43.560, 6272640, 4046.8564224},
                    {0.0247105381, 1, 0.01, 1000000, 1076.391041671, 155000.31000062, 100},
                    {2.4710538147, 100, 1, 100000000, 107639.10416709, 15500031.000062, 10000},
                    {2.47105381E-8, 0.000001, 0.00000001, 1, 0.001076391, 0.15500031, 0.0001},
                    {0.0000229569, 0.0009290304, 0.0000092903, 929.0304, 1, 144, 0.09290404},
                    {1.59422508E-7, 0.0000064516, 0.0000000645, 6.4516, 0.0069444444, 1, 0.00064516},
                    {0.0002471054, 0.01, 0.0001, 10000, 10.7639104167, 1550.0031000062, 1}
            },
            {
                    {1, 0.1, 0.001, 0.000001, 0.0393700787, 0.0032808399, 0.0010936133, 0.0000006214, 0.00000054, 39.3700787402},
                    {10, 1, 0.01, 0.00001, 0.3937007874, 0.032808399, 0.010936133, 0.0000062137, 0.0000053996, 393.7007874016},
                    {1000, 100, 1, 0.001, 39.3700787402, 3.280839895, 1.0936132983, 0.0006213712, 0.0005399568, 39370.078740157},
                    {1000000, 100000, 1000, 1, 39370.078740157, 3280.8398950131, 1093.6132983377, 0.6213711922, 0.53399568035, 39370078.740157},
                    {25.4, 2.54, 0.0254, 0.0000254, 0.0833333333, 0.02777777778, 0.0000157828, 0.0000137149, 1000},
                    {304.8, 30.48, 0.3048, 0.0003048, 12, 1, 0.3333333333, 0.0001893939, 0.001645788, 12000},
                    {914.4, 91.44, 0.9144, 0.0009144, 36, 3, 1, 0.0005681818, 0.0004937365, 36000},
                    {1609344, 160934.4, 1609.344, 1.609344, 63360, 5280, 1760, 1, 0.8689762419, 63360000},
                    {1852000, 185200, 1852, 1.852, 72913.385826771, 6076.1154855643, 2025.3718285214, 1.150779448, 1, 72913385.826771},
                    {0.0254, 0.00254, 0.0000254, 0.0000000254, 0.001, 0.0000833333, 0.0000277778, 1.57828283E-8, 1.37149028E-8, 1}
            },
            {},
            {
                    {1, 1.2009499255, 4.54609, 4546.09, 4546.09, 0.00454609, 277.4194327916, 0.1605436532},
                    {0.8326741846, 1, 3.785411784, 3785.411784, 3785.411784, 0.0037854118, 231, 0.1336805556},
                    {0.2199692483, 0.2641720524, 1, 1000, 1000, 0.001, 61.0237440947, 0.0353146667},
                    {0.0002199692, 0.000264172, 0.001, 1, 1, 0.000001, 0.0610237441, 0.0000353147},
                    {0.0002199692, 0.000264172, 0.001, 1, 1, 0.000001, 0.0610237441, 0.0000353147},
                    {219.9692482991, 264.1720523581, 1000, 1000000, 1000000, 1, 61023.744094732},
                    {},
                    {}
            }
    };



    // To convert temperature

    public double convertTemp(int tempCatFrom, int tempCatTo, double tempValue)
    {
        if (tempCatFrom == 0)
        {
            if (tempCatTo == 0)
                return tempValue;
            else if (tempCatTo == 1)
                return (tempValue * 1.8) + 32;
            else
                return tempValue + 273.15;
        }
        else if (tempCatFrom == 1)
        {
            if (tempCatTo == 0)
                return (tempValue - 32) * (5/9);
            else if (tempCatTo == 1)
                return tempValue;
            else
                return ((tempValue - 32) * (5/9)) + 273.15;
        }
        else
        {
            if (tempCatTo == 0)
                return tempValue - 273.15;
            else if (tempCatTo == 1)
                return ((tempValue - 273.15) * (9/5)) + 32;
            else
                return tempValue;
        }
    }

    // Change drop-down list and unit

    public void setListsAndUnits(Button button, final int res_id, final int selectedCategoryId)
    {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter = ArrayAdapter.createFromResource(context,
                        res_id, android.R.layout.simple_spinner_item);

                areaSelectorTop.setAdapter(adapter);
                areaSelectorBottom.setAdapter(adapter);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                categoryId = selectedCategoryId;

            }
        });
    }


    //Method to scroll to whatever radio button is selected

    public void scrollToMiddle(final HorizontalScrollView menuButtons, final Button button)
    {
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int x = button.getLeft();
                int y = button.getTop();
                menuButtons.scrollTo(x,y);
            }
        });
    }


    //Method to process a numpad button click

    public void updateText(Button button, final String token)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prevInput)
                {
                    areaValueTop.setText("");
                    prevInput = false;
                }
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                areaValueTop.setText(areaValueTop.getText() + token);

                if (categoryId == 2)
                    areaValueBottom.setText(df.format((Double) convertTemp(areaSelectorTop.getSelectedItemPosition(), areaSelectorBottom.getSelectedItemPosition(), Double.parseDouble(areaValueTop.getText().toString()))));
                else
                    areaValueBottom.setText(df.format((Double)(Double.parseDouble(areaValueTop.getText().toString()) * areaConv[categoryId][areaSelectorTop.getSelectedItemPosition()][areaSelectorBottom.getSelectedItemPosition()])));            }}
        );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        switch(com.farjad.calculator.ThemeVar.getData())
        {
            case ORIG:
                break;
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
        setContentView(R.layout.activity_converter);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setTitle("Unit Converter");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final HorizontalScrollView menuButtons = findViewById(R.id.conv_list);
        menuButtons.setVerticalScrollBarEnabled(false);
        menuButtons.setHorizontalScrollBarEnabled(false);

        final Button BTNAREA, BTNLENGTH, BTNTEMP, BTNVOL, BTNMASS, BTNDATA;

        df.setMaximumFractionDigits(340);

        BTNAREA = findViewById(R.id.BTNAREA);
        BTNLENGTH = findViewById(R.id.BTNLENGTH);
        BTNTEMP = findViewById(R.id.BTNTEMP);
        BTNVOL = findViewById(R.id.BTNVOL);
        BTNMASS = findViewById(R.id.BTNMASS);
        BTNDATA = findViewById(R.id.BTNDATA);

        scrollToMiddle(menuButtons, BTNAREA);
        scrollToMiddle(menuButtons, BTNLENGTH);
        scrollToMiddle(menuButtons, BTNTEMP);
        scrollToMiddle(menuButtons, BTNVOL);
        scrollToMiddle(menuButtons, BTNMASS);
        scrollToMiddle(menuButtons, BTNDATA);

        //Area Conversion Spinner initialization

        areaSelectorTop = findViewById(R.id.areaSelectorTop);
        areaSelectorBottom = findViewById(R.id.areaSelectorBottom);

        adapter = ArrayAdapter.createFromResource(context,
                R.array.areaUnit, R.layout.spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        areaSelectorTop.setAdapter(adapter);
        areaSelectorBottom.setAdapter(adapter);

        setListsAndUnits(BTNAREA, R.array.areaUnit, 0);
        setListsAndUnits(BTNLENGTH, R.array.lengthUnit, 1);
        setListsAndUnits(BTNTEMP, R.array.tempUnit, 2);
        setListsAndUnits(BTNVOL, R.array.volUnit, 3);
        setListsAndUnits(BTNMASS, R.array.massUnit, 4);
        setListsAndUnits(BTNDATA, R.array.dataUnit, 5);





        //-----------------[[ ON-CLICK-LISTENERS FOR NUMPAD ]]----------------------

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
        BTLD = findViewById(R.id.BTLD);

        areaValueTop = findViewById(R.id.areaValueTop);
        areaValueBottom = findViewById(R.id.areaValueBottom);

        areaUnitTop = findViewById(R.id.areaUnitTop);
        areaUnitBottom = findViewById(R.id.areaUnitBottom);

        areaValueTop.setText("0");
        areaValueBottom.setText("0");


        //Update number field after numpad button press

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



        //Processing Clear Input

        CBTC.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                areaValueTop.setText("0");
                areaValueBottom.setText("0");
                prevInput = true;
            }
        });


        //Processing Calculate Operation

        CBTCAL.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                if (categoryId == 2)
                    areaValueBottom.setText(df.format((Double) convertTemp(areaSelectorTop.getSelectedItemPosition(), areaSelectorBottom.getSelectedItemPosition(), Double.parseDouble(areaValueTop.getText().toString()))));
                else
                    areaValueBottom.setText(df.format((Double)(Double.parseDouble(areaValueTop.getText().toString()) * areaConv[categoryId][areaSelectorTop.getSelectedItemPosition()][areaSelectorBottom.getSelectedItemPosition()])));
            }
        });

        areaSelectorTop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long  ex ) {
                areaUnitTop.setText(areaUnitList[categoryId][position]);

                if (categoryId == 2)
                    areaValueBottom.setText(df.format((Double) convertTemp(areaSelectorTop.getSelectedItemPosition(), areaSelectorBottom.getSelectedItemPosition(), Double.parseDouble(areaValueTop.getText().toString()))));
                else
                    areaValueBottom.setText(df.format((Double)(Double.parseDouble(areaValueTop.getText().toString()) * areaConv[categoryId][areaSelectorTop.getSelectedItemPosition()][areaSelectorBottom.getSelectedItemPosition()])));            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        areaSelectorBottom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaUnitBottom.setText(areaUnitList[categoryId][position]);

                if (categoryId == 2)
                    areaValueBottom.setText(df.format((Double) convertTemp(areaSelectorTop.getSelectedItemPosition(), areaSelectorBottom.getSelectedItemPosition(), Double.parseDouble(areaValueTop.getText().toString()))));
                else
                    areaValueBottom.setText(df.format((Double)(Double.parseDouble(areaValueTop.getText().toString()) * areaConv[categoryId][areaSelectorTop.getSelectedItemPosition()][areaSelectorBottom.getSelectedItemPosition()])));            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        BTLD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.farjad.calculator.ThemeVar.setData(1-com.farjad.calculator.ThemeVar.getData());
                recreate();
                /*
                finish();
                Intent intent = new Intent(context, Converter.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
