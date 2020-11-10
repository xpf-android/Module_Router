package com.xpf.module_router;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xpf.api.ARouterLoadPath;
import com.xpf.module_router.test.ARouter$$Group$$order;
import com.xpf.module_router.test.ARouter$$Group$$personal;
import com.xpf.order.Order_MainActivity;
import com.xpf.personal.Personal_MainActivity;
import com.xpf.router_annotation.ARouter;
import com.xpf.router_annotation.modle.RouterBean;

import java.util.Map;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * 跳转至order模块的Order_MainActivity
     */
    public void jumpOrder(View view) {
        //最终集成化模式，所有子模块APT生成的类文件都会打包到apk中
        ARouter$$Group$$order loadGroup = new ARouter$$Group$$order();
        Map<String, Class<? extends ARouterLoadPath>> groupMap = loadGroup.loadGroup();
        //app--->personal
        Class<? extends ARouterLoadPath> clazz = groupMap.get("order");
        //类加载技术
        try {
            ARouterLoadPath path = clazz.newInstance();
            Map<String, RouterBean> pathMap = path.loadPath();
            //获取/order/Order_MainActivity
            RouterBean routerBean = pathMap.get("/order/Order_MainActivity");
            if (routerBean != null) {
                Intent intent = new Intent(this, routerBean.getClazz());
                intent.putExtra("name", "xpf");
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转至personal的Personal_MainActivity
     */
    public void jumpPersonal(View view) {
        //最终集成化模式，所有子模块APT生成的类文件都会打包到apk中
        ARouter$$Group$$personal loadGroup = new ARouter$$Group$$personal();
        Map<String, Class<? extends ARouterLoadPath>> groupMap = loadGroup.loadGroup();
        //app--->personal
        Class<? extends ARouterLoadPath> clazz = groupMap.get("personal");
        //类加载技术
        try {
            ARouterLoadPath path = clazz.newInstance();
            Map<String, RouterBean> pathMap = path.loadPath();
            //获取/personal/Personal_MainActivity
            RouterBean routerBean = pathMap.get("/personal/Personal_MainActivity");
            if (routerBean != null) {
                Intent intent = new Intent(this, routerBean.getClazz());
                intent.putExtra("name", "xpf");
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
