package com.aysenurk.akilli_yoklama;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserLoginActivity extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        session = new Session(this);

        TextView classNameView = findViewById(R.id.classNameView);
        classNameView.setText("Sınıf: " + session.getClassroom());

        // NFC adaptörünü ayarla
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // Cihazda NFC varmı kontrol et
        // Eğer yok ise uyarı göster
        if (mNfcAdapter == null) {
            Toast.makeText(this, "Cihazınız NFC desteklemiyor. Lütfen NFC destekli bir cihazda deneyin.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC'yi aktif hale getirin!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NFC kullanıma hazır", Toast.LENGTH_SHORT).show();
            handleIntent(getIntent());
        }
    }

    public void changeClassroom(View view) {
        session.removeClass();
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NfcReaderTask(this, "student").execute(tag);
            } else {
                Toast.makeText(this, "Hatalı mime tipi", Toast.LENGTH_SHORT).show();
            }
        }
//        else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
//
//            // In case we would still use the Tech Discovered Intent
//            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            String[] techList = tag.getTechList();
//            String searchedTech = Ndef.class.getName();
//
//            for (String tech : techList) {
//                if (searchedTech.equals(tech)) {
//                    new NfcReaderTask(this, "student").execute(tag);
//                    stopForegroundDispatch(this, mNfcAdapter);
//                    break;
//                }
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
}
