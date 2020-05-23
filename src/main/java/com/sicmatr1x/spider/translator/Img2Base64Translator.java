package com.sicmatr1x.spider.translator;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

public class Img2Base64Translator implements Translator {
    @Override
    public Element translate(Element imgElement) {
        String srcAddress = null;
        Attributes attrs = imgElement.attributes();
        Iterator<Attribute> iterator = attrs.iterator();
        while(iterator.hasNext()){
            Attribute attr = iterator.next();
            if(attr.getValue() != null && attr.getValue().contains("http")){
                srcAddress = attr.getValue();
            }
        }
        imgElement.attr("alt", srcAddress);
        String base64 = imageToBase64ByOnline(srcAddress);
        imgElement.attr("src", "data:image/png;base64," + base64);
        return imgElement;
    }

    /**
     * 在线图片转换成base64字符串
     * https://segmentfault.com/q/1010000009065824
     * @param imgURL 图片线上路径
     * @return
     */
    public static String imageToBase64ByOnline(String imgURL) {
        if (imgURL != null && imgURL.length() > 0) {
            return null;
        }

        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 创建URL
            URL url = new URL(imgURL);

            // 创建链接
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            is = conn.getInputStream();

            // 将内容读取内存中
            int len = -1;
            byte[] by = new byte[1024];
            while ((len = is.read(by)) != -1) {
                out.write(by, 0, len);
            }

            byte[] dataByte = out.toByteArray();
            //如果 图片经过nginx就会被压缩，返回头中包含 Content-Encoding:gzip 则需要对图片数据解压缩
            String contentEncoding = conn.getContentEncoding();
            if("gzip".equalsIgnoreCase(contentEncoding)){
                dataByte = uncompress(dataByte);
            }

            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(dataByte);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                // 关闭流
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    is = null;
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }

    /**
     * GZIP解压缩
     *
     * @param bytes 压缩内容
     * @return
     */
    public static byte[] uncompress(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPInputStream ungzip = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int n = 0;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            ungzip.close(); //这个会写入一些数据，所以要在out使用前调用

            return out.toByteArray();
        } finally {
            out.close();
        }
    }
}
