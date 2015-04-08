[33mcommit 5cb704a54d31fce109aa59703f48318958960201[m
Merge: 0b8df22 6c23035
Author: baptiste_martin <martin_baptiste@live.fr>
Date:   Wed Apr 8 18:16:35 2015 +0200

    end register push, and clean android project

[1mdiff --cc DroneApplication/.gitignore[m
[1mindex 1406630,1406630..3e64901[m
[1m--- a/DroneApplication/.gitignore[m
[1m+++ b/DroneApplication/.gitignore[m
[36m@@@ -4,3 -4,3 +4,4 @@@[m [mlocal.propertie[m
  .DS_Store[m
  build[m
  *.iml[m
[32m++gradle/[m
[1mdiff --cc DroneApplication/app/app.iml[m
[1mindex 23c43e8,b81104e..0000000[m
[1mdeleted file mode 100644,100644[m
[1m--- a/DroneApplication/app/app.iml[m
[1m+++ /dev/null[m
[36m@@@ -1,97 -1,93 +1,0 @@@[m
[31m--<?xml version="1.0" encoding="UTF-8"?>[m
[31m--<module external.linked.project.path="$MODULE_DIR$" external.root.project.path="$MODULE_DIR$/.." external.system.id="GRADLE" external.system.module.group="DroneApplication" external.system.module.version="unspecified" type="JAVA_MODULE" version="4">[m
[31m--  <component name="FacetManager">[m
[31m--    <facet type="android-gradle" name="Android-Gradle">[m
[31m--      <configuration>[m
[31m--        <option name="GRADLE_PROJECT_PATH" value=":app" />[m
[31m--      </configuration>[m
[31m--    </facet>[m
[31m--    <facet type="android" name="Android">[m
[31m--      <configuration>[m
[31m--        <option name="SELECTED_BUILD_VARIANT" value="debug" />[m
[31m -        <option name="SELECTED_TEST_ARTIFACT" value="_android_test_" />[m
[31m--        <option name="ASSEMBLE_TASK_NAME" value="assembleDebug" />[m
[31m--        <option name="COMPILE_JAVA_TASK_NAME" value="compileDebugSources" />[m
[31m--        <option name="ASSEMBLE_TEST_TASK_NAME" value="assembleDebugAndroidTest" />[m
[31m--        <option name="SOURCE_GEN_TASK_NAME" value="generateDebugSources" />[m
[31m--        <option name="TEST_SOURCE_GEN_TASK_NAME" value="generateDebugAndroidTestSources" />[m
[31m--        <option name="ALLOW_USER_CONFIGURATION" value="false" />[m
[31m--        <option name="MANIFEST_FILE_RELATIVE_PATH" value="/src/main/AndroidManifest.xml" />[m
[31m--        <option name="RES_FOLDER_RELATIVE_PATH" value="/src/main/res" />[m
[31m--        <option name="RES_FOLDERS_RELATIVE_PATH" value="file://$MODULE_DIR$/src/main/res" />[m
[31m--        <option name="ASSETS_FOLDER_RELATIVE_PATH" value="/src/main/assets" />[m
[31m--      </configuration>[m
[31m--    </facet>[m
[31m--  </component>[m
[31m--  <component name="NewModuleRootManager" inherit-compiler-output="false">[m
[31m--    <output url="file://$MODULE_DIR$/build/intermediates/classes/debug" />[m
[31m--    <output-test url="file://$MODULE_DIR$/build/intermediates/classes/androidTest/debug" />[m
[31m--    <exclude-output />[m
[31m--    <content url="file://$MODULE_DIR$">[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/build/generated/source/r/debug" isTestSource="false" generated="true" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/build/generated/source/aidl/debug" isTestSource="false" generated="true" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/build/generated/source/buildConfig/debug" isTestSource="false" generated="true" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/build/generated/source/rs/debug" isTestSource="false" generated="true" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/build/generated/res/rs/debug" type="java-resource" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/build/generated/res/generated/debug" type="java-resource" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/build/generated/source/r/androidTest/debug" isTestSource="true" generated="true" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/build/generated/source/aidl/androidTest/debug" isTestSource="true" generated="true" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/build/generated/source/buildConfig/androidTest/debug" isTestSource="true" generated="true" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/build/generated/source/rs/androidTest/debug" isTestSource="true" generated="true" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/build/generated/res/rs/androidTest/debug" type="java-test-resource" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/build/generated/res/generated/androidTest/debug" type="java-test-resource" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/debug/res" type="java-resource" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/debug/resources" type="java-resource" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/debug/assets" type="java-resource" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/debug/aidl" isTestSource="false" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/debug/java" isTestSource="false" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/debug/jni" isTestSource="false" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/debug/rs" isTestSource="false" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/main/res" type="java-resource" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/main/resources" type="java-resource" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/main/assets" type="java-resource" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/main/aidl" isTestSource="false" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/main/java" isTestSource="false" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/main/jni" isTestSource="false" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/main/rs" isTestSource="false" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/androidTest/res" type="java-test-resource" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/androidTest/resources" type="java-test-resource" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/androidTest/assets" type="java-test-resource" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/androidTest/aidl" isTestSource="true" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/androidTest/java" isTestSource="true" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/androidTest/jni" isTestSource="true" />[m
[31m--      <sourceFolder url="file://$MODULE_DIR$/src/androidTest/rs" isTestSource="true" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/assets" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/bundles" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/classes" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/coverage-instrumented-classes" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/dependency-cache" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/dex" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/dex-cache" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/incremental" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/jacoco" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/javaResources" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/libs" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/lint" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/manifests" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/ndk" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/pre-dexed" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/proguard" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/res" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/rs" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/intermediates/symbols" />[m
[31m--      <excludeFolder url="file://$MODULE_DIR$/build/outputs" />[m
[31m -      <excludeFolder url="file://$MODULE_DIR$/build/tmp" />[m
[31m--    </content>[m
[31m--    <orderEntry type="jdk" jdkName="Android API 21 Platform" jdkType="Android SDK" />[m
[31m--    <orderEntry type="sourceFolder" forTests="false" />[m
[31m- <<<<<<< HEAD[m
[31m--    <orderEntry type="library" exported="" name="support-v4-13.0.0" level="project" />[m
[31m-     <orderEntry type="library" exported="" name="play-services-3.1.59" level="project" />[m
[31m- =======[m
[31m--    <orderEntry type="library" exported="" name="svg-android-1.1" level="project" />[m
[31m-     <orderEntry type="library" exported="" name="support-v4-22.0.0" level="project" />[m
[31m-     <orderEntry type="library" exported="" name="support-annotations-22.0.0" level="project" />[m
[31m-     <orderEntry type="library" exported="" name="appcompat-v7-22.0.0" level="project" />[m
[31m- >>>>>>> c194c890925a4e06e5b5f596a3a21818ea4fab69[m
[31m -    <orderEntry type="library" exported="" name="play-services-3.1.59" level="project" />[m
[31m--  </component>[m
[31m--</module>[m
[31m--[m
[1mdiff --cc DroneApplication/app/build.gradle[m
[1mindex 0c6519a,29ae26d..3d2cce5[m
[1m--- a/DroneApplication/app/build.gradle[m
[1m+++ b/DroneApplication/app/build.gradle[m
[36m@@@ -5,8 -5,8 +5,8 @@@[m [mandroid [m
      buildToolsVersion "21.1.2"[m
  [m
      defaultConfig {[m
[31m--        applicationId "fr.m2gla.istic.droneapplication"[m
[31m-         minSdkVersion 19[m
[31m -        minSdkVersion 15[m
[32m++        applicationId "fr.m2gla.istic.project"[m
[32m++        minSdkVersion 16[m
          targetSdkVersion 21[m
          versionCode 1[m
          versionName "1.0"[m
[36m@@@ -21,6 -21,5 +21,6 @@@[m
  [m
  dependencies {[m
      compile fileTree(dir: 'libs', include: ['*.jar'])[m
[31m -    compile 'com.google.android.gms:play-services:3.1.+'[m
[32m +    compile 'com.android.support:support-v13:21.0.3'[m
[31m-     compile 'com.google.android.gms:play-services:3.1.+'[m
[32m++    compile 'com.google.android.gms:play-services:6.5.87'[m
  }[m
[1mdiff --cc DroneApplication/app/src/main/AndroidManifest.xml[m
[1mindex 39f4fdc,3f1827f..f8c8258[m
[1m--- a/DroneApplication/app/src/main/AndroidManifest.xml[m
[1m+++ b/DroneApplication/app/src/main/AndroidManifest.xml[m
[36m@@@ -1,28 -1,32 +1,40 @@@[m
  <?xml version="1.0" encoding="utf-8"?>[m
  <manifest xmlns:android="http://schemas.android.com/apk/res/android"[m
[31m--    package="fr.m2gla.istic.projet.activity" >[m
[32m++    package="fr.m2gla.istic.projet" >[m
  [m
      <uses-permission android:name="android.permission.INTERNET" />[m
      <uses-permission android:name="android.permission.GET_ACCOUNTS" />[m
      <uses-permission android:name="android.permission.WAKE_LOCK" />[m
      <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />[m
  [m
[31m--    <permission android:name="com.example.gcm.permission.C2D_MESSAGE"[m
[32m++    <permission android:name="fr.m2gla.istic.projet.permission.C2D_MESSAGE"[m
          android:protectionLevel="signature" />[m
[31m--    <uses-permission android:name="com.example.gcm.permission.C2D_MESSAGE" />[m
[32m++    <uses-permission android:name="fr.m2gla.istic.projet.permission.C2D_MESSAGE" />[m
  [m
      <application[m
          android:allowBackup="true"[m
          android:icon="@mipmap/ic_launcher"[m
          android:label="@string/app_name" >[m
  [m
[31m--        <activity android:name=".MainActivity">[m
[32m++        <activity android:name=".activity.MainActivity">[m
              <intent-filter>[m
                  <action android:name="android.intent.action.MAIN" />[m
                  <action android:name="android.intent.category.LAUNCHER" />[m
              </intent-filter>[m
          </activity>[m
  [m
[31m -        <activity android:name=".UserActivity">[m
[31m -        </activity>[m
[32m++        <activity android:name=".activity.UserActivity" />[m
[32m+ [m
[32m++        <receiver[m
[32m++            android:name=".receiver.GcmBroadcastReceiver"[m
[32m++            android:permission="com.google.android.c2dm.permission.SEND" >[m
[32m++            <intent-filter>[m
[32m++                <action android:name="com.google.android.c2dm.intent.RECEIVE" />[m
[32m++                <category android:name="fr.m2gla.istic.projet" />[m
[32m++            </intent-filter>[m
[32m++        </receiver>[m
[32m+ [m
[32m++        <service android:name=".intent.GcmIntentService" />[m
      </application>[m
  [m
  </manifest>[m
[1mdiff --cc DroneApplication/app/src/main/java/fr/m2gla/istic/projet/activity/MainActivity.java[m
[1mindex 248d271,b00ec8b..743b02c[m
[1m--- a/DroneApplication/app/src/main/java/fr/m2gla/istic/projet/activity/MainActivity.java[m
[1m+++ b/DroneApplication/app/src/main/java/fr/m2gla/istic/projet/activity/MainActivity.java[m
[36m@@@ -1,8 -1,7 +1,7 @@@[m
  package fr.m2gla.istic.projet.activity;[m
  [m
[31m- import android.content.Context;[m
[31m -import android.app.Activity;[m
  import android.os.AsyncTask;[m
[32m +import android.app.Activity;[m
  import android.os.Bundle;[m
  import android.util.Log;[m
  import android.view.Menu;[m
[36m@@@ -13,7 -12,21 +12,22 @@@[m [mimport android.widget.RadioButton[m
  import android.widget.RadioGroup;[m
  import android.widget.Toast;[m
  [m
[31m- import fr.m2gla.istic.projet.service.impl.PushServiceImpl;[m
[32m+ import org.apache.http.HttpEntity;[m
[32m+ import org.apache.http.HttpResponse;[m
[32m+ import org.apache.http.NameValuePair;[m
[32m+ import org.apache.http.message.BasicNameValuePair;[m
[32m+ [m
[32m+ import java.util.ArrayList;[m
[32m+ import java.util.LinkedList;[m
[32m+ import java.util.List;[m
[32m+ import java.util.Map;[m
[32m+ [m
[32m++import fr.m2gla.istic.projet.R;[m
[32m+ import fr.m2gla.istic.projet.command.Command;[m
[32m+ import fr.m2gla.istic.projet.context.RestAPI;[m
[32m+ import fr.m2gla.istic.projet.context.UserQualification;[m
[32m+ import fr.m2gla.istic.projet.service.RestService;[m
[32m+ import fr.m2gla.istic.projet.service.impl.*;[m
  [m
  public class MainActivity extends Activity {[m
  [m
[36m@@@ -86,20 -112,64 +113,60 @@@[m
          radioBSelect = roleRadioG.getCheckedRadioButtonId();[m
  [m
          if (radioBSelect == R.id.codisRadioButton) {[m
[31m-             Toast.makeText(getApplicationContext(), "CODIS", Toast.LENGTH_SHORT).show();[m
[32m+             this.userQualification = UserQualification.CODIS;[m
[32m+             // Toast.makeText(getApplicationContext(), "CODIS", Toast.LENGTH_SHORT).show();[m
          }[m
          else if (radioBSelect == R.id.userRadioButton) {[m
[31m-             Toast.makeText(getApplicationContext(), "Superviseur", Toast.LENGTH_SHORT).show();[m
[32m+             this.userQualification = UserQualification.SIMPLEUSER;[m
[32m+             // Toast.makeText(getApplicationContext(), "Superviseur", Toast.LENGTH_SHORT).show();[m
          }[m
[32m+         Log.i("actiValider", "PRE sendLoginAsync");[m
[32m+ [m
[32m+         // Demander l'envoi des éléments de connexion au serveur[m
[32m+         sendLoginAsync();[m
  [m
          // Lancement d'une tache asynchrone pour envoyer les donnees de connexion au serveur[m
[31m-         new SendLoginAsync().execute();[m
[32m+         // new SendLoginAsync().execute();[m
  [m
[31m -[m
[31m -        // lancement de l'activité, suivante[m
[31m -//        startActivity(intent);[m
[32m +        PushServiceImpl.getInstance().setContext(getApplicationContext());[m
[32m +        PushServiceImpl.getInstance().register();[m
      }[m
  [m
  [m
[32m+     private boolean sendLoginAsync() {[m
[32m+ [m
[32m+         RestService         loginSnd = RestServiceImpl.getInstance();[m
[32m+         List<NameValuePair> loginList = new ArrayList<>();[m
[32m+         NameValuePair       loginPair = new BasicNameValuePair("username", this.loginName);[m
[32m+         NameValuePair       passwordPair = new BasicNameValuePair("password", this.loginPassword);[m
[32m+ [m
[32m+         Log.i("sendLoginAsync", "PRE Send Data");[m
[32m+ [m
[32m+         loginList.add(loginPair);[m
[32m+         loginList.add(passwordPair);[m
[32m+ [m
[32m+         loginSnd.post(RestAPI.POST_PUSH_LOGIN, loginList, new LoginResult(), null);[m
[32m+ [m
[32m+         Log.i("sendLoginAsync", "POST Send Data");[m
[32m+ [m
[32m+ [m
[32m+         return true;[m
[32m+     }[m
[32m+ [m
[32m+ [m
[32m+     private class LoginResult implements Command {[m
[32m+         @Override[m
[32m+         public void execute(HttpResponse response) {[m
[31m -            HttpEntity  respEntity;[m
[31m -[m
[32m+             Log.i("HttpResponse", "Fin Login");[m
[32m+ [m
[31m -            respEntity = response.getEntity();[m
[32m+             Toast.makeText(getApplicationContext(), "Status de ligne : " + response.getStatusLine().getStatusCode(), Toast.LENGTH_SHORT).show();[m
[32m+             Log.i("HttpResponse", "Status = " + response.getStatusLine().getStatusCode());[m
[32m+ [m
[32m+         }[m
[32m+     }[m
[32m+ [m
[32m+ [m
[32m+ [m
  [m
      private class SendLoginAsync extends AsyncTask<Void, Integer, Boolean> {[m
  [m
[1mdiff --cc DroneApplication/app/src/main/java/fr/m2gla/istic/projet/activity/UserActivity.java[m
[1mindex 0000000,5d35d0a..3e4222b[m
mode 000000,100644..100644[m
[1m--- a/DroneApplication/app/src/main/java/fr/m2gla/istic/projet/activity/UserActivity.java[m
[1m+++ b/DroneApplication/app/src/main/java/fr/m2gla/istic/projet/activity/UserActivity.java[m
[36m@@@ -1,0 -1,103 +1,104 @@@[m
[32m++[m
[32m+ package fr.m2gla.istic.projet.activity;[m
[32m+ [m
[32m+ import android.app.Activity;[m
[32m+ import android.content.Intent;[m
[32m+ import android.os.Bundle;[m
[32m+ import android.view.View;[m
[32m+ import android.widget.ListView;[m
[32m+ import android.widget.SimpleAdapter;[m
[32m+ import android.widget.Toast;[m
[32m+ [m
[32m+ import java.util.ArrayList;[m
[32m+ import java.util.HashMap;[m
[32m+ [m
[32m+ /**[m
[32m+  * Created by david on 09/02/15.[m
[32m+  */[m
[32m+ [m
[32m+ public class UserActivity  extends Activity {[m
[32m+ /*[m
[32m+     private ListView idList;[m
[32m+     private ArrayList<HashMap<String, String>> listItem;[m
[32m+     private SimpleAdapter mSchedule;[m
[32m+     private PersoInfoDataDAO                    clientDAO;[m
[32m+ [m
[32m+     @Override[m
[32m+     protected void onCreate(Bundle savedInstanceState) {[m
[32m+         super.onCreate(savedInstanceState);[m
[32m+         setContentView(mmm.m2gla.istic.fr.droneapplication.R.layout.activity_clients);[m
[32m+ [m
[32m+         //Récupération de la listview créée dans le fichier clients.xml[m
[32m+         this.idList = (ListView) findViewById(mmm.m2gla.istic.fr.droneapplication.R.id.idListView);[m
[32m+ [m
[32m+         //Création d'une instance de la classe DAO (PersoInfoDataDAO)[m
[32m+         PersoInfoDataDAO clientDAO = new PersoInfoDataDAO(this);[m
[32m+ [m
[32m+         //Création de la ArrayList qui nous permettra de remplire la listView[m
[32m+         this.listItem = new ArrayList<HashMap<String, String>>();[m
[32m+ [m
[32m+         //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue disp_item[m
[32m+         this.mSchedule = new SimpleAdapter(this.getBaseContext(), this.listItem, mmm.m2gla.istic.fr.droneapplication.R.layout.disp_item,[m
[32m+ //                new String[] {"nom", "prenom, dateNais"},[m
[32m+ //                new int[] {R.id.nomElem, R.id.prenomElem, R.id.dateNaisElem});[m
[32m+                 new String[] {"nom", "prenom", "dateNais", "ville"},[m
[32m+                 new int[] {mmm.m2gla.istic.fr.droneapplication.R.id.nomElem, mmm.m2gla.istic.fr.droneapplication.R.id.prenomElem, mmm.m2gla.istic.fr.droneapplication.R.id.dateNaisElem, mmm.m2gla.istic.fr.droneapplication.R.id.villeElem});[m
[32m+ [m
[32m+         //On attribut à notre listView l'adapter que l'on vient de créer[m
[32m+         this.idList.setAdapter(mSchedule);[m
[32m+ [m
[32m+ //        System.out.println("- TOTO -");[m
[32m+ [m
[32m+ [m
[32m+     }[m
[32m+ [m
[32m+     public void finClt(View view) {[m
[32m+         // arret de l'activity ici[m
[32m+         finish();[m
[32m+     }[m
[32m+ [m
[32m+     public void actiNewClt(View view) {[m
[32m+         Intent intent = new Intent(getApplicationContext(), MainActivity.class);[m
[32m+ [m
[32m+         //lancement de la seconde activité, en demandant un code retour[m
[32m+         startActivityForResult(intent, 0);[m
[32m+ [m
[32m+     }[m
[32m+ [m
[32m+     @Override[m
[32m+     public void onActivityResult(int a, int b, Intent retIntent) {[m
[32m+         PersoInfoData persoInfoData;[m
[32m+ [m
[32m+         persoInfoData = retIntent.getParcelableExtra(MainActivity.refReturn);[m
[32m+ [m
[32m+         // Verifier si une insertion est a faire[m
[32m+         if ((persoInfoData == null) || (persoInfoData.getNom().length() <= 0)) {[m
[32m+             return;[m
[32m+         }[m
[32m+ [m
[32m+         //On déclare la HashMap qui contiendra les informations pour un item[m
[32m+         HashMap<String, String> map;[m
[32m+ [m
[32m+         //Création d'une HashMap pour insérer les informations du premier item de notre listView[m
[32m+         map = new HashMap<String, String>();[m
[32m+ [m
[32m+         //on insère un élément nom que l'on récupérera dans le textView titre créé dans le fichier disp_item.xml[m
[32m+         map.put("nom", persoInfoData.getNom());[m
[32m+         //on insère un élément titre que l'on récupérera dans le textView titre créé dans le fichier disp_item.xml[m
[32m+         map.put("prenom", persoInfoData.getPrenom());[m
[32m+         //enfin on ajoute cette hashMap dans la arrayList[m
[32m+         map.put("dateNais", persoInfoData.getDateNais());[m
[32m+         //enfin on ajoute cette hashMap dans la arrayList[m
[32m+         map.put("ville", persoInfoData.getVille());[m
[32m+         //enfin on ajoute cette hashMap dans la arrayList[m
[32m+         listItem.add(map);[m
[32m+ [m
[32m+         // Mettre a jour la liste d'affichage[m
[32m+         this.mSchedule.notifyDataSetChanged();[m
[32m+ [m
[32m+ [m
[32m+         Toast.makeText(getApplicationContext(), persoInfoData.getNom().toString(), Toast.LENGTH_SHORT).show();[m
[32m+ [m
[32m+     }[m
[32m+ */[m
[32m+ }[m
[1mdiff --cc DroneApplication/app/src/main/java/fr/m2gla/istic/projet/context/RestAPI.java[m
[1mindex 0b6bfc2,d732069..2e4359f[m
[1m--- a/DroneApplication/app/src/main/java/fr/m2gla/istic/projet/context/RestAPI.java[m
[1m+++ b/DroneApplication/app/src/main/java/fr/m2gla/istic/projet/context/RestAPI.java[m
[36m@@@ -1,8 -1,9 +1,13 @@@[m
  package fr.m2gla.istic.projet.context;[m
  [m
  /**[m
[31m-- * Created by baptiste on 07/04/15.[m
[32m++ * Interface to list rest service API for application[m
   */[m
  public interface RestAPI {[m
[32m++    // Push register block[m
      public static final String POST_PUSH_REGISTER = "/register";[m
[32m++    public static final String DELETE_PUSH_REGISTER = "/register/:id";[m
[32m++[m
[32m++    // Login block[m
[32m+     public static final String POST_PUSH_LOGIN = "/user/login";[m
  }[m
[1mdiff --cc DroneApplication/app/src/main/java/fr/m2gla/istic/projet/service/impl/PushServiceImpl.java[m
[1mindex f9e4738,0000000..9be5eae[m
mode 100644,000000..100644[m
[1m--- a/DroneApplication/app/src/main/java/fr/m2gla/istic/projet/service/impl/PushServiceImpl.java[m
[1m+++ b/DroneApplication/app/src/main/java/fr/m2gla/istic/projet/service/impl/PushServiceImpl.java[m
[36m@@@ -1,95 -1,0 +1,105 @@@[m
[32m +package fr.m2gla.istic.projet.service.impl;[m
[32m +[m
[32m +import android.content.Context;[m
[32m +import android.os.AsyncTask;[m
[32m +import android.util.Log;[m
[32m +[m
[32m +import com.google.android.gms.common.ConnectionResult;[m
[32m +import com.google.android.gms.common.GooglePlayServicesUtil;[m
[32m +import com.google.android.gms.gcm.GoogleCloudMessaging;[m
[32m +[m
[32m +import org.apache.http.HttpResponse;[m
[32m +import org.apache.http.NameValuePair;[m
[32m +import org.apache.http.message.BasicNameValuePair;[m
[32m +[m
[32m +import java.io.IOException;[m
[32m +import java.util.ArrayList;[m
[32m +import java.util.List;[m
[32m +[m
[32m +import fr.m2gla.istic.projet.command.Command;[m
[32m +import fr.m2gla.istic.projet.context.RestAPI;[m
[32m +import fr.m2gla.istic.projet.service.PushService;[m
[32m +[m
[32m +public class PushServiceImpl implements PushService {[m
[32m +    private static final PushService INSTANCE = new PushServiceImpl();[m
[32m +    private static final String TAG = "PushService";[m
[31m-     private static final String CLIENT_KEY = "AIzaSyBO1geWvgWqYwLQyOzNTdFPMHGCDHrGfPc";[m
[32m++    private static final String SENDER_ID = "836789679656";[m
[32m +[m
[31m-     private GoogleCloudMessaging gcm;[m
[32m +    private Context context;[m
[32m +[m
[32m +    protected PushServiceImpl() {}[m
[32m +[m
[32m +    public static PushService getInstance() {[m
[32m +        return INSTANCE;[m
[32m +    }[m
[32m +[m
[32m +    @Override[m
[32m +    public void setContext(Context context) {[m
[32m +        this.context = context;[m
[32m +    }[m
[32m +[m
[32m +    @Override[m
[32m +    public void register() {[m
[31m-         if (checkPlayServices()) {[m
[31m-             gcm = GoogleCloudMessaging.getInstance(context);[m
[32m++        final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);[m
[32m +[m
[32m++        if (gcm != null && checkPlayServices()) {[m
[32m +            (new AsyncTask() {[m
[32m +                private String registeredId;[m
[32m +[m
[32m +                @Override[m
[32m +                protected Object doInBackground(Object[] params) {[m
[32m +                    try {[m
[31m-                         registeredId = gcm.register(CLIENT_KEY);[m
[32m++                        Log.i(TAG + ".AsyncTask", "Registration start");[m
[32m++                        registeredId = gcm.register(SENDER_ID);[m
[32m++                        Log.i(TAG + ".AsyncTask", "Registration id " + registeredId);[m
[32m +                    } catch (IOException e) {[m
[31m-                         Log.e(TAG + ".AsyncTask", "Error register");[m
[32m++                        Log.e(TAG + ".AsyncTask", "Error register", e);[m
[32m +                        return false;[m
[32m +                    }[m
[32m +[m
[32m +                    return true;[m
[32m +                }[m
[32m +[m
[32m +                @Override[m
[32m +                protected void onPostExecute(Object o) {[m
[32m +                    final Boolean result = (Boolean)o;[m
[32m +                    if (result) {[m
[32m +                        List<NameValuePair> content = new ArrayList<NameValuePair>();[m
[32m +                        content.add(new BasicNameValuePair("id", registeredId));[m
[32m +[m
[32m +                        RestServiceImpl.getInstance()[m
[32m +                            .post(RestAPI.POST_PUSH_REGISTER, content, new Command() {[m
[32m +                                    @Override[m
[32m +                                    public void execute(HttpResponse response) {[m
[32m +                                        if (response.getStatusLine().getStatusCode() != 204) {[m
[31m-                                             Log.e(TAG, "Erreur register");[m
[32m++                                            Log.i(TAG, "Erreur register code " + response.getStatusLine().getStatusCode());[m
[32m +                                        }[m
[32m +                                    }[m
[32m +                                }, null);[m
[32m +                    }[m
[32m +                }[m
[32m +            }).execute();[m
[32m +        }[m
[32m +    }[m
[32m +[m
[32m +    /**[m
[32m +     * Check the device to make sure it has the Google Play Services APK. If[m
[32m +     * it doesn't, display a dialog that allows users to download the APK from[m
[32m +     * the Google Play Store or enable it in the device's system settings.[m
[32m +     */[m
[32m +    private boolean checkPlayServices() {[m
[32m +        if (context == null) {[m
[32m +            throw new IllegalArgumentException("Context not set");[m
[32m +        }[m
[31m-         return GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;[m
[32m++[m
[32m++        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);[m
[32m++        if (resultCode != ConnectionResult.SUCCESS) {[m
[32m++            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {[m
[32m++                Log.e(TAG, "Google not available");[m
[32m++            } else {[m
[32m++                Log.wtf(TAG, "This device is not supported.");[m
[32m++            }[m
[32m++        }[m
[32m++        return resultCode == ConnectionResult.SUCCESS;[m
[32m +    }[m
[32m +}[m
[1mdiff --cc DroneApplication/app/src/main/res/layout/activity_user.xml[m
[1mindex 32a5634,32a5634..cecaa0d[m
[1m--- a/DroneApplication/app/src/main/res/layout/activity_user.xml[m
[1m+++ b/DroneApplication/app/src/main/res/layout/activity_user.xml[m
[36m@@@ -2,46 -2,46 +2,21 @@@[m
  <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"[m
      android:orientation="vertical" android:layout_width="match_parent"[m
      android:layout_height="match_parent">[m
[31m--[m
      <ListView[m
          android:layout_width="wrap_content"[m
          android:layout_height="wrap_content"[m
          android:id="@+id/idListView"[m
[31m--        android:layout_alignParentLeft="true"[m
[31m--        android:layout_alignParentRight="true"[m
          android:layout_alignParentTop="true"[m
          android:layout_above="@+id/buttonLayout"[m
          />[m
  [m
[31m--[m
      <LinearLayout[m
          android:orientation="horizontal"[m
          android:layout_width="fill_parent"[m
          android:layout_height="wrap_content"[m
          android:layout_centerHorizontal="true"[m
          android:layout_alignParentBottom="true"[m
[31m--        android:layout_alignParentLeft="true"[m
[31m--        android:layout_alignParentStart="true"[m
          android:id="@+id/buttonLayout"[m
          >[m
[31m--[m
[31m--        <Button[m
[31m--            android:layout_width="match_parent"[m
[31m--            android:layout_height="wrap_content"[m
[31m--            android:id="@+id/newCltButton"[m
[31m--            android:onClick="actiNewClt"[m
[31m--            android:layout_weight="1"[m
[31m--        />[m
[31m--[m
[31m--        <Button[m
[31m--            android:layout_width="match_parent"[m
[31m--            android:layout_height="wrap_content"[m
[31m--            android:id="@+id/finCltButton"[m
[31m--            android:onClick="finClt"[m
[31m--            android:layout_weight="1"[m
[31m--        />[m
[31m--[m
      </LinearLayout>[m
[31m--[m
[31m--[m
  </RelativeLayout>[m
[1mdiff --cc DroneApplication/gradle/wrapper/gradle-wrapper.properties[m
[1mindex 0c71e76,0c71e76..0000000[m
[1mdeleted file mode 100644,100644[m
[1m--- a/DroneApplication/gradle/wrapper/gradle-wrapper.properties[m
[1m+++ /dev/null[m
[36m@@@ -1,6 -1,6 +1,0 @@@[m
[31m--#Wed Apr 10 15:27:10 PDT 2013[m
[31m--distributionBase=GRADLE_USER_HOME[m
[31m--distributionPath=wrapper/dists[m
[31m--zipStoreBase=GRADLE_USER_HOME[m
[31m--zipStorePath=wrapper/dists[m
[31m--distributionUrl=https\://services.gradle.org/distributions/gradle-2.2.1-all.zip[m
[1mdiff --cc server-rest/src/main/java/adapter/AdapterGCM.java[m
[1mindex 68364e7,68364e7..0000000[m
[1mdeleted file mode 100644,100644[m
[1m--- a/server-rest/src/main/java/adapter/AdapterGCM.java[m
[1m+++ /dev/null[m
[36m@@@ -1,27 -1,27 +1,0 @@@[m
[31m--package adapter;[m
[31m--[m
[31m--import java.io.IOException;[m
[31m--import java.util.ArrayList;[m
[31m--import java.util.List;[m
[31m--[m
[31m--import com.google.android.gcm.server.Message.Builder;[m
[31m--import com.google.android.gcm.server.Sender;[m
[31m--[m
[31m--public class AdapterGCM {[m
[31m--	private static final String SERVER_KEY = "AIzaSyB8e1INV7eARjM9y--ztTmYFxeJjD5VWvk";[m
[31m--	private static final int RETRIES = 5;[m
[31m--	[m
[31m--	private static List<String> registersClient = new ArrayList<String>();[m
[31m--	[m
[31m--	public static void addRegisterClient(String idClient) {[m
[31m--		registersClient.add(idClient);[m
[31m--	}[m
[31m--	[m
[31m--	public void sendMessage(String scope, String message) throws IOException {[m
[31m--		Sender sender = new Sender(SERVER_KEY);[m
[31m--		Builder messageBuilder = new Builder();[m
[31m--		messageBuilder.addData(scope, message);[m
[31m--		[m
[31m--		sender.send(messageBuilder.build(), registersClient, RETRIES);[m
[31m--	}[m
[31m--}[m
[1mdiff --cc server-rest/src/main/java/entity/EntityPushClient.java[m
[1mindex 0000000,0000000..166ffcc[m
[1mnew file mode 100644[m
[1m--- /dev/null[m
[1m+++ b/server-rest/src/main/java/entity/EntityPushClient.java[m
[36m@@@ -1,0 -1,0 +1,13 @@@[m
[32m++package entity;[m
[32m++[m
[32m++public class EntityPushClient {[m
[32m++	private String id;[m
[32m++[m
[32m++	public String getId() {[m
[32m++		return id;[m
[32m++	}[m
[32m++[m
[32m++	public void setId(String id) {[m
[32m++		this.id = id;[m
[32m++	}[m
[32m++}[m
[1mdiff --cc server-rest/src/main/java/rest/PushRegister.java[m
[1mindex 81e5432,81e5432..446f559[m
[1m--- a/server-rest/src/main/java/rest/PushRegister.java[m
[1m+++ b/server-rest/src/main/java/rest/PushRegister.java[m
[36m@@@ -1,15 -1,15 +1,26 @@@[m
  package rest;[m
  [m
  import javax.ws.rs.Consumes;[m
[32m++import javax.ws.rs.DELETE;[m
  import javax.ws.rs.POST;[m
  import javax.ws.rs.Path;[m
[32m++import javax.ws.rs.PathParam;[m
  import javax.ws.rs.core.MediaType;[m
  [m
[31m--@Path("register")[m
[32m++import service.impl.PushServiceImpl;[m
[32m++import entity.EntityPushClient;[m
[32m++[m
[32m++@Path("/register")[m
  public class PushRegister {[m
  	@POST[m
  	@Consumes(MediaType.APPLICATION_JSON)[m
[31m--	public void registerClient() {[m
[31m--		[m
[32m++	public void registerClient(EntityPushClient entityGCM) {[m
[32m++		PushServiceImpl.getInstance().registerClient(entityGCM.getId());[m
[32m++	}[m
[32m++	[m
[32m++	@DELETE[m
[32m++	@Path("/{id}")[m
[32m++	public void unregisterClient(@PathParam("id") String id) {[m
[32m++		PushServiceImpl.getInstance().unregisterClient(id);[m
  	}[m
  }[m
[1mdiff --cc server-rest/src/main/java/service/PushService.java[m
[1mindex 0000000,0000000..e03ce89[m
[1mnew file mode 100644[m
[1m--- /dev/null[m
[1m+++ b/server-rest/src/main/java/service/PushService.java[m
[36m@@@ -1,0 -1,0 +1,9 @@@[m
[32m++package service;[m
[32m++[m
[32m++import java.io.IOException;[m
[32m++[m
[32m++public interface PushService {[m
[32m++	public void registerClient(String idClient);[m
[32m++	public void unregisterClient(String idClient);[m
[32m++	public void sendMessage(String scope, String message) throws IOException;[m
[32m++}[m
[1mdiff --cc server-rest/src/main/java/service/impl/PushServiceImpl.java[m
[1mindex 0000000,0000000..0f8f9a2[m
[1mnew file mode 100644[m
[1m--- /dev/null[m
[1m+++ b/server-rest/src/main/java/service/impl/PushServiceImpl.java[m
[36m@@@ -1,0 -1,0 +1,42 @@@[m
[32m++package service.impl;[m
[32m++[m
[32m++import java.io.IOException;[m
[32m++import java.util.ArrayList;[m
[32m++import java.util.List;[m
[32m++[m
[32m++import service.PushService;[m
[32m++[m
[32m++import com.google.android.gcm.server.Message.Builder;[m
[32m++import com.google.android.gcm.server.Sender;[m
[32m++[m
[32m++public class PushServiceImpl implements PushService {[m
[32m++	private static final PushService INSTANCE = new PushServiceImpl();[m
[32m++	private static final String SERVER_KEY = "AIzaSyB8e1INV7eARjM9y--ztTmYFxeJjD5VWvk";[m
[32m++	private static final int RETRIES = 5;[m
[32m++	[m
[32m++	private List<String> registersClient;[m
[32m++	[m
[32m++	protected PushServiceImpl() {[m
[32m++		registersClient = new ArrayList<String>();[m
[32m++	}[m
[32m++	[m
[32m++	public static PushService getInstance() {[m
[32m++		return INSTANCE;[m
[32m++	}[m
[32m++[m
[32m++	public void registerClient(String idClient) {[m
[32m++		registersClient.add(idClient);[m
[32m++	}[m
[32m++[m
[32m++	public void unregisterClient(String idClient) {[m
[32m++		registersClient.remove(idClient);[m
[32m++	}[m
[32m++	[m
[32m++	public void sendMessage(String scope, String message) throws IOException {[m
[32m++		Sender sender = new Sender(SERVER_KEY);[m
[32m++		Builder messageBuilder = new Builder();[m
[32m++		messageBuilder.addData(scope, message);[m
[32m++		[m
[32m++		sender.send(messageBuilder.build(), registersClient, RETRIES);[m
[32m++	}[m
[32m++}[m
