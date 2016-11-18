package com.cbu.dunckel.rushhour;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by NickNatali on 11/12/16.
 */
//Dialog Fragment.
public class dFragment extends android.support.v4.app.DialogFragment {

    private static Context context;
    private ImageView menuView;
    private static boolean canGetURl = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_fragment, container, false);
        getDialog().setTitle("Menu");

        menuView = (ImageView) rootView.findViewById(R.id.menuImage);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

//        StrictMode.setThreadPolicy(policy);
//        menuView.setImageDrawable(LoadImageFromWebOperations("app/res/drawable/ic_android_black_24dp.xml"));

        Drawable myDrwable = getResources().getDrawable(R.drawable.menu);
        menuView.setImageDrawable(myDrwable);
//        UrlImageViewHelper.setUrlDrawable(menuView,"https://www.google.com/url?sa=i&rct=j&q=&esrc=s&source=images&cd" +
//                "=&cad=rja&uact=8&ved=0ahUKEwjCjbDWzqfQAhWDsJQKHaVNCzoQjRwIBw&url=https%3A%2F%2Fwww.canva.com%2Ftemp" +
//                "lates%2Fmenus%2FMABKz_xy31k-red-minimalist-diner" +
//                "-menu%2F&bvm=bv.138493631,d.dGo&psig=AFQjCNEDRqTAKIOB0ldjFiyC7CHBz2g9sA&ust=1479191160028591");
        return rootView;
    }


    public static Drawable LoadImageFromWebOperations(String url) {

                try {
                    InputStream is = (InputStream) new URL(url).getContent();
                    Log.d("TAG", " Drawable");
                    Drawable d = Drawable.createFromStream(is, "src name");
                    Log.d("TAG", "Got Drawable" + d);
                    return d;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

    }
//
//public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {
//
//    ImageView imageView = null;
//
//    @Override
//    protected Bitmap doInBackground(ImageView... imageViews) {
//        this.imageView = imageViews[0];
//        return download_Image((String)imageView.getTag());
//    }
//
//    @Override
//    protected void onPostExecute(Bitmap result) {
//        imageView.setImageBitmap(result);
//    }
//
//    private Bitmap download_Image(String url) {
//        Bitmap bm = null;
//        try {
//            URL aURL = new URL(url);
//            URLConnection conn = aURL.openConnection();
//            conn.connect();
//            InputStream is = conn.getInputStream();
//            BufferedInputStream bis = new BufferedInputStream(is);
//            bm = BitmapFactory.decodeStream(bis);
//            bis.close();
//            is.close();
//        } catch (IOException e) {
//            Log.e("Hub","Error getting the image from server : " + e.getMessage().toString());
//        }
//        return bm;
//    }
//


//}






