package com.example.android.multithreading;

import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private EditText editText;
    private ListView listView;
    private String[] listOfImages;
    private ProgressBar progressBar;
    private LinearLayout loadingSection;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText= (EditText) findViewById(R.id.editText);
        listView= (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        listOfImages=getResources().getStringArray(R.array.url);
        loadingSection= (LinearLayout) findViewById(R.id.loading);
        handler=new Handler();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        editText.setText(listOfImages[position]);

    }

    public void download(View view)
    {
        String url=editText.getText().toString();
        Thread thread=new Thread(new MyThread(url));
        thread.start();


    }

    public boolean downloadImageUsingThreads(String url)
    {
        //Create URL object
        //Open connection for the url object
        //Get the InputStream from the connection

        boolean success=false;
        URL downloadUrl=null;
        HttpURLConnection connection=null;
        InputStream inputStream=null;
        FileOutputStream fileOutputStream=null;
        File file=null;
        try {
            downloadUrl=new URL(url);
            connection= (HttpURLConnection) downloadUrl.openConnection();
            inputStream=connection.getInputStream();
            file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+Uri.parse(url).getLastPathSegment());
            fileOutputStream=new FileOutputStream(file);
            int read=-1;
            byte[] buffer=new byte[1024];
            while((read=inputStream.read(buffer))!=-1)
            {
                fileOutputStream.write(buffer,0,read);
            }
            success=true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        finally {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    loadingSection.setVisibility(View.GONE);
                }
            });

            if(connection!=null)
            {
                connection.disconnect();
            }
            if(inputStream!=null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileOutputStream!=null)
            {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return success;
    }

    public class MyThread implements Runnable{

        private String url;

        public MyThread(String url)
        {
            this.url=url;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loadingSection.setVisibility(View.VISIBLE);
                }
            });
            downloadImageUsingThreads(url);
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
