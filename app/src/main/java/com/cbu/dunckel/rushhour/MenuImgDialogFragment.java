package com.cbu.dunckel.rushhour;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.barteksc.pdfviewer.PDFView;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Dunckel on 11/18/2016.
 */

public class MenuImgDialogFragment extends android.support.v4.app.DialogFragment {

    private static Context context;
    private ImageView menuView;
    private static boolean canGetURl = true;
    private PDFView pdfView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_fragment, container, false);
        getDialog().setTitle("Menu");

        menuView = (ImageView) rootView.findViewById(R.id.menuImage);
        Picasso.with(getActivity()).load("http://i.imgur.com/pP0MSby.jpg")
                .resize(1000,1500)
                .placeholder(R.drawable.del_me)
                .error(R.drawable.del_me)
                .into(menuView);

//        pdfView = (PDFView) rootView.findViewById(R.id.pdfView);
//        Uri uri = Uri.parse("http://www.cbu.edu.zm/downloads/pdf-sample.pdf");
//        pdfView.fromUri(uri).pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
//                .enableSwipe(true)
//                .swipeHorizontal(false)
//                .enableDoubletap(true)
//                .defaultPage(0)
//                .enableAnnotationRendering(false)
//                .password(null)
//                .scrollHandle(null)
//                .load();

//        StrictMode.setThreadPolicy(policy);
//        menuView.setImageDrawable(LoadImageFromWebOperations("app/res/drawable/ic_android_black_24dp.xml"));
        getDialog().show();;

        return rootView;
    }


//    public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {
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



}