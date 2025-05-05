# Ứng dụng Image Loader Android

Ứng dụng Android đơn giản minh họa tải ảnh từ URL, xử lý kết nối mạng, và background services.

## Tổng quan

Ứng dụng này cho phép người dùng:
- Nhập URL ảnh từ Internet và tải về để hiển thị
- Tự động kiểm tra và cập nhật trạng thái kết nối Internet  
- Chạy background service hiển thị notification mỗi 5 phút

## Yêu cầu hệ thống

- Android Studio Dolphin trở lên
- Android device/emulator (API 24+)
- Kết nối Internet

## Cài đặt và chạy

1. Clone repository này hoặc tải source code
2. Mở project trong Android Studio
3. Sync Project with Gradle Files
4. Chạy app trên emulator/device

## Hướng dẫn sử dụng

1. **Mở ứng dụng**
2. **Nhập URL ảnh** vào ô text (ví dụ: `https://picsum.photos/800/600`)
3. **Nhấn nút "Load Image"** để tải ảnh
4. **App sẽ kiểm tra kết nối Internet:**
   - ✅ Nếu có Internet: ảnh sẽ được tải và hiển thị
   - ❌ Nếu không có Internet: nút sẽ bị vô hiệu hóa và hiện thông báo
5. **Notification** từ background service sẽ xuất hiện mỗi 5 phút

## Kiến trúc ứng dụng

### 1. MainActivity
- UI chính của ứng dụng
- Xử lý input URL và hiển thị ảnh
- Quản lý BroadcastReceiver cho network connectivity
- Khởi động ImageLoaderService

### 2. LoadImageTask (AsyncTask)
- Tải ảnh từ URL trong background thread
- Hiển thị ảnh sau khi tải xong
- Xử lý lỗi và timeout

### 3. LoadImageTaskLoader (AsyncTaskLoader)
- Phiên bản cải tiến của AsyncTask
- Tự động xử lý configuration changes (xoay màn hình)
- Quản lý lifecycle tốt hơn

### 4. ImageLoaderService
- Background service chạy liên tục
- Hiển thị notification mỗi 5 phút
- Notification mở app khi nhấn vào

### 5. Quản lý kết nối mạng
- BroadcastReceiver theo dõi thay đổi network
- Cập nhật UI dựa trên trạng thái kết nối
- Tương thích API 24+

## Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

## Cấu trúc thư mục

```
app/
├── manifests/
│   └── AndroidManifest.xml
├── kotlin+java/com.example.imageloaderproject/
│   ├── MainActivity.kt
│   ├── LoadImageTask.kt
│   ├── LoadImageTaskLoader.kt
│   └── ImageLoaderService.kt
├── res/
│   ├── layout/
│   │   └── activity_main.xml
│   └── values/
│       ├── strings.xml
│       └── styles.xml
└── build.gradle.kts
```

## Các tính năng chính

### 1. Tải ảnh bất đồng bộ
- Tải ảnh không chặn UI thread
- Hiển thị progress message
- Xử lý lỗi

### 2. Theo dõi trạng thái mạng
- Kiểm tra kết nối real-time
- Cập nhật UI động
- Broadcast thay đổi mạng

### 3. Background Service
- Chạy độc lập với activities
- Notification định kỳ
- Khả năng foreground service

### 4. Giao diện linh hoạt
- Layout thích ứng
- Xử lý nhiều kích thước màn hình
- Hỗ trợ thay đổi cấu hình

## Khắc phục sự cố

### App bị crash
- Kiểm tra permissions trong manifest
- Đảm bảo layout files tồn tại
- Clean và rebuild project

### Ảnh không tải được
- Kiểm tra URL hợp lệ
- Xác nhận có kết nối Internet
- Kiểm tra logcat để debug

### Notification không hiển thị
- Kiểm tra permission POST_NOTIFICATIONS
- Xác nhận service được start
- Kiểm tra notification channel (API 26+)


