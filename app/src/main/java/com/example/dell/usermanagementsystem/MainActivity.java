package com.example.dell.usermanagementsystem;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.usermanagementsystem.model.User;
import com.example.dell.usermanagementsystem.service.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class MainActivity extends AppCompatActivity {

    TextView output;
    ProgressBar pb;
    //List<MyTask> task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize TextView for Scrolling
        output = (TextView) findViewById(R.id.textView);
        output.setMovementMethod(new ScrollingMovementMethod());

        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);

        //task = new ArrayList<>();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.action_name){
            if(isOnline()){
                //requestData();
                getUserDetails();
            }
            else{
                Toast.makeText(this,"Network isn't available",Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    /*private void requestData() {
        MyTask task = new MyTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"Param1","Param2","Param3");
    }*/

    protected void updateDisplay(String message){
        output.append(message + "\n");
    }

    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }else {
            return false;
        }
    }

    private void getUserDetails(){


    /*private class MyTask extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            if (task.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            task.add(this);
            updateDisplay("Starting task");
        }

        @Override
        protected String doInBackground(String... params){
            for ( int i = 0; i < params.length; i++){
                publishProgress("Working with " +  params[i]);

                try{
                    Thread.sleep(1000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            return "Task Complete";
        }

        @Override
        protected void onPostExecute(String result) {
            task.remove(this);
            if (task.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }
            updateDisplay(result);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            updateDisplay(values[0]);
        }*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://morning-castle-74227.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<List<User>> call = service.getUserDetails();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Response<List<User>> response, Retrofit retrofit) {
                List<User> peopleData = response.body();
                String details = "";
                for (int i = 0; i < peopleData.size(); i++) {
                    String title = peopleData.get(i).getTitle();
                    details += "Title: " + title + "\n";
                    updateDisplay(details);
                 }
            }

            @Override
            public void onFailure(Throwable t) {
                updateDisplay("Error");
            }
        });
    }
}
