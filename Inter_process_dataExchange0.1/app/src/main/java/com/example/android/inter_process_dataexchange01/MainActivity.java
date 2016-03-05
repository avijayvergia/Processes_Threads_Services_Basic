package com.example.android.inter_process_dataexchange01;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView status;
    EditText text;
    String packageName="com.example.android.inter_process_dataexchange";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text= (EditText) findViewById(R.id.editText);
        status= (TextView) findViewById(R.id.textView);
    }

    public void loadFile(View view)
    {
        // PackageManager returns all the packages installed onn the device
        PackageManager packageManager=getPackageManager();
        try {
            //ApplicationInfo returns information regarding the app supplied in the package info
            ApplicationInfo applicationInfo=packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);// Meta data refers to all the information
            String fullPath=applicationInfo.dataDir+"/files/Data.txt";
            readFile(fullPath);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readFile(String fullpath)
    {
        FileInputStream fis=null;
        try {
            fis=new FileInputStream(new File(fullpath));
            int read=-1;
            StringBuffer buffer=new StringBuffer();
            while((read=fis.read())!=-1)
            {
                    buffer.append((char)read);
            }
            text.setText(buffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(fis!=null)
            {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
