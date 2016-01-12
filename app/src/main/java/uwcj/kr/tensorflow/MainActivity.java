package uwcj.kr.tensorflow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static String log = "Zoonoo Camera App";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button cameraButton = (Button)findViewById(R.id.button_camera);
        cameraButton.setOnClickListener(cameraListener);
        Button uploadButton = (Button)findViewById(R.id.upload_button);
        uploadButton.setVisibility(Button.GONE);
        uploadButton.setOnClickListener(uploadListener);
    }

    private OnClickListener cameraListener = new OnClickListener() {
        public void onClick(View v) {
            takePhoto(v);
        }
    };

    public int printPixelARGB(int pixel) {
        int red = Color.red(pixel);
        int green = Color.green(pixel);
        int blue = Color.blue(pixel);
        int reverse_avg = (255-(red+green+blue)/3);
        return reverse_avg;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String responseString = (String)msg.obj;
            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Response")
                    .setMessage(responseString)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        }
    };

    private OnClickListener uploadListener = new OnClickListener() {
        public void onClick(View v) {
            try {
                Bitmap bitmap = getPhotoImage();
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 28, 28, true);
                int w = resized.getWidth();
                int h = resized.getHeight();

                int[] arr = new int[784];
                for (int i=0;i<h;i++) {
                    for (int j=0;j<w;j++) {
                        arr[28*i+j] = printPixelARGB(resized.getPixel(j, i));
                    }
                }
                JSONArray jsArray = new JSONArray();
                for (int i=0;i<784;i++) jsArray.put(arr[i]);
                JSONObject obj = new JSONObject();
                obj.put("data", jsArray);

                APIConnector.uploadJsonToServer(obj, handler);
            } catch(Exception e) {
                Log.d(log, e.toString());
            }
        }
    };

    private void takePhoto(View v) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture.jpg");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    protected Bitmap getPhotoImage() throws Exception {
        Uri selectedImage = imageUri;
        getContentResolver().notifyChange(selectedImage, null);
        ContentResolver cr = getContentResolver();

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
        int W = bitmap.getWidth();
        int H = bitmap.getHeight();
        int mWH = W>H?H:W;

        int sW = (W-mWH)/2;
        int sH = (H-mWH)/2;
        Bitmap cropped = Bitmap.createBitmap(bitmap, sW, sH, W - sW, H - sH);

        return cropped;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            ImageView imageView = (ImageView)findViewById(R.id.image_camera);

            try {
                Bitmap bitmap = this.getPhotoImage();
                imageView.setImageBitmap(bitmap);
                findViewById(R.id.upload_button).setVisibility(Button.VISIBLE);
                Toast.makeText(MainActivity.this, imageUri.toString(), Toast.LENGTH_LONG);
            } catch(Exception e) {
                Log.d(log, e.toString());
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



    public static String encodeTobase64(Bitmap image) {
        System.gc();

        if (image==null) {return null;}

        Bitmap imagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    /*
    private void SetImage(Bitmap image) {

        this.imageView.setImageBitmap(image);
        String imageData = encodeTobase64(image);

        ContentValues values = new ContentValues();
        values.put("image", imageData);
        // add additional information if required. e.g. user information.
    }

    */

}
