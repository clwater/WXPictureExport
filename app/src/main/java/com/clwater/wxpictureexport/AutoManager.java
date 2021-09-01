package com.clwater.wxpictureexport;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public enum AutoManager {
    INSTANCE;
    String TAG = "AutoManager";

    Context context;
    private String packageName = "com.tencent.mm";
    private String activiyName = "com.tencent.mm.ui.AlbumUI";

    private WxPictureExportService service;
    private AccessibilityEvent event;

    public void init(WxPictureExportService wxPictureExportService,
                     AccessibilityEvent event){
        this.service = wxPictureExportService;
        this.event = event;


    }

    public void start(Context context){
        this.context = context;
        service = WxPictureExportService.service;
        openWX();


        new Thread(() -> {
            try {
                Thread.sleep(3000);
                isMyPicture();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private boolean isMyPicture() {
        String packages = event.getPackageName().toString();
        String name = event.getClassName().toString();
        Log.d(TAG, "isMyPicture: packages " + packages);
        Log.d(TAG, "isMyPicture: name " + name);
        return (packageName.equals(packages) && name.equals(activiyName));
    }

    private void showInfo(List<AccessibilityNodeInfo> nodeInfos){
        for (AccessibilityNodeInfo nodeInfo: nodeInfos){
            Log.d(TAG, "nodeInfo: " + nodeInfo.getText().toString() + " " + nodeInfo.getClassName());
        }
    }

    //点击我
    private void clickMe() {
        List<AccessibilityNodeInfo> nodeInfos = findNodesByText("我");
        showInfo(nodeInfos);
        if (nodeInfos.size() == 1){
            AccessibilityNodeInfo node = nodeInfos.get(0);
            node = node.getParent();
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }


    private AccessibilityNodeInfo getRootNodeInfo() {
        AccessibilityEvent curEvent = event;
        AccessibilityNodeInfo nodeInfo = null;
        // 建议使用getRootInActiveWindow，这样不依赖当前的事件类型
        if (service != null) {
            nodeInfo = service.getRootInActiveWindow();
//            Log.d(TAG, "nodeInfo: " + nodeInfo);
        }
        // 下面这个必须依赖当前的AccessibilityEvent
        return nodeInfo;
    }

    public List<AccessibilityNodeInfo> findNodesByText(String text) {
        return findNodesByText(text, false);
    }

    /**
     * 根据Text搜索所有符合条件的节点
     */
    public List<AccessibilityNodeInfo> findNodesByText(String text, boolean isVague) {
        AccessibilityNodeInfo nodeInfo = getRootNodeInfo();
        if (nodeInfo != null) {
            if (isVague) {
                return nodeInfo.findAccessibilityNodeInfosByText(text);
            }else {
                List<AccessibilityNodeInfo> nodeInfos = new ArrayList<>();
                for (AccessibilityNodeInfo nodeInfo1: nodeInfo.findAccessibilityNodeInfosByText(text)){
                    if (text.equals(nodeInfo1.getText().toString())){
                        nodeInfos.add(nodeInfo1);
                    }
                }
                return nodeInfos;
            }
        }


        return null;
    }

    //打开微信
    public void openWX(){
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage("com.tencent.mm");
        context.startActivity(intent);
    }




}
