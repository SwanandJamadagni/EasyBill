package sj_infotech.easybill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

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
 * Created by Swanand on 21-02-2019.
 */
public class GetCategory_Barcode extends AsyncTask<String, Void, String> {


    Context context;
    AlertDialog alertdialog;
    final List<String> barcodelist = new ArrayList<String>();
    String[] category;
    GetCategory_Barcode (Context ctx) { context = ctx; }
    @Override
    protected String doInBackground(String... params) {

        String ip = params[0];
        String type = params[1];
        String login_url = "http://"+ip+"/getcategory.php";


        if (type.equals("getcategory")) {
            try {
                String request = params[1];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("request", "UTF-8")+"="+URLEncoder.encode(request,"UTF-8");
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
        if(result.equals("error")){
            alertdialog.setMessage("Can't fetch products");
            alertdialog.show();
        }
        else{
            category = result.split("&");
            Intent intent = new Intent(context, Add_Stock.class);
            intent.putExtra("Category", (Serializable) category);
            context.startActivity(intent);
        }

    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
