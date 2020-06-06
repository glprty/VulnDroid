package com.Mihir.VulnDroid.localstorage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ExternalStorage extends AppCompatActivity {
    public int totalCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("External Storage");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_storage);
        setTitle("External Storage");

        if (isExternalStorageWritable()) {
            File file = new File (Environment.getExternalStorageDirectory(), "flag.txt");

//            Log.d("External Storage Directory", String.valueOf(Environment.getExternalStorageDirectory()));
            String flag="";
            try {
                flag = AESUtils.decrypt("848158E155106BA380AF0EA25D544A9B74E412283656C6584ED95EF25110B5D6");
            } 				
        /*
		CWE-780,327
		Необходимо дописать наличие соответствующих run-time исключений.
		https://cwe.mitre.org/data/definitions/780.html
		https://cwe.mitre.org/data/definitions/327.html
		*/
		catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
			catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}

            FileOutputStream fos;
            /*
	   CWE-459
            В случае закрытия в блоке try,может быть утечка, если будет исключение.
	    Решение: закрыть файл не в try {}, можно сделать это в finally
			finally { fos.close();}
			https://cwe.mitre.org/data/definitions/459.html
            */

            try {
                fos = new FileOutputStream(file);
                fos.write(flag.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Button b1 = findViewById(R.id.button3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText mEditText= findViewById(R.id.editText3);
                String str = mEditText.getText().toString();
                String hey = md5(str);
                if (hey.equals("8c212e2cfe68eff4aa53459e5f2174c9"))
                {
                    SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    totalCount = prefs.getInt("counter", 0);
                    totalCount+=100;
                    editor.putInt("counter", totalCount);
                    editor.apply();
                    Intent intenti = new Intent(ExternalStorage.this, congo.class);
                    startActivity(intenti);
                }
                else
                {
                    Toast.makeText(ExternalStorage.this, "C'mon, TRY HARDER!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
    public static String md5(String s) {
        MessageDigest digest;
        try {
            /*
            CWE-327.326
            MD5 необходимо заменить на SHA-256. Безопаность MD5 скомпрометирована, необходимо использовать хеш-функция, которая надежней
            https://cwe.mitre.org/data/definitions/327.html
			https://cwe.mitre.org/data/definitions/326.html
            */
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")), 0, s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        		
		/*
		CWE-780,327
		Необходимо дописать наличие соответствующих run-time исключений.
		https://cwe.mitre.org/data/definitions/780.html
		https://cwe.mitre.org/data/definitions/327.html
		*/
			catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
        return "";
    }

}
