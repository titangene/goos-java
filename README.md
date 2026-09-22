# goos-java

參考 [GOOS（Growing Object-Oriented Software, Guided by Tests）](https://www.growing-object-oriented-software.com/) 書中的 Auction Sniper 範例，重新用 TDD 刻一次，盡量比照作者原始的 [goos-code](https://github.com/sf105/goos-code) repo 的簡單管理方式：依賴用 vendor jar（見下方）、手動 `javac`/`java`、不用額外的建置工具（不用 Maven/Gradle/Ant）。

## 依賴套件：vendor jar

依賴的 jar 放在 `lib/`，不用 Maven/Gradle 等建置工具管理版本，直接 commit 進 git。這樣每個人 checkout 到同一個 commit，拿到的就是 bit-for-bit 相同的 jar。跟 goos-code 一樣分兩個資料夾：

- `lib/deploy`：app 實際執行需要的 runtime 依賴（例如之後接 XMPP 會用到的 smack）
- `lib/develop`：只有開發/測試會用到的依賴（junit、hamcrest、jmock、windowlicker...），不會進到實際跑起來的 app

編譯 app 只用 `lib/deploy` 的 classpath；編譯/跑測試則是 `lib/deploy` + `lib/develop` 一起用（`lib/develop` 裡的 `-src.jar` 是原始碼、不放進 classpath）。
