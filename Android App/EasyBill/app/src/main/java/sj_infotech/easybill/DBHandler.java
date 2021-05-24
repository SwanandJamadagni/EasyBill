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
 * Created by Swanand on 28-12-2018.
 */
public class DBHandler extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertdialog;
    final List<String> barcodelist = new ArrayList<String>();
    String[] products;
    DBHandler (Context ctx) { context = ctx; }
    @Override
    protected String doInBackground(String... params) {

        String ip = params[0];
        String type = params[1];
        String login_url = "http://"+ip+"/getproducts.php";


        if (type.equals("getproducts")) {
            try {
                String scandata = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("scandata","UTF-8")+"="+URLEncoder.encode(scandata,"UTF-8");
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
            products = result.split("&");
            for (int i =1; i < products.length; i++){
                barcodelist.add(products[i]);
            }
            Intent intent = new Intent(context, bill.class);
            intent.putExtra("Barcodes", (Serializable) barcodelist);
            context.startActivity(intent);
        }

    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
