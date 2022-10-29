# ZeroError


**게임 퀘스트 형식으로 검수자의 흥미를 높여 업무 속도를 높이면서 앱 내 스캐너를 통해 상품 & 송장 검수 정확도도 높이는 꼼꼼한 검수 앱**


## Download

**Go to the [release page](https://github.com/PurpleCheck/Android/tree/main/app/release) to download the latest available apk**



## Screenshots

- 스캔한 상품이 주문 내역에 존재하지 않을 경우 경고창으로 알림

  <img src="https://user-images.githubusercontent.com/52341650/185955886-ad8856b4-6e6a-4257-b8b9-203476f20b74.gif" width="280" height="600"/>

- 스캔한 상품이 주문 내역에 존재하는 경우
  1. 검수 번호 (Inspect Id) 스캔
  2. 주문 내역에 맞게 상품 (Product Id) 스캔 - 달성율과 작업 독려 멘트가 상단에 실시간으로 표시됨
  3. 송장 번호 (Tracking Id) 스캔 - 상품 & 송장 검수 완료 후 다시 검수 번호 스캔 화면으로 돌아감
  
  <img src="https://user-images.githubusercontent.com/52341650/185955906-0f6f6b57-b31b-4474-a6a3-9e6448413228.gif" width="280" height="600"/>
  
- 전체 화면 설명
  <br/>
  <img src="https://user-images.githubusercontent.com/52341650/198842104-6598a94b-fca5-4b62-b313-e2fb2164600f.png" width="600" height="400"/>
  
  <img src="https://user-images.githubusercontent.com/52341650/198842103-4b96dd1b-0ee0-4577-b7aa-a2a256f56500.png" width="680" height="420"/>
  
  <img src="https://user-images.githubusercontent.com/52341650/198842101-c272ea8a-af7c-451a-a660-609838f9bc4e.png" width="600" height="380"/>


## Stack & Libraries

- **Minimum SDK level 23**

- **100% [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for asynchronous.**

- **JetPack**

  - **[LiveData](https://developer.android.com/topic/libraries/architecture/livedata?hl=ko) - observable data holder class**

  - **[Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle?hl=ko) - dispose observing data when lifecycle state changes.**
  - **[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel?gclid=EAIaIQobChMI04KT4pLa-QIV3sEWBR3gKgvoEAAYASAAEgKgZ_D_BwE&gclsrc=aw.ds) - UI related data holder, lifecycle aware.**
  - **[Room Persistence](https://developer.android.com/training/data-storage/room?hl=ko) - construct database.**
  - **[Material Components](https://material.io/components) - interactive building blocks for creating a user interface**

- **Architecture**

  - **MVVM Architecture (View - ViewModel - Model)**
  - **Repository Pattern**
  - **[Hilt](https://github.com/googlecodelabs/android-hilt) - dependency injection**

- **[Retrofit2 & Gson](https://github.com/square/retrofit) - constructing the REST API**

- **[ZXing](https://github.com/journeyapps/zxing-android-embedded) - Barcode Scanning library**

  

## Architecture

- **MVVM Architecture (View - ViewModel - Model)**
- **Repository Pattern**
- **[Hilt](https://github.com/googlecodelabs/android-hilt) - dependency injection**

<img width="500" alt="git1" src="https://user-images.githubusercontent.com/52341650/185891693-75cd6f6f-7713-404a-8a99-c68b0169d5ca.png">


## MAD Scorecard

<img width="500" alt="summary" src="https://user-images.githubusercontent.com/52341650/185891769-00b07387-aa47-4367-9485-405e7778b47e.png">
<img width="500" alt="kotlin" src="https://user-images.githubusercontent.com/52341650/185891827-f768e09c-de24-4b02-a5eb-9b6b8829b72b.png">



