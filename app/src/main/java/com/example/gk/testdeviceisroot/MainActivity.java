package com.example.gk.testdeviceisroot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_root()) {
                    Toast.makeText(getApplicationContext(), "设备已经具有ROOT权限!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "设备暂未获得ROOT权限!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn_check_app_have_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upgradeRootPermission()) {
                    Toast.makeText(getApplicationContext(), "APP已经具有ROOT权限!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "APP暂未获得ROOT权限!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRoot();
            }
        });
    }

    // 获取Root权限
    public void getRoot() {
        try {
            Runtime.getRuntime().exec("su");
            Toast.makeText(getApplicationContext(), "成功获取ROOT权限!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "获取ROOT权限时出错!", Toast.LENGTH_SHORT).show();
        }
    }


    // 判断是否具有ROOT权限
    public static boolean is_root() {
        int i = execRootCmdSilent("echo test"); // 通过执行测试命令来检测
        if (i != -1) {
            return true;
        }
        return false;
    }

    /**
     * su模式运行命令
     *
     * @param paramString
     * @return
     */
    protected static int execRootCmdSilent(String paramString) {
        try {
            Process localProcess = Runtime.getRuntime().exec("su");
            Object localObject = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(
                    (OutputStream) localObject);
            String str = String.valueOf(paramString);
            localObject = str + "\n";
            localDataOutputStream.writeBytes((String) localObject);
            localDataOutputStream.flush();
            localDataOutputStream.writeBytes("exit\n");
            localDataOutputStream.flush();
            localProcess.waitFor();
            int result = localProcess.exitValue();
            return (Integer) result;
        } catch (Exception localException) {
            localException.printStackTrace();
            return -1;
        }
    }

    /**
     * 判断app是否获取到root权限
     *
     * @return
     */
    public static boolean upgradeRootPermission() {
        Process process = null;
        DataOutputStream os = null;
        try {

            Log.i("roottest", "try it");
            String cmd = "touch /data/roottest.txt";
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }
}
