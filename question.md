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
