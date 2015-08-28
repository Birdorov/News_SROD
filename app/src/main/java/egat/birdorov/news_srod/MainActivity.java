package egat.birdorov.news_srod;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    // Explicit
    private UserTABLE objUserTABLE;
    private NewsTABLE objNewsTABLE;
    private EditText userEditText, passwordEditText;
    private Button loginButton;
    private String userString, passwordString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Widget
        bindWidget();

        // Create & Connected Database
        createConnected();

        // Test Add New Value
        // testerAddValue();

        // Delete All Data
        deleteAllData();

        // Synchronize JSON to SQLite
        synJSONtoSQLite();

        // Button Controller
        buttonController();

    }   // onCreate

    private void buttonController() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                // Check Zero
                if (userString.equals("") || passwordString.equals("") ) {

                    // Have Space
                    errorDialog("Have Space", "Please Fill All Blank");

                } else {

                    // No Space
                    checkUser();
                } // if
            }
        });
    }   // buttonController

    private void checkUser() {

        try {
            String strMyResult[] = objUserTABLE.searchUser(userString);

            // Check Password
            if (passwordString.equals(strMyResult[2])) {

                // Intent to Listnews

            } else {
                errorDialog("Password False", "Plaese Try Again Password False");
            }

        } catch (Exception e) {
            errorDialog("ไม่มี User", "ไม่มี" + userString + " ในฐานข้อมูลของเรา");
        }


    }   // check user

    private void errorDialog(String strTitle, String strMessage) {

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.danger);
        objBuilder.setTitle(strTitle);
        objBuilder.setMessage(strMessage);
        objBuilder.setCancelable(false);
        objBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }   // event
        });
        objBuilder.show();

    }   // Error Dialog

    private void bindWidget() {
        userEditText = (EditText) findViewById(R.id.edtUser);
        passwordEditText = (EditText) findViewById(R.id.edtPassword);
        loginButton = (Button) findViewById(R.id.btnLogin);
    }   // bindWidget

    private void synJSONtoSQLite() {

        // Change Policy
        StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(myPolicy);


        int intTimes = 0;
        while (intTimes <= 1) {

            // 1. Create InputStream
            InputStream objInputStream = null;
            String strJSON = null;
            String userURL = "http://swiftcodingthai.com/egat/php_get_data_Birdorov.php";
            String newsURL = "http://swiftcodingthai.com/egat/php_get_data_news.php";
            HttpPost objHttpPost;

            try {

                HttpClient objHttpClient = new DefaultHttpClient();

                if (intTimes != 1) {
                    objHttpPost = new HttpPost(userURL);

                } else {
                    objHttpPost = new HttpPost(newsURL);
                }

                HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
                HttpEntity objHttpEntity = objHttpResponse.getEntity();
                objInputStream = objHttpEntity.getContent();


            } catch (Exception e) {
                Log.d("egat", "inputStream ==> " + e.toString());
            }
            // 2. Create strJSON

            try {

                BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
                StringBuilder objStringBuilder = new StringBuilder();
                String strLine = null;
                while ((strLine = objBufferedReader.readLine()) != null) {
                    objStringBuilder.append(strLine);

                }   // while
                objInputStream.close();
                strJSON = objStringBuilder.toString();

            } catch (Exception e) {
                Log.d("egat", "strJSON" + e.toString());
            }
            // 3. Update SQLite

            try {

                final JSONArray objJsonArray = new JSONArray(strJSON);

                for (int i = 0; i < objJsonArray.length(); i++) {

                    JSONObject jsonObject = objJsonArray.getJSONObject(i);

                    if (intTimes != 1) {

                        //  For userTABLE
                        String strUSER = jsonObject.getString("User");
                        String strPassword = jsonObject.getString("Password");
                        String strName = jsonObject.getString("Name");
                        objUserTABLE.addNewUser(strUSER, strPassword, strName);

                    } else {

                        //  For newsTABLE
                        String strDate = jsonObject.getString("Date");
                        String strHead = jsonObject.getString("Head");
                        String strDetail = jsonObject.getString("Detail");
                        String strImage = jsonObject.getString("Image");
                        String strOwner = jsonObject.getString("Owner");
                        objNewsTABLE.addNews(strDate, strHead, strDetail, strImage, strOwner);

                    }

                }   // for

            } catch (Exception e) {
                Log.d("egat", "Update ==> " + e.toString());
            }

            intTimes += 1;
        }   // while

    }

    private void deleteAllData() {
        SQLiteDatabase objSqLiteDatabase = openOrCreateDatabase("srod.db", MODE_PRIVATE, null);
        objSqLiteDatabase.delete("userTABLE", null, null);
        objSqLiteDatabase.delete("newsTABLE", null, null);

    }

    private void testerAddValue() {
        objUserTABLE.addNewUser("testUser", "estPass", "ทดสอบชื่อ");
        objNewsTABLE.addNews("27 Aug 2015", "testHead", "testDetail", "http//Image", "Egat");
    }

    private void createConnected() {
        objUserTABLE = new UserTABLE(this);
        objNewsTABLE = new NewsTABLE(this);
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
}   // Main Class
