# BioHoward
這是一個用java開發的純文字遊戲, 系統參考早期的multi-user dungeon (mud) 呈現介面來設計, 但加入明顯的主線劇情以及隊伍機制. 介面、指令等設計參考mud遊戲"四度空間".

主角為一個大一的學生"霍華", 在某天發現整間學校的人都失去理智, 變得像惡靈古堡的殭屍一樣. 序章的內容就是設法讓他逃離宿舍. 

由於序章同時作為教學任務 (有許多教學說明), 人物死亡時並不會直接game over, 而是傳送回原本的房間, 幫你把血補滿, 任務劇情的推進保留. 希望能夠透過這樣的方式讓玩家有較多時間與意願來接觸所謂的"文字遊戲".

# 執行方式
將整包github code下載下來, 解壓縮後執行biohoward.jar這個runnable jar file. 

for windows: 滑鼠左鍵雙擊打開即可.

for linux: 開啟command line, change directory進入該目錄, 輸入"java -jar biohoward.jar"即可執行.

當然有興趣的人也可以將整個project build起來, 找到MainClass.java, 執行它.

注意: biohoward.jar與resources資料夾須放在同一個目錄底下.

# 使用者介面
傳統的mud由於只需要操控一位角色, 因此狀態列直接使用command line的風格即可呈現, 但若是考慮多位角色同時顯示的情況下, command line就顯得不太夠了. 因此我用java最基本的GUI功能實作了一個window, 如下圖.

![default](https://cloud.githubusercontent.com/assets/8130848/7352367/a7f0dd8c-ed3d-11e4-8770-5f870f872887.png)

遊戲中, 每移動一次所到之處稱為一個"房間" (並非一定是室內, 把它想像為一個場景會更容易理解). 房間會有各自的名稱、敘述、出口等資訊, 並且在觀察時也會顯現出有那些npc與你所在同一位置, 或是那些物品掉落在地上.

下圖是一個當玩家輸入"l"或"look"觀察房間時顯示出來的範例.

![default](https://cloud.githubusercontent.com/assets/8130848/7371768/7d050e46-edf5-11e4-9f39-5757c392be12.png)

# 戰鬥方式
戰鬥方式採取時間條模式, 並考慮到玩家打字速度可能是個瓶頸, 可以選擇即時戰鬥/非即時戰鬥兩種模式.

在非即時戰鬥中, 當我方其中一位角色的時間條到達100%時, 同一場戰鬥中的其他角色都會停下來等待他行動. 注意, 時間停下來的只有這場戰鬥中的角色, 遊戲中其他地方的時間會照常運作. 即時戰鬥則是在角色時間條到達100%時, 其他角色的時間條也會照常增加, 打字慢的人可能就會被多打好幾下囉.

well, 其實這也不是什麼新鮮的東西, 很多RPG都採取這樣的方式進行. 只能說這是在同時想要保有多人隊伍以及即時戰鬥感的需求下折衷的方案了.

# 操作指令
遊戲中能夠輸入"help"觀看所有可使用的指令, 選定要查詢的指令後輸入"help <指令名稱>"即可得到該指令詳細的介紹. 所以當你不知道接下來可以做什麼的時候, 不妨"help"一下, 慢慢熟悉指令系統. 這些指令的說明在github的wiki上面也有, 可以直接進wiki查詢: https://github.com/shockwang/BioHoward/wiki

# Mud
根據wiki頁面: http://zh.wikipedia.org/wiki/MUD

"MUD最初是1978年出現在英國的一個連線遊戲英文名稱Multi-User Dungeon縮寫[1]。其後出現的類似系統沿用相同的MUD縮寫，但賦予不同的全名如："Multi-User Dimension"或"Multiple User Dialogue"。台灣使用者通常直接稱呼為MUD。通常將縮寫字直譯為「網路泥巴」或是簡稱「泥巴」（英文mud的意思為泥巴）。"

使用純文字呈現遊戲畫面、人物、場景、劇情等元素. 在這個遊戲畫面一個比一個華麗的年代, 很多人已經不能想像這種文字的呈現方式好玩在哪. 其實我完全理解, 要不是我真的去玩過, 我也絕對不會相信純文字的遊戲可以很好玩.

如果要我說純文字遊戲好玩在哪, 我會說是因為沒有畫面. 就因為沒有畫面, 於是能給予玩家無限的想像空間. 靠純文字勾勒出的圖案, 可能在每個人心目中都不同.

我最早接觸過的mud是"四度空間", 遊戲時間最長, 受其風格影響也最深. 之後也有玩過"迴風追夢", "小貓的世界", "重生的世界", "三國歪傳"等mud遊戲. 順便一提, 台灣的mud其實還是有許多伺服器默默在運行的, 從"重生的世界"官網可以瀏覽各mud的連結: https://www.revivalworld.org/

既然是多人遊戲, mud的賣點主要是在組團打怪, 多樣化的職業、技能與人物配點...好啦, 其實這些現代的遊戲都早已具備. 

mud的缺點則是沒有主線劇情, 解任務的時候沒有明確的提示, 並且可能一個環節錯了就要重來. 嘛, 也不一定是缺點啦, 畢竟也有人喜歡這種需要不斷探索的風格. 此外, 視乎開發人員的團隊, 任務與任務之間容易產生發散、沒有主題的狀況. 

mud難以推廣的原因主要是對於新手的gap太大. 不但要記一大堆指令(根本是在用linux嘛), 打字速度還要快! 不然等你一個攻擊指令打完, 人物早就被打死了...我現在一分鐘108個英文單字的英打速度就是玩mud練出來的.

但mud風格的遊戲對於程式開發者有個超便利的優勢: 不需要考慮畫面! (雖然可能沒有人要玩..呃呃呃...), 因此可以專心考慮系統層面的東西如何設計與實作, 一個人也能夠製作出來. 嘛, 身為一個遊戲愛好者, 寫出一個自己的遊戲是我人生中必達成的目標之一, 而因應誕生的就是這個BioHoward! 現在只有序章而已, 希望我有毅力繼續往下開發囉!

# License
BioHoward專案是open source專案, 採用GPL3.0發布.
