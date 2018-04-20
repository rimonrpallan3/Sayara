package com.voyager.sayara.updateprofile.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.voyager.sayara.R;
import com.voyager.sayara.updateprofile.view.IUpProfileView;
import com.voyager.sayara.registerpage.model.UserDetails;
import com.voyager.sayara.webservices.ApiClient;
import com.voyager.sayara.webservices.WebServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by User on 11-Dec-17.
 */

public class UpProfilePresenter implements IUpProfilePresenter {

    IUpProfileView iUpProfileView;
    Context context;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    UserDetails userDetails;

    String fNmae = "";
    String pnoneNo = "";
    String pswd = "";
    String city = "";
    String country = "";
    String email = "";
    int userID;

    String mCurrentPhotoPath;
    Drawable image;
    String imgPath = "";



    public UpProfilePresenter() {
    }

    public UpProfilePresenter(IUpProfileView iUpProfileView, Context context, UserDetails userDetails) {
        this.iUpProfileView = iUpProfileView;
        this.context = context;
        this.userDetails = userDetails;
        getUserSDetails();
    }


    private void getUserSDetails() {
        if (userDetails != null) {
                fNmae = userDetails.getFName().toString();
                pnoneNo = userDetails.getPhno().toString();
                pswd = userDetails.getPswd().toString();
                city = userDetails.getCity().toString();
                userID = userDetails.getUserID();
                imgPath = userDetails.getImgPath();
                country = userDetails.getCountry();
                email = userDetails.getEmail();

            if (imgPath != null) {
                System.out.println("this is a path for pic ");
                //drawableImg = convertFileToImg(imgPath);

            } else {
                System.out.println("there is no path for pic ");
            }
            iUpProfileView.setUserDetails(userID,fNmae,pnoneNo,city,email,pswd,country,imgPath);
        }


    }


    private File CameraClick() {
        File storageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getApplicationContext().getPackageName()
                + "/Files");


        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        String mImageName = "Profile Image-" + userID + ".jpg";
        mediaFile = new File(storageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Profile Image-" + userID + ".jpg";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir.getPath() + File.separator + imageFileName);

            /*    File.createTempFile(
                imageFileName,  *//* prefix *//*
                ".jpg",         *//* suffix *//*
                storageDir      *//* directory *//*
        );*/


        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        System.out.println("createImageFile : " + image.getName());
        return image;
    }


    public void tkPhoto() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            File imageFile = new File(String.valueOf(CameraClick()));
            Uri imageFileUri = Uri.fromFile(imageFile);
            mCurrentPhotoPath = String.valueOf(imageFile.getAbsoluteFile());
            System.out.println("--------------KITKAT_imageFileUri" + mCurrentPhotoPath);
            if (imageFileUri != null) {
                iUpProfileView.profileImgBelowKitkat(imageFileUri, mCurrentPhotoPath);
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    iUpProfileView.profileImgMAndAbove(createImageFile(), mCurrentPhotoPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    iUpProfileView.profileImgAboveKitkat(createImageFile(), mCurrentPhotoPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void showDialog() {
        Button galleryBtn;
        Button cameraBtn;
        Button cancelBtn;
        final Dialog dialog = new Dialog(context);
        //create dialog without title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set the custom dialog's layout to the dialog
        dialog.setContentView(R.layout.custom_dialog);
        //set the background of dialog box as transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //display the dialog box
        dialog.show();

        //initializing views of custom dialog
        galleryBtn = (Button) dialog.findViewById(R.id.galleryBtn);
        cameraBtn = (Button) dialog.findViewById(R.id.cameraBtn);
        cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);


        //Typeface class specifies style of a font.
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Aller_Lt.ttf");
        //setting the font of some textviews and buttons
        galleryBtn.setTypeface(typeface);
        cameraBtn.setTypeface(typeface);
        cancelBtn.setTypeface(typeface);

        //dismiss the dialog and show toast on pressing the Login button
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choosePic();
                dialog.dismiss();
                // Toast.makeText(PhotoLiciense.this,"You pressed galleryBtn button",Toast.LENGTH_LONG).show();
            }
        });

        //dismiss the dialog and show toast on pressing the Login button
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tkPhoto();
                dialog.dismiss();
                //  Toast.makeText(PhotoLiciense.this,"You pressed cameraBtn button",Toast.LENGTH_LONG).show();
            }
        });

        //dismiss the dialog and show toast on pressing the Login button
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // Toast.makeText(PhotoLiciense.this,"You pressed cancelBtn button",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void addUserGsonInSharedPrefrences() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(userDetails);
        //UserModel user1 = gson.fromJson(jsonString,UserModel.class);
        if (jsonString != null) {
            editor.putString("UserDetails", jsonString);
            editor.commit();
            System.out.println("-----------uploadProfileName UserDetails" + jsonString);

        }

    }


    @Override
    public void changeContent(String name, int userID, String type) {
        System.out.println("changeContent");
        userDetails.checkNameID(userID, name);

        Boolean isLoginSuccess = true;
        final int code = userDetails.checkNameID(userID, name);
        if (code != 0) {
            isLoginSuccess = false;
        } else {
        }
        Boolean result = isLoginSuccess;
        iUpProfileView.goToEditName(result, code, type);
    }

    @Override
    public void newName(String fNmae, int userID) {
        System.out.println("-----------changeContent name: ---" + fNmae + ", userID: ----" + userID);
        this.fNmae = fNmae;
        this.userID = userID;
        //userDetails.setFName(fNmae);
    }

    @Override
    public void getProfilePic() {
        showDialog();
    }

    @Override
    public void uploadProfileImg(String imagePath) {
        mCurrentPhotoPath = imagePath;
        System.out.println("------- onActivityResult : mCurrentPhotoPath - " + mCurrentPhotoPath);
       // Bitmap imageFile = compressImage(mCurrentPhotoPath);
        File imageFile = new File(mCurrentPhotoPath);
        RequestBody requestDriverId =
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userID));

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(imageFile));
        MultipartBody.Part body = MultipartBody.Part.createFormData("user_photo", imageFile.getName(), requestFile);

        Retrofit retrofit = new ApiClient().getRetrofitClient();
        WebServices webServices = retrofit.create(WebServices.class);

        if (mCurrentPhotoPath != null) {
            Call<UserDetails> call = webServices.uploadProfileImg(body, requestDriverId);
            call.enqueue(new Callback<UserDetails>() {
                @Override
                public void onResponse(Call<UserDetails> call,
                                       Response<UserDetails> response) {
                    if (!response.body().isError) {
                        userDetails.setImgPath(mCurrentPhotoPath);
                        Toast.makeText(context.getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
                        addUserGsonInSharedPrefrences();
                        iUpProfileView.setImgPath(mCurrentPhotoPath);
                        mCurrentPhotoPath = null;

                    } else {
                        Toast.makeText(context.getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                    }

                    Log.v("Upload", "success");
                }

                @Override
                public void onFailure(Call<UserDetails> call, Throwable t) {
                    Toast.makeText(context.getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Upload error:", t.getMessage());
                    mCurrentPhotoPath = null;
                }
            });

        }

    }


    public Bitmap compressImage(String imageUri) {


        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        try {
            File imageFile =createImageFile();
            imageFile.getAbsoluteFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return scaledBitmap;

    }

    public String getFilename() {
        String imageFileName = "Profile Image-" + userID + ".jpg";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(storageDir.getPath() + File.separator + imageFileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = imageFileName;
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}
