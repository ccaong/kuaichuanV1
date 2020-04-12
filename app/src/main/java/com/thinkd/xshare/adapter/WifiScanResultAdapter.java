//package com.thinkd.xshare.adapter;
//
//import android.content.Context;
//import android.net.wifi.ScanResult;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.thinkd.xshare.R;
//
//import java.util.List;
//
//
///**
// * 未使用，可以删除
// */
//
//public class WifiScanResultAdapter extends CommonAdapter<ScanResult> {
//    Context context;
//
//    public WifiScanResultAdapter(Context context, List<ScanResult> dataList) {
//        super(context, dataList);
//        this.context = context;
//    }
//
//    @Override
//    public View convertView(int position, View convertView) {
//        ScanResultHolder viewHolder = null;
//
//        if (convertView == null) {
//            convertView = View.inflate(context, R.layout.item_wifi_scan_result, null);
//            viewHolder = new ScanResultHolder();
//            viewHolder.iv_device = (ImageView) convertView.findViewById(R.id.iv_device);
//            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
//            viewHolder.tv_mac = (TextView) convertView.findViewById(R.id.tv_mac);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ScanResultHolder) convertView.getTag();
//        }
//        ScanResult scanResult = getDataList().get(position);
//        if (scanResult != null) {
//            String iconid = scanResult.SSID.substring(1,2);
//            int temp = iconid.charAt(0);
//            int icon = temp%8;
//            viewHolder.tv_name.setText(scanResult.SSID);
//            viewHolder.tv_mac.setText(scanResult.BSSID);
//            switch (icon){
//                case 0:
//                    viewHolder.iv_device.setImageResource(R.mipmap.avatar1);
//                    break;
//                case 1:
//                    viewHolder.iv_device.setImageResource(R.mipmap.avatar1);
//                    break;
//                case 2:
//                    viewHolder.iv_device.setImageResource(R.mipmap.avatar1);
//                    break;
//                case 3:
//                    viewHolder.iv_device.setImageResource(R.mipmap.avatar1);
//                    break;
//                case 4:
//                    viewHolder.iv_device.setImageResource(R.mipmap.avatar1);
//                    break;
//                case 5:
//                    viewHolder.iv_device.setImageResource(R.mipmap.avatar1);
//                    break;
//                case 6:
//                    viewHolder.iv_device.setImageResource(R.mipmap.avatar1);
//                    break;
//                case 7:
//                    viewHolder.iv_device.setImageResource(R.mipmap.avatar1);
//                    break;
//                default:
//                    viewHolder.iv_device.setImageResource(R.mipmap.avatar1);
//                    break;
//            }
//
//
//        }
//        return convertView;
//    }
//
//    static class ScanResultHolder {
//        ImageView iv_device;
//        TextView tv_name;
//        TextView tv_mac;
//    }
//}
