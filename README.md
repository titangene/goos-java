# goos-java

參考 [GOOS（Growing Object-Oriented Software, Guided by Tests）](https://www.growing-object-oriented-software.com/) 書中的 Auction Sniper 範例，重新用 TDD 刻一次，盡量比照作者原始的 [goos-code](https://github.com/sf105/goos-code) repo 的簡單管理方式：依賴用 vendor jar、手動 `javac`/`java`、不用額外的建置工具（不用 Maven/Gradle/Ant）。

`baby-step-v2` 分支參考 [letterliu/goos-java-practice](https://github.com/letterliu/goos-java-practice/commits/tdd-baby-step/) 的節奏：紅燈、綠燈、重構各自是獨立的 commit，環境與依賴只在當下真正需要時才加入。
