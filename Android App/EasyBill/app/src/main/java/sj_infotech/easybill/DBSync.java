package sj_infotech.easybill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swanand on 27-04-2019.
 */
public class DBSync extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertdialog;
    final List<String> barcodelist = new ArrayList<String>();
    String[] products;
    DBSync (Context ctx) { context = ctx; }
    @Override
    protected String doInBackground(String... params) {

        String ip = params[0];
        String type = params[1];
        String login_url = "http://"+ip+"/sync_data.php";


        if (type.equals("sync")) {
            try {
                String table_data = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("table_data", "UTF-8")+"="+URLEncoder.encode(table_data,"UTF-8");
                bufferedwriter.write(post_data);
                bufferedwriter.flush();
                bufferedwriter.close();
                outputstream.close();
                InputStream inputstream = httpURLConnection.getInputStream();
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedreader.readLine())!= null) {
                    result += line;
                }
                bufferedreader.close();
                inputstream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertdialog = new AlertDialog.Builder(context).create();
        alertdialog.setTitle("Status");
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("data sync successful")){
            Toast.makeText(context, "Data Synced", Toast.LENGTH_LONG).show();
        }
        if(result.equals("error while syncing data")){
            Toast.makeText(context, "Error while Data Syncing", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
