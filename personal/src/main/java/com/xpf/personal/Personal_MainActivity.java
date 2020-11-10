package com.xpf.personal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.xpf.common.RecordPathManager;
import com.xpf.common.utils.Config;

public class Personal_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_activity_main);
    }

    public void jumpApp(View view) {
        // 类加载方式交互，需要准确的全类名路径，维护成本较高且容易出现人为失误
        /*try{
            Class targetClass = Class.forName("com.xpf.module_interaction.MainActivity");
            Intent intent = new Intent(this, targetClass);
            intent.putExtra("params","xpf");
            startActivity(intent);
        }catch(ClassNotFoundException e) {
            e.printStackTrace();
        }*/

        //全局Map记录路径信息方式实现交互(跳转)
        Class<?> targetClass = RecordPathManager.getTargetClass("app", "MainActivity");
        if (targetClass == null) {
            Log.d(Config.TAG, "获取targetClass为空...");
        }
        Intent intent = new Intent(this, targetClass);
        intent.putExtra("name", "xpf");
        startActivity(intent);
    }

    public void jumpOrder(View view) {
        // 类加载方式交互，需要准确的全类名路径，维护成本较高且容易出现人为失误
        /*try{
            Class targetClass = Class.forName("com.xpf.order.Order_MainActivity");
            Intent intent = new Intent(this, targetClass);
            intent.putExtra("params","xpf");
            startActivity(intent);
        }catch(ClassNotFoundException e) {
            e.printStackTrace();
        }*/

        //全局Map记录路径信息方式实现交互(跳转)
        Class<?> targetClass = RecordPathManager.getTargetClass("order", "Order_MainActivity");
        if (targetClass == null) {
            Log.d(Config.TAG, "获取targetClass为空...");
        }
        Intent intent = new Intent(this, targetClass);
        intent.putExtra("name", "xpf");
        startActivity(intent);
    }
}
