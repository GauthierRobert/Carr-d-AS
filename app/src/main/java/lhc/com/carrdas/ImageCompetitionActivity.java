package lhc.com.carrdas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.GenericSignatureFormatError;

import lhc.com.dtos.ImageCompetitionDto;
import lhc.com.otherRessources.BaseActivity;
import lhc.com.volley.JsonObjectRequestPost;
import lhc.com.volley.MySingletonRequestQueue;

import static lhc.com.otherRessources.ApplicationConstants.COMPETITION_REF;
import static lhc.com.otherRessources.ApplicationConstants.URL_BASE;
import static lhc.com.otherRessources.ApplicationConstants.URL_COMPETITION_ADD_IMAGE;
import static lhc.com.otherRessources.ApplicationConstants.URL_COMPETITION_POST;

public class ImageCompetitionActivity extends BaseActivity {

    ImageView targetImage;
    private String imageAsBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_competition);



        Button buttonLoadImage = findViewById(R.id.loadimage);
        targetImage = findViewById(R.id.targetimage);

        buttonLoadImage.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        Button skipButton = findViewById(R.id.btn_skip_image_competition);
        Button submitButton = findViewById(R.id.btn_submit_image_competition);
        
        skipButton.setOnClickListener(skipImage());
        submitButton.setOnClickListener(submitImage());

    }

    private View.OnClickListener skipImage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToListCompetitions();
            }
        };

    }

    private void goToListCompetitions() {
            Intent listCompetitionIntent = new Intent(ImageCompetitionActivity.this, ListCompetitions.class);
            finish();
            startActivity(listCompetitionIntent);
    }


    private View.OnClickListener submitImage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostImage();
            }
        };
    }


    private void PostImage() {
        Context mContext = ImageCompetitionActivity.this;
        RequestQueue requestQueue = MySingletonRequestQueue.getInstance(mContext.getApplicationContext()).getRequestQueue();
        final String mRequestBody = getJsonRequest();

        JsonObjectRequestPost jsonObjectRequest = JsonObjectRequestPost.jsonObjectRequestPost(
                URL_BASE + URL_COMPETITION_ADD_IMAGE,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "Create Image : " + response.toString());
                        goToListCompetitions();
                    }
                },
                mRequestBody,
                mContext
        );
        requestQueue.add(jsonObjectRequest);
    }

    private String getJsonRequest() {
        String competition_ref = getIntent().getStringExtra(COMPETITION_REF);
        ImageCompetitionDto imageCompetitionDto = new ImageCompetitionDto(imageAsBase64, competition_ref);

        Gson gson = new Gson();
        return gson.toJson(imageCompetitionDto);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                imageAsBase64= Base64.encodeToString(byteArray, Base64.NO_WRAP);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



}
