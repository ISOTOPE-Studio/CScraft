# CScraft

## 职业
``/csclass set <职业名称>``

背包里的装备当 这个职业的装备

``/csclass delete <职业名称>``

``/csclass list``

``/csclass setinvisible <true|false>``

### 权限
``csclass.<职业名字>``

## 创建竞技场
### 创建
``/cscraft create <名字> <team|infect|protect>``

### 地图范围
``/cscraft pos1 <名字>``

``/cscraft pos2 <名字>``

### 设置玩家数
``/cscraft min <名字>``

``/cscraft max <名字>``

### 添加职业
`` /cs addAclass <职业>``

`` /cs addBclass <职业>``

### 设置等待大厅
``/cscraft lobby <名字>``

### 设置出生点
``/cscraft teamA <名字>``

``/cscraft teamB <名字>``

### 添加药水
``/cscraft effect <漂浮物品ID> <获得药水> <药水等级> <时间> <冷却>``

### 设置奖励
``/cscraft reward <名字> <命令>``

## 流程
- *竞技场GUI*点击传送至竞技场
- 等待时*职业GUI*钻石选职业，绿宝石选阵营，粘液退游戏
- 游戏 **装备不能脱 还有 武器位置也不能交换**

名字显示名字那显示你选择的职业

## 游戏模式
### 团队
达到配置里的 最高人数

#### 设置目标人数
``/cscraft goal <名字> <目标人数>``

### 感染（生化）
2种阵营 A阵营 打死 B阵营的人 B为A的人  B死完 A赢   B打死 所有A A赢 A只有 2跳明  A差不多就是僵尸  B就人类

就是 变僵尸。或者僵尸 像躲猫猫一样 关某小黑屋 x秒 然后释放出  然后僵尸打死 玩家 ！变僵尸  僵尸只有3跳命 如果死亡了 僵尸 人类赢

### 守卫
地图中有个xx血的怪 谁先打死 谁赢。

可以设置 怪的 攻击和 生命值。

类似于起床战争，家里有个怪。击杀后。那方无法复活！

## 记分板
CS那种显示 团队 显示 A B方击杀人数

爆破显示 多久结束 炸弹爆炸时间！

保卫 就显示 双方怪物 血量多少

然后模式。每个地图都是单独一个配置 比如 团队1模式  是最大击杀100人  而团队2是20人  都是单独配置的！

## 奖励
每局打完后 赢的可获得 配置设置的物品奖励！然后感染的胜利判断 为 僵尸与人的数量！  僵尸赢了 则母体奖励  人类赢了 则 剩余人类奖励