package com.example.utils;

/**
 * @PROJECT_NAME: finalTest
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/7/19 17:01
 */

import android.os.Build;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class OssUploader {
    //    private static final String OSS_ENDPOINT = "https://transferapi.imreliable.com";///hardware/test/uploadFile
    private static final String BUCKET_NAME = "https://reliable-b-custom.oss-cn-qingdao.aliyuncs.com";
    private static String  store_point[] = new String[3];
    private static final String OSS_API_TOKEN_URL = "https://transferapi.imreliable.com/hardware/test/ossToken";
    private static String urlf = " ";
    private static Map map = new HashMap<>();
    private static String dir_store;

    //String localImagePath
    public static CompletableFuture<String[]> uploadImageToOSS(final String localImagePath) {
        CompletableFuture<String[]> future = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            future = new CompletableFuture<>();
        }
        final OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data");//"multipart/form-data","image/jpeg"
        RequestBody body = RequestBody.create(mediaType, new File(localImagePath));//new File(localImagePath)

        Request request = new Request.Builder()
                .url(OSS_API_TOKEN_URL)
                .post(body)
                .build();

        CompletableFuture<String[]> finalFuture = future;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败时的处理逻辑
                e.printStackTrace();
                System.out.println("上传失败！");
                System.out.println("错误信息：" + e.getMessage());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    finalFuture.complete(null); // 将结果设置为null，表示请求失败
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    System.out.println("图片上传成功" + responseBody);
                    // 解析服务器返回的签名信息JSON数据
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        JSONObject signData = json.getJSONObject("data");
                        String accessid = signData.getString("accessid");
                        String host = signData.getString("host");
                        String policy = signData.getString("policy");
                        String signature = signData.getString("signature");
                        String dir = signData.getString("dir");
                        dir_store = dir;
                        System.out.println("HOST:" + host);
                        // 构建上传请求
                        Map<String, String> formDataParams = new HashMap<>();
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        formDataParams.put("key", dir + "photo_hardware_" + System.currentTimeMillis() + ".png");
                        formDataParams.put("policy", policy);
                        formDataParams.put("OSSAccessKeyId", accessid);
                        formDataParams.put("success_action_status", "200");
                        formDataParams.put("signature", signature);
                        String objectKey = dir + "photo_hardware_" + System.currentTimeMillis() + ".png";
                        map = formDataParams;
                        urlf = objectKey;
                        final String url = host + objectKey;

                        // 添加文件参数
                        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM);

                        for (Map.Entry<String, String> param : formDataParams.entrySet()) {
                            requestBodyBuilder.addFormDataPart(param.getKey(), param.getValue());
                        }
                        //file.getName(),file
                        File file = new File(localImagePath);
//                        System.out.println("名字："+file.getName());
                        requestBodyBuilder.addFormDataPart("file", file.getName(),
                                RequestBody.create(MediaType.parse("image/png"), file));

                        Request uploadRequest = new Request.Builder()
                                .url(host)
                                .post(requestBodyBuilder.build())
                                .build();
                        client.newCall(uploadRequest).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                System.out.println("服务器返回失败！------2");
                                // 处理上传失败的情况
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    finalFuture.complete(null);
                                }

                            }

                            @Override
                            public void onResponse(Call call, Response uploadRequest) throws IOException {
                                if (!uploadRequest.isSuccessful()) {
                                    // 处理上传失败的情况
                                    System.out.println("服务器返回失败！------2");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        finalFuture.complete(null);
                                    }
                                    return;
                                }
                                // 获取上传结果
                                String uploadResult = uploadRequest.body().string();
                                System.out.println("上传成功uploadResult："+uploadResult);
                                System.out.println("上传成功URL1111111111111：" + url);
//                                String NAME ="photo/20230630/photo_1688113243635_6416301527640.png";
                                // store_point = identifyKeyPoints(urlf);
                                String back[] = identifyKeyPoints(urlf);
                                store_point[0] = back[0];
                                store_point[1] = back[1];
//                                segmentImage(urlf);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    segmentImage(urlf).thenAccept(result -> {
//                                        System.out.println("图像切割和图片上传成功！");
//                                        finalFuture.complete(store_point);
//                                    }).exceptionally(ex -> {
//                                        System.out.println("图像切割或图片上传失败！");
//                                        finalFuture.completeExceptionally(ex);
//                                        return null;
//                                    });
//                                }
                                store_point[2] = objectKey;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    finalFuture.complete(store_point);
                                }
                                // 处理上传成功的情况

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("服务器返回失败！------1");
                        // JSON解析出错时的处理逻辑
                    }finally{
                        if (response.body() != null) {
                            response.body().close();
                        }
                    }
                } else {
                    // 服务器返回不成功时的处理逻辑
                    System.out.println("服务器处理请求返回失败！");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        finalFuture.complete(null);
                    }
                }
            }
        });
        return future;
    }

    //关键点识别
    public static String[] identifyKeyPoints(String imageUrl) {
        try {
            String apiEndpoint = "https://transferapi.imreliable.com/hardware/test/pointIdentification";
            String fullUrl = apiEndpoint + "?imgName=" + imageUrl;

            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject json = new JSONObject(response.toString());
                JSONArray listArray = json.getJSONObject("data").getJSONArray("person_info");
                JSONObject firstPersonInfo = listArray.getJSONObject(0);
                JSONObject body_parts = firstPersonInfo.getJSONObject("body_parts");
                JSONObject location = firstPersonInfo.getJSONObject("location");
                String[] strings = new String[2];
                strings[0] = body_parts.toString();
                strings[1] = location.toString();

                System.out.println("关键点识别结果：" + response.toString());
                connection.disconnect();
                return strings;
            } else {
                System.out.println("关键点识别失败，错误代码：" + responseCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("关键点识别失败，错误信息：" + e.getMessage());
        }
        return null;
    }


    //图像切割  http://127.0.0.1:8080/hardware/test/imgSegmentation?imgName=photo/20230630/photo_1688113243635_6416301527640.png
    public static  CompletableFuture<Void>  segmentImage(String imageName, String i, String url_temp) {
        CompletableFuture<Void> future = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            future = new CompletableFuture<>();
        }
        try {
            String apiEndpoint = "https://transferapi.imreliable.com/hardware/test/imgSegmentation";
            String fullUrl = apiEndpoint + "?imgName=" + imageName;

            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("图像切割结果：" + response.toString());
                JSONObject json = new JSONObject(String.valueOf(response));
                JSONObject signData = json.getJSONObject("data");
                String foreground = signData.getString("foreground");
                String savePath = "/storage/emulated/0/Pictures/MyPictures/image"+ i +".png";

                byte[] decodedImage = new byte[0];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    decodedImage = Base64.getDecoder().decode(foreground);
                }
                Path outputPath = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    outputPath = Paths.get(savePath);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Files.write(outputPath, decodedImage);
                }

                String outputDir = "/storage/emulated/0/Pictures/MyPictures/"; // 输出目录
                String outputFileName = "image"+ i +".png"; // 输出文件名
                int maxWidth = 263; // 图片最大宽度
                int maxHeight = 500; // 图片最大高度
                int quality = 80; // 图片质量，范围 0-100

                ImageUtils.compressAndSaveImage(savePath, outputDir, outputFileName, maxWidth, maxHeight, quality);
                System.out.println("图片保存成功，保存路径：" + savePath);
                uploadToOSS1(savePath, url_temp);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    CompletableFuture<Void> finalFuture = future;
//                    future.thenCompose(ignored -> uploadToOSS1(savePath)).thenAccept(result -> {
//                        System.out.println("图片上传到OSS成功！");
//                        finalFuture.complete(null);
//                    }).exceptionally(ex -> {
//                        System.out.println("图片上传到OSS失败！");
//                        finalFuture.completeExceptionally(ex);
//                        return null;
//                    });
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    CompletableFuture<Void> finalFuture = future;
//
//                    uploadToOSS(savePath).thenAccept(result -> {
//                        // 处理上传完成后的逻辑
//                        System.out.println("图片上传到OSS成功！");
//                        finalFuture.complete(null); // 完成 CompletableFuture
//                    }).exceptionally(ex -> {
//                        // 处理上传失败的情况
//                        ex.printStackTrace();
//                        finalFuture.completeExceptionally(ex);
//                        return null;
//                    });
//                }

            } else {
                System.out.println("图像切割失败，错误代码：" + responseCode);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    future.completeExceptionally(new IOException("图像切割失败，错误代码：" + responseCode));
                }
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("图像切割失败，错误信息：" + e.getMessage());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                future.completeExceptionally(e);
            }
        }
        return future;
    }



    //压缩后图片上传阿里云
    public static CompletableFuture<Void> uploadToOSS(String localImagePath) {
        CompletableFuture<Void> future = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            future = new CompletableFuture<>();
        }
        final OkHttpClient client = new OkHttpClient();
        File file = new File(urlf);
        String Imagename = file.getName();
        // 构建上传请求
        Map<String, String> formDataParams = map;
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        map.put("key", dir_store + Imagename);
        String objectKey = dir_store + Imagename;
        String url = BUCKET_NAME + objectKey;

        // 添加文件参数
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (Map.Entry<String, String> param : formDataParams.entrySet()) {
            requestBodyBuilder.addFormDataPart(param.getKey(), param.getValue());
        }

        File file1 = new File(localImagePath);
        requestBodyBuilder.addFormDataPart("file", file1.getName(),
                RequestBody.create(MediaType.parse("image/png"), file1));

        Request uploadRequest = new Request.Builder()
                .url(BUCKET_NAME)
                .post(requestBodyBuilder.build())
                .build();

        // 执行上传请求
        CompletableFuture<Void> finalFuture = future;
        client.newCall(uploadRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                System.out.println("上传到OSS失败！");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    finalFuture.completeExceptionally(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    // 上传成功的处理逻辑
                    System.out.println("上传到OSS成功！");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        finalFuture.complete(null);
                    }
                } else {
                    // 上传失败的处理逻辑
                    System.out.println("上传到OSS失败！");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        finalFuture.completeExceptionally(new IOException("上传到OSS失败！"));
                    }
                }
            }
        });

        //关键点、图像切割、压缩
        return future;
    }

        //压缩后图片上传阿里云
    public static CompletableFuture<Void> uploadToOSS1(String localImagePath, String url_temp) {
        final OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data");//"multipart/form-data","image/jpeg"
        RequestBody body = RequestBody.create(mediaType, new File(localImagePath));//new File(localImagePath)

        Request request = new Request.Builder()
                .url(OSS_API_TOKEN_URL)
                .post(body)
                .build();

        CompletableFuture<Void> future = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            future = new CompletableFuture<>();
        }

        CompletableFuture<Void> finalFuture = future;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败时的处理逻辑
                e.printStackTrace();
                System.out.println("上传失败！");
                System.out.println("错误信息：" + e.getMessage());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    finalFuture.completeExceptionally(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    System.out.println("图片上传成功" + responseBody);
                    // 解析服务器返回的签名信息JSON数据
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        JSONObject signData = json.getJSONObject("data");
                        String accessid = signData.getString("accessid");
                        String host = signData.getString("host");
                        String policy = signData.getString("policy");
                        String signature = signData.getString("signature");
                        String dir = signData.getString("dir");
                        System.out.println("HOST:" + host);
                        File file = new File(url_temp);
                        String Imagename = file.getName();
                        // 构建上传请求
                        Map<String, String> formDataParams = new HashMap<>();
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        formDataParams.put("key", dir + Imagename);
                        formDataParams.put("policy", policy);
                        formDataParams.put("OSSAccessKeyId", accessid);
                        formDataParams.put("success_action_status", "200");
                        formDataParams.put("signature", signature);
                        String objectKey = dir + Imagename;
                        String url = host + objectKey;

                        // 添加文件参数
                        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM);

                        for (Map.Entry<String, String> param : formDataParams.entrySet()) {
                            requestBodyBuilder.addFormDataPart(param.getKey(), param.getValue());
                        }

                        File file1 = new File(localImagePath);
                        requestBodyBuilder.addFormDataPart("file", file1.getName(),
                                RequestBody.create(MediaType.parse("image/png"), file1));

                        Request uploadRequest = new Request.Builder()
                                .url(host)
                                .post(requestBodyBuilder.build())
                                .build();
                        client.newCall(uploadRequest).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                System.out.println("服务器返回失败！------2");
                                // 处理上传失败的情况
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    finalFuture.completeExceptionally(e);
                                }
                            }

                            @Override
                            public void onResponse(Call call, Response uploadRequest) throws IOException {
                                if (!uploadRequest.isSuccessful()) {
                                    // 处理上传失败的情况
                                    System.out.println("服务器返回失败！------2");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        finalFuture.completeExceptionally(new IOException("服务器返回失败！"));
                                    }
                                }
                                // 获取上传结果
                                String uploadResult = uploadRequest.body().string();
                                System.out.println("上传成功uploadResult："+uploadResult);
                                // 将url传递给需要使用的地方进行处理、显示等
                                System.out.println("切割后上传成功URL2222222222222：" + url);
                                // 处理上传成功的情况
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    finalFuture.complete(null); // 完成 CompletableFuture
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("服务器返回失败！------1");
                        // JSON解析出错时的处理逻辑
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            finalFuture.completeExceptionally(e);
                        }
                    }
                } else {
                    // 服务器返回不成功时的处理逻辑
                    System.out.println("服务器处理请求返回失败！");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        finalFuture.completeExceptionally(new IOException("服务器返回失败！"));
                    }
                }
            }
        });
        //关键点、图像切割、压缩
        return future;
    }
}
//
//    //压缩后图片上传阿里云
//    public static CompletableFuture<Void> uploadToOSS(String localImagePath) {
//        final OkHttpClient client = new OkHttpClient();
//
//        MediaType mediaType = MediaType.parse("multipart/form-data");//"multipart/form-data","image/jpeg"
//        RequestBody body = RequestBody.create(mediaType, new File(localImagePath));//new File(localImagePath)
//
//        Request request = new Request.Builder()
//                .url(OSS_API_TOKEN_URL)
//                .post(body)
//                .build();
//
//
//        CompletableFuture<Void> future = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            future = new CompletableFuture<>();
//        }
//
//
//        CompletableFuture<Void> finalFuture = future;
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // 请求失败时的处理逻辑
//                e.printStackTrace();
//                System.out.println("上传失败！");
//                System.out.println("错误信息：" + e.getMessage());
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    finalFuture.completeExceptionally(e);
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String responseBody = response.body().string();
//                    System.out.println("图片上传成功" + responseBody);
//                    // 解析服务器返回的签名信息JSON数据
//                    try {
//                        JSONObject json = new JSONObject(responseBody);
//                        JSONObject signData = json.getJSONObject("data");
//                        String accessid = signData.getString("accessid");
//                        String host = signData.getString("host");
//                        String policy = signData.getString("policy");
//                        String signature = signData.getString("signature");
//                        String dir = signData.getString("dir");
//                        System.out.println("HOST:" + host);
//                        File file = new File(urlf);
//                        String Imagename = file.getName();
//                        // 构建上传请求
//                        Map<String, String> formDataParams = new HashMap<>();
////                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//                        formDataParams.put("key", dir + Imagename);
//                        formDataParams.put("policy", policy);
//                        formDataParams.put("OSSAccessKeyId", accessid);
//                        formDataParams.put("success_action_status", "200");
//                        formDataParams.put("signature", signature);
//                        String objectKey = dir + Imagename;
//                        String url = host + objectKey;
//
//                        // 添加文件参数
//                        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
//                                .setType(MultipartBody.FORM);
//
//                        for (Map.Entry<String, String> param : formDataParams.entrySet()) {
//                            requestBodyBuilder.addFormDataPart(param.getKey(), param.getValue());
//                        }
//
//                        File file1 = new File(localImagePath);
//                        requestBodyBuilder.addFormDataPart("file", file1.getName(),
//                                RequestBody.create(MediaType.parse("image/png"), file1));
//
//                        Request uploadRequest = new Request.Builder()
//                                .url(host)
//                                .post(requestBodyBuilder.build())
//                                .build();
//                        client.newCall(uploadRequest).enqueue(new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                e.printStackTrace();
//                                System.out.println("服务器返回失败！------2");
//                                // 处理上传失败的情况
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    finalFuture.completeExceptionally(e);
//                                }
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response uploadRequest) throws IOException {
//                                if (!uploadRequest.isSuccessful()) {
//                                    // 处理上传失败的情况
//                                    System.out.println("服务器返回失败！------2");
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                        finalFuture.completeExceptionally(new IOException("服务器返回失败！"));
//                                    }
//                                }
//                                // 获取上传结果
//                                String uploadResult = uploadRequest.body().string();
//                                System.out.println("上传成功uploadResult："+uploadResult);
//                                // 将url传递给需要使用的地方进行处理、显示等
//                                System.out.println("切割后上传成功URL2222222222222：" + url);
//                                // 处理上传成功的情况
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    finalFuture.complete(null); // 完成 CompletableFuture
//                                }
//                            }
//                        });
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        System.out.println("服务器返回失败！------1");
//                        // JSON解析出错时的处理逻辑
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            finalFuture.completeExceptionally(e);
//                        }
//                    }
//                } else {
//                    // 服务器返回不成功时的处理逻辑
//                    System.out.println("服务器处理请求返回失败！");
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        finalFuture.completeExceptionally(new IOException("服务器返回失败！"));
//                    }
//                }
//            }
//        });
//        //关键点、图像切割、压缩
//        return future;
//    }