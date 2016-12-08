package progressbar.com.progressbardemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    ProgressDialog prgDialog;
    private int prgStatus = 0;
    private Handler prgBarHandler = new Handler();
    private long fileSize = 0;
    private Button btnStartPrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnStartPrg = (Button) findViewById(R.id.btnStartProgress);
        btnStartPrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // prepare for a progress bar dialog
                prgDialog = new ProgressDialog(view.getContext());
                prgDialog.setCancelable(true);
                prgDialog.setMessage("File downloading ...");
                prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                prgDialog.setProgress(0);
                prgDialog.setMax(100);
                prgDialog.show();

                //reset progress bar status
                prgStatus = 0;

                //reset filesize
                fileSize = 0;

                new Thread(new Runnable() {
                    public void run() {
                        while (prgStatus < 100) {

                            // process some tasks
                            prgStatus = doSomeTasks();

                            // your computer is too fast, sleep 1 second
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // Update the progress bar
                            prgBarHandler.post(new Runnable() {
                                public void run() {
                                    prgDialog.setProgress(prgStatus);
                                }
                            });
                        }

                        // ok, file is downloaded,
                        if (prgStatus >= 100) {

                            // sleep 2 seconds, so that you can see the 100%
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // close the progress bar dialog
                            prgDialog.dismiss();
                        }
                    }
                }).start();
            }
        });
    }

    public int doSomeTasks() {
        while (fileSize <= 1000000) {
            fileSize++;
            if (fileSize == 100000) {
                return 10;
            } else if (fileSize == 200000) {
                return 20;
            } else if (fileSize == 300000) {
                return 30;
            }
        }
        return 100;
    }
}
