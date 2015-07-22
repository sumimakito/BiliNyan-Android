# BiliNyan 哔哩喵
> 一个参照 Material Design 设计语言制作的第三方哔哩哔哩([http://www.bilibili.tv](http://www.bilibili.tv)) Android 客户端

### 前言

##### 为什么制作这个应用

在哔哩哔哩 Android 客户端 4.0 内测版本的时候，官方改用了带有较浓重的 iOS 味道的设计，从那时起就开始产生这个想法。

后来，他们又重新设计了 Android 客户端的界面，采纳了 Material Design 的设计语言并加入了他们的特色，除了最终版本的界面主色调外，其他界面基本上我都是很喜欢的，有点想放弃这个念头。

即将放假前，得知哔哩哔哩也有开放接口，不需要辛苦地去抓包分析（就算抓到我们很难分析出 sign 这个参数的用法），就有点动心了，毕竟这也算是高中生涯里面的最后一个暑假（不含高三暑假），过完这个月就没时间再去折腾这些玩意了，不如干一番稍微大一点的事。

于是乎，放假后就开始挖着个坑了（然而假期大多时间我都花去打游戏上面去了，剩下最后这几天不知道能不能填完QAQ）

##### 为什么只支持 4.2+ (Jellybean)

没为什么，近两年市面上的国产主流机都有 4.2 以上了，我的两台机子都能上 Lollipop （其中 HTC One S 留在 Kitkat 做测试用），既然无法保证低版本能完美运行，我到不如提前告诉他们不能用。刚开始我可是疯到想提高到 4.4/5.x，毕竟只是个人开发作品，不会有盈利这种东西干扰。

### 接口说明

本项目为开源项目，但不提供 API 的 App key 和 Secret，请自行申请。

API 的使用方法在 [哔哩哔哩文档Wiki](http://docs.bilibili.cn/wiki) 上有，但似乎偶尔服务器会抽风。为了方便使用，搬运并重写了 Markdown 版本：[https://github.com/fython/BilibiliAPIDocs](https://github.com/fython/BilibiliAPIDocs)（推荐）

开发前请务必了解清楚 API 接口使用和注意事项。

### 开发环境

+ Android Studio 1.x （建议 1.2 以上）
+ Windows/Linux/Mac OS X （~~这不废话么233~~）
+ Gradle 2.4+ 以及良好的网络环境
+ SDK Platform 5.1+ & Android Support M2Repository r15+

### 联系方式

+ 新浪微博：[@某烧饼](http://weibo.com/fython/)
+ Telegram：@fython
+ ###### 如果你很乐意支持本项目而且非常壕，欢迎捐赠点小钱到支付宝： 316643843#qq.com （#请自行替换为@）

### 开源协议

本项目采用 GPLv3 开源协议，项目中部分依赖库可能是遵循其它开源协议（如使用 Apache License 的 [StatusBarCompat](https://github.com/fython/MaterialStatusBarCompat) 库）

```
Copyright (C) 2015 Fung Jichun

BiliNyan is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

BiliNyan is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with BiliNyan. If not, see http://www.gnu.org/licenses/.
```
