# novel-spider
总述：我使用了Jsoup（ [https://jsoup.org/](https://jsoup.org/)）这个第三方包，jsoup 是一款Java 的HTML解析器，可直接解析某个URL地址、HTML文本内容。它提供了一套非常省力的API，可通过DOM，CSS以及类似于jQuery的操作方法来取出和操作数据。详细的Jsoup开发指南可以参考指导文档（ [http://www.open-open.com/jsoup/](http://www.open-open.com/jsoup/)）。

实现的小说爬虫可以基本实现三七中文（ [http://www.37zw.net/](http://www.37zw.net/)）该网站上所有小说的下载，使用时需输入所需下载小说的目录页地址，如全职高手（ [http://www.37zw.net/0/230/](http://www.37zw.net/0/230/)）。程序运行完毕后将在根目录生成相应的小说。

下文将以全职高手这部小说为例分析该爬虫实现的原理和方法。

问题分析：

1.小说目录页面源代码分析

小说目录页面源代码如图所示：

观察可知，小说的目录以及对应内容的网页地址存放在&lt;dd&gt;&lt;/dd&gt;这组标签内的&lt;a href&gt;中，通过Jsoup和正则表达式可以对这部分进行提取，将所有正文地址存入一个string数组中。

2.小说正文页面分析

首先我们需要知道每一章的小说标题，通过网页源代码分析，可以发现每一章的小说标题都放在&lt;h1&gt;&lt;/h1&gt;标签内。通过Jsoup可以直接取该标签内的内容作为每一章的标题。

其次是获取小说正文内容，如下图所示：

正文内容都放在id为content的&lt;div&gt;&lt;/div&gt;标签内，通过Jsoup可以获取这个标签内的内容。注意到改内容中存在&amp;nbsp会对Jsoup提取后的内容造成字符识别上的失败，需要对其进行替换，这将在代码中额外实现。

3.类Getinformation.java

该类主要实现爬取目录，获取标题、内容等任务，主要由以下几个函数组成：

| 函数 | 描述 |
| --- | --- |
| public static Document getdocument(String url) | 读取指定url页面生成Document对象。 |
| public static String gettitle(Document document) | 分析Document对象内容找出其中小说章节的标题 |
| public static String getcontent(Document document) | 分析Document对象内容找出其中小说正文的内容 |
| public static String[] geturl(String url) | 分析小说目录页面，将小说各个章节的地址存入String数组中 |

- 函数：public static Document getdocument(String url)

使用Jsoup.connect(url).timeout(10000).get()获取指定url的内容，连接时间超过10s后仍然失败后作超时处理。

- 函数：public static String gettitle(Document document)

使用document.getElementsByTag(&quot;h1&quot;).text()获取标签为h1的标签内内容。

- 函数：public static String getcontent(Document document)

使用document.getElementById(&quot;content&quot;).text().replace(Jsoup.parse(&quot;    &quot;).text(),&quot;\r\n    &quot;);

获取id为content的标签内的内容，并对&amp;nbsp进行替换，将其换成换行以及首行的两个空格，增加生成小说的美观性。

- 函数：public static String[] geturl(String url)

使用正则表达式String regex=&quot;&lt;a href=\&quot;(.\*?)\&quot;&gt;(.\*?)&lt;/a&gt;&quot;对符合这一方法的标签进行提取即可得到每一章正文网址的地址信息，并将这些地址存入数组中供主程序调用。

4.主程序：

主程序处理流程：读取用户输入的小说目录网页地址-&gt;分析小说目录得到各章节网页地址-&gt;遍历各章节网页地址-&gt;将各章节的题目和正文写入文本文档。

5.示例：

输入http://www.37zw.net/0/230/

进入处理过程

处理完成：

生成小说：