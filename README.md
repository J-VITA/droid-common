# droid-common
* 사용방법

* [app build.gradle]
* dependencies 추가
<pre>
<code>
implementation 'com.skyware:droid-commons:1.0.1-RELEASE'
</code>
</pre>
---
* [project build.gradle]
* 스크립트 추가 Maven
<pre>
<code>
buildscript {
    repositories {
        ...
        maven {url "http://1.221.205.250:9495/nexus/content/repositories/releases"}	//<-- 추가
    }
    ...
}

allprojects {
    repositories {
        ...
        maven {url "http://1.221.205.250:9495/nexus/content/repositories/releases"}	//<-- 추가
    }
}
</code>
</pre>
---
* **Notification Manager**
* FCM 서비스 등록 후 json 파일 배치 및 gradle 설정 후 FirebaseService를 Manifest service 등록 후 사용하면됨.
* (sendNotification은 디바이스에 노티알림을 위한 메서드임.)
<pre>
<code>
SkyNotification noti = new SkyNotification();
HashMap<String, String> info = new HashMap<>();
info.put("title", "타이틀");
info.put("body", "바디");

/**
* @params HashMap, Context, (int)icon, (android.net.Uri) sound
**/
noti.sendNotification(info, mContext, R.mipmap.ic_launcher, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
</code>
</pre>
---
* **SkyAsyncHttpClient**
<pre>
<code>
//사용 예시 : get method
public static void get(String url, RequestParams params, SkyAsyncHttpResponseHandler responseHandler)  {
    client.get(getAbsoluteUrl(url), params, responseHandler);
}
//use
get("{url}", params, new SkyJsonHttpResponseHandler(){
    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
    }
    ...
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
    }
    ...
});

※ post, put, delete 등 각 HTTP METHOD별 사용하기 편리하게 구현하여 사용하면 됨.
</code>
</pre>
---
* **SQLITE**
    * SkyDBHelper, SkyDBProvider, SkyDBData

Class | 의미 
---|:---:|
`SkyDBHelper` | 로컬 데이터베이스에 테이블 생성 역활(현재 user, history 테이블을 생성하게 되있음)
`SkyDBProvider` | 쿼리 공급자 역활수행
`SkyDBData` | user, history table 초기 값 설정(사용안해도됨)





---


> created by jayKim
> 인용 : AsycHttp & cz_msebera_android_httpclient 4.3.*
> _지속적으로 입맛에 맞게 변경하여 사용하기_