package com.Mihir.VulnDroid.localstorage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ReadDatabase extends AppCompatActivity {
    public int totalCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_database);
        setTitle("Read Database");
        Button b1 = findViewById(R.id.button3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText mEditText= findViewById(R.id.editText3);
                String str = mEditText.getText().toString();
                String hey = md5(str);
                if (hey.equals("2975a14e029c9c986a8fa04345a5bf6e"))
                {
                    SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    totalCount = prefs.getInt("counter", 0);
                    totalCount+=100;
                    editor.putInt("counter", totalCount);
                    editor.apply();
                    Intent intenti = new Intent(ReadDatabase.this, congo.class);
                    startActivity(intenti);
                }
                else
                {
                    Toast.makeText(ReadDatabase.this, "C'mon, TRY HARDER!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static String md5(String s)
    {
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
