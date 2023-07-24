package com.example.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @PROJECT_NAME: finalTest
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/7/24 15:08
 */
public class ImageUtils {
    public static void compressAndSaveImage(String imagePath, String outputDir, String outputFileName, int maxWidth, int maxHeight, int quality) {
        // 读取图片文件
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        // 计算压缩比例
        float scale = calculateScale(bitmap.getWidth(), bitmap.getHeight(), maxWidth, maxHeight);

        // 设置缩放矩阵
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // 缩放图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

        // 释放原始图片资源
        bitmap.recycle();

        // 创建输出目录
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 创建输出文件
        File outputFile = new File(dir, outputFileName);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFile);

            // 将压缩后的图片写入输出文件
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            System.out.println("图片压缩成功!!!!!!!!!");

            // 刷新缓冲区
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static float calculateScale(int originalWidth, int originalHeight, int maxWidth, int maxHeight) {
        float scaleWidth = maxWidth / (float) originalWidth;
        float scaleHeight = maxHeight / (float) originalHeight;

        // 选择更小的缩放比例，确保图片压缩后不会超过目标宽度和高度
        return Math.min(scaleWidth, scaleHeight);
    }
}
