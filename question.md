1.
Order.html ~~  line 140
```js
 let url = /*[[@{/order/}]]*/ '' + addressId;
        console.log(addressId);
        console.log(url);
```
为什么拼接不了变量？<br/>
打印出来addressId：31 ,url:/order/ 🤯

2.
艹，mybaits居然不支持@Value注解，还不报错！🤯<br/>
+了application配置<br/>
抽象，tinyint类型居然能对应的是int不是boolean

3.<br/>
 js的sessionStorage 和 后端的session不一样<br/>
 sessionStorage 存储在浏览器本地，session 存储在服务器端<br/>
https://juejin.cn/post/6844903975800537096


4.大坑大坑！！
①
mybatis会自动将数据库字段名转换为驼峰命名法<br/>
eg: 数据库中的is_admin 如果你的User类属性为为is_admin，获取不到，就离谱<br/>
必须为isAdmin才可以<br/>
②
mybatis不会自动将tinyint类型转换为boolean<br/>
java类中对应属性用int类型
🤯