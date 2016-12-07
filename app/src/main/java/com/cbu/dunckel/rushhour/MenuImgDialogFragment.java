package com.cbu.dunckel.rushhour;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

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

        Bundle bundle = getArguments();
        String url = (String) bundle.get("url");
        int screenHeight = (Integer) bundle.get("screenHeight");
        int screenWidth = (Integer) bundle.get("screenWidth");
        System.out.println("url "+url);

            menuView = (ImageView) rootView.findViewById(R.id.menuImage);
        if(!url.isEmpty()) {
            Picasso.with(rootView.getContext()).load(url)
                    .resize(screenWidth, screenHeight)
                    .placeholder(R.drawable.menu_not_available)
                    .error(R.drawable.menu_not_available)
                    .into(menuView);
        } else {
            Picasso.with(rootView.getContext()).load(R.drawable.menu_not_available)
                    .resize(screenWidth, screenHeight)
                    .placeholder(R.drawable.menu_not_available)
                    .error(R.drawable.menu_not_available)
                    .into(menuView);
        }
//        String uniqueID = UUID.randomUUID().toString();
//            String[] fileName = url.split("/");
////            download(url, fileName[fileName.length-1]);
//        download(url, uniqueID+".pdf");
////            view(fileName[fileName.length-1]);
//            view(uniqueID+".pdf");

//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        startActivity(browserIntent);

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
        getDialog().show();

        return rootView;
    }

    public void download(String url, String fileName)
    {
        new DownloadFile().execute(url, fileName);
    }

    public void view(String url)
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/RushHourPdfs/" + url);  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        System.out.println("Uri Path: "+ path);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(getContext(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> pdf url
            String fileName = strings[1];  // -> pdf name
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "RushHourPdfs");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }

}