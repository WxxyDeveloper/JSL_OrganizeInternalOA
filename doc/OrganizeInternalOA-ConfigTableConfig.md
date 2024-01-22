# OrganizeInternalOA Config表详细内容设计

<div align=right>Author: 筱锋xiao_lfeng | Version: 1.1.0</div>

## oa_config 数据表

### 数据表结构


| 序号 |     名称     |    描述    |      类型       |  键  | 为空 |       额外        |      默认值       |
| :--: | :----------: | :--------: | :-------------: | :--: | :--: | :---------------: | :---------------: |
|  1   |     `id`     |    主键    | bigint unsigned | PRI  |  NO  |  auto_increment   |                   |
|  2   |   `value`    | 调用关键字 |   varchar(50)   | UNI  |  NO  |                   |                   |
|  3   |    `data`    |  json数据  |      json       |      | YES  |                   |                   |
|  4   | `created_at` |  创建时间  |    timestamp    |      |  NO  | DEFAULT_GENERATED | CURRENT_TIMESTAMP |
|  5   | `updated_at` |  修改时间  |    timestamp    |      | YES  |                   |                   |


### 各功能内容解释

1. `id` : 主键，构建索引所用
2. `value` : 构建索引，用于索引字符串内容，方便对 `data` 的获取
3. `data` : 输出的 ‘json’ 数据（重要）
4. `created_at` : 创建时间
5. `updated_at` : 修改时间



## 说明

> 在这张数据表中，为存储相关配置信息，例如前端需要展示的轮播图、展览业、合作厂商列表、优秀成员列表等等，为了方便开发以及后续维护不需要进行额外进行对表的设计（对于新的一个前端展示功能模块就要新建一张表，显得麻烦），故将所有的内容压缩到这张表，需要展示的内容已结构化 json 数据存储到 `data` 字段。下面是内容举例。

假设我现在有一个config表
id    value    data

然后我有两个前端模块要动态（可修改）展示，一个是团队信息展示，另外一个轮播图，那么我新增一行
id为主键（不管），团队成员信息定义value：organize_user_info，设计数据结构，最终结构化为json存储到 data
轮播图一样，定义value: image_index，设计数据结构存储 data

一段时间后，有新的业务要展示，例如合作厂家展示

那就在这张表新建一行数据，定义value:cooperate，然后设计数据结构 存储data

以后有新的展示业务，以此类推。

这样取对应地方数据WHERE value后取出data，直接发送前端或定时（解析后给redis）。也不用一个业务新建一张表，也没那么多数据表查找循环。
对于数据体量非巨大的应该有一定可行性。



## 轮播图数据结构设计

```json
{
    "order": string,
    "data": [
        {
            "display_order": integer,
            "image": string,
            "title": string,
            "description": string,
            "is_active": boolean,
            "created_at": timestamp,
            "updated_at": timestamp,
            "author": string
        },
        {
            ......
        }
    ]
}
```

1. `order`: 展示顺序，可选值 [asc|desc]
2. `data`: 数据内容
   1. `display_order`: 展示顺序（输入integer，自定义处理）
   2. `image`: 图片地址
   3. `title`: 标题
   4. `description`: 描述
   5. `is_active`: 是否展示 [true|false]
   6. `created_at`: 创建时间
   7. `updated_at`: 修改时间
   8. `autohr`: 填写作者

## 项目信息展示
1. `order`: 展示顺序，可选值 [asc|desc]
2. `data`: 数据内容
   1. `display_order`: 展示顺序（输入integer，自定义处理）
   2. `name`: 项目名
   3. `type`: 项目类型
   4. `status`: 项目状态
   5. `is_active`: 是否展示 [true|false]
   6. `created_at`: 创建时间
   7. `updated_at`: 修改时间
   8. `autohr`: 填写作者


## 新闻信息展示
1. `order`: 展示顺序，可选值[asc|desc]
2. `data`: 数据内容
   1. `display_order`：展示顺序（输入integer，自定义处理）
   2. `title`：        新闻标题
   3. `content`：      新闻内容
   4. `tags`：         标签
   5. `likes`：        点赞数
   6. `comments`：     评论数
   7. `status`：       新闻状态
   8. `is_active`：    是否展示[true|false]
   9. `created_at` ：  创建时间
   10. `updated_at` ： 更新时间
   11. `author`：      填写作者
   

