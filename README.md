# MenuListWidget

#### 介绍
可作为菜单列表也可作为输入框列表，可自定义列表头部、尾部图片和四部下划线，丰富的XML操作接口供您自定义此控件。

#### 使用说明

Step 1. Add the JitPack repository to your build file

```
allprojects {
    repositories {
        ...
	maven { url 'https://jitpack.io' }
    }
}
```

	
Step 2. Add the dependency

```
dependencies {
    implementation 'com.gitee.zhou-sg:MenuListWidget:1.1.0'
}
```

	

#### XML接口

```
//列表名
app:titleText="菜单列表风格"
//列表名字体大小
app:titleTextSize="16sp"
//列表名字体颜色
app:titleTextColor="@color/colorPrimary"
//列表头部图标
app:startIconDrawable="@mipmap/ic_launcher_round"
//列表尾部图标
app:endIconDrawable="@mipmap/ic_launcher"
//列表头尾部图标的显隐性
app:startIconVisibility="true"
app:endIconVisibility="true"
//列表下划线颜色
app:dividerColor="@color/colorAccent"
//下划线显隐性
app:dividerVisibility="true"
//列表图标大小
app:iconDrawableSize="30dp"
//输入框是否能输入
app:enableEditInput="true"
//是否启用输入框
app:enableEditText="true"
//设置输入框内容
app:setEditContent="内容"
//输入框hint
app:setEditHint="请输入..."
//输入框内容位置
app:editContentGravity="center"
```
