package sj_infotech.easybill;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Swanand on 09-09-2017.
 */
public class BackgroundWorker extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertdialog;
    BackgroundWorker (Context ctx) {
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {

        String ip = params[0];
        String type = params[1];
        String login_url = "http://"+ip+"/login.php";
        String login_url1 = "http://"+ip+"/adduser.php";
        String login_url2 = "http://"+ip+"/bill.php";
        String login_url3 = "http://"+ip+"/printbill.php";
        String login_url4 = "http://"+ip+"/addstock.php";
        String login_url5 = "http://"+ip+"/addproduct.php";
        String login_url6 = "http://"+ip+"/welcome.php";
        String login_url7 = "http://"+ip+"/generate_qr_code_add_stock.php";
        String login_url8 = "http://"+ip+"/generate_qr_code_add_product.php";
        String login_url9 = "http://"+ip+"/addcategory.php";
        String login_url10 = "http://"+ip+"/add_uncoded_product.php";
        String login_url11 = "http://"+ip+"/manual_bill.php";

        if (type.equals("login")) {
            try {
                String email_id = params[2];
                String pass_word = params[3];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("email_id","UTF-8")+"="+URLEncoder.encode(email_id,"UTF-8")+"&"
                        +URLEncoder.encode("pass_word","UTF-8")+"="+URLEncoder.encode(pass_word,"UTF-8");
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

        if (type.equals("create_user")) {
            try {
                String fname = params[2];
                String lname = params[3];
                String email = params[4];
                String password = params[5];
                String adminaccess = params[6];
                URL url = new URL(login_url1);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("fname","UTF-8")+"="+URLEncoder.encode(fname,"UTF-8")+"&"
                        +URLEncoder.encode("lname","UTF-8")+"="+URLEncoder.encode(lname,"UTF-8")+"&"
                        +URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password, "UTF-8")+"&"
                        +URLEncoder.encode("admin","UTF-8")+"="+URLEncoder.encode(adminaccess, "UTF-8");
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

        if (type.equals("done")) {
            try {
                String scandata = params[2];
                String discount = params[3];
                String phonenumber = params[4];
                String email =  params[5];
                URL url = new URL(login_url2);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("scandata","UTF-8")+"="+URLEncoder.encode(scandata, "UTF-8")+"&"
                        +URLEncoder.encode("discount","UTF-8")+"="+URLEncoder.encode(discount, "UTF-8")+"&"
                        +URLEncoder.encode("phonenumber","UTF-8")+"="+URLEncoder.encode(phonenumber, "UTF-8")+"&"
                        +URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email, "UTF-8");
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

        if (type.equals("genbill")) {
            try {
                String phoneno = params[2];
                URL url = new URL(login_url3);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("phoneno","UTF-8")+"="+URLEncoder.encode(phoneno, "UTF-8");
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

        if (type.equals("add_stock")) {
            try {
                String product_id = params[2];
                String category = params[3];
                String description = params[4];
                String prize = params[5];
                String quantity = params[6];
                URL url = new URL(login_url4);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("product_id","UTF-8")+"="+URLEncoder.encode(product_id, "UTF-8")+"&"
                        +URLEncoder.encode("category","UTF-8")+"="+URLEncoder.encode(category, "UTF-8")+"&"
                        +URLEncoder.encode("description","UTF-8")+"="+URLEncoder.encode(description, "UTF-8")+"&"
                        +URLEncoder.encode("prize","UTF-8")+"="+URLEncoder.encode(prize, "UTF-8")+"&"
                        +URLEncoder.encode("quantity","UTF-8")+"="+URLEncoder.encode(quantity, "UTF-8");
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

        if (type.equals("add_product")) {
            try {
                String product_id = params[2];
                String category = params[3];
                String description = params[4];
                String prize = params[5];
                URL url = new URL(login_url5);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("product_id","UTF-8")+"="+URLEncoder.encode(product_id, "UTF-8")+"&"
                        +URLEncoder.encode("category","UTF-8")+"="+URLEncoder.encode(category, "UTF-8")+"&"
                        +URLEncoder.encode("description","UTF-8")+"="+URLEncoder.encode(description, "UTF-8")+"&"
                        +URLEncoder.encode("prize","UTF-8")+"="+URLEncoder.encode(prize, "UTF-8");
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

        if (type.equals("welcome")) {
            try {
                String email_id = params[2];
                String pass_word = params[3];
                URL url = new URL(login_url6);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email_id,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pass_word,"UTF-8");
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

        if (type.equals("add_stock_qr")) {
            try {
                String category = params[2];
                String description = params[3];
                String prize = params[4];
                String quantity = params[5];
                String email = params[6];
                URL url = new URL(login_url7);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("category","UTF-8")+"="+URLEncoder.encode(category, "UTF-8")+"&"
                        +URLEncoder.encode("description","UTF-8")+"="+URLEncoder.encode(description, "UTF-8")+"&"
                        +URLEncoder.encode("prize","UTF-8")+"="+URLEncoder.encode(prize, "UTF-8")+"&"
                        +URLEncoder.encode("quantity","UTF-8")+"="+URLEncoder.encode(quantity, "UTF-8")+"&"
                        +URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email, "UTF-8");
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

        if (type.equals("add_product_qr")) {
            try {
                String category = params[2];
                String description = params[3];
                String prize = params[4];
                URL url = new URL(login_url8);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("category","UTF-8")+"="+URLEncoder.encode(category, "UTF-8")+"&"
                        +URLEncoder.encode("description","UTF-8")+"="+URLEncoder.encode(description, "UTF-8")+"&"
                        +URLEncoder.encode("prize","UTF-8")+"="+URLEncoder.encode(prize, "UTF-8");
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

        if (type.equals("add_category")) {
            try {
                String category = params[2];
                String gst = params[3];
                URL url = new URL(login_url9);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("category","UTF-8")+"="+URLEncoder.encode(category, "UTF-8")+"&"
                        +URLEncoder.encode("gst","UTF-8")+"="+URLEncoder.encode(gst, "UTF-8");
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

        if (type.equals("add_stock_uncoded_product")) {
            try {
                String description = params[2];
                String prize = params[3];
                String quantity = params[4];
                URL url = new URL(login_url10);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("description","UTF-8")+"="+URLEncoder.encode(description, "UTF-8")+"&"
                        +URLEncoder.encode("prize","UTF-8")+"="+URLEncoder.encode(prize, "UTF-8")+"&"
                        +URLEncoder.encode("quantity","UTF-8")+"="+URLEncoder.encode(quantity, "UTF-8");
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

        if (type.equals("manual_bill_done")) {
            try {
                String scandata = params[2];
                String discount = params[3];
                String email =  params[4];
                URL url = new URL(login_url11);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String post_data = URLEncoder.encode("scandata","UTF-8")+"="+URLEncoder.encode(scandata, "UTF-8")+"&"
                        +URLEncoder.encode("discount","UTF-8")+"="+URLEncoder.encode(discount, "UTF-8")+"&"
                        +URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email, "UTF-8");
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


    private void gotoadminhome() {
        final String admin = "1";
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Intent intent = new Intent(context, home.class);
                intent.putExtra("Admin", (Serializable) admin);
                context.startActivity(intent);
            }
        }, 2000);
    }

    private void gotohome() {
        final String admin = "0";
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Intent intent = new Intent(context, home.class);
                intent.putExtra("Admin", (Serializable) admin);
                context.startActivity(intent);
                //context.startActivity(new Intent(context, home.class));
            }
        }, 2000);
    }

    private void gotologin() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                context.startActivity(new Intent(context, login.class));
            }
        }, 2000);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("welcome_admin")){
            //context.startActivity(new Intent(context, home.class));
            gotoadminhome();
        }
        if(result.equals("welcome")){
            //context.startActivity(new Intent(context, home.class));
            gotohome();
        }
        if(result.equals("go_to_login")){
            //context.startActivity(new Intent(context, login.class));
            gotologin();
        }
        if(result.equals("hello_admin")){
            ProgressDialog progress;
            progress = new ProgressDialog(context);
            progress.setMessage("Loging In....");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
            gotoadminhome();
            //context.startActivity(new Intent(context, home.class));
        }
        if(result.equals("hello")){
            ProgressDialog progress;
            progress = new ProgressDialog(context);
            progress.setMessage("Loging In....");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
            gotohome();
            //context.startActivity(new Intent(context, home.class));
        }
        if (result.equals("error")){
            ProgressDialog progress;
            progress = new ProgressDialog(context);
            progress.setMessage("Loging In....");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
            gotologin();
            Toast.makeText(context,"Email or Password Wrong",Toast.LENGTH_LONG).show();
            //alertdialog.setMessage("Error Connecting Server");
            //alertdialog.show();
            //Toast.makeText(context,"Email or Password is Wrong",Toast.LENGTH_SHORT).show();
        }
        if(result.equals("user_not_found")){
            //context.startActivity(new Intent(context, login.class));
            ProgressDialog progress;
            progress = new ProgressDialog(context);
            progress.setMessage("Loging In....");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
            gotologin();
            Toast.makeText(context,"User Does not Exists",Toast.LENGTH_LONG).show();
        }
        if(result.equals("succesfull")){
            //alertdialog.setMessage("User Created Succesfully");
            //alertdialog.show();
            Toast.makeText(context,"User Created Succesfully",Toast.LENGTH_LONG).show();
        }
        if(result.equals("failed")){
            //alertdialog.setMessage("Cant Create User");
            //alertdialog.show();
            Toast.makeText(context,"Cant Create User",Toast.LENGTH_LONG).show();
        }
        if(result.equals("donebill")) {
            //alertdialog.setMessage("successfull: click generate bill to proceed");
            //alertdialog.show();
            Toast.makeText(context,"successfull: click generate bill to proceed",Toast.LENGTH_LONG).show();
        }
        if(result.equals("stock_added")) {
            //alertdialog.setMessage("Stock Added Successfully");
           //alertdialog.show();
            Toast.makeText(context,"Stock Added Successfully",Toast.LENGTH_LONG).show();
        }
        if(result.equals("product_not_found")) {
            //alertdialog.setMessage("product not found Please press add product if you want to add this as a new product");
            //alertdialog.show();
            Toast.makeText(context,"product not found Please press add product if you want to add this as a new product",Toast.LENGTH_LONG).show();
        }
        if(result.equals("product_added")) {
            //alertdialog.setMessage("product added successfully please press add stock to update stock quantity");
            //alertdialog.show();
            Toast.makeText(context,"product added successfully please press add stock to update stock quantity",Toast.LENGTH_LONG).show();
        }
        if(result.equals("product_exists")) {
            //alertdialog.setMessage("product already exists");
            //alertdialog.show();
            Toast.makeText(context,"product already exists",Toast.LENGTH_LONG).show();
        }
        if(result.equals("stock_added_goto_generate_qr_code")) {
            //alertdialog.setMessage("Stock Added Successfully");
            //alertdialog.show();
            Toast.makeText(context,"Stock Added Successfully please press generate QR code to get qr codes",Toast.LENGTH_LONG).show();
        }
        if(result.equals("category_added")) {
            //alertdialog.setMessage("Stock Added Successfully");
            //alertdialog.show();
            Toast.makeText(context,"Category Added Successfully",Toast.LENGTH_LONG).show();
        }
        if(result.equals("manual_stock_updated")) {
            //alertdialog.setMessage("Stock Added Successfully");
            //alertdialog.show();
            Toast.makeText(context,"Stock Updated Successfully",Toast.LENGTH_LONG).show();
        }
        if(result.equals("done_manual_bill")) {
            //alertdialog.setMessage("successfull: click generate bill to proceed");
            //alertdialog.show();
            Toast.makeText(context,"successfull: click generate bill to proceed",Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}


