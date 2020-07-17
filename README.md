### ElasticSearch:

es是一个开源的可拓展的全文检索引擎服务器,它可以近乎实时的存储,检索数据;本身拓展性好,可以拓展到上百台服务器,处理pb级别的数据.es使用java开发并使用lucene作为其核心来实现索引和搜索的功能,但是它通过简单的RestfulAPI和javaAPI来隐藏lucene的复杂性,从而让全文检索变得简单

**全文检索**:全文检索是利用倒排索引技术对需要搜索的数据进行处理,然后提供快速匹配的技术.其实全文检索还有另外一种专业定义,先创建索引然后对索引进行所搜的过程,就是全文检索

> 网速太慢可以从百度网盘下载
>
> https://blog.csdn.net/weixin_37281289/article/details/101483434

### es的安装:

1. ##### 创建用户 

   useradd es

2. ##### 设置密码

   passwd es 

3. ##### 切换用户

   su - es

4. ##### (root用户)为该用户增加权限

   vim /etc/security/limits.conf 

   #可打开的文件描述符的最大数(软限制)

   *soft nofile 65536

   #可打开的文件描述符的最大数(硬限制)

   *hard nofile 131072

   #单个用户可用的最大进程数(软限制)

   *soft nproc 4096

   #单个用户可用的最大进程数量(硬限制)

   *hard nproc 4096

5. ##### (root用户)修改es用户的最大虚拟内存,不能小于262144

   vim /etc/sysctl.conf

   **增加内容:**  vm.max_map_count=262144

   **执行命令:**  sysctl -p

6. ##### (root)增加es用户对jvm.options的文件权限

   chown -R es:es /usr/local/es/es/config/jvm.options

7. ##### 下载 https://www.elastic.co/cn/downloads/past-releases#elasticsearch

   wget 	https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.8.8.tar.gz

8. ##### 解压

   tar zxvf elasticsearch-6.8.8.tar.gz

9. ##### 重命名

   mv elasticsearch-6.8.8/ es

10. ##### 修改jvm.options

    cd config

    -Xms256m
    -Xmx256m

11. ##### 修改elasticsearch.yml

    pah.data: /usr/local/es/es/data

    path.logs: /usr/local/es/es/logs

    network.host: 0.0.0.0

12. ##### 启动

    cd bin

    ./elasticsearch -d

### kibana安装:

1. ##### 下载

   wget https://artifacts.elastic.co/downloads/kibana/kibana-6.8.8-linux-x86_64.tar.gz

2. ##### 解压

   tar zxvf  tar zxvf kibana-6.8.8-linux-x86_64.tar.gz

3. ##### 重命名

   mv kibana-6.8.8-linux-x86_64 kibana

4. ##### 修改配置

   cd config

   vim kibana.yml

   server.host: "0.0.0.0"

   il8n.locale: "zh-CN"

5. ##### 启动

   ./kibana

### elasticsearche-head安装

### IK分词器安装

1. #####  下载

   https://github.com/medcl/elasticsearch-analysis-ik/releases

2. ##### 解压

   将解压包放到elasticsearch/plugin目录下,命名为analysis-ik

### 安装docker

1. ###### 查看内核版本,要求版本高于3.10

   uname -r

   ![image-20200710144726497](D:\mingbyte\typora\image-20200710144726497.png)

2. ###### .使用root权限登录Centos,更新yum包,生产环境慎用

   sudo yum update

3. ###### 卸载docker

   sudo yum remove docker  docker-common docker-selinux docker-engine

4. ###### 安装需要的依赖

   sudo yum install -y yum-utils device-mapper-persistent-data lvm2

5. ###### 设置yum源

   sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

6. ###### 查看仓库中docker的版本

   yum list docker-ce --showduplicates | sort -r

7. ###### 安装docker

   sudo yum install docker-ce

8. ###### 启动并加入开机启动

   sudo systemctl start docker
   sudo systemctl enable docker

9. ###### 验证是否成功

   docker version

### Docker中安装elasticsearch

1. 查询仓库中elasticsearch版本

   ![image-20200713132704728](D:\mingbyte\typora\image-20200713132704728.png)

2. ###### 拉取镜像

   docker pull elasticsearch:7.8.0

   ![image-20200713132726062](D:\mingbyte\typora\image-20200713132726062.png)

3. ###### 查看镜像

   docker images

   ![image-20200713134211802](D:\mingbyte\typora\image-20200713134211802.png)

4. ###### 运行es(虚拟机的话可以设置内存,默认为2G)

   docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.8.0

5. ###### 校验

   在浏览器中输入ip:9200

   ![image-20200713134225870](D:\mingbyte\typora\image-20200713134225870.png)

6. ###### 增加跨域配置

   docker exec -it elasticsearch /bin/bash

   vi elasticsearch.yml

   http.cors.enabled: true

   http.cors.allow-origin; "*"

7. ###### 重启

   docker restart  80bf4b72d30e

   

### docker中安装elasticsearch-heap

1. ###### 拉取elasticsearch-heap的镜像

   docker pull mobz/elasticsearch-head:5

2. ###### 运行elasticsearch-heap容器

   docker run -d --name es_admin -p 9100:9100 mobz/elasticsearch-head:5

3. ###### 连接elasticsearch

   ![image-20200713155746170](D:\mingbyte\typora\image-20200713155746170.png)

### docker中安装ik中文分词器

1. ###### 下载对应的ik分词器版本

   从 https://github.com/medcl/elasticsearch-analysis-ik/releases 中查找对应版本的ik分词器并下载

   wget https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.8.0/elasticsearch-analysis-ik-7.8.0.zip

2. ###### 查询elasticsearch的容器id

   docker ps 

   ![image-20200713141136897](D:\mingbyte\typora\image-20200713141136897.png)

3. ###### 将ik分词器复制到elasticsearch容器的plugins/analysis-ik目录下

   docker cp /usr/local/es/elasticsearch-analysis-ik-7.8.0.zip 80bf4b72d30e:/usr/share/elasticsearch/plugins/analysis-ik/

4. ###### 进入到容器

   docker exec  -it elasticsearch /bin/bash

5. ###### 解压

   unzip elasticsearch-analysis-ik-7.8.0.zip

6. ###### 删除压缩包

   rm -rf  elasticsearch-analysis-ik-7.8.0.zip

7. ###### 重启elasticsearch

   docker restart 80bf4b72d30e

### ES相关概念

ElasticSearch是文件存储,是一种面向文档型数据库,一条数据在这里就是一个文档,用JSON作为文档序列化的格式

- 索引库indexs ----------------------------database数据库
- 类型type--------------------------------table数据表
- 文档document----------------------------row行
- 字段field--------------------------------columns列
- 映射配置mappings-------------------------表结构

#### 分析器(Analyzer)

ElasticSearch无论时内置分析器还是自定义分析器,都由三部分组成:字符过滤器(Character Filter),分词器(tokenizer),词元过滤器(Token Filter).

1. ##### 分析器analyzer工作流程

   Input Text ==> Character Filter(如果有多个,按顺序应用) ==> Tokenizer ==>Token Filter(如果多个,按顺序应用) ==>Output Token

   1. ###### 字符过滤器(Character Filter)

      对原始文本进行预处理,如去除HTML标签,"&" 转成"and"等.

   2. ###### 分词器(Tokenizer)

      将字符串分解成一些列的词元Token,如根据空格将英文档次分开

   3. ###### 词元过滤器(Token Filter)

      对分词器分出来的词元做进一步处理,如转换大小写,移除停用词,单复数转换,同义词转换

2. ##### Mapping映射

   映射定义索引中有什么字段,字段的类型等结构信息,想到哪与数据库中表结构定义,或solr中的schema.因为lucene索引文档时需要知道该如何来索引存储文档的字段.ES中支持手动定义映射和动态映射两种.

   创建索引库时创建索引映射

   PUT index_name{							//创建索引库

   ​	"mappings":{								 //映射定义

   ​		"type1":{										//名为type1的映射类别mapping type

   ​			"properties":{						  //字段定义

   ​				"field1":{								//名为field1的字段

   ​					"type":"text" 				  //它的field datatype为text

   ​				}

   ​			}

   ​		}	

   ​	}

   }

   ES最先设计时是用索引库类比关系型数据库的数据库,用mapping type来类比表,一个索引库中可以包含多个映射类别.这个类比存在一个严重的问题,就是当多个mapping type中存在同名字段时(特别是同名字段还是不同的类型),在一个索引中不好处理,因为搜索引擎中只有索引-文档的结果,不同的映射类别的数据都是一个一个的文档(只是包含的字段不一样而已)

   从6.0.0开始限定仅包含一个映射类别定义("index.mapping.single_type" :"true"),兼容5.x中的多映射类别,从7.0开始将移除一身类别

   为了与未来规划匹配,请现在将这个唯一的映射类别定义为"_doc",因为索引的请求地址将规范为:

   PUT {index}/_doc/{id} and POST {index}/_doc

3. ##### 字段类型

   1. 以下只列举几个常用的字段类型,具体内容可以在官方文档中查询

      https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html

      - ###### string

        1. text

           用来索引这个文本的值,比如email,description,product,tags,address,hostnames,status code等,可以进行分词,但通常不能用来排序(可以设置"filedata":"ture",但会消耗大量内存)和聚合.主要用来对整个文本进行搜索

        2. keyword

           用来索引文本的内容,例如IDs, email addresses, hostnames, status codes, zip codes or tags.

           通常被用来过滤,排序和聚合.keyword只能被用来搜索已经存在的值

      - ###### numeric

        ```console
        PUT my_index
        {
          "mappings": {
            "properties": {
              "number_of_bytes": {
                "type": "integer"
              },
              "time_in_seconds": {
                "type": "float"
              },
              "price": {
                "type": "scaled_float",
                "scaling_factor": 100
              }
            }
          }
        }
        ```

        1. long
        2. integer
        3. short
        4. byte
        5. double
        6. float
        7. half_float
        8. scaled_float

      - ###### date

        ```console
        PUT my_index
        {
          "mappings": {
            "properties": {
              "date": {
                "type": "date" 
              }
            }
          }
        }
        
        PUT my_index/_doc/1
        { "date": "2015-01-01" } 
        
        PUT my_index/_doc/2
        { "date": "2015-01-01T12:10:30Z" } 
        
        PUT my_index/_doc/3
        { "date": 1420070400001 } 
        
        GET my_index/_search
        {
          "sort": { "date": "asc"} 
        }
        ```

      - ###### date nanoseconds(纳秒)

      - ###### boolean

        ```console
        PUT my_index
        {
          "mappings": {
            "properties": {
              "is_published": {
                "type": "boolean"
              }
            }
          }
        }
        
        POST my_index/_doc/1
        {
          "is_published": "true" 
        }
        
        GET my_index/_search
        {
          "query": {
            "term": {
              "is_published": true 
            }
          }
        }
        ```

      - ###### binary

        The `binary` type accepts a binary value as a [Base64](https://en.wikipedia.org/wiki/Base64) encoded string. The field is not stored by default and is not searchable:

        ```console
        PUT my_index
        {
          "mappings": {
            "properties": {
              "name": {
                "type": "text"
              },
              "blob": {
                "type": "binary"
              }
            }
          }
        }
        
        PUT my_index/_doc/1
        {
          "name": "Some binary blob",
          "blob": "U29tZSBiaW5hcnkgYmxvYg==" 
        }
        ```

      - ###### range

        1. integer_range
        2. float_range
        3. long_range
        4. date_range
        5. ip_range

      - ###### Complex 

        1. object

           JSON documents are hierarchical in nature: the document may contain inner objects which, in turn, may contain inner objects themselves:

           ```console
           PUT my_index/_doc/1
           { 
             "region": "US",
             "manager": { 
               "age":     30,
               "name": { 
                 "first": "John",
                 "last":  "Smith"
               }
             }
           }
           ```

           Internally, this document is indexed as a simple, flat list of key-value pairs, something like this:

           ```js
           {
             "region":             "US",
             "manager.age":        30,
             "manager.name.first": "John",
             "manager.name.last":  "Smith"
           }
           ```

        2. nested

           The `nested` type is a specialised version of the [`object`](https://www.elastic.co/guide/en/elasticsearch/reference/current/object.html) data type that allows arrays of objects to be indexed in a way that they can be queried independently of each other.

           ```console
           PUT my_index/_doc/1
           {
             "group" : "fans",
             "user" : [ 
               {
                 "first" : "John",
                 "last" :  "Smith"
               },
               {
                 "first" : "Alice",
                 "last" :  "White"
               }
             ]
           }
           ```

      - ###### multi-fields

        It is often useful to index the same field in different ways for different purposes. For instance, a `string` field could be mapped as a `text` field for full-text search, and as a `keyword` field for sorting or aggregations. 

        ```console
        PUT my_index
        {
          "mappings": {
            "properties": {
              "city": {
                "type": "text",
                "fields": {
                  "raw": { 
                    "type":  "keyword"
                  }
                }
              }
            }
          }
        }
        
        PUT my_index/_doc/1
        {
          "city": "New York"
        }
        
        PUT my_index/_doc/2
        {
          "city": "York"
        }
        
        GET my_index/_search
        {
          "query": {
            "match": {
              "city": "york" 
            }
          },
          "sort": {
            "city.raw": "asc" 
          },
          "aggs": {
            "Cities": {
              "terms": {
                "field": "city.raw" 
              }
            }
          }
        }
        ```

        ### Multi-fields with multiple analyzers[edit](https://github.com/elastic/elasticsearch/edit/7.8/docs/reference/mapping/params/multi-fields.asciidoc)

        Another use case of multi-fields is to analyze the same field in different ways for better relevance. For instance we could index a field with the [`standard` analyzer](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-standard-analyzer.html) which breaks text up into words, and again with the [`english` analyzer](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#english-analyzer) which stems words into their root form:

        ```console
        PUT my_index
        {
          "mappings": {
            "properties": {
              "text": { 
                "type": "text",
                "fields": {
                  "english": { 
                    "type":     "text",
                    "analyzer": "english"
                  }
                }
              }
            }
          }
        }
        
        PUT my_index/_doc/1
        { "text": "quick brown fox" } 
        
        PUT my_index/_doc/2
        { "text": "quick brown foxes" } 
        
        GET my_index/_search
        {
          "query": {
            "multi_match": {
              "query": "quick brown foxes",
              "fields": [ 
                "text",
                "text.english"
              ],
              "type": "most_fields" 
            }
          }
        }
        ```

   ###### 

4. ##### 字段定义属性

   字段的datatype定义了如何索引存储字段值,还有一些属性可以让我们根据需要来覆盖默认的值或进行特别的定义,具体内容可查阅官方文档:https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-params.html

   - analyzer:指定分词器(**只有text类型字段支持**)

   - normalizer 指字段定标准化器

   - boost 指定权重值

   - coerce 强制类型转换

   - copy_to 值复制给另一字段

   - doc_values 是否存储docValues

   - dynamic 允许动态创建字段

   - enabled 字段是否可用（设置为false,不会被搜索）

   - fielddata（对于分词的字段是否需要做聚合及排序时需要考虑设置该属性）

   - eager_global_ordinals

   - format 指定时间值的格式

   - ignore_above 忽略大于

   - ignore_malformed

   - index_options

   - index:默认为true,没有索引的字段是不允许被查询的

   - fields:

     It is often useful to index the same field in different ways for different purposes. This is the purpose of *multi-fields*. For instance, a `string` field could be mapped as a `text` field for full-text search, and as a `keyword` field for sorting or aggregations:

   - norms

   - null_value

   - position_increment_gap

   - properties:一个字段中包含了子文档

   - search_analyzer

   - similarity

   - store

   - term_vector

   

###### 基本查询

- 基本查询
- _source过滤
- 结束过滤
- 高级查询
- 排序
- 高亮
- 分页