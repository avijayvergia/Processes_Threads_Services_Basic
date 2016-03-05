package com.example.android.multithreading_asynctask;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText editText;
    private ListView listView;
    private String[] listOfImages;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText= (EditText) findViewById(R.id.editText);
        listView= (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        listOfImages=getResources().getStringArray(R.array.url_invisible);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        editText.setText(listOfImages[position]);
    }

    public void download(View view)
    {
        if(editText.getText().toString()!=null && editText.getText().toString().length()>0)
        {
            MyTask myTask=new MyTask();
            myTask.execute(editText.getText().toString());
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



        return super.onOptionsItemSelected(item);
    }

    ///// ASYNC TASK ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class MyTask extends AsyncTask<String,Integer,Boolean>
    {

        private int contentLength=-1;
        private int counter=0;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean success=false;
            URL downloadUrl=null;
            HttpURLConnection connection=null;
            InputStream inputStream=null;
            FileOutputStream fileOutputStream=null;
            File file=null;
            try {
                downloadUrl=new URL(params[0]);
                connection= (HttpURLConnection) downloadUrl.openConnection();
                contentLength=connection.getContentLength();
                inputStream=connection.getInputStream();
                file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+ Uri.parse(params[0]).getLastPathSegment());
                fileOutputStream=new FileOutputStream(file);
                int read=-1;
                byte[] buffer=new byte[1024];
                while((read=inputStream.read(buffer))!=-1)
                {
                    fileOutputStream.write(buffer,0,read);
                    counter=counter+read;
                    publishProgress(counter);
                }
                success=true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            finally {

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

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress((int)(((double)values[0]/contentLength)*100));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressBar.setVisibility(View.GONE);
        }
    }

    ///////// ASYNC TASK ENDED ////////////////////////////////////////////////////////////////////////////////////////////////////

}

