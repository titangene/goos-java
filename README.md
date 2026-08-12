# goos-java

參考 [GOOS（Growing Object-Oriented Software, Guided by Tests）](https://www.growing-object-oriented-software.com/) 書中的 Auction Sniper 範例，重新用 TDD 刻一次，盡量比照作者原始的 [goos-code](https://github.com/sf105/goos-code) repo 的簡單管理方式：依賴用 vendor jar（見下方）、手動 `javac`/`java`、不用額外的建置工具（不用 Maven/Gradle/Ant）。

跟 AI agent 協作的規則見 [CLAUDE.md](CLAUDE.md)。

## 依賴套件：vendor jar

依賴的 jar 直接放在 `lib/` 並 commit 進 git，不用 Maven/Gradle 等建置工具管理版本——這樣每個人 checkout 到同一個 commit，拿到的就是 bit-for-bit 相同的 jar。跟 goos-code 一樣分兩個資料夾：

- `lib/deploy`：app 實際執行需要的 runtime 依賴（例如之後接 XMPP 會用到的 smack）
- `lib/develop`：只有開發/測試會用到的依賴（junit、hamcrest、jmock、windowlicker...），不會進到實際跑起來的 app

編譯 app 只用 `lib/deploy` 的 classpath；編譯/跑測試則是 `lib/deploy` + `lib/develop` 一起用（`lib/develop` 裡的 `-src.jar` 是原始碼、不放進 classpath，`docker/scripts/run-e2e-tests.sh` 裡有排除）。

## IDE 設定

兩個 IDE 都設定成指向 `lib/deploy`、`lib/develop` 這兩個資料夾層級，新增/移除 jar 不用手動改設定檔：

- **IntelliJ**：`.idea/libraries/lib.xml` 的 library 用兩個 `jarDirectory` 分別指向 `lib/deploy`、`lib/develop`，新增/移除 jar 後重新整理 IDE 即可自動反映。
- **VS Code**：裝 Java extension 後，`.vscode/settings.json` 裡的 `java.project.referencedLibraries` 設成包含 `lib/deploy/*.jar`、`lib/develop/*.jar`，並排除 `-src.jar`/`-sources.jar`；這個設定檔會被 git 追蹤，team 每個人開箱即用。
