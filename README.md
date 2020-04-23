# Wrboot-Jdbc
前言：王睿的数据库框架。基于Springjdbc，可以单独使用，也可以配合SpringBoot2使用。
从2009年以来学习和使用java以来一直使用Springjdbc和hibernate。今年是2020年，将要转战MyBatis框架了，算是给自己留个纪念和笔记。
基于我这么多年的使用过程和痛点，以及学习别人的代码，重新编写了这个框架（之前也有类似框架，每次项目都会更新，但是没统一名字，代码写的也非常乱，
过段时间就会忘记当初的思路）。
因为是初版，所以肯定有不少bug。如果有兴趣使用的同学碰到问题可以一起加入修改或者发邮件给我woerry@qq.com
#目前的一些痛点
##1.多数据源（完成）
因为经常跨库和跨数据库类型查询，所以要配置。网上的教程大多数都是把所有的数据源默认写在DataSourceConfig中，因为我觉得源码修改麻烦，而且如果
代码给别人复用更麻烦。所以选择把除主数据源以外的数据源配置在数据库中，动态获取。
##2.CRUD（完成）
框架的核心和基础。最初的框架是学习网上的basedao，本身就有不少依赖类和方法，后来不断的使用中增加和修改了很多，乱的很。这次干脆重写了所有的方法。
##3.数据库类型（未完成）
因为工作原因主要使用informix和mysql，而网上的软件，通用源码对informix的优化不怎么好（毕竟冷门）。所以准备针对informix进行更深地优化。
##4.数据库操作（未完成）
### 4.1 删除外健时先删除关联表的关联数据(完成)
这个是我很久以前一直要吐槽的一个功能，这次做掉了。
### 4.2 自增主键的优化(完成)
自增主键数据的insert和update的优化。
### 4.3 联合主键的优化(完成部分)
联合主键也是个坑。
### 4.4 外键的优化(未完成)
大坑中的大坑。
##5.缓存（未开始）
作为合格的orm，是应该有orm的。不过我还没做。
##6.图形化配置（未开始）
毕竟操作时，图形化比代码更加不容易出错。
##7.代码自动化（未完成）
手动写Entity/pojo绝对是个痛苦的事，将引入freemark来完成该功能。