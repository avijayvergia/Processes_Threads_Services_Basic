package com.example.android.talkwith_backgroundthread;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MyThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thread=new MyThread();
        thread.start();
    }

    public void sendMessage(View view)
    {
        thread.handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("Debug",Thread.currentThread().getName());
            }
        });
    }

    public class MyThread extends Thread
    {
        Handler handler;

        public MyThread()
        {

        }

        @Override
        public void run() {
            // Looper called so that this thread can receive a queue of messages also
            Looper.prepare();
            //Handler Here is associated with the background Thread as it is implemented in the run method
            handler=new Handler();
            Looper.loop();
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
